# 02 Catalogue Recherche

[← Index des diagrammes](./README.md)


## D08 — Parcourir les annonces par catégorie

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as CategorieView
    participant S as ArticleService / ReferenceService
    participant R as ArticleRepository / ReferentielRepository
    participant DB as MySQL (article + référentiels)
    U->>V: Ouvre /categorie/{id}
    V->>S: Charge catégories et sous-catégories
    S->>R: Appelle la recherche paginée
    R->>DB: Retourne 10 annonces
    DB-->>R: Affiche la grille
    opt Variantes et erreurs
        Note over U,V: Catégorie inconnue: filtre appliqué mais sélecteur inchangé
        Note over U,V: Aucun résultat: main.empty
    end
```

**Résultat attendu :** parcourir les annonces par catégorie selon le scénario décrit.

## D09 — Changer la catégorie et réinitialiser la sous-catégorie

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as CategorieView
    participant S as ReferenceService + ArticleService
    participant R as ReferentielRepository + ArticleRepository
    participant DB as MySQL (référentiels + article)
    U->>V: Choisit une catégorie
    V->>S: Efface la sous-catégorie
    S->>R: Charge les sous-catégories de la catégorie
    R->>DB: Réinitialise page à 1
    DB-->>R: Recharge les annonces
    opt Variantes et erreurs
        Note over U,V: Échec service: pas de traitement local explicite
    end
```

**Résultat attendu :** changer la catégorie et réinitialiser la sous-catégorie selon le scénario décrit.

## D10 — Filtrer par sous-catégorie

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as CategorieView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article)
    U->>V: Choisit une sous-catégorie
    V->>S: Met à jour le filtre
    S->>R: Réinitialise page à 1
    R->>DB: Recherche les annonces correspondantes
    DB-->>R: Affiche les résultats
    opt Variantes et erreurs
        Note over U,V: Aucun résultat: message de liste vide
    end
```

**Résultat attendu :** filtrer par sous-catégorie selon le scénario décrit.

## D11 — Paginer les résultats par catégorie

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as CategorieView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article)
    U->>V: Clique page N
    V->>S: Met à jour le paramètre page
    S->>R: Relance la recherche filtrée
    R->>DB: Retourne les annonces
    DB-->>R: Rend la page demandée
    opt Variantes et erreurs
        Note over U,V: Page invalide: valeur ramenée à 1
    end
```

**Résultat attendu :** paginer les résultats par catégorie selon le scénario décrit.

## D12 — Recherche avancée multicritère

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as CategorieView / dialogue avancé
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + attributs dynamiques)
    U->>V: Ouvre le dialogue
    V->>S: Saisit nom marque catégorie sous-catégorie prix et enchères
    S->>R: Collecte les critères dans FiltreArticle
    R->>DB: Collecte les champs dynamiques
    DB-->>R: Appelle findByFiltresDynamiques et countByFiltresDynamiques
    R-->>S: Remplace la liste et ferme le dialogue
    opt Variantes et erreurs
        Note over U,V: Numérique invalide: converti en null
        Note over U,V: Annuler: aucune modification
        Note over U,V: Réinitialiser: réinitialise certains champs seulement
    end
```

**Résultat attendu :** recherche avancée multicritère selon le scénario décrit.

## D13 — Recherche avancée avec attributs spécifiques de catégorie

```mermaid
sequenceDiagram
    actor U as Visiteur
    participant V as Dialogue recherche avancée
    participant S as ArticleDynamicDataService
    participant R as ArticleRepository / référentiels
    participant DB as MySQL (attributs catégorie)
    U->>V: Sélectionne une catégorie
    V->>S: Affiche les champs dynamiques associés
    S->>R: Renseigne les valeurs propres au domaine
    R->>DB: Collecte les valeurs
    DB-->>R: Transmet le filtre dynamique à la recherche
    opt Variantes et erreurs
        Note over U,V: Champ disque dur affiché mais non collecté
        Note over U,V: Anciens filtres dynamiques non purgés au changement de catégorie
    end
```

**Résultat attendu :** recherche avancée avec attributs spécifiques de catégorie selon le scénario décrit.
