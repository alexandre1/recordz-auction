# Utilisateurs, boutiques et dépôts

[← Modèle de données](../modele-de-donnees.md) · [Dictionnaire](dictionnaire.md)

Comptes (`personne`), boutiques, dépôts, relations entre visiteurs et comptes techniques.

```mermaid
erDiagram
    canton_fr |o--o{ personne : "ref_canton"
    personne ||--o{ boutique : "ref_personne"
    boutique ||--o{ boutique_a_categorie : "ref_boutique"
    categorie ||--o{ boutique_a_categorie : "ref_categorie"
    personne ||--o{ depot : "ref_responsable"
    personne ||--o{ visiteur : "ref_personne"
    personne ||--o{ visiteur : "ref_visiteur"
    pays ||..o{ depot : "ref_pays (sans FK)"
    personne {
        int id_personne PK
        varchar nom_utilisateur
        varchar mot_de_passe
        varchar nom
        varchar prenom
        varchar adresse
        int npa
        varchar ville
        varchar pays
        varchar email
        varchar no_telephone
        int active
        int level
        varchar ref_host
        varchar lang
        varchar banque
        varchar no_compte
        varchar iban
        varchar msn_messenger
        varchar skype_name
        varchar date_start
        date date_expiration
        int ref_canton FK
        varchar paypal_me
    }
    boutique {
        int id_boutique PK
        varchar nom
        date date_creation
        int ref_personne FK
        int ref_main_categorie
        varchar login
    }
    boutique_a_categorie {
        int ref_boutique PK, FK
        int ref_categorie PK, FK
    }
    depot {
        int id_depot PK
        varchar nom
        varchar adresse
        varchar ville
        int npa
        int ref_pays "réf. sans FK"
        varchar telephone
        varchar email
        int ref_responsable PK, FK
    }
    visiteur {
        int ref_personne PK, FK
        int ref_visiteur PK, FK
    }
    app_user {
        int id PK
        varchar email
        varchar display_name
        varchar avatar_url
        datetime created_at
        datetime updated_at
    }
    sessions {
        char id PK
        text a_session
        varchar username
    }
    pays_present {
        int id_pays_present PK
        varchar nom
    }
    type_de_compte {
        int id PK
    }
```

Ligne continue : clé étrangère déclarée. Ligne pointillée : référence logique sans contrainte en base. Les tables sans colonnes dans le diagramme appartiennent à un autre domaine.

## Tables de ce domaine

| Table | Colonnes | Lignes (dump) | Références sortantes |
|---|---:|---:|---|
| [app_user](dictionnaire.md#app_user) | 6 | 0 | - |
| [boutique](dictionnaire.md#boutique) | 6 | 0 | personne |
| [boutique_a_categorie](dictionnaire.md#boutique_a_categorie) | 2 | 0 | boutique, categorie |
| [depot](dictionnaire.md#depot) | 9 | 0 | personne |
| [pays_present](dictionnaire.md#pays_present) | 2 | 2 | - |
| [personne](dictionnaire.md#personne) | 24 | 4 | canton_fr |
| [sessions](dictionnaire.md#sessions) | 3 | 0 | - |
| [type_de_compte](dictionnaire.md#type_de_compte) | 1 | 0 | - |
| [visiteur](dictionnaire.md#visiteur) | 2 | 0 | personne |
