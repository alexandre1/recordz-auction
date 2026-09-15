# Recordz — Marketplace

Cette application Web Java Full Stack représente une place de marché Suisse permettant aux vendeurs et aux acheteurs
d'acheter et de vendre des articles de luxe sans frais de transaction. Aucun coût n'est facturé lors de la mise en vente
ou de l'achat d'un article.

Le financement se fera par la mise en évidence des articles sur les pages principales.

Stack : **Java 25 · Vaadin 25 · Spring Boot 3.4 · Spring Security · OAuth2 Google · jOOQ 3.19 · MySQL 9 · Flyway · Docker**

Base de données : schéma **recordz** (marketplace suisse — vêtements, électronique, automobile, vins, enchères…)

https://github.com/user-attachments/assets/3d7bcc19-9083-4080-a51e-5fcf0e230eb3

https://github.com/user-attachments/assets/502e66e0-90f8-4fb0-b37f-e65362d9bcac

https://github.com/user-attachments/assets/e759f9c3-66ef-4efb-99f5-e367ad3b2620

---

## Architecture du schéma (résumé)

| Groupe | Tables clés |
|---|---|
| Catalogue | `article`, `met_en_vente`, `genre`, `categorie`, `main_categorie`, `subcategorie` |
| Utilisateurs | `personne`, `boutique`, `visiteur`, `sessions` |
| Enchères | `enchere`, `a_paye`, `a_livre` |
| Commandes | `commande`, `commande_article`, `transaction` |
| Évaluations | `evaluation_achat`, `evaluation_article`, `evaluation_vente` |
| Référentiels | `libelle`, `langue`, `canton_fr`, `departement`, `pays_present`, `pays_region_vin`, `cepage` |
| Souhaits | `wish`, `wish_list` |
| Divers | `commentaire`, `demande_visite`, `recherche`, `depot`, `article_ip`, `article_visite` |

---

## Démarrage rapide

```bash
# 1. Variables d'environnement
cp .env.example .env
# Remplir GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, DB_*

# 2. Démarrer MySQL
docker compose up mysql -d

# 3. Générer le code jOOQ (après que Flyway ait créé les tables)
mvn generate-sources \
  -Djooq.codegen.url=jdbc:mysql://localhost:3306/new_db_recordz \
  -Djooq.codegen.user=root \
  -Djooq.codegen.password= \
  -Djooq.codegen.schema=new_db_recordz


(Ou alors importer le fichier .sql dans PhpMyAdmin sans passer par l'étape numéro 3)

# 4. Lancer l'application
export GOOGLE_CLIENT_ID=...
export GOOGLE_CLIENT_SECRET=...
mvn spring-boot:run -pl recordz-web
```

→ [http://localhost:8081](http://localhost:8081)

---

## Structure du projet

```
recordz-core/
└── src/main/java/com/example/recordz/
    ├── model/domain/
    │   ├── Article.java                      # Annonce marketplace
    │   ├── Personne.java                      # Utilisateur / membre
    │   ├── Enchere.java                       # Mise aux enchères
    │   ├── Commande.java                      # Commande
    │   ├── Transaction.java                   # Transaction financière
    │   ├── Boutique.java                      # Boutique vendeur
    │   ├── Wish.java / WishList.java          # Liste de souhaits
    │   ├── Commentaire.java                   # Commentaire article
    │   ├── Referentiel.java                   # Tous les types de référence
    │   └── Evaluations.java                   # Notes achat/vente/article
    ├── repository/
    │   ├── ArticleRepository.java             # jOOQ — articles
    │   ├── PersonneRepository.java            # jOOQ — utilisateurs
    │   ├── EnchereRepository.java             # jOOQ — enchères
    │   ├── CommandeRepository.java            # jOOQ — commandes
    │   └── ReferentielRepository.java         # jOOQ — tables de référence (@Cacheable)
    └── service/
        ├── ArticleService.java                # Logique métier articles
        ├── EnchereService.java                # Logique enchères avec validation
        └── PersonneService.java               # Logique utilisateurs

recordz-web/
└── src/main/java/com/example/recordz/
    ├── RecordzApplication.java
    ├── config/
    │   └── JooqConfig.java                    # DSL settings + @EnableCaching
    ├── security/
    │   ├── SecurityConfig.java                # Vaadin + OAuth2 Google
    │   ├── CustomOAuth2UserService.java       # Sync OAuth → personne
    │   ├── AuthenticatedUser.java             # Accès utilisateur courant
    │   └── LoginView.java                     # Page de connexion Vaadin
    └── ui/
        ├── layouts/MainLayout.java            # Shell + nav drawer
        └── views/
            ├── HomeView.java                  # Accueil
            ├── CatalogueView.java             # Catalogue + recherche
            ├── EncheresView.java              # Enchères actives
            └── OtherViews.java                # MesAnnonces, MesAchats, Wishlist, Boutique, Profil
```

---

## Architecture logicielle

### Modules Maven

Le projet est découpé en deux modules pour séparer la logique métier de la couche web :

- **`recordz-core`** — modèles de domaine, repositories (accès données) et services (logique métier). Indépendant de Vaadin/Spring Web, réutilisable si une autre interface (API REST, batch) devait un jour consommer la même logique.
- **`recordz-web`** — point d'entrée Spring Boot, configuration, sécurité et interface utilisateur Vaadin. Dépend de `recordz-core`.

### Couches applicatives

L'application suit une architecture en couches classique :

```
┌─────────────────────────────────────┐
│   UI (Vaadin Views)                  │  recordz-web/ui
│   HomeView, CatalogueView, ...       │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   Service (logique métier)           │  recordz-core/service
│   ArticleService, EnchereService...  │
│   @Transactional                     │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   Repository (accès données)         │  recordz-core/repository
│   DSL jOOQ brut                      │
└──────────────┬────────────────────────┘
               │
┌──────────────▼────────────────────────┐
│   MySQL (schéma recordz)             │  via HikariCP
└─────────────────────────────────────┘
```

Chaque couche ne dépend que de la couche immédiatement inférieure : les vues Vaadin n'appellent jamais un repository directement, elles passent systématiquement par un service.

### Sécurité et authentification

L'authentification repose entièrement sur **OAuth2 Google** (pas de mot de passe local) :

1. `SecurityConfig` délègue la connexion à `VaadinSecurityConfigurer` + `oauth2Login`.
2. À chaque connexion, `CustomOAuth2UserService` intercepte le flux OIDC et fait un **upsert** dans la table `personne` (email = identifiant unique). Le champ `mot_de_passe` legacy n'est pas utilisé.
3. `AuthenticatedUser` expose ensuite l'utilisateur courant aux vues Vaadin sans qu'elles aient à connaître les détails OAuth2.

### Accès aux données

- **jOOQ en DSL brut** (`table("article")`, `field("nom")`) tant que la génération de code typée n'a pas encore été branchée sur le schéma final. Une fois générée, remplacer par les classes typées : `import static com.example.recordz.jooq.tables.Article.ARTICLE`.
- **HikariCP** comme pool de connexions, configuré dans `JooqConfig`.
- **Flyway** pilote le schéma (`V1__recordz_schema.sql`, qui recrée tout le schéma en `utf8mb4`). Toute évolution du schéma passe par une nouvelle migration versionnée, jamais par une modification du fichier existant. Si vous importez le dump original (`new_db_recordz.sql`) directement, passez `baseline-on-migrate: true` et `baseline-version: 1`.
- **Concurrence** : les opérations sensibles à la concurrence (ex. upsert OAuth2 dans `PersonneRepository`) s'appuient sur les contraintes `UNIQUE` en base plutôt que sur des vérifications applicatives, pour rester correctes sous forte charge concurrente (virtual threads).

### Caching

`ReferentielRepository` utilise `@Cacheable` pour les données de référence peu volatiles (cantons, catégories, libellés, etc.) — chargées une fois en mémoire au lieu d'un aller-retour DB à chaque affichage.