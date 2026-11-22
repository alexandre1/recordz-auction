# Modèle de données

Diagramme entité-relation **déduit du code Java** (classes de `model/domain`) et des requêtes SQL
visibles dans les services et le scheduler. Il n'a pas été vérifié contre le script
`dump_new_db_recordzv3.sql` : les noms de colonnes en `snake_case` sont déduits des noms Java,
et les relations viennent des colonnes `ref_*`. Les clés étrangères réellement déclarées en base
peuvent différer.

```mermaid
erDiagram
    PERSONNE ||--o{ ENCHERE : "ref_enchereur"
    ARTICLE ||--o{ ENCHERE : "ref_article"
    PERSONNE ||--o{ MET_EN_VENTE : "ref_vendeur"
    ARTICLE ||--o| MET_EN_VENTE : "ref_article"
    ARTICLE ||--o{ A_PAYE : "ref_article"
    PERSONNE ||--o{ A_PAYE : "ref_acheteur / ref_vendeur"
    PERSONNE ||--o{ COMMANDE : "client_ref / ref_vendeur"
    PERSONNE ||--o{ TRANSACTION : "ref_acheteur / ref_vendeur"
    ARTICLE ||--o{ TRANSACTION : "ref_article"
    ARTICLE ||--o{ COMMENTAIRE : "ref_article"
    PERSONNE ||--o{ COMMENTAIRE : "ref_emetteur"
    PERSONNE ||--o{ WISHLIST : "ref_user"
    WISHLIST ||--o{ WISH : "ref_wish_list"
    PERSONNE ||--o{ BOUTIQUE : "ref_personne"
    REF_STATUT ||--o{ ARTICLE : "ref_statut"

    PERSONNE {
        bigint id_personne PK
        string nom_utilisateur
        string nom
        string prenom
        string email
        string adresse
        int npa
        string ville
        string pays
        string no_telephone
        int active
        int level
        string lang
        int ref_canton
        date date_expiration
    }

    ARTICLE {
        bigint id_article PK
        string nom
        string marque
        string auteur
        double prix
        int ref_categorie
        int ref_subcategorie
        int ref_statut FK
        int enchere "1 = mise aux enchères"
        string enchere_date_debut
        string enchere_date_fin
        int vendu "1 = vendu"
        int ref_condition_payement
        int ref_mode_de_livraison
        string entrupy_customer_item_id
        string entrupy_status
        string entrupy_id
        string entrupy_certificate_url
    }

    ENCHERE {
        bigint id_enchere PK
        bigint ref_article FK
        bigint ref_enchereur FK
        decimal prix
        datetime date_enchere
    }

    MET_EN_VENTE {
        int ref_article FK
        int ref_vendeur FK
    }

    A_PAYE {
        int ref_article FK
        int ref_vendeur FK
        int ref_acheteur FK
        int ref_statut
        datetime date_fermeture_enchere
        int ref_condition_payement
        int ref_mode_de_livraison
        double montant
    }

    COMMANDE {
        bigint id_commande PK
        string session_id
        bigint client_ref FK
        bigint ref_vendeur FK
        date date
        date date_payement
        int ref_mode_de_payement
        int ref_mode_de_livraison
        int ref_statut
    }

    TRANSACTION {
        bigint id_transaction PK
        bigint ref_acheteur FK
        bigint ref_vendeur FK
        bigint ref_article FK
        datetime date
        decimal prix
        int validate
    }

    COMMENTAIRE {
        bigint id_commentaire PK
        bigint ref_article FK
        bigint ref_emetteur FK
        string question
        string texte
        datetime date
    }

    WISHLIST {
        bigint id_wish_list PK
        bigint ref_user FK
        date date_creation
    }

    WISH {
        bigint id_wish PK
        bigint ref_wish_list FK
        bigint ref_user FK
        string nom
        string marque
        int ref_categorie
        int vendu
    }

    BOUTIQUE {
        bigint id_boutique PK
        string nom
        bigint ref_personne FK
        int ref_main_categorie
        string login
        date date_creation
    }

    REF_STATUT {
        int id_statut PK
    }
```

Le diagramme ne liste que les colonnes structurantes. L'entité `Article` en porte beaucoup plus
(voir ci-dessous).

## Cycle de vie d'un article mis aux enchères

Déduit de `EnchereScheduler` et de `ArticleAuthenticationService`.

```mermaid
stateDiagram-v2
    [*] --> EnVente: article créé
    EnVente --> EnVente: nouvelle enchère (EnchereService)
    EnVente --> Invendu: échéance sans offre (ref_statut = 8)
    EnVente --> Vendu: échéance avec offre (ref_statut = 1, vendu = 1)
    Vendu --> AttentePaiement: ligne créée dans a_paye (statut 8)
    AttentePaiement --> [*]
    Invendu --> [*]
```

Pour les articles soumis à Entrupy, `ref_statut` passe à `2` (valeur à confirmer, voir le `TODO`
du code) quand le webhook `session.completed` annonce un résultat `authentic`.

## Spécificités de l'entité Article

`Article` est une classe unique qui regroupe les attributs de toutes les catégories. Seuls les
champs de la catégorie concernée sont renseignés.

| Catégorie | Champs propres |
|---|---|
| Véhicules | `nb_km`, `premiere_immatriculation`, `nb_cheveaux`, `nb_cylindre`, `essence_ou_diesel`, `ref_boite_de_vitesse`, `clima`, `nb_porte` |
| Immobilier | `nb_piece`, `surface_habitable`, `superficie_terrain`, `annee_construction`, `ref_location_ou_achat` |
| Vins | `ref_cepage`, `ref_type_de_vin`, `ref_pays_region_vin`, millésime (stocké dans `tx`) |
| DVD | `acteurs`, `realisateur`, durée (stockée dans `presound`) |
| Informatique | `processeur`, `ram`, `disque_dur` |
| TV | `ref_type_ecran`, `dimension` |
| Vêtements | `ref_taille`, `taille`, `ref_couleur` |
| Jeux | `ref_type_de_jeux` |
| Authentification | `entrupy_status`, `entrupy_id`, `entrupy_certificate_url`, `entrupy_customer_item_id`, `entrupy_updated_at` |

Les catégories (`ref_categorie`) sont aussi décrites par l'énumération `ArticleCategory`
(Mode, Montres, Bijoux, Voiture, Vins, Immobilier, etc.) et les sous-catégories par
`ArticleSubCategory`.

> Plusieurs colonnes sont réutilisées pour des usages différents selon la catégorie
> (`tx` pour le millésime, `presound` pour la durée d'un DVD) : à garder en tête avant de
> modifier le schéma.
