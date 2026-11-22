# Listes de souhaits

[← Modèle de données](../modele-de-donnees.md) · [Dictionnaire](dictionnaire.md)

Listes de souhaits des utilisateurs et leurs éléments.

```mermaid
erDiagram
    personne ||--o{ wish_list : "ref_user"
    canton_fr |o--o{ wish : "ref_canton"
    categorie ||--o{ wish : "ref_categorie"
    wish_list ||--o{ wish : "ref_wish_list"
    condition_livraison ||--o{ wish : "ref_mode_de_livraison"
    condition_payement_ref ||--o{ wish : "ref_condition_payement"
    personne ||--o{ wish : "ref_user"
    genre ||..o{ wish : "ref_genre (sans FK)"
    depot ||..o{ wish : "ref_depot (sans FK)"
    pays ||..o{ wish : "ref_pays (sans FK)"
    wish_list {
        int id_wish_list PK
        int ref_user FK
        date date_creation
    }
    wish {
        int id_wish PK
        int ref_wish_list FK
        int ref_user FK
        varchar auteur
        varchar marque
        varchar nom
        text label
        int prix
        varchar pochette
        varchar presound
        int ref_genre "réf. sans FK"
        varchar etat
        int ref_categorie FK
        int ref_subcategorie
        int owned
        int ref_statut
        datetime date
        int ref_depot "réf. sans FK"
        int enchere
        int ref_condition_payement FK
        int ref_mode_de_livraison FK
        datetime enchere_date_debut
        datetime enchere_date_fin
        int vendu
        varchar lang
        int ref_canton FK
        int ref_pays "réf. sans FK"
    }
```

Ligne continue : clé étrangère déclarée. Ligne pointillée : référence logique sans contrainte en base. Les tables sans colonnes dans le diagramme appartiennent à un autre domaine.

## Tables de ce domaine

| Table | Colonnes | Lignes (dump) | Références sortantes |
|---|---:|---:|---|
| [wish](dictionnaire.md#wish) | 27 | 0 | canton_fr, categorie, condition_livraison, condition_payement_ref, personne, wish_list |
| [wish_list](dictionnaire.md#wish_list) | 3 | 0 | personne |
