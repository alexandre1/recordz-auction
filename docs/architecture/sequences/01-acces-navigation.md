# 01 Acces Navigation

[← Index des diagrammes](./README.md)


## D01 — Connexion Google et création/actualisation du compte

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as LoginView
    participant S as Spring Security / CustomOAuth2UserService
    participant R as PersonneService
    participant DB as MySQL (personne)
    U->>V: Ouvre une vue protégée
    V->>S: redirige vers /login
    S->>R: Clique OAuth Google
    R->>DB: redirige vers Google
    DB-->>R: Retourne avec OIDC email/nom
    R-->>S: Charge ou synchronise la personne
    S-->>V: Crée la personne si absente
    V-->>U: Ouvre la session et redirige vers /
    opt Variantes et erreurs
        Note over U,V: Jeton sans e-mail: OAuth2AuthenticationException
        Note over U,V: Compte déjà connecté: redirection accueil
    end
```

**Résultat attendu :** connexion google et création/actualisation du compte selon le scénario décrit.

## D02 — Redirection vers login depuis une vue protégée

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as Vaadin Router
    participant S as Spring Security
    participant R as LoginView
    U->>V: Demande une route @PermitAll
    V->>S: Contrôle l authentification
    S->>R: Refuse l accès anonyme
    R-->>S: Redirige vers /login
    S-->>V: Affiche le bouton Google
    opt Variantes et erreurs
        Note over U,V: Session valide: accès à la vue
        Note over U,V: Login déjà ouvert: retour accueil
    end
```

**Résultat attendu :** redirection vers login depuis une vue protégée selon le scénario décrit.

## D03 — Changer la langue de l interface

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MainLayout
    participant S as Locale/session Vaadin
    participant DB as MySQL (Session)
    U->>V: Ouvre le sélecteur
    V->>S: Choisit fr/en/de
    S->>DB: Stocke app.locale
    DB-->>S: Applique ui.setLocale
    S-->>V: Recharge la vue
    opt Variantes et erreurs
        Note over U,V: Valeur non issue du client: changement ignoré
        Note over U,V: Textes en dur: restent en français
    end
```

**Résultat attendu :** changer la langue de l interface selon le scénario décrit.

## D04 — Recherche depuis la barre de navigation

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MainLayout
    participant S as Vaadin Router
    participant R as CategorieView
    U->>V: Saisit nom et catégorie
    V->>S: Valide par bouton ou Entrée
    S->>R: Encode le nom en UTF-8
    R-->>S: Navigue vers /categorie/{id}?nom=...
    S-->>V: La vue cible initialise ses filtres
    opt Variantes et erreurs
        Note over U,V: Recherche vide autorisée
        Note over U,V: Catégorie par défaut 0
    end
```

**Résultat attendu :** recherche depuis la barre de navigation selon le scénario décrit.

## D05 — Afficher la page d accueil et ses annonces

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MainView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article)
    U->>V: Ouvre /
    V->>S: Demande findAllTries(page,10)
    S->>R: Exécute la requête triée
    R->>DB: Retourne les annonces
    DB-->>R: Affiche image nom marque prix statut et badge pub
    opt Variantes et erreurs
        Note over U,V: Liste vide: main.empty
        Note over U,V: Page invalide: ramenée à 1
        Note over U,V: Page au-delà du total: liste vide
    end
```

**Résultat attendu :** afficher la page d accueil et ses annonces selon le scénario décrit.

## D06 — Paginer les annonces de l accueil

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as MainView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article)
    U->>V: Clique page suivante/précédente
    V->>S: Calcule la page cible
    S->>R: Appelle findAllTries(page,10)
    R->>DB: Lit les annonces
    DB-->>R: Remplace la grille
    opt Variantes et erreurs
        Note over U,V: Page nulle ou négative: page 1
        Note over U,V: Page trop élevée: résultat vide
    end
```

**Résultat attendu :** paginer les annonces de l accueil selon le scénario décrit.

## D07 — Consulter À propos

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as AProposView
    participant S as —
    U->>V: Navigue vers /a-propos
    V-->>U: Construit le titre et le texte
    U->>V: Affiche le contenu statique
    opt Variantes et erreurs
        Note over U,V: Aucune action métier ni accès base
    end
```

**Résultat attendu :** consulter à propos selon le scénario décrit.
