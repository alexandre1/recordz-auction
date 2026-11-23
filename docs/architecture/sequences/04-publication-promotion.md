# 04 Publication Promotion

[← Index des diagrammes](./README.md)


## D21 — Ouvrir le formulaire de publication

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as ArticleFormView
    participant S as AuthenticatedUser / PersonneService
    participant R as PersonneRepository
    participant DB as MySQL (personne)
    U->>V: Ouvre /ajouter-article
    V->>S: Vérifie session OIDC et e-mail
    S->>R: Construit DynamicArticleForm
    R->>DB: Charge référentiels de catégories et conditions
    DB-->>R: Affiche le formulaire
    opt Variantes et erreurs
        Note over U,V: Compte personne absent: publication non initialisable
        Note over U,V: Route protégée pour anonyme
    end
```

**Résultat attendu :** ouvrir le formulaire de publication selon le scénario décrit.

## D22 — Renseigner les champs communs d une annonce

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as DynamicArticleForm
    participant S as ArticleSubmitService
    U->>V: Saisit nom description prix condition paiement livraison état quantité
    V->>S: Téléverse une image
    S-->>V: Contrôle format image et taille
    V-->>U: Conserve les données du formulaire
    opt Variantes et erreurs
        Note over U,V: Formats autorisés jpeg png gif webp
        Note over U,V: Taille max 1 Mo
        Note over U,V: Nom catégorie prix obligatoires vérifiés à la validation
    end
```

**Résultat attendu :** renseigner les champs communs d une annonce selon le scénario décrit.

## D23 — Afficher les champs dynamiques par catégorie

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as DynamicArticleForm
    participant S as ArticleDynamicDataService / ReferenceService
    participant R as ReferentielRepository
    participant DB as MySQL (référentiels)
    U->>V: Choisit une catégorie et sous-catégorie
    V->>S: Demande les champs spécifiques
    S->>R: Construit les composants adaptés
    R->>DB: Affiche les attributs métier
    DB-->>R: Collecte leurs valeurs à la soumission
    opt Variantes et erreurs
        Note over U,V: Champs spécifiques non tous validés par isValid
        Note over U,V: Vidéo non intégrée au panneau visible
    end
```

**Résultat attendu :** afficher les champs dynamiques par catégorie selon le scénario décrit.

## D24 — Configurer une vente à prix fixe

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as DynamicArticleForm
    participant S as ArticleSubmitService
    participant R as ArticleRepository
    participant DB as MySQL (article + met_en_vente)
    U->>V: Sélectionne prix fixe
    V->>S: Renseigne quantité et prix
    S->>R: Valide le formulaire
    R->>DB: Collecte ArticleFormData
    DB-->>R: Transmet au service de sauvegarde
    opt Variantes et erreurs
        Note over U,V: Prix absent ou négatif: erreur
        Note over U,V: Quantité minimale 1
    end
```

**Résultat attendu :** configurer une vente à prix fixe selon le scénario décrit.

## D25 — Configurer une vente aux enchères

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as DynamicArticleForm
    participant S as ArticleSubmitService / EnchereService
    participant R as ArticleRepository / EnchereRepository
    participant DB as MySQL (article + met_en_vente + enchere)
    U->>V: Sélectionne enchère
    V->>S: Choisit durée 1 à 30 jours
    S->>R: Calcule date début et fin
    R->>DB: Masque quantité
    DB-->>R: Collecte les données
    R-->>S: Soumet l annonce
    opt Variantes et erreurs
        Note over U,V: Durée absente: date fin peut rester nulle
        Note over U,V: Durée hors plage: formulaire à corriger
    end
```

**Résultat attendu :** configurer une vente aux enchères selon le scénario décrit.

## D26 — Publier une annonce standard avec transaction SQL

```mermaid
sequenceDiagram
    actor U as Vendeur
    participant V as ArticleFormView
    participant S as ArticleSubmitService
    participant R as ArticleRepository
    participant DB as MySQL (article + met_en_vente)
    U->>V: Clique Publier
    V->>S: Exécute isValid
    S->>R: Collecte les valeurs
    R->>DB: Ouvre transaction
    DB-->>R: Insère article
    R-->>S: Insère met_en_vente avec vendeur
    S-->>V: Commit
    V-->>U: Écrit image après commit
    U->>V: Redirige vers detail/{id}
    opt Variantes et erreurs
        Note over U,V: Validation échouée: erreurs sous champs
        Note over U,V: Échec SQL: rollback et notification
        Note over U,V: Échec image: compensation suppression article
    end
```

**Résultat attendu :** publier une annonce standard avec transaction sql selon le scénario décrit.

## D27 — Traiter le webhook Entrupy

```mermaid
sequenceDiagram
    actor U as Système Entrupy
    participant V as EntrupyWebhookController
    participant S as EntrupyWebhookService
    participant R as ArticleService / ArticleRepository
    participant DB as MySQL (article)
    U->>V: Entrupy envoie POST webhook
    V->>S: Contrôle et désérialise le payload
    S->>R: Résout l article par identifiant externe
    R->>DB: Met à jour statut et données d authentification
    DB-->>R: Retourne réponse HTTP
    opt Variantes et erreurs
        Note over U,V: Payload invalide ou article inconnu: erreur webhook
        Note over U,V: Contrat exact des champs dépend de la version du service
    end
```

**Résultat attendu :** traiter le webhook entrupy selon le scénario décrit.
