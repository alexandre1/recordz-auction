# Dictionnaire des tables

[← Modèle de données](../modele-de-donnees.md)

Généré à partir de `dump_new_db_recordzv3.sql`. « Lignes » est le nombre de lignes présentes dans ce dump (approximatif). Les colonnes **FK** sont des clés étrangères déclarées ; **réf. logique** désigne une colonne qui référence une table sans que la base le contraigne.

## Index

- **[Utilisateurs, boutiques et dépôts](01-utilisateurs.md)** : [app_user](#app_user), [boutique](#boutique), [boutique_a_categorie](#boutique_a_categorie), [depot](#depot), [pays_present](#pays_present), [personne](#personne), [sessions](#sessions), [type_de_compte](#type_de_compte), [visiteur](#visiteur)
- **[Articles, catalogue et suivi](02-articles.md)** : [article](#article), [article_ip](#article_ip), [article_visite](#article_visite), [categorie](#categorie), [commentaire](#commentaire), [demande_visite](#demande_visite), [genre](#genre), [met_en_vente](#met_en_vente), [pointure](#pointure), [publication_option](#publication_option), [recherche](#recherche)
- **[Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md)** : [a_livre](#a_livre), [a_paye](#a_paye), [commande](#commande), [commande_article](#commande_article), [condition_livraison](#condition_livraison), [condition_payement_ref](#condition_payement_ref), [enchere](#enchere), [evaluation_achat](#evaluation_achat), [evaluation_article](#evaluation_article), [evaluation_vente](#evaluation_vente), [mode_de_payement](#mode_de_payement), [transaction](#transaction)
- **[Listes de souhaits](04-wishlist.md)** : [wish](#wish), [wish_list](#wish_list)
- **[Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md)** : [boite_de_vitesse](#boite_de_vitesse), [canton_fr](#canton_fr), [cepage](#cepage), [departement](#departement), [etat](#etat), [mois](#mois), [pays](#pays), [pays_region_vin](#pays_region_vin), [states](#states), [temps](#temps), [type_de_jeux](#type_de_jeux), [type_ecran](#type_ecran), [type_essence](#type_essence)
- **[Libellés multilingues](06-multilingue.md)** : [boite_de_vitesse_libelle_langue](#boite_de_vitesse_libelle_langue), [categorie_libelle_langue](#categorie_libelle_langue), [condition_livraison_libelle_langue](#condition_livraison_libelle_langue), [condition_payement_libelle_langue](#condition_payement_libelle_langue), [etat_libelle_langue](#etat_libelle_langue), [langue](#langue), [libelle](#libelle), [location_ou_achat_libelle_langue](#location_ou_achat_libelle_langue), [main_categorie_libelle_langue](#main_categorie_libelle_langue), [mode_de_payement_libelle_langue](#mode_de_payement_libelle_langue), [mois_libelle_langue](#mois_libelle_langue), [publication_option_libelle_langue](#publication_option_libelle_langue), [statut_libelle_langue](#statut_libelle_langue), [subcategorie_libelle_langue](#subcategorie_libelle_langue), [taille_libelle_langue](#taille_libelle_langue), [temps_libelle_langue](#temps_libelle_langue), [type_de_compte_libelle_langue](#type_de_compte_libelle_langue), [type_de_jeux_libelle_langue](#type_de_jeux_libelle_langue), [type_de_vin_libelle_langue](#type_de_vin_libelle_langue), [type_ecran_libelle_langue](#type_ecran_libelle_langue), [type_essence_libelle_langue](#type_essence_libelle_langue)

## Utilisateurs, boutiques et dépôts

### app_user

6 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |
| `email` | varchar(255) |  |  |
| `display_name` | varchar(255) | oui |  |
| `avatar_url` | varchar(1024) | oui |  |
| `created_at` | datetime(3) |  |  |
| `updated_at` | datetime(3) |  |  |

Contraintes d'unicité : (email)

### boutique

6 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_boutique` | int |  | PK, auto-incrément |
| `nom` | varchar(40) |  |  |
| `date_creation` | date |  |  |
| `ref_personne` | int |  | FK → [personne](#personne).id_personne |
| `ref_main_categorie` | int |  | référence sans table |
| `login` | varchar(100) | oui |  |

### boutique_a_categorie

2 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_boutique` | int |  | PK, FK → [boutique](#boutique).id_boutique |
| `ref_categorie` | int |  | PK, FK → [categorie](#categorie).id_categorie |

### depot

9 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_depot` | int |  | PK |
| `nom` | varchar(50) |  |  |
| `adresse` | varchar(300) |  |  |
| `ville` | varchar(150) |  |  |
| `npa` | int |  |  |
| `ref_pays` | int |  | réf. logique → [pays](#pays) |
| `telephone` | varchar(20) |  |  |
| `email` | varchar(30) |  |  |
| `ref_responsable` | int |  | PK, FK → [personne](#personne).id_personne |

### pays_present

2 colonnes, 2 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_pays_present` | int |  | PK, auto-incrément |
| `nom` | varchar(40) |  |  |

Contraintes d'unicité : (id_pays_present)

### personne

24 colonnes, 4 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_personne` | int |  | PK, auto-incrément |
| `nom_utilisateur` | varchar(40) |  |  |
| `mot_de_passe` | varchar(41) |  |  |
| `nom` | varchar(40) |  |  |
| `prenom` | varchar(40) |  |  |
| `adresse` | varchar(40) |  |  |
| `npa` | int |  |  |
| `ville` | varchar(40) |  |  |
| `pays` | varchar(40) |  |  |
| `email` | varchar(100) |  |  |
| `no_telephone` | varchar(20) |  |  |
| `active` | int |  |  |
| `level` | int |  |  |
| `ref_host` | varchar(50) |  |  |
| `lang` | varchar(10) | oui |  |
| `banque` | varchar(40) | oui |  |
| `no_compte` | varchar(40) | oui |  |
| `iban` | varchar(20) | oui |  |
| `msn_messenger` | varchar(20) | oui |  |
| `skype_name` | varchar(20) | oui |  |
| `date_start` | varchar(20) | oui |  |
| `date_expiration` | date | oui |  |
| `ref_canton` | int | oui | FK → [canton_fr](#canton_fr).id_canton |
| `paypal_me` | varchar(200) | oui |  |

Contraintes d'unicité : (nom_utilisateur), (email)

### sessions

3 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | char(32) |  | PK |
| `a_session` | text |  |  |
| `username` | varchar(100) |  |  |

### type_de_compte

1 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### visiteur

2 colonnes, 0 lignes dans le dump. Domaine : [Utilisateurs, boutiques et dépôts](01-utilisateurs.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_personne` | int |  | PK, FK → [personne](#personne).id_personne |
| `ref_visiteur` | int |  | PK, FK → [personne](#personne).id_personne |

## Articles, catalogue et suivi

### article

83 colonnes, 7 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_article` | int |  | PK, auto-incrément |
| `auteur` | varchar(40) | oui |  |
| `marque` | varchar(40) |  |  |
| `nom` | varchar(50) |  |  |
| `label` | text |  |  |
| `prix` | double |  |  |
| `prix_achat` | decimal(10,0) | oui |  |
| `pochette` | varchar(100) |  |  |
| `presound` | varchar(100) |  |  |
| `ref_genre` | int |  | réf. logique → [genre](#genre) |
| `etat` | int |  |  |
| `ref_categorie` | int |  | réf. logique → [categorie](#categorie) |
| `ref_subcategorie` | int | oui | référence sans table |
| `owned` | int |  |  |
| `ref_statut` | int |  | référence sans table |
| `date` | datetime |  |  |
| `ref_depot` | int |  | réf. logique → [depot](#depot) |
| `ref_taille` | int | oui | référence sans table |
| `ref_couleur` | int | oui | référence sans table |
| `enchere` | int |  |  |
| `ref_condition_payement` | int |  | réf. logique → [condition_payement_ref](#condition_payement_ref) |
| `ref_mode_de_livraison` | int |  | FK → [condition_livraison](#condition_livraison).id |
| `enchere_date_debut` | varchar(40) | oui |  |
| `enchere_date_fin` | varchar(40) | oui |  |
| `vendu` | int |  |  |
| `ref_type_ecran` | int | oui | FK → [type_ecran](#type_ecran).id |
| `dimension` | int | oui |  |
| `poids` | int | oui |  |
| `nb_porte` | int | oui |  |
| `nb_cheveaux` | int | oui |  |
| `nb_km` | int | oui |  |
| `premiere_immatriculation` | varchar(20) | oui |  |
| `annee` | int | oui |  |
| `options` | text | oui |  |
| `essence_ou_diesel` | int | oui | FK → [type_essence](#type_essence).id |
| `nb_piece` | int | oui |  |
| `surface_habitable` | int | oui |  |
| `superficie_terrain` | varchar(40) | oui |  |
| `annee_construction` | varchar(40) | oui |  |
| `visites` | int | oui |  |
| `nbr_enchere` | int | oui |  |
| `lang` | varchar(2) |  |  |
| `ref_canton` | int | oui | FK → [canton_fr](#canton_fr).id_canton |
| `lieu` | varchar(50) | oui |  |
| `adresse` | varchar(100) | oui |  |
| `npa` | varchar(4) | oui |  |
| `ref_location_ou_achat` | int | oui | référence sans table |
| `ref_departement` | int | oui | réf. logique → [departement](#departement) |
| `ref_pays` | int | oui | réf. logique → [pays](#pays) |
| `ref_boite_de_vitesse` | int | oui | FK → [boite_de_vitesse](#boite_de_vitesse).id |
| `clima` | tinyint | oui |  |
| `processeur` | decimal(10,2) | oui |  |
| `ram` | varchar(10) | oui |  |
| `disque_dur` | varchar(10) | oui |  |
| `quantite` | int | oui |  |
| `ref_provenance` | tinyint | oui | référence sans table |
| `longueur` | int | oui |  |
| `largeur` | int | oui |  |
| `consomation` | int | oui |  |
| `acteurs` | varchar(100) | oui |  |
| `duree` | varbinary(10) | oui |  |
| `realisateur` | varchar(40) | oui |  |
| `taille` | varchar(40) | oui |  |
| `ref_type_de_jeux` | int | oui | FK → [type_de_jeux](#type_de_jeux).id |
| `ref_cepage` | int | oui | FK → [cepage](#cepage).id_cepage |
| `ref_pays_region_vin` | int | oui | FK → [pays_region_vin](#pays_region_vin).id_pays_region_vin |
| `ref_type_de_vin` | int | oui | référence sans table |
| `ref_etat` | int | oui | FK → [etat](#etat).id |
| `frais_livraison` | decimal(2,2) | oui |  |
| `wat` | int | oui |  |
| `nb_cylindre` | int | oui |  |
| `pub` | int | oui |  |
| `pub_date_start` | varchar(40) | oui |  |
| `pub_date_end` | varchar(40) | oui |  |
| `tx` | varchar(20) | oui |  |
| `link_youtube` | varchar(500) |  |  |
| `pochette2` | varchar(100) | oui |  |
| `entrupy_customer_item_id` | varchar(64) | oui |  |
| `entrupy_status` | varchar(32) | oui |  |
| `entrupy_id` | varchar(64) | oui |  |
| `entrupy_certificate_url` | varchar(255) | oui |  |
| `entrupy_updated_at` | timestamp | oui |  |
| `video_path` | varchar(255) | oui |  |

Contraintes d'unicité : (entrupy_customer_item_id)

### article_ip

2 colonnes, 0 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_article` | int |  | PK, FK → [article](#article).id_article |
| `ip` | varchar(20) |  |  |

### article_visite

16 colonnes, 272 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_visite` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_personne` | int |  | FK → [personne](#personne).id_personne |
| `nom` | varchar(40) |  |  |
| `prenom` | varchar(40) |  |  |
| `adresse` | text | oui |  |
| `ville` | varchar(40) | oui |  |
| `npa` | int | oui |  |
| `no_prive` | varchar(40) | oui |  |
| `no_mobile` | int | oui |  |
| `commentaire` | text | oui |  |
| `par_email` | int | oui |  |
| `par_telephone` | int | oui |  |
| `par_mobile` | int | oui |  |
| `email` | varchar(40) |  |  |
| `date_visite` | timestamp |  |  |

### categorie

1 colonnes, 45 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_categorie` | int |  | PK |

### commentaire

6 colonnes, 3 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_commentaire` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_emetteur` | int |  | FK → [personne](#personne).id_personne |
| `question` | varchar(500) |  |  |
| `texte` | text |  |  |
| `date` | datetime |  |  |

### demande_visite

11 colonnes, 10 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_demande` | int |  | PK, auto-incrément |
| `nom` | varchar(40) |  |  |
| `prenom` | varchar(40) |  |  |
| `adresse` | varchar(100) |  |  |
| `tel_mobile` | varchar(30) |  |  |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `commentaire` | text |  |  |
| `email` | varchar(100) | oui |  |
| `date_visite` | datetime | oui |  |
| `date_visite_fin` | datetime | oui |  |

### genre

2 colonnes, 0 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_genre` | int |  | PK, auto-incrément |
| `genre` | varchar(40) |  |  |

### met_en_vente

8 colonnes, 7 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_vendeur` | int |  | PK, FK → [personne](#personne).id_personne |
| `ref_article` | int |  | PK, FK → [article](#article).id_article |
| `date_stock` | date |  |  |
| `page_principale` | varchar(40) | oui |  |
| `page_categorie` | varchar(40) | oui |  |
| `pack_photo` | varchar(20) | oui |  |
| `notre_selection` | int | oui |  |
| `statut` | int | oui |  |

### pointure

2 colonnes, 0 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_pointure` | int |  | PK, auto-incrément |
| `valeur` | varchar(4) |  |  |

Contraintes d'unicité : (valeur)

### publication_option

1 colonnes, 0 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |

### recherche

4 colonnes, 0 lignes dans le dump. Domaine : [Articles, catalogue et suivi](02-articles.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_recherche` | int |  | PK, auto-incrément |
| `nom` | varchar(40) |  |  |
| `nbr` | int |  |  |
| `ref_categorie` | int |  | FK → [categorie](#categorie).id_categorie |

## Enchères, commandes, paiements et évaluations

### a_livre

12 colonnes, 53 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_a_livre` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_acheteur` | int |  | FK → [personne](#personne).id_personne |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `ref_enchere` | int | oui | FK → [enchere](#enchere).id_enchere |
| `date_achat` | datetime | oui |  |
| `ref_mode_de_livraison` | int | oui | FK → [condition_livraison](#condition_livraison).id |
| `ref_statut` | int |  | référence sans table |
| `quantite` | int | oui |  |
| `montant` | varchar(10) | oui |  |
| `date_reception` | date | oui |  |
| `ref_a_paye` | int | oui | réf. logique → [a_paye](#a_paye) |

### a_paye

17 colonnes, 60 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_a_paye` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `ref_acheteur` | int |  | FK → [personne](#personne).id_personne |
| `ref_enchere` | int | oui | FK → [enchere](#enchere).id_enchere |
| `ref_condition_payement` | int |  | réf. logique → [condition_payement_ref](#condition_payement_ref) |
| `ref_mode_de_livraison` | int |  | FK → [condition_livraison](#condition_livraison).id |
| `ref_statut` | int |  | référence sans table |
| `montant` | decimal(10,0) |  |  |
| `date_fermeture_enchere` | datetime | oui |  |
| `date_payement` | datetime | oui |  |
| `date_echeance` | datetime | oui |  |
| `rappel_1` | date | oui |  |
| `rappel_2` | date | oui |  |
| `rappel_3` | date | oui |  |
| `quantite` | int |  |  |
| `ref_canton` | int | oui | FK → [canton_fr](#canton_fr).id_canton |

### commande

9 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_commande` | int |  | PK, auto-incrément |
| `session_id` | char(32) |  |  |
| `client_ref` | int |  | FK → [personne](#personne).id_personne |
| `date` | date |  |  |
| `ref_mode_de_payement` | int |  | FK → [mode_de_payement](#mode_de_payement).id |
| `date_payement` | date | oui |  |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `ref_statut` | int |  | référence sans table |
| `ref_mode_de_livraison` | int |  | FK → [condition_livraison](#condition_livraison).id |

### commande_article

3 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_commande` | int |  | PK, FK → [commande](#commande).id_commande |
| `ref_article` | int |  | PK, FK → [article](#article).id_article |
| `date_payement` | date |  | PK |

### condition_livraison

1 colonnes, 4 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### condition_payement_ref

1 colonnes, 5 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_condition_payement` | int |  | PK |

### enchere

5 colonnes, 2 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_enchere` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_enchereur` | int |  | FK → [personne](#personne).id_personne |
| `prix` | decimal(10,0) |  |  |
| `date_enchere` | datetime |  |  |

Contraintes d'unicité : (id_enchere)

### evaluation_achat

7 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_evaluation_achat` | int |  | PK, auto-incrément |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `ref_acheteur` | int |  | FK → [personne](#personne).id_personne |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `note` | tinyint |  |  |
| `commentaire` | text |  |  |
| `date` | datetime |  |  |

### evaluation_article

6 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_evaluation_article` | int |  | PK, auto-incrément |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `ref_vendeur` | int |  | réf. logique → [personne](#personne) |
| `ref_acheteur` | int |  | FK → [personne](#personne).id_personne |
| `note` | int |  |  |
| `date` | datetime |  |  |

### evaluation_vente

7 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_evaluation_vente` | int |  | PK, auto-incrément |
| `ref_vendeur` | int |  | FK → [personne](#personne).id_personne |
| `ref_acheteur` | int |  | FK → [personne](#personne).id_personne |
| `ref_article` | int |  | FK → [article](#article).id_article |
| `note` | tinyint |  |  |
| `commentaire` | text |  |  |
| `date` | datetime |  |  |

### mode_de_payement

1 colonnes, 0 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |

### transaction

7 colonnes, 17 lignes dans le dump. Domaine : [Enchères, commandes, paiements et évaluations](03-encheres-commandes-paiements.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_transaction` | int |  | PK, auto-incrément |
| `ref_acheteur` | int |  | réf. logique → [personne](#personne) |
| `ref_vendeur` | int |  | réf. logique → [personne](#personne) |
| `date` | datetime |  |  |
| `prix` | decimal(10,0) |  |  |
| `validate` | int |  |  |
| `ref_article` | int |  | réf. logique → [article](#article) |

## Listes de souhaits

### wish

27 colonnes, 0 lignes dans le dump. Domaine : [Listes de souhaits](04-wishlist.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_wish` | int |  | PK, auto-incrément |
| `ref_wish_list` | int |  | FK → [wish_list](#wish_list).id_wish_list |
| `ref_user` | int |  | FK → [personne](#personne).id_personne |
| `auteur` | varchar(40) | oui |  |
| `marque` | varchar(40) |  |  |
| `nom` | varchar(40) |  |  |
| `label` | text |  |  |
| `prix` | int |  |  |
| `pochette` | varchar(100) |  |  |
| `presound` | varchar(100) |  |  |
| `ref_genre` | int |  | réf. logique → [genre](#genre) |
| `etat` | varchar(50) |  |  |
| `ref_categorie` | int |  | FK → [categorie](#categorie).id_categorie |
| `ref_subcategorie` | int | oui | référence sans table |
| `owned` | int |  |  |
| `ref_statut` | int |  | référence sans table |
| `date` | datetime |  |  |
| `ref_depot` | int |  | réf. logique → [depot](#depot) |
| `enchere` | int |  |  |
| `ref_condition_payement` | int |  | FK → [condition_payement_ref](#condition_payement_ref).ref_condition_payement |
| `ref_mode_de_livraison` | int |  | FK → [condition_livraison](#condition_livraison).id |
| `enchere_date_debut` | datetime |  |  |
| `enchere_date_fin` | datetime |  |  |
| `vendu` | int |  |  |
| `lang` | varchar(2) |  |  |
| `ref_canton` | int | oui | FK → [canton_fr](#canton_fr).id_canton |
| `ref_pays` | int | oui | réf. logique → [pays](#pays) |

### wish_list

3 colonnes, 0 lignes dans le dump. Domaine : [Listes de souhaits](04-wishlist.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_wish_list` | int |  | PK, auto-incrément |
| `ref_user` | int |  | FK → [personne](#personne).id_personne |
| `date_creation` | date |  |  |

## Référentiels (géographie, vin, état, véhicules, calendrier)

### boite_de_vitesse

1 colonnes, 3 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### canton_fr

2 colonnes, 26 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_canton` | int |  | PK, auto-incrément |
| `nom` | varchar(60) |  |  |

### cepage

4 colonnes, 541 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_cepage` | int |  | PK, auto-incrément |
| `ref_pays_region_vin` | int |  | FK → [pays_region_vin](#pays_region_vin).id_pays_region_vin |
| `ref_type_de_vin` | int |  | référence sans table |
| `nom` | varchar(50) |  |  |

### departement

3 colonnes, 0 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_departement` | int |  | PK, auto-incrément |
| `nom` | varchar(50) |  |  |
| `code` | varchar(3) |  | PK |

### etat

1 colonnes, 2 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### mois

1 colonnes, 12 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |

### pays

3 colonnes, 0 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_pays` | int |  | PK, auto-incrément |
| `nom` | varchar(80) |  |  |
| `pays_code` | char(2) |  |  |

Contraintes d'unicité : (id_pays)

### pays_region_vin

4 colonnes, 2 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_pays_region_vin` | int |  | PK, auto-incrément |
| `ref_pays` | int |  | FK → [pays](#pays).id_pays |
| `parent_id` | int | oui | FK → [pays_region_vin](#pays_region_vin).id_pays_region_vin |
| `nom` | varchar(40) |  |  |

### states

3 colonnes, 0 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |
| `state` | varchar(50) |  | PK |
| `abbrev` | char(2) |  | PK |

Contraintes d'unicité : (state)

### temps

1 colonnes, 0 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK, auto-incrément |

### type_de_jeux

1 colonnes, 3 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### type_ecran

1 colonnes, 2 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

### type_essence

1 colonnes, 3 lignes dans le dump. Domaine : [Référentiels (géographie, vin, état, véhicules, calendrier)](05-referentiels.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id` | int |  | PK |

## Libellés multilingues

### boite_de_vitesse_libelle_langue

3 colonnes, 6 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_boite_de_vitesse` | int |  | PK, FK → [boite_de_vitesse](#boite_de_vitesse).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### categorie_libelle_langue

3 colonnes, 124 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_categorie` | int |  | PK, FK → [categorie](#categorie).id_categorie |
| `ref_libelle` | int |  | réf. logique → [libelle](#libelle) |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### condition_livraison_libelle_langue

4 colonnes, 12 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_mode_de_livraison` | int |  | PK, FK → [condition_livraison](#condition_livraison).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |
| `frais` | decimal(10,0) |  |  |

### condition_payement_libelle_langue

3 colonnes, 15 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_condition_payement` | int |  | PK |
| `ref_libelle` | int |  | réf. logique → [libelle](#libelle) |
| `ref_langue` | int |  | PK |

### etat_libelle_langue

3 colonnes, 6 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_etat` | int |  | PK, FK → [etat](#etat).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### langue

3 colonnes, 3 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_langue` | int |  | PK |
| `nom` | varchar(10) |  |  |
| `key` | varchar(2) |  |  |

Contraintes d'unicité : (id_langue)

### libelle

2 colonnes, 1409 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `id_libelle` | int |  | PK |
| `libelle` | varchar(500) |  |  |

Contraintes d'unicité : (id_libelle)

### location_ou_achat_libelle_langue

3 colonnes, 6 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_location_ou_achat` | int |  | PK |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### main_categorie_libelle_langue

3 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_main_categorie` | int |  | PK |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### mode_de_payement_libelle_langue

3 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_mode_de_payement` | int |  | PK, FK → [mode_de_payement](#mode_de_payement).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### mois_libelle_langue

3 colonnes, 12 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_mois` | int |  | PK, FK → [mois](#mois).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### publication_option_libelle_langue

4 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_publication_option` | int |  | PK, FK → [publication_option](#publication_option).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |
| `prix` | decimal(5,0) |  |  |

### statut_libelle_langue

3 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_statut` | int |  | PK |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### subcategorie_libelle_langue

4 colonnes, 1465 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_subcategorie` | int |  | PK |
| `ref_categorie` | int |  | PK, FK → [categorie](#categorie).id_categorie |
| `ref_libelle` | int |  | FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### taille_libelle_langue

4 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_taille` | int |  | PK |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |
| `valeur` | varchar(40) |  |  |

### temps_libelle_langue

3 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_temps` | int |  | PK, FK → [temps](#temps).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### type_de_compte_libelle_langue

4 colonnes, 0 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_type_de_compte` | int |  | PK, FK → [type_de_compte](#type_de_compte).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |
| `prix` | decimal(4,2) |  |  |

### type_de_jeux_libelle_langue

3 colonnes, 9 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_type_de_jeux` | int |  | PK, FK → [type_de_jeux](#type_de_jeux).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### type_de_vin_libelle_langue

3 colonnes, 12 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_type_de_vin` | int |  | PK |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### type_ecran_libelle_langue

3 colonnes, 6 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_type_ecran` | int |  | PK, FK → [type_ecran](#type_ecran).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

### type_essence_libelle_langue

3 colonnes, 9 lignes dans le dump. Domaine : [Libellés multilingues](06-multilingue.md).

| Colonne | Type | Null | Clé / référence |
|---|---|:---:|---|
| `ref_type_essence` | int |  | PK, FK → [type_essence](#type_essence).id |
| `ref_libelle` | int |  | PK, FK → [libelle](#libelle).id_libelle |
| `ref_langue` | int |  | PK, FK → [langue](#langue).id_langue |

