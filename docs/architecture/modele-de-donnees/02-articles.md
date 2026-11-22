# Articles, catalogue et suivi

[← Modèle de données](../modele-de-donnees.md) · [Dictionnaire](dictionnaire.md)

Annonces (`article`), mise en vente, commentaires, visites, demandes de visite et recherches.

```mermaid
erDiagram
    boite_de_vitesse |o--o{ article : "ref_boite_de_vitesse"
    canton_fr |o--o{ article : "ref_canton"
    cepage |o--o{ article : "ref_cepage"
    type_essence |o--o{ article : "essence_ou_diesel"
    etat |o--o{ article : "ref_etat"
    condition_livraison ||--o{ article : "ref_mode_de_livraison"
    pays_region_vin |o--o{ article : "ref_pays_region_vin"
    type_ecran |o--o{ article : "ref_type_ecran"
    type_de_jeux |o--o{ article : "ref_type_de_jeux"
    article ||--o{ met_en_vente : "ref_article"
    personne ||--o{ met_en_vente : "ref_vendeur"
    article ||--o| article_ip : "ref_article"
    article ||--o{ article_visite : "ref_article"
    personne ||--o{ article_visite : "ref_personne"
    article ||--o{ commentaire : "ref_article"
    personne ||--o{ commentaire : "ref_emetteur"
    article ||--o{ demande_visite : "ref_article"
    personne ||--o{ demande_visite : "ref_vendeur"
    categorie ||--o{ recherche : "ref_categorie"
    genre ||..o{ article : "ref_genre (sans FK)"
    categorie ||..o{ article : "ref_categorie (sans FK)"
    depot ||..o{ article : "ref_depot (sans FK)"
    condition_payement_ref ||..o{ article : "ref_condition_payement (sans FK)"
    departement ||..o{ article : "ref_departement (sans FK)"
    pays ||..o{ article : "ref_pays (sans FK)"
    article {
        int id_article PK
        varchar auteur
        varchar marque
        varchar nom
        text label
        double prix
        decimal prix_achat
        varchar pochette
        varchar presound
        int ref_genre "réf. sans FK"
        int etat
        int ref_categorie "réf. sans FK"
        int ref_subcategorie
        int owned
        int ref_statut
        datetime date
        int ref_depot "réf. sans FK"
        int ref_taille
        int ref_couleur
        int enchere
        int ref_condition_payement "réf. sans FK"
        int ref_mode_de_livraison FK
        varchar enchere_date_debut
        varchar enchere_date_fin
        int vendu
        int ref_type_ecran FK
        int dimension
        int poids
        int nb_porte
        int nb_cheveaux
        int nb_km
        varchar premiere_immatriculation
        int annee
        text options
        int essence_ou_diesel FK
        int nb_piece
        int surface_habitable
        varchar superficie_terrain
        varchar annee_construction
        int visites
        int nbr_enchere
        varchar lang
        int ref_canton FK
        varchar lieu
        varchar adresse
        varchar npa
        int ref_location_ou_achat
        int ref_departement "réf. sans FK"
        int ref_pays "réf. sans FK"
        int ref_boite_de_vitesse FK
        tinyint clima
        decimal processeur
        varchar ram
        varchar disque_dur
        int quantite
        tinyint ref_provenance
        int longueur
        int largeur
        int consomation
        varchar acteurs
        varbinary duree
        varchar realisateur
        varchar taille
        int ref_type_de_jeux FK
        int ref_cepage FK
        int ref_pays_region_vin FK
        int ref_type_de_vin
        int ref_etat FK
        decimal frais_livraison
        int wat
        int nb_cylindre
        int pub
        varchar pub_date_start
        varchar pub_date_end
        varchar tx
        varchar link_youtube
        varchar pochette2
        varchar entrupy_customer_item_id
        varchar entrupy_status
        varchar entrupy_id
        varchar entrupy_certificate_url
        timestamp entrupy_updated_at
        varchar video_path
    }
    met_en_vente {
        int ref_vendeur PK, FK
        int ref_article PK, FK
        date date_stock
        varchar page_principale
        varchar page_categorie
        varchar pack_photo
        int notre_selection
        int statut
    }
    article_ip {
        int ref_article PK, FK
        varchar ip
    }
    article_visite {
        int id_visite PK
        int ref_article FK
        int ref_personne FK
        varchar nom
        varchar prenom
        text adresse
        varchar ville
        int npa
        varchar no_prive
        int no_mobile
        text commentaire
        int par_email
        int par_telephone
        int par_mobile
        varchar email
        timestamp date_visite
    }
    commentaire {
        int id_commentaire PK
        int ref_article FK
        int ref_emetteur FK
        varchar question
        text texte
        datetime date
    }
    demande_visite {
        int id_demande PK
        varchar nom
        varchar prenom
        varchar adresse
        varchar tel_mobile
        int ref_article FK
        int ref_vendeur FK
        text commentaire
        varchar email
        datetime date_visite
        datetime date_visite_fin
    }
    recherche {
        int id_recherche PK
        varchar nom
        int nbr
        int ref_categorie FK
    }
    categorie {
        int id_categorie PK
    }
    genre {
        int id_genre PK
        varchar genre
    }
    pointure {
        int id_pointure PK
        varchar valeur
    }
    publication_option {
        int id PK
    }
```

Ligne continue : clé étrangère déclarée. Ligne pointillée : référence logique sans contrainte en base. Les tables sans colonnes dans le diagramme appartiennent à un autre domaine.

## Tables de ce domaine

| Table | Colonnes | Lignes (dump) | Références sortantes |
|---|---:|---:|---|
| [article](dictionnaire.md#article) | 83 | 7 | boite_de_vitesse, canton_fr, cepage, condition_livraison, etat, pays_region_vin, type_de_jeux, type_ecran, type_essence |
| [article_ip](dictionnaire.md#article_ip) | 2 | 0 | article |
| [article_visite](dictionnaire.md#article_visite) | 16 | 272 | article, personne |
| [categorie](dictionnaire.md#categorie) | 1 | 45 | - |
| [commentaire](dictionnaire.md#commentaire) | 6 | 3 | article, personne |
| [demande_visite](dictionnaire.md#demande_visite) | 11 | 10 | article, personne |
| [genre](dictionnaire.md#genre) | 2 | 0 | - |
| [met_en_vente](dictionnaire.md#met_en_vente) | 8 | 7 | article, personne |
| [pointure](dictionnaire.md#pointure) | 2 | 0 | - |
| [publication_option](dictionnaire.md#publication_option) | 1 | 0 | - |
| [recherche](dictionnaire.md#recherche) | 4 | 0 | categorie |
