# Diagrammes d'architecture

Diagrammes Mermaid (rendus directement par GitHub) générés à partir du code source.
Pour l'analyse détaillée, voir [analyse-et-architecture.md](analyse-et-architecture.md) ;
pour les tables, voir [modele-de-donnees.md](modele-de-donnees.md).

## 1. Vue d'ensemble des composants

Maven multi-modules : `recordz-core` (domaine, services, accès aux données) et
`recordz-web` (interface Vaadin, sécurité, scheduler, webhooks). Java 21, Spring Boot 4.0.1,
Vaadin 25, jOOQ 3.20, MySQL 9.1.

```mermaid
flowchart LR
    Browser["Navigateur"]

    subgraph WEB["recordz-web"]
        UI["ui : vues Vaadin\nCatalogueView, EncheresView,\nDetailArticleView, MonCompteView..."]
        SEC["security\nSecurityConfig,\nCustomOAuth2UserService"]
        CTRL["web\nMediaPrecheckController"]
        HOOK["webhook\nEntrupyWebhookController"]
        SCHED["EnchereScheduler\n(toutes les 60 s)"]
        VID["integration/video"]
    end

    subgraph CORE["recordz-core"]
        SVC["service\nEnchereService, ArticleService,\nArticleSubmitService, ReferenceService..."]
        REPO["repository\nArticle, Enchere, Personne,\nCommande, Referentiel"]
        DOM["model.domain\nArticle, Enchere, Personne..."]
        ENT["entrupy\nEntrupyWebhookService"]
    end

    DB[("MySQL\nnew_db_recordzv3")]
    REDIS[("Redis")]
    GOOGLE["Google OAuth2"]
    ENTRUPY["Entrupy\n(authentification d'articles)"]
    SMTP["Serveur e-mail"]

    Browser --> UI
    Browser --> SEC
    UI --> SVC
    CTRL --> SVC
    SVC --> REPO
    REPO --> DB
    SVC --> DOM
    SEC --> GOOGLE
    ENTRUPY -->|"POST /webhooks/entrupy"| HOOK
    HOOK --> ENT
    ENT --> SVC
    SCHED --> DB
    SCHED --> SMTP
    WEB --> REDIS
```

> Le rôle exact de Redis (cache, sessions...) n'est pas déduit ici : seuls la configuration
> Docker et le package `model/domain/redis` le mentionnent.

## 2. Déploiement (docker-compose)

```mermaid
flowchart TB
    subgraph HOST["Hôte Docker"]
        APP["recordz-app\nSpring Boot, profil production\nhôte 8098 -> conteneur 8081"]
        MYSQL["recordz-mysql\nmysql:9.1\nhôte 3307 -> conteneur 3306"]
        RED["recordz-redis\nredis:7-alpine\n6379"]
        V1[("volume mysql_data")]
        V2[("volume redis_data")]
    end

    APP -->|"jdbc:mysql://mysql:3306"| MYSQL
    APP -->|"redis:6379"| RED
    MYSQL --- V1
    RED --- V2
    G["Google OAuth2\nGOOGLE_CLIENT_ID / SECRET"] -.-> APP
```

`app` démarre seulement quand les healthchecks de `mysql` et `redis` sont au vert
(`depends_on: condition: service_healthy`).

## 3. Séquence : placer une enchère

Règles appliquées par `EnchereService.placerEnchere`.

```mermaid
sequenceDiagram
    actor U as Enchérisseur
    participant V as EncheresView
    participant S as EnchereService
    participant AR as ArticleRepository
    participant ER as EnchereRepository

    U->>V: saisit un montant
    V->>S: placerEnchere(refArticle, refEnchereur, montant)
    S->>AR: findById(refArticle)
    AR-->>S: Article
    alt article.enchere != 1
        S-->>V: IllegalStateException (pas en mode enchère)
    else article.vendu == 1
        S-->>V: IllegalStateException (déjà vendu)
    else
        S->>ER: findHighestBid(refArticle)
        ER-->>S: meilleure enchère (optionnelle)
        alt montant <= meilleure enchère
            S-->>V: IllegalArgumentException (montant trop bas)
        else
            S->>ER: save(refArticle, refEnchereur, montant)
            S-->>V: OK
        end
    end
```

## 4. Séquence : clôture automatique des enchères

`EnchereScheduler.cloturerEncheresExpirees`, exécuté avec `@Scheduled(fixedDelay = 60_000)`.

```mermaid
sequenceDiagram
    participant SCH as EnchereScheduler
    participant DB as MySQL (jOOQ)
    participant MAIL as JavaMailSender

    loop toutes les 60 s
        SCH->>DB: articles en enchère non vendus dont enchere_date_fin = maintenant (à la minute)
        loop pour chaque enchère échue
            SCH->>DB: meilleure offre (ref_enchereur, max prix)
            alt aucune offre
                SCH->>DB: article.ref_statut = 8
            else un gagnant
                SCH->>DB: lecture acheteur et vendeur (personne)
                SCH->>DB: article.ref_statut = 1, vendu = 1
                SCH->>DB: INSERT a_paye (statut 8, en attente de paiement)
                SCH->>MAIL: e-mail à l'acheteur
                SCH->>MAIL: e-mail au vendeur
            end
        end
    end
```

## 5. Séquence : authentification Entrupy (webhook)

```mermaid
sequenceDiagram
    participant E as Entrupy
    participant C as EntrupyWebhookController
    participant W as EntrupyWebhookService
    participant A as ArticleAuthenticationService
    participant DB as MySQL

    E->>C: POST /webhooks/entrupy (en-tête Entrupy-Signature)
    C->>W: isSignatureValid(payload, signature) [HMAC-SHA256]
    alt signature invalide
        C-->>E: 401
    else signature valide
        C->>W: processEvent(payload)
        W->>W: aiguillage sur event
        Note over W: session.completed traité ; support.message et certificate.transfer journalisés seulement
        W->>A: updateAuthenticationStatus(customerItemId, entrupyId, certificateUrl, isAuthentic)
        A->>DB: UPDATE article (entrupy_status, entrupy_id, entrupy_certificate_url)
        opt article authentique
            A->>DB: UPDATE article SET ref_statut = 2
        end
        C-->>E: 200
    end
```

## Points d'attention observés dans le code

Ces remarques ne modifient rien : ce sont des constats à garder en tête en lisant les diagrammes.

- **Clôture à la minute exacte** : le scheduler compare `enchere_date_fin` avec l'heure courante
  formatée `yyyy-MM-dd HH:mm` (clause `LIKE`). Une enchère dont la minute de fin passe pendant
  un arrêt ou un redémarrage de l'application ne serait pas reprise au passage suivant.
- **Statuts numériques** : `article.ref_statut` utilise des valeurs en dur (1 = vendu aux enchères,
  8 = invendu, 2 = publié, avec un `TODO` à confirmer dans `ArticleAuthenticationService`).
  La valeur 8 désigne aussi « en attente de paiement » dans `a_paye`.
- **Packages** : `EnchereScheduler.java` déclare le package `com.example.recordz.scheduler` et
  `EntrupyWebhookController.java` le package `com.example.recordz.web.webhook`, alors que leurs
  fichiers sont rangés dans `recordz/` et `recordz/webhook/`.
- **Dockerfile** : `docker-compose.yml` référence un `Dockerfile` à la racine, absent de la liste
  des fichiers versionnés à la date de génération de ce document.
