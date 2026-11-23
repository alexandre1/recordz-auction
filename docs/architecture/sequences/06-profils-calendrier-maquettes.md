# 06 Profils Calendrier Maquettes

[← Index des diagrammes](./README.md)


## D51 — Modifier les informations personnelles

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as InformationsPersonnellesView
    participant S as PersonneService
    participant R as PersonneRepository
    participant DB as MySQL (personne)
    U->>V: Charge la personne par e-mail OIDC
    V->>S: Affiche adresse téléphone IBAN PayPal et pays
    S->>R: Valide NPA
    R->>DB: Convertit téléphone E.164
    DB-->>R: Appelle updateProfil
    R-->>S: Confirme la sauvegarde
    opt Variantes et erreurs
        Note over U,V: NPA non numérique: erreur
        Note over U,V: Téléphone invalide: erreur
        Note over U,V: Personne absente: RuntimeException
        Note over U,V: IBAN non validé
    end
```

**Résultat attendu :** modifier les informations personnelles selon le scénario décrit.

## D52 — Réinitialiser certains champs du profil personnel

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as InformationsPersonnellesView
    participant S as —
    U->>V: Clique Réinitialiser
    V-->>U: Vide prénom nom adresse ville NPA téléphone
    U->>V: Conserve pays numéro rue IBAN PayPal
    V-->>U: Ne recharge pas les données initiales
    opt Variantes et erreurs
        Note over U,V: Enregistrer après reset peut effacer le prénom en base
        Note over U,V: Comportement partiel documenté
    end
```

**Résultat attendu :** réinitialiser certains champs du profil personnel selon le scénario décrit.

## D53 — Consulter les statistiques d un profil

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as ProfilView
    participant S as PersonneService
    participant R as PersonneRepository
    participant DB as MySQL (personne)
    U->>V: Ouvre /profil/{username}
    V->>S: Recherche par nom utilisateur
    S->>R: Charge les statistiques de profil
    R->>DB: Affiche la fiche synthétique
    opt Variantes et erreurs
        Note over U,V: Utilisateur inconnu: profil.notfound
        Note over U,V: Paramètre username utilisé aussi comme e-mail dans une statistique à vérifier
    end
```

**Résultat attendu :** consulter les statistiques d un profil selon le scénario décrit.

## D54 — Déplier les enchères ouvertes d un profil

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as ProfilView
    participant S as EnchereService
    participant R as EnchereRepository
    participant DB as MySQL (enchere)
    U->>V: Clique la ligne enchères ouvertes
    V->>S: Appelle findVentesEncheres
    S->>R: Retourne les enchères
    R->>DB: Affiche le détail sous les statistiques
    opt Variantes et erreurs
        Note over U,V: Liste vide: profil.empty.vente
    end
```

**Résultat attendu :** déplier les enchères ouvertes d un profil selon le scénario décrit.

## D55 — Déplier les ventes directes d un profil

```mermaid
sequenceDiagram
    actor U as Utilisateur
    participant V as ProfilView
    participant S as ArticleService
    participant R as ArticleRepository
    participant DB as MySQL (article + met_en_vente)
    U->>V: Clique ventes ouvertes ou fermées
    V->>S: Appelle findVentesDirectes
    S->>R: Retourne les annonces
    R->>DB: Affiche le détail
    opt Variantes et erreurs
        Note over U,V: Liste vide: message profil.empty.vente
    end
```

**Résultat attendu :** déplier les ventes directes d un profil selon le scénario décrit.
