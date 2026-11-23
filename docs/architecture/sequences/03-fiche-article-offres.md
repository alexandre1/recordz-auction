# 03 Fiche Article Offres

[← Index des diagrammes](./README.md)


## D14 — Consulter une fiche article et enregistrer la visite

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as DetailArticleView
    participant S as ArticleService + ReferenceService + PersonneService
    participant R as ArticleRepository + PersonneRepository
    participant DB as MySQL (article + article_visite)
    U->>V: Lit articleId
    V->>S: Valide le format numérique
    S->>R: Charge article et incrémente visites
    R->>DB: Si OIDC enregistre la visite nominative
    DB-->>R: Trouve le vendeur
    R-->>S: Charge sa fiche
    S-->>V: Affiche caractéristiques description image et questions
    opt Variantes et erreurs
        Note over U,V: ID invalide: detail.invalidid
        Note over U,V: Article absent: detail.notfound
        Note over U,V: Échec visite ou vendeur: journalisé
    end
```

**Résultat attendu :** consulter une fiche article et enregistrer la visite selon le scénario décrit.

## D15 — Afficher les derniers enchérisseurs

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as DetailArticleView
    participant S as EnchereService
    participant R as EnchereRepository
    participant DB as MySQL (enchere)
    U->>V: Ouvre la section
    V->>S: Demande les offres paginées par 5
    S->>R: Lit les enchères récentes
    R->>DB: Retourne nom montant et date
    DB-->>R: Affiche avatar et met en évidence la première offre
    opt Variantes et erreurs
        Note over U,V: Aucune offre: detail.bidders.none
        Note over U,V: Nom vide: detail.anonymous
        Note over U,V: Profil accessible par clic
    end
```

**Résultat attendu :** afficher les derniers enchérisseurs selon le scénario décrit.

## D16 — Consulter les visiteurs d un article

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as DetailArticleView / dialogue visiteurs
    participant S as ReferenceService
    participant R as ArticleRepository
    participant DB as MySQL (article_visite)
    U->>V: Ouvre le dialogue
    V->>S: Demande findVisiteurs
    S->>R: Lit au plus 50 visites triées récent vers ancien
    R->>DB: Masque l e-mail
    DB-->>R: Affiche 5 lignes par page
    opt Variantes et erreurs
        Note over U,V: Pagination côté client
        Note over U,V: Lien profil construit avec partie locale de l e-mail
    end
```

**Résultat attendu :** consulter les visiteurs d un article selon le scénario décrit.

## D17 — Poser une question sur un article

```mermaid
sequenceDiagram
    actor U as Utilisateur connecté
    participant V as DetailArticleView
    participant S as ReferenceService
    participant R as PersonneRepository / ArticleRepository
    participant DB as MySQL (commentaire)
    U->>V: Saisit sujet et détail
    V->>S: Vérifie principal OIDC
    S->>R: Résout personne par e-mail
    R->>DB: Enregistre commentaire
    DB-->>R: Retourne succès
    R-->>S: Vide le formulaire et reconstruit la page
    opt Variantes et erreurs
        Note over U,V: Sujet vide: erreur
        Note over U,V: Non connecté: demande login
        Note over U,V: Compte absent: erreur
        Note over U,V: Aucun e-mail vendeur envoyé
    end
```

**Résultat attendu :** poser une question sur un article selon le scénario décrit.

## D18 — Valider les règles d une offre

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as Dialogue offre
    participant S as EnchereService
    participant R as EnchereRepository
    participant DB as MySQL (enchere)
    U->>V: Ouvre le dialogue
    V->>S: Lit prix actuel ou prix de départ
    S->>R: Saisit le montant
    R->>DB: Normalise virgule décimale
    DB-->>R: Vérifie montant et conditions de l enchère
    R-->>S: Autorise ou refuse l offre
    opt Variantes et erreurs
        Note over U,V: Montant invalide ou trop bas: notification dédiée
        Note over U,V: Article non enchérissable ou compte absent: refus
    end
```

**Résultat attendu :** valider les règles d une offre selon le scénario décrit.

## D19 — Enregistrer une offre valide

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as DetailArticleView
    participant S as EnchereService
    participant R as EnchereRepository
    participant DB as MySQL (enchere + article)
    U->>V: Confirme le montant validé
    V->>S: Résout l enchérisseur
    S->>R: Insère une ligne enchere
    R->>DB: Incrémente article.nbr_enchere
    DB-->>R: Retourne confirmation
    R-->>S: Ferme le dialogue
    opt Variantes et erreurs
        Note over U,V: Exception insertion: erreur et aucune confirmation
        Note over U,V: Concurrence d offres: validation à recontrôler côté service
    end
```

**Résultat attendu :** enregistrer une offre valide selon le scénario décrit.

## D20 — Acheter directement un article

```mermaid
sequenceDiagram
    actor U as Acheteur
    participant V as DetailArticleView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + a_paye)
    U->>V: Clique acheter sur vente directe
    V->>S: Vérifie le type de vente et la disponibilité affichée
    S->>R: Demande la création de l achat
    R->>DB: Enregistre l attente de paiement et les références acheteur vendeur article
    DB-->>R: Retourne confirmation
    opt Variantes et erreurs
        Note over U,V: Article en enchère: action non applicable
        Note over U,V: Article absent ou erreur persistance: achat non confirmé
    end
```

**Résultat attendu :** acheter directement un article selon le scénario décrit.
