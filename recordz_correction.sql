-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  mer. 09 sep. 2026 à 16:05
-- Version du serveur :  10.3.16-MariaDB
-- Version de PHP :  7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `recordz_correction`
--

-- --------------------------------------------------------

--
-- Structure de la table `app_user`
--

CREATE TABLE `app_user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(1024) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT current_timestamp(3),
  `updated_at` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

CREATE TABLE `article` (
  `id_article` int(11) NOT NULL,
  `auteur` varchar(40) DEFAULT NULL,
  `marque` varchar(40) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `label` text NOT NULL,
  `prix` double NOT NULL,
  `prix_achat` decimal(10,0) DEFAULT NULL,
  `pochette` varchar(100) NOT NULL DEFAULT '',
  `presound` varchar(100) NOT NULL DEFAULT '',
  `ref_genre` int(11) NOT NULL DEFAULT 0,
  `etat` varchar(50) NOT NULL,
  `ref_categorie` int(11) NOT NULL,
  `ref_subcategorie` int(11) DEFAULT NULL,
  `owned` int(11) NOT NULL,
  `ref_statut` int(11) NOT NULL,
  `date` varchar(40) DEFAULT NULL,
  `ref_depot` int(11) NOT NULL DEFAULT 9,
  `ref_taille` int(11) DEFAULT NULL,
  `ref_couleur` int(11) DEFAULT NULL,
  `enchere` int(11) NOT NULL,
  `ref_condition_payement` int(11) NOT NULL,
  `ref_mode_de_livraison` int(11) NOT NULL,
  `enchere_date_debut` varchar(40) DEFAULT NULL,
  `enchere_date_fin` varchar(40) DEFAULT NULL,
  `vendu` int(11) NOT NULL DEFAULT 0,
  `ref_type_ecran` int(11) DEFAULT NULL,
  `dimension` int(11) DEFAULT NULL,
  `poids` int(11) DEFAULT NULL,
  `nb_porte` int(11) DEFAULT NULL,
  `nb_cheveaux` int(11) DEFAULT NULL,
  `nb_km` int(11) DEFAULT NULL,
  `premiere_immatriculation` varchar(20) DEFAULT NULL,
  `annee` int(11) DEFAULT NULL,
  `options` text DEFAULT NULL,
  `essence_ou_diesel` int(11) DEFAULT NULL,
  `nb_piece` int(11) DEFAULT NULL,
  `surface_habitable` int(11) DEFAULT NULL,
  `superficie_terrain` varchar(40) DEFAULT NULL,
  `annee_construction` varchar(40) DEFAULT NULL,
  `visites` int(11) DEFAULT 0,
  `nbr_enchere` int(11) DEFAULT 0,
  `lang` varchar(2) NOT NULL DEFAULT 'fr',
  `ref_canton` int(11) DEFAULT NULL,
  `lieu` varchar(50) DEFAULT NULL,
  `adresse` varchar(100) DEFAULT NULL,
  `npa` varchar(4) DEFAULT NULL,
  `ref_location_ou_achat` int(11) DEFAULT NULL,
  `ref_departement` int(11) DEFAULT NULL,
  `ref_pays` int(11) DEFAULT NULL,
  `ref_boite_de_vitesse` int(11) DEFAULT NULL,
  `clima` tinyint(4) DEFAULT NULL,
  `processeur` decimal(10,2) DEFAULT NULL,
  `ram` varchar(10) DEFAULT NULL,
  `disque_dur` varchar(10) DEFAULT NULL,
  `quantite` int(11) DEFAULT NULL,
  `ref_provenance` tinyint(4) DEFAULT NULL,
  `longueur` int(11) DEFAULT NULL,
  `largeur` int(11) DEFAULT NULL,
  `consomation` int(11) DEFAULT NULL,
  `acteurs` varchar(100) DEFAULT NULL,
  `duree` varbinary(10) DEFAULT NULL,
  `realisateur` varchar(40) DEFAULT NULL,
  `taille` varchar(40) DEFAULT NULL,
  `ref_type_de_jeux` int(11) DEFAULT NULL,
  `ref_cepage` int(11) DEFAULT NULL,
  `ref_pays_region_vin` int(11) DEFAULT NULL,
  `ref_type_de_vin` int(11) DEFAULT NULL,
  `ref_etat` int(11) DEFAULT NULL,
  `frais_livraison` decimal(2,2) DEFAULT NULL,
  `wat` int(11) DEFAULT NULL,
  `nb_cylindre` int(11) DEFAULT NULL,
  `pub` tinyint(4) DEFAULT NULL,
  `pub_date_start` varchar(40) DEFAULT NULL,
  `pub_date_end` varchar(40) DEFAULT NULL,
  `tx` varchar(20) DEFAULT NULL,
  `link_youtube` varchar(500) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `article_ip`
--

CREATE TABLE `article_ip` (
  `ref_article` int(11) NOT NULL,
  `ip` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `article_visite`
--

CREATE TABLE `article_visite` (
  `ref_article` int(11) NOT NULL,
  `ref_personne` int(11) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `prenom` varchar(40) NOT NULL,
  `adresse` text DEFAULT NULL,
  `ville` varchar(40) DEFAULT NULL,
  `npa` int(11) DEFAULT NULL,
  `no_prive` varchar(40) DEFAULT NULL,
  `no_mobile` int(11) DEFAULT NULL,
  `commentaire` text DEFAULT NULL,
  `par_email` int(11) DEFAULT NULL,
  `par_telephone` int(11) DEFAULT NULL,
  `par_mobile` int(11) DEFAULT NULL,
  `email` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `a_livre`
--

CREATE TABLE `a_livre` (
  `id_a_livre` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_enchere` int(11) DEFAULT NULL,
  `date_achat` varchar(20) DEFAULT NULL,
  `ref_mode_de_livraison` int(11) DEFAULT NULL,
  `ref_statut` int(11) NOT NULL,
  `quantite` int(11) DEFAULT NULL,
  `montant` varchar(10) DEFAULT NULL,
  `date_reception` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `a_paye`
--

CREATE TABLE `a_paye` (
  `id_a_paye` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `ref_enchere` int(11) DEFAULT NULL,
  `ref_condition_payement` int(11) NOT NULL,
  `ref_mode_de_livraison` int(11) NOT NULL,
  `ref_statut` int(11) NOT NULL,
  `montant` decimal(10,0) NOT NULL,
  `date_fermeture_enchere` datetime NOT NULL,
  `date_payement` datetime DEFAULT NULL,
  `date_echeance` datetime DEFAULT NULL,
  `rappel_1` date DEFAULT NULL,
  `rappel_2` date DEFAULT NULL,
  `rappel_3` date DEFAULT NULL,
  `quantite` int(11) NOT NULL DEFAULT 1,
  `ref_canton` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `boite_de_vitesse`
--

CREATE TABLE `boite_de_vitesse` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `boite_de_vitesse_libelle_langue`
--

CREATE TABLE `boite_de_vitesse_libelle_langue` (
  `ref_boite_de_vitesse` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `boutique`
--

CREATE TABLE `boutique` (
  `id_boutique` int(11) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `date_creation` date NOT NULL,
  `ref_personne` int(11) NOT NULL,
  `ref_main_categorie` int(11) NOT NULL,
  `login` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `boutique_a_categorie`
--

CREATE TABLE `boutique_a_categorie` (
  `ref_boutique` int(11) NOT NULL,
  `ref_categorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `canton_fr`
--

CREATE TABLE `canton_fr` (
  `id_canton` int(11) NOT NULL,
  `nom` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `id_categorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `categorie_libelle_langue`
--

CREATE TABLE `categorie_libelle_langue` (
  `ref_categorie` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `cepage`
--

CREATE TABLE `cepage` (
  `id_cepage` int(11) NOT NULL,
  `ref_pays_region_vin` int(11) NOT NULL,
  `ref_type_de_vin` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `id_commande` int(11) NOT NULL,
  `session_id` char(32) NOT NULL,
  `client_ref` int(11) NOT NULL,
  `date` date NOT NULL,
  `ref_mode_de_payement` int(11) NOT NULL,
  `date_payement` date DEFAULT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_statut` int(11) NOT NULL,
  `ref_mode_de_livraison` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `commande_article`
--

CREATE TABLE `commande_article` (
  `ref_commande` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `date_payement` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `commentaire`
--

CREATE TABLE `commentaire` (
  `id_commentaire` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_emetteur` int(11) NOT NULL,
  `question` varchar(500) NOT NULL,
  `texte` text NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `condition_livraison`
--

CREATE TABLE `condition_livraison` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `condition_livraison_libelle_langue`
--

CREATE TABLE `condition_livraison_libelle_langue` (
  `ref_mode_de_livraison` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL,
  `frais` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `condition_payement_libelle_langue`
--

CREATE TABLE `condition_payement_libelle_langue` (
  `ref_condition_payement` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `demande_visite`
--

CREATE TABLE `demande_visite` (
  `id_demande` int(11) NOT NULL,
  `reponse_par_email` int(11) NOT NULL,
  `reponse_par_tel` int(11) NOT NULL,
  `reponse_par_mobile` int(11) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `prenom` varchar(40) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `npa` varchar(4) NOT NULL,
  `ville` varchar(40) NOT NULL,
  `tel_prive` varchar(10) NOT NULL,
  `tel_mobile` varchar(10) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `commentaire` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `departement`
--

CREATE TABLE `departement` (
  `id_departement` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `code` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `depot`
--

CREATE TABLE `depot` (
  `id_depot` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `adresse` varchar(300) NOT NULL,
  `ville` varchar(150) NOT NULL,
  `npa` int(11) NOT NULL,
  `ref_pays` int(11) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `ref_responsable` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `enchere`
--

CREATE TABLE `enchere` (
  `id_enchere` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_enchereur` int(11) NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  `date_enchere` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `etat`
--

CREATE TABLE `etat` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `etat_libelle_langue`
--

CREATE TABLE `etat_libelle_langue` (
  `ref_etat` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `evaluation_achat`
--

CREATE TABLE `evaluation_achat` (
  `id_evaluation_achat` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `note` tinyint(4) NOT NULL,
  `commentaire` text NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `evaluation_article`
--

CREATE TABLE `evaluation_article` (
  `id_evaluation_article` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `note` int(11) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `evaluation_vente`
--

CREATE TABLE `evaluation_vente` (
  `id_evaluation_vente` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL,
  `note` tinyint(4) NOT NULL,
  `commentaire` text NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `genre`
--

CREATE TABLE `genre` (
  `id_genre` int(11) NOT NULL,
  `genre` varchar(40) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `langue`
--

CREATE TABLE `langue` (
  `id_langue` int(11) NOT NULL,
  `nom` varchar(10) NOT NULL,
  `key` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `langue`
--

INSERT INTO `langue` (`id_langue`, `nom`, `key`) VALUES
(1, 'Français', 'FR'),
(2, 'Deutsch', 'DE');

-- --------------------------------------------------------

--
-- Structure de la table `libelle`
--

CREATE TABLE `libelle` (
  `id_libelle` int(11) NOT NULL,
  `libelle` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `location_ou_achat`
--

CREATE TABLE `location_ou_achat` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `location_ou_achat_libelle_langue`
--

CREATE TABLE `location_ou_achat_libelle_langue` (
  `ref_location_ou_achat` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `main_categorie`
--

CREATE TABLE `main_categorie` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `main_categorie_libelle_langue`
--

CREATE TABLE `main_categorie_libelle_langue` (
  `ref_main_categorie` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `met_en_vente`
--

CREATE TABLE `met_en_vente` (
  `ref_vendeur` int(11) NOT NULL DEFAULT 0,
  `ref_article` int(11) NOT NULL DEFAULT 0,
  `date_stock` date NOT NULL DEFAULT '0000-00-00',
  `page_principale` varchar(40) DEFAULT NULL,
  `page_categorie` varchar(40) DEFAULT NULL,
  `pack_photo` varchar(20) DEFAULT NULL,
  `notre_selection` int(11) DEFAULT 0,
  `statut` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `mode_de_payement`
--

CREATE TABLE `mode_de_payement` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `mode_de_payement_libelle_langue`
--

CREATE TABLE `mode_de_payement_libelle_langue` (
  `ref_mode_de_payement` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `mois`
--

CREATE TABLE `mois` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `mois_libelle_langue`
--

CREATE TABLE `mois_libelle_langue` (
  `ref_mois` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `pays`
--

CREATE TABLE `pays` (
  `id_pays` int(11) NOT NULL,
  `nom` varchar(80) NOT NULL,
  `pays_code` char(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `pays_present`
--

CREATE TABLE `pays_present` (
  `id_pays_present` int(11) NOT NULL,
  `nom` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `pays_present`
--

INSERT INTO `pays_present` (`id_pays_present`, `nom`) VALUES
(1, 'Suisse'),
(2, 'France'),
(3, 'Canada'),
(4, 'United States');

-- --------------------------------------------------------

--
-- Structure de la table `pays_region_vin`
--

CREATE TABLE `pays_region_vin` (
  `id_pays_region_vin` int(11) NOT NULL,
  `ref_pays` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `nom` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `pays_region_vin`
--

INSERT INTO `pays_region_vin` (`id_pays_region_vin`, `ref_pays`, `parent_id`, `nom`) VALUES
(1, 1, 1, 'Suisse'),
(2, 2, 2, 'France');

-- --------------------------------------------------------

--
-- Structure de la table `personne`
--

CREATE TABLE `personne` (
  `id_personne` int(11) NOT NULL,
  `nom_utilisateur` varchar(40) NOT NULL,
  `mot_de_passe` varchar(41) NOT NULL DEFAULT '',
  `nom` varchar(40) NOT NULL DEFAULT '',
  `prenom` varchar(40) NOT NULL DEFAULT '',
  `adresse` varchar(40) NOT NULL DEFAULT '',
  `npa` int(11) NOT NULL DEFAULT 0,
  `ville` varchar(40) NOT NULL DEFAULT '',
  `pays` varchar(40) NOT NULL DEFAULT '',
  `email` varchar(100) NOT NULL DEFAULT '',
  `no_telephone` varchar(20) NOT NULL DEFAULT '',
  `active` int(11) NOT NULL DEFAULT 0,
  `level` int(11) NOT NULL,
  `ref_host` varchar(50) NOT NULL DEFAULT '',
  `lang` varchar(10) DEFAULT NULL,
  `banque` varchar(40) DEFAULT NULL,
  `no_compte` varchar(40) DEFAULT NULL,
  `iban` varchar(20) DEFAULT NULL,
  `msn_messenger` varchar(20) DEFAULT NULL,
  `skype_name` varchar(20) DEFAULT NULL,
  `date_start` varchar(20) DEFAULT NULL,
  `date_expiration` date DEFAULT NULL,
  `ref_canton` int(11) DEFAULT NULL,
  `paypal_me` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `personne`
--

INSERT INTO `personne` (`id_personne`, `nom_utilisateur`, `mot_de_passe`, `nom`, `prenom`, `adresse`, `npa`, `ville`, `pays`, `email`, `no_telephone`, `active`, `level`, `ref_host`, `lang`, `banque`, `no_compte`, `iban`, `msn_messenger`, `skype_name`, `date_start`, `date_expiration`, `ref_canton`, `paypal_me`) VALUES
(1, 'alexjaquet', '', 'Jaquet', 'Alexandre', '', 0, 'Belfaux', '', 'alexjaquet@gmail.com', 'non autorisé', 1, 1, '', NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `pointure`
--

CREATE TABLE `pointure` (
  `id_pointure` int(11) NOT NULL,
  `valeur` varchar(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `publication_option`
--

CREATE TABLE `publication_option` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `publication_option_libelle_langue`
--

CREATE TABLE `publication_option_libelle_langue` (
  `ref_publication_option` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL,
  `prix` decimal(5,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `recherche`
--

CREATE TABLE `recherche` (
  `id_recherche` int(11) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `nbr` int(11) NOT NULL,
  `ref_categorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `sessions`
--

CREATE TABLE `sessions` (
  `id` char(32) NOT NULL,
  `a_session` text NOT NULL,
  `username` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `states`
--

CREATE TABLE `states` (
  `id` int(11) NOT NULL,
  `state` varchar(50) NOT NULL,
  `abbrev` char(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `statut_libelle_langue`
--

CREATE TABLE `statut_libelle_langue` (
  `ref_statut` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `subcategorie`
--

CREATE TABLE `subcategorie` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `subcategorie_libelle_langue`
--

CREATE TABLE `subcategorie_libelle_langue` (
  `ref_subcategorie` int(11) NOT NULL,
  `ref_categorie` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `taille`
--

CREATE TABLE `taille` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `taille_libelle_langue`
--

CREATE TABLE `taille_libelle_langue` (
  `ref_taille` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL,
  `valeur` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `temps`
--

CREATE TABLE `temps` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `temps_libelle_langue`
--

CREATE TABLE `temps_libelle_langue` (
  `ref_temps` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `transaction`
--

CREATE TABLE `transaction` (
  `id_transaction` int(11) NOT NULL,
  `ref_acheteur` int(11) NOT NULL,
  `ref_vendeur` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  `validate` int(11) NOT NULL,
  `ref_article` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_compte`
--

CREATE TABLE `type_de_compte` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_compte_libelle_langue`
--

CREATE TABLE `type_de_compte_libelle_langue` (
  `ref_type_de_compte` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL,
  `prix` decimal(4,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_jeux`
--

CREATE TABLE `type_de_jeux` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_jeux_libelle_langue`
--

CREATE TABLE `type_de_jeux_libelle_langue` (
  `ref_type_de_jeux` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_vin`
--

CREATE TABLE `type_de_vin` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_de_vin_libelle_langue`
--

CREATE TABLE `type_de_vin_libelle_langue` (
  `ref_type_de_vin` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_ecran`
--

CREATE TABLE `type_ecran` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_ecran_libelle_langue`
--

CREATE TABLE `type_ecran_libelle_langue` (
  `ref_type_ecran` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_essence`
--

CREATE TABLE `type_essence` (
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `type_essence_libelle_langue`
--

CREATE TABLE `type_essence_libelle_langue` (
  `ref_type_essence` int(11) NOT NULL,
  `ref_libelle` int(11) NOT NULL,
  `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `visiteur`
--

CREATE TABLE `visiteur` (
  `ref_personne` int(11) NOT NULL,
  `ref_visiteur` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `wish`
--

CREATE TABLE `wish` (
  `id_wish` int(11) NOT NULL,
  `ref_wish_list` int(11) NOT NULL,
  `ref_user` int(11) NOT NULL,
  `auteur` varchar(40) DEFAULT NULL,
  `marque` varchar(40) NOT NULL,
  `nom` varchar(40) NOT NULL DEFAULT '',
  `label` text NOT NULL,
  `prix` int(11) NOT NULL,
  `pochette` varchar(100) NOT NULL DEFAULT '',
  `presound` varchar(100) NOT NULL DEFAULT '',
  `ref_genre` int(11) NOT NULL DEFAULT 0,
  `etat` varchar(50) NOT NULL,
  `ref_categorie` int(11) NOT NULL,
  `ref_subcategorie` int(11) DEFAULT NULL,
  `owned` int(11) NOT NULL,
  `ref_statut` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `ref_depot` int(11) NOT NULL,
  `enchere` int(11) NOT NULL,
  `ref_condition_payement` int(11) NOT NULL,
  `ref_mode_de_livraison` int(11) NOT NULL,
  `enchere_date_debut` datetime NOT NULL,
  `enchere_date_fin` datetime NOT NULL,
  `vendu` int(11) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `ref_canton` int(11) DEFAULT NULL,
  `ref_pays` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `wish_list`
--

CREATE TABLE `wish_list` (
  `id_wish_list` int(11) NOT NULL,
  `ref_user` int(11) NOT NULL,
  `date_creation` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `app_user`
--
ALTER TABLE `app_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_app_user_email` (`email`);

--
-- Index pour la table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`id_article`);

--
-- Index pour la table `article_ip`
--
ALTER TABLE `article_ip`
  ADD PRIMARY KEY (`ref_article`);

--
-- Index pour la table `article_visite`
--
ALTER TABLE `article_visite`
  ADD PRIMARY KEY (`ref_article`,`ref_personne`),
  ADD KEY `fk_av_personne` (`ref_personne`);

--
-- Index pour la table `a_livre`
--
ALTER TABLE `a_livre`
  ADD PRIMARY KEY (`id_a_livre`);

--
-- Index pour la table `a_paye`
--
ALTER TABLE `a_paye`
  ADD PRIMARY KEY (`id_a_paye`);

--
-- Index pour la table `boite_de_vitesse`
--
ALTER TABLE `boite_de_vitesse`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `boite_de_vitesse_libelle_langue`
--
ALTER TABLE `boite_de_vitesse_libelle_langue`
  ADD PRIMARY KEY (`ref_boite_de_vitesse`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `boutique`
--
ALTER TABLE `boutique`
  ADD PRIMARY KEY (`id_boutique`);

--
-- Index pour la table `boutique_a_categorie`
--
ALTER TABLE `boutique_a_categorie`
  ADD PRIMARY KEY (`ref_boutique`,`ref_categorie`);

--
-- Index pour la table `canton_fr`
--
ALTER TABLE `canton_fr`
  ADD PRIMARY KEY (`id_canton`);

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`id_categorie`);

--
-- Index pour la table `categorie_libelle_langue`
--
ALTER TABLE `categorie_libelle_langue`
  ADD PRIMARY KEY (`ref_categorie`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `cepage`
--
ALTER TABLE `cepage`
  ADD PRIMARY KEY (`id_cepage`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`id_commande`);

--
-- Index pour la table `commande_article`
--
ALTER TABLE `commande_article`
  ADD PRIMARY KEY (`ref_commande`,`ref_article`,`date_payement`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE `commentaire`
  ADD PRIMARY KEY (`id_commentaire`);

--
-- Index pour la table `condition_livraison`
--
ALTER TABLE `condition_livraison`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `condition_livraison_libelle_langue`
--
ALTER TABLE `condition_livraison_libelle_langue`
  ADD PRIMARY KEY (`ref_mode_de_livraison`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `condition_payement_libelle_langue`
--
ALTER TABLE `condition_payement_libelle_langue`
  ADD PRIMARY KEY (`ref_condition_payement`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `demande_visite`
--
ALTER TABLE `demande_visite`
  ADD PRIMARY KEY (`id_demande`);

--
-- Index pour la table `departement`
--
ALTER TABLE `departement`
  ADD PRIMARY KEY (`id_departement`,`code`);

--
-- Index pour la table `depot`
--
ALTER TABLE `depot`
  ADD PRIMARY KEY (`id_depot`,`ref_responsable`);

--
-- Index pour la table `enchere`
--
ALTER TABLE `enchere`
  ADD PRIMARY KEY (`id_enchere`,`ref_article`,`ref_enchereur`,`prix`,`date_enchere`);

--
-- Index pour la table `etat`
--
ALTER TABLE `etat`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `etat_libelle_langue`
--
ALTER TABLE `etat_libelle_langue`
  ADD PRIMARY KEY (`ref_etat`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `evaluation_achat`
--
ALTER TABLE `evaluation_achat`
  ADD PRIMARY KEY (`id_evaluation_achat`);

--
-- Index pour la table `evaluation_article`
--
ALTER TABLE `evaluation_article`
  ADD PRIMARY KEY (`id_evaluation_article`);

--
-- Index pour la table `evaluation_vente`
--
ALTER TABLE `evaluation_vente`
  ADD PRIMARY KEY (`id_evaluation_vente`);

--
-- Index pour la table `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`id_genre`);

--
-- Index pour la table `langue`
--
ALTER TABLE `langue`
  ADD PRIMARY KEY (`id_langue`,`nom`);

--
-- Index pour la table `libelle`
--
ALTER TABLE `libelle`
  ADD PRIMARY KEY (`id_libelle`,`libelle`);

--
-- Index pour la table `location_ou_achat`
--
ALTER TABLE `location_ou_achat`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `location_ou_achat_libelle_langue`
--
ALTER TABLE `location_ou_achat_libelle_langue`
  ADD PRIMARY KEY (`ref_location_ou_achat`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `main_categorie`
--
ALTER TABLE `main_categorie`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `main_categorie_libelle_langue`
--
ALTER TABLE `main_categorie_libelle_langue`
  ADD PRIMARY KEY (`ref_main_categorie`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `met_en_vente`
--
ALTER TABLE `met_en_vente`
  ADD PRIMARY KEY (`ref_vendeur`,`ref_article`);

--
-- Index pour la table `mode_de_payement`
--
ALTER TABLE `mode_de_payement`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `mode_de_payement_libelle_langue`
--
ALTER TABLE `mode_de_payement_libelle_langue`
  ADD PRIMARY KEY (`ref_mode_de_payement`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `mois`
--
ALTER TABLE `mois`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `mois_libelle_langue`
--
ALTER TABLE `mois_libelle_langue`
  ADD PRIMARY KEY (`ref_mois`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `pays`
--
ALTER TABLE `pays`
  ADD PRIMARY KEY (`id_pays`,`pays_code`);

--
-- Index pour la table `pays_present`
--
ALTER TABLE `pays_present`
  ADD PRIMARY KEY (`id_pays_present`,`nom`);

--
-- Index pour la table `pays_region_vin`
--
ALTER TABLE `pays_region_vin`
  ADD PRIMARY KEY (`id_pays_region_vin`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
  ADD PRIMARY KEY (`id_personne`),
  ADD UNIQUE KEY `uq_personne_nom_utilisateur` (`nom_utilisateur`),
  ADD UNIQUE KEY `uq_personne_email` (`email`);

--
-- Index pour la table `pointure`
--
ALTER TABLE `pointure`
  ADD PRIMARY KEY (`id_pointure`),
  ADD UNIQUE KEY `uq_pointure_valeur` (`valeur`);

--
-- Index pour la table `publication_option`
--
ALTER TABLE `publication_option`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `publication_option_libelle_langue`
--
ALTER TABLE `publication_option_libelle_langue`
  ADD PRIMARY KEY (`ref_publication_option`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `recherche`
--
ALTER TABLE `recherche`
  ADD PRIMARY KEY (`id_recherche`);

--
-- Index pour la table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `states`
--
ALTER TABLE `states`
  ADD PRIMARY KEY (`id`,`state`,`abbrev`),
  ADD UNIQUE KEY `uq_states_state` (`state`);

--
-- Index pour la table `statut_libelle_langue`
--
ALTER TABLE `statut_libelle_langue`
  ADD PRIMARY KEY (`ref_statut`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `subcategorie`
--
ALTER TABLE `subcategorie`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `subcategorie_libelle_langue`
--
ALTER TABLE `subcategorie_libelle_langue`
  ADD PRIMARY KEY (`ref_subcategorie`,`ref_categorie`,`ref_langue`);

--
-- Index pour la table `taille`
--
ALTER TABLE `taille`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `taille_libelle_langue`
--
ALTER TABLE `taille_libelle_langue`
  ADD PRIMARY KEY (`ref_taille`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `temps`
--
ALTER TABLE `temps`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `temps_libelle_langue`
--
ALTER TABLE `temps_libelle_langue`
  ADD PRIMARY KEY (`ref_temps`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id_transaction`);

--
-- Index pour la table `type_de_compte`
--
ALTER TABLE `type_de_compte`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_de_compte_libelle_langue`
--
ALTER TABLE `type_de_compte_libelle_langue`
  ADD PRIMARY KEY (`ref_type_de_compte`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `type_de_jeux`
--
ALTER TABLE `type_de_jeux`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_de_jeux_libelle_langue`
--
ALTER TABLE `type_de_jeux_libelle_langue`
  ADD PRIMARY KEY (`ref_type_de_jeux`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `type_de_vin`
--
ALTER TABLE `type_de_vin`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_de_vin_libelle_langue`
--
ALTER TABLE `type_de_vin_libelle_langue`
  ADD PRIMARY KEY (`ref_type_de_vin`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `type_ecran`
--
ALTER TABLE `type_ecran`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_ecran_libelle_langue`
--
ALTER TABLE `type_ecran_libelle_langue`
  ADD PRIMARY KEY (`ref_type_ecran`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `type_essence`
--
ALTER TABLE `type_essence`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_essence_libelle_langue`
--
ALTER TABLE `type_essence_libelle_langue`
  ADD PRIMARY KEY (`ref_type_essence`,`ref_libelle`,`ref_langue`);

--
-- Index pour la table `visiteur`
--
ALTER TABLE `visiteur`
  ADD PRIMARY KEY (`ref_personne`,`ref_visiteur`);

--
-- Index pour la table `wish`
--
ALTER TABLE `wish`
  ADD PRIMARY KEY (`id_wish`);

--
-- Index pour la table `wish_list`
--
ALTER TABLE `wish_list`
  ADD PRIMARY KEY (`id_wish_list`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `app_user`
--
ALTER TABLE `app_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `article`
--
ALTER TABLE `article`
  MODIFY `id_article` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `a_livre`
--
ALTER TABLE `a_livre`
  MODIFY `id_a_livre` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `a_paye`
--
ALTER TABLE `a_paye`
  MODIFY `id_a_paye` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `boutique`
--
ALTER TABLE `boutique`
  MODIFY `id_boutique` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `canton_fr`
--
ALTER TABLE `canton_fr`
  MODIFY `id_canton` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `cepage`
--
ALTER TABLE `cepage`
  MODIFY `id_cepage` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `id_commande` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
  MODIFY `id_commentaire` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `demande_visite`
--
ALTER TABLE `demande_visite`
  MODIFY `id_demande` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `departement`
--
ALTER TABLE `departement`
  MODIFY `id_departement` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `enchere`
--
ALTER TABLE `enchere`
  MODIFY `id_enchere` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `evaluation_achat`
--
ALTER TABLE `evaluation_achat`
  MODIFY `id_evaluation_achat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `evaluation_article`
--
ALTER TABLE `evaluation_article`
  MODIFY `id_evaluation_article` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `evaluation_vente`
--
ALTER TABLE `evaluation_vente`
  MODIFY `id_evaluation_vente` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `genre`
--
ALTER TABLE `genre`
  MODIFY `id_genre` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `main_categorie`
--
ALTER TABLE `main_categorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `mode_de_payement`
--
ALTER TABLE `mode_de_payement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `mois`
--
ALTER TABLE `mois`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `pays`
--
ALTER TABLE `pays`
  MODIFY `id_pays` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `pays_present`
--
ALTER TABLE `pays_present`
  MODIFY `id_pays_present` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `pays_region_vin`
--
ALTER TABLE `pays_region_vin`
  MODIFY `id_pays_region_vin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `personne`
--
ALTER TABLE `personne`
  MODIFY `id_personne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `pointure`
--
ALTER TABLE `pointure`
  MODIFY `id_pointure` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `publication_option`
--
ALTER TABLE `publication_option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `recherche`
--
ALTER TABLE `recherche`
  MODIFY `id_recherche` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `states`
--
ALTER TABLE `states`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `subcategorie`
--
ALTER TABLE `subcategorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `taille`
--
ALTER TABLE `taille`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `temps`
--
ALTER TABLE `temps`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `id_transaction` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `wish`
--
ALTER TABLE `wish`
  MODIFY `id_wish` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `wish_list`
--
ALTER TABLE `wish_list`
  MODIFY `id_wish_list` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `article_visite`
--
ALTER TABLE `article_visite`
  ADD CONSTRAINT `fk_av_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_av_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
