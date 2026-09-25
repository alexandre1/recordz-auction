-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  mer. 16 sep. 2026 à 09:15
-- Version du serveur :  10.3.16-MariaDB
-- Version de PHP :  7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET FOREIGN_KEY_CHECKS = 0; -- Désactivation globale pour l'import
SET AUTOCOMMIT = 0;
SET time_zone = "+00:00";

-- 2. Destruction et recréation de la base (HORS transaction)
DROP DATABASE IF EXISTS new_db_recordzv3;
CREATE DATABASE new_db_recordzv3;
USE new_db_recordzv3;

-- 3. C'est ICI que l'on commence la transaction pour les structures/données
START TRANSACTION;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `new_db_recordzv3`
--

-- --------------------------------------------------------

--
-- Structure de la table `app_user`
--

DROP DATABASE new_db_recordzv3;
CREATE DATABASE new_db_recordzv3;
USE new_db_recordzv3;
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
                           `etat` int(50) NOT NULL,
                           `ref_categorie` int(11) NOT NULL,
                           `ref_subcategorie` int(11) DEFAULT NULL,
                           `owned` int(11) NOT NULL,
                           `ref_statut` int(11) NOT NULL,
                           `date` datetime NOT NULL DEFAULT current_timestamp(),
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
                           `pub` int(4) DEFAULT NULL,
                           `pub_date_start` varchar(40) DEFAULT NULL,
                           `pub_date_end` varchar(40) DEFAULT NULL,
                           `tx` varchar(20) DEFAULT NULL,
                           `link_youtube` varchar(500) NOT NULL DEFAULT '',
                           `pochette2` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id_article`, `auteur`, `marque`, `nom`, `label`, `prix`, `prix_achat`, `pochette`, `presound`, `ref_genre`, `etat`, `ref_categorie`, `ref_subcategorie`, `owned`, `ref_statut`, `date`, `ref_depot`, `ref_taille`, `ref_couleur`, `enchere`, `ref_condition_payement`, `ref_mode_de_livraison`, `enchere_date_debut`, `enchere_date_fin`, `vendu`, `ref_type_ecran`, `dimension`, `poids`, `nb_porte`, `nb_cheveaux`, `nb_km`, `premiere_immatriculation`, `annee`, `options`, `essence_ou_diesel`, `nb_piece`, `surface_habitable`, `superficie_terrain`, `annee_construction`, `visites`, `nbr_enchere`, `lang`, `ref_canton`, `lieu`, `adresse`, `npa`, `ref_location_ou_achat`, `ref_departement`, `ref_pays`, `ref_boite_de_vitesse`, `clima`, `processeur`, `ram`, `disque_dur`, `quantite`, `ref_provenance`, `longueur`, `largeur`, `consomation`, `acteurs`, `duree`, `realisateur`, `taille`, `ref_type_de_jeux`, `ref_cepage`, `ref_pays_region_vin`, `ref_type_de_vin`, `ref_etat`, `frais_livraison`, `wat`, `nb_cylindre`, `pub`, `pub_date_start`, `pub_date_end`, `tx`, `link_youtube`, `pochette2`) VALUES
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (5, NULL, 'Test', 'Test', '<p>Test</p>', 10, NULL, 'article_5.jpg', '', 0, 1, 22222, 41, 0, 1, '0000-00-00 00:00:00', 9, NULL, NULL, 2, 2, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 110, 3, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (8, NULL, 'Test2', 'Test2', '<p>Test2</p>', 198, NULL, 'article_8.png', '', 0, 1, 2, 13, 0, 1, '0000-00-00 00:00:00', 9, NULL, NULL, 2, 4, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 12, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (9, NULL, 'Test', 'Coucou', 'Coucou', 100, NULL, '0650_jack-jones-men-jeans-blue-denim-straight-fit-12168290-411_f.webp', '', 0, 1, 2, 2, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 3, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '34', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (10, NULL, 'Test', 'Coucou2', 'Coucou', 10, NULL, '10.webp', '', 0, 1, 2, 2, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 1, 3, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '34', NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (11, NULL, 'Test', 'Coucou3', 'Coucou', 121212, NULL, 'article_11.webp', '', 0, 1, 2, 2, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 4, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '34', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (12, NULL, 'test', 'TestTest', 'Test', 1212, NULL, 'article_12.png', '', 0, 1, 2, 13, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 1, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (13, NULL, '', 'Vroom', 'Vroom', 1212121, NULL, 'article_13.png', '', 0, 1, 5, 54, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 3, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, 121212, 0, '0000', 2027, NULL, 1, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 12, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (14, NULL, '', 'Encore', 'ENcore', 1212212, NULL, 'article_14.png', '', 0, 1, 5, 54, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 1, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, 500, 0, '0000', 2002, NULL, 1, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 12, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (15, NULL, 'Test', 'Encore', '', 9087532, NULL, 'article_15.png', '', 0, 1, 5, 54, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 4, 4, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, 1000, 0, '0000', 2027, NULL, 1, NULL, NULL, NULL, NULL, 11, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 12, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (16, NULL, 'Test', 'Vroom', 'Voici une voiture', 9999999, NULL, 'article_16.jpg', '', 0, 1, 5, 54, 0, 1, '2026-03-09 00:00:00', 9, NULL, NULL, 0, 3, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, 1000, 0, '20027', 20027, NULL, 1, NULL, NULL, NULL, NULL, 40, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 12, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (17, NULL, 'Gucci', 'TShirt Gucci', 'TShirt Gucci', 340, NULL, 'article_17.png', '', 0, 1, 1, 24, 0, 1, '2026-03-10 00:00:00', 9, NULL, NULL, 1, 1, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 2, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (18, NULL, 'Test', 'Test', 'Test', 1212121212, NULL, 'article_18.jpg', '', 0, 1, 94, 531, 0, 1, '2026-03-10 00:00:00', 9, NULL, NULL, 1, 4, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 12, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (19, NULL, 'Chanel', 'Channel Dress', 'Une jolie robe', 12121212, NULL, 'article_19.jpeg', '', 0, 1, 1, 31, 0, 1, '2026-03-10 00:00:00', 9, NULL, NULL, 1, 1, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 100, 1, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'kCKqS97hV98', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (26, NULL, '', 'Belfaux', 'TEST', 1212121212, NULL, 'article_26.png', '', 0, 1, 25, 304, 0, 1, '2026-03-10 00:00:00', 9, NULL, NULL, 1, 2, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 450, '1000', '2027', 94, 2, 'fr', 7, 'Belfaux', 'route d\'Autafond', '1782', 2, NULL, 1, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(27, NULL, 'Paris Hilon', 'Belfaux', 'Best', 150, NULL, 'article_27.jpg', '', 0, 1, 13, 131, 0, 1, '2026-03-11 00:00:00', 9, NULL, NULL, 0, 1, 3, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 29, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(28, NULL, 'Gucci', 'Coucou2', 'Test', 12, NULL, 'article_28.jpg', '', 0, 1, 1, 7, 0, 1, '2026-03-11 00:00:00', 9, NULL, NULL, 1, 1, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, 1, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(29, NULL, 'Nike', 'Vroom', 'Test', 200, NULL, 'article_29.png', '', 0, 1, 2, 13, 0, 1, '2026-03-11 00:00:00', 9, NULL, NULL, 1, 4, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(32, NULL, 'Apple', 'Test', 'Test', 1000, NULL, 'article_32.jpg', '', 0, 1, 188, 79, 1, 8, '2026-03-11 00:00:00', 9, NULL, NULL, 0, 2, 2, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 39, 0, 'fr', 7, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '900.00', '900', '5000', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(33, NULL, 'Pomerol', 'Vin', 'Vom', 12, NULL, 'article_33.jpeg', '', 0, 1, 27, 1, 1, 8, '2026-03-12 00:00:00', 9, NULL, NULL, 0, 3, 4, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, 11, 1, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(34, NULL, 'Test', 'Hm', 'Desc', 12, NULL, 'article_34.jpg', '', 0, 1, 27, 2, 1, 1, '2026-03-12 00:00:00', 9, NULL, NULL, 1, 1, 4, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 22, 1, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '75cl', NULL, NULL, 1, 2, 2, NULL, NULL, NULL, 1, NULL, NULL, NULL, '', NULL),
(35, NULL, 'Test', 'Miam', 'Miam', 1212, NULL, 'article_35.jpg', '', 0, 1, 27, 2, 1, 1, '2026-03-12 00:00:00', 9, NULL, NULL, 0, 3, 4, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 52, 2, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '75 cl', NULL, 26, 1, 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(36, NULL, 'De l\'élevage à Grenoble', 'Yoky', 'Yoki tout mignin', 2990, NULL, 'article_36.png', '', 0, 1, 33, 365, 1, 8, '2026-03-13 00:00:00', 9, NULL, NULL, 0, 1, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 109, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (37, NULL, '', 'Nouveau', 'Test', 1212121, NULL, 'article_37.png', '', 0, 1, 25, 304, 1, 1, '2026-03-17 00:00:00', 9, NULL, NULL, 0, 3, 3, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 500, '1200', '2027', 162, 4, 'fr', 7, 'Belfaux', 'rte d\'Autafond', '1782', 2, NULL, 1, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(38, NULL, 'Dev', 'Dev', 'Dev', 1212, NULL, 'article_38.png', '', 0, 1, 93, 475, 3, 1, '2026-03-17 00:00:00', 9, NULL, NULL, 0, 4, 3, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 74, 6, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(39, NULL, 'Tesla', 'Cybertruck', 'Tesla', 1212212, NULL, 'article_39.jpg', '', 0, 1, 5, 56, 1, 1, '2026-03-20 00:00:00', 9, NULL, NULL, 0, 5, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, 0, '2026', 2026, NULL, 3, NULL, NULL, NULL, NULL, 90, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, NULL, NULL, NULL, 76, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, 0, 1, NULL, NULL, NULL, '', NULL),
(40, NULL, 'Test', 'Canne de hockey Christo Juet', 'Canne de hockey de Chrsto Huet signé\r\n\r\n\r\n', 1000, NULL, 'article_40.png', '', 0, 1, 99, 99999, 1, 1, '2026-03-21 00:00:00', 9, NULL, NULL, 0, 5, 4, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 12, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(41, NULL, 'Gottéron', 'Maillot de Marc', 'test', 230, NULL, 'article_41.png', '', 0, 1, 99, 550, 1, 1, '2026-03-21 00:00:00', 9, NULL, NULL, 0, 5, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 106, 2, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, 1, NULL, NULL, NULL, '', NULL),
(42, NULL, 'Test', 'Test II', 'Coucou', 1212, NULL, 'article_42.jpg', '', 0, 1, 1, 31, 1, 1, '2026-03-22 00:00:00', 9, NULL, NULL, 0, 3, 2, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 84, 1, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 87, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(43, NULL, 'Yoky', 'enchere', 'enchere', 12, NULL, 'article_43.png', '', 0, 1, 33, 365, 1, 1, '2026-03-22 00:00:00', 9, NULL, NULL, 1, 5, 3, '2026-03-22 18:22', '2026-04-22 18:22', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 2, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(44, NULL, 'Converse', 'Vroom', 'Vroom2', 12, NULL, 'article_44.jpg', '', 0, 1, 2, 13, 3, 1, '2026-03-26 00:00:00', 9, NULL, NULL, 0, 5, 4, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 34, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL),
(46, NULL, '', 'Villa', 'Villa', 1500000, NULL, 'article_46.png', '', 0, 1, 25, 304, 1, 1, '2026-03-31 00:00:00', 9, NULL, NULL, 0, 3, 3, '2026-03-22 18:22', '2026-04-22 18:22', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8, 400, '1200', '2027', 4, 0, 'fr', 7, 'Belfaux', 'rte d\'Autafond', '1782', 2, NULL, 1, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, 1, NULL, NULL, NULL, '', NULL),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 (47, NULL, 'Louis Vuitton', 'Nouveau test', 'Nouveau test description', 1212, NULL, 'article_47.webp', '', 0, 1, 1, 30, 1, 1, '2026-09-15 00:00:00', 9, NULL, NULL, 1, 5, 3, '2026-09-15 15:29', '2026-10-15 15:29', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 'fr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL);

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
                                  `id_visite` int(11) NOT NULL,
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
                                  `email` varchar(40) NOT NULL,
                                  `date_visite` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `article_visite`
--

INSERT INTO `article_visite` (`id_visite`, `ref_article`, `ref_personne`, `nom`, `prenom`, `adresse`, `ville`, `npa`, `no_prive`, `no_mobile`, `commentaire`, `par_email`, `par_telephone`, `par_mobile`, `email`, `date_visite`) VALUES
                                                                                                                                                                                                                                      (2, 46, 1, 'Jaquet', 'Alexandre', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alexjaquet@gmail.com', '2026-09-16 06:52:52'),
                                                                                                                                                                                                                                      (3, 46, 1, 'Jaquet', 'Alexandre', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alexjaquet@gmail.com', '2026-09-16 07:07:31'),
                                                                                                                                                                                                                                      (4, 46, 1, 'Jaquet', 'Alexandre', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alexjaquet@gmail.com', '2026-09-16 07:08:09'),
                                                                                                                                                                                                                                      (5, 46, 3, 'Jaquet', 'Alexandre', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'dev.jaquet.alexandre@gmail.com', '2026-09-16 07:11:59');

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

--
-- Déchargement des données de la table `a_livre`
--

INSERT INTO `a_livre` (`id_a_livre`, `ref_article`, `ref_acheteur`, `ref_vendeur`, `ref_enchere`, `date_achat`, `ref_mode_de_livraison`, `ref_statut`, `quantite`, `montant`, `date_reception`) VALUES
                                                                                                                                                                                                    (97, 42, 3, 1, NULL, '2026-04-09 13:06:25', 2, 12, NULL, '1212.0', NULL),
                                                                                                                                                                                                    (98, 39, 3, 1, NULL, '2026-03-25 13:31:12', 4, 12, 1, '1212212.0', '2026-03-25'),
                                                                                                                                                                                                    (99, 39, 3, 1, NULL, '2026-03-25 16:10:00', 4, 12, 1, '1212212.0', '2026-03-25'),
                                                                                                                                                                                                    (100, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, 1, '12.0', '2026-03-26'),
                                                                                                                                                                                                    (101, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, 1, '12.0', '2026-03-26'),
                                                                                                                                                                                                    (102, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, NULL, '12.0', NULL),
                                                                                                                                                                                                    (103, 36, 3, 1, NULL, '2026-03-27 11:28:43', 4, 12, NULL, '2990.0', NULL),
                                                                                                                                                                                                    (104, 42, 3, 1, NULL, '2026-04-09 13:06:25', 2, 12, NULL, '1212.0', NULL),
                                                                                                                                                                                                    (105, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, NULL, '12.0', NULL),
                                                                                                                                                                                                    (106, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, NULL, '12.0', NULL),
                                                                                                                                                                                                    (107, 44, 1, 3, NULL, '2026-04-06 10:48:57', 4, 12, NULL, '12.0', NULL),
                                                                                                                                                                                                    (108, 42, 3, 1, NULL, '2026-04-09 13:06:25', 2, 12, NULL, '1212.0', NULL),
                                                                                                                                                                                                    (109, 39, 3, 1, NULL, '2026-04-13 10:45:48', 4, 8, NULL, '1212212.0', NULL),
                                                                                                                                                                                                    (110, 41, 3, 1, NULL, '2026-04-13 10:46:05', 4, 8, NULL, '230.0', NULL);

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
                          `date_fermeture_enchere` datetime DEFAULT NULL,
                          `date_payement` datetime DEFAULT NULL,
                          `date_echeance` datetime DEFAULT NULL,
                          `rappel_1` date DEFAULT NULL,
                          `rappel_2` date DEFAULT NULL,
                          `rappel_3` date DEFAULT NULL,
                          `quantite` int(11) NOT NULL DEFAULT 1,
                          `ref_canton` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `a_paye`
--

INSERT INTO `a_paye` (`id_a_paye`, `ref_article`, `ref_vendeur`, `ref_acheteur`, `ref_enchere`, `ref_condition_payement`, `ref_mode_de_livraison`, `ref_statut`, `montant`, `date_fermeture_enchere`, `date_payement`, `date_echeance`, `rappel_1`, `rappel_2`, `rappel_3`, `quantite`, `ref_canton`) VALUES
                                                                                                                                                                                                                                                                                                          (89, 42, 1, 3, NULL, 3, 2, 14, '1212', '2026-04-08 10:18:30', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (90, 39, 1, 3, NULL, 5, 4, 14, '1212212', '2026-04-13 10:49:17', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (91, 39, 1, 1, NULL, 5, 4, 7, '1212212', '2026-03-25 16:10:51', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (92, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (93, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (94, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (95, 36, 1, 3, NULL, 1, 4, 14, '2990', '2026-03-27 11:27:30', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (96, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (97, 42, 1, 1, NULL, 3, 2, 14, '1212', '2026-04-08 10:18:14', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (98, 42, 1, 1, NULL, 3, 2, 14, '1212', '2026-04-08 10:18:14', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (99, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (100, 44, 3, 3, NULL, 5, 4, 14, '12', '2026-04-06 10:49:49', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (101, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (102, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (103, 44, 3, 1, NULL, 5, 4, 14, '12', '2026-04-04 18:16:33', NULL, NULL, NULL, NULL, NULL, 1, 7),
                                                                                                                                                                                                                                                                                                          (106, 39, 1, 3, NULL, 5, 4, 14, '1212212', '2026-04-13 10:49:17', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (107, 41, 1, 3, NULL, 5, 4, 14, '230', '2026-04-13 10:48:59', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (109, 34, 1, 3, NULL, 1, 4, 8, '23', '2026-04-22 18:22:00', NULL, NULL, NULL, NULL, NULL, 1, NULL),
                                                                                                                                                                                                                                                                                                          (111, 39, 1, 1, NULL, 5, 4, 8, '1212212', NULL, NULL, NULL, NULL, NULL, NULL, 1, 7);

-- --------------------------------------------------------

--
-- Structure de la table `boite_de_vitesse`
--

CREATE TABLE `boite_de_vitesse` (
                                    `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `boite_de_vitesse`
--

INSERT INTO `boite_de_vitesse` (`id`) VALUES
                                          (1),
                                          (2),
                                          (3);

-- --------------------------------------------------------

--
-- Structure de la table `boite_de_vitesse_libelle_langue`
--

CREATE TABLE `boite_de_vitesse_libelle_langue` (
                                                   `ref_boite_de_vitesse` int(11) NOT NULL,
                                                   `ref_libelle` int(11) NOT NULL,
                                                   `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `boite_de_vitesse_libelle_langue`
--

INSERT INTO `boite_de_vitesse_libelle_langue` (`ref_boite_de_vitesse`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                        (1, 10046, 1),
                                                                                                        (2, 10047, 1);

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

--
-- Déchargement des données de la table `canton_fr`
--

INSERT INTO `canton_fr` (`id_canton`, `nom`) VALUES
                                                 (1, 'Appenzell Rhodes-Extérieures - (AR)'),
                                                 (2, 'Appenzell Rhodes-Intérieures - (AI)'),
                                                 (3, 'Argovie - (AG)'),
                                                 (4, 'Berne - (BE)'),
                                                 (5, 'Bâle-Campagne - (BL)'),
                                                 (6, 'Bâle-Ville - (BS)'),
                                                 (7, 'Fribourg - (FR)'),
                                                 (8, 'Genève - (GE)'),
                                                 (9, 'Glaris - (GL)'),
                                                 (10, 'Grisons - (GR)'),
                                                 (11, 'Jura - (JU)'),
                                                 (12, 'Lucerne - (LU)'),
                                                 (13, 'Neuchâtel - (NE)'),
                                                 (14, '\r\nNidwald - (NW)'),
                                                 (15, 'Obwald - (OW)'),
                                                 (16, 'Schaffhouse - (SH)'),
                                                 (17, 'Schwyz - (SZ)'),
                                                 (18, 'Soleure - (SO)'),
                                                 (19, 'St-Gall - (SG)'),
                                                 (20, 'Tessin - (TI)'),
                                                 (21, 'Thurgovie - (TG)'),
                                                 (22, 'Uri - (UR)'),
                                                 (23, 'Valais - (VS)'),
                                                 (24, 'Vaud - (VD)'),
                                                 (25, 'Zoug - (ZG)'),
                                                 (26, 'Zurich - (ZH)');

-- --------------------------------------------------------

--
-- Structure de la table `categorie_libelle_langue`
--

CREATE TABLE `categorie_libelle_langue` (
                                            `ref_categorie` int(11) NOT NULL,
                                            `ref_libelle` int(11) NOT NULL,
                                            `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `categorie_libelle_langue`
--

INSERT INTO `categorie_libelle_langue` (`ref_categorie`, `ref_libelle`, `ref_langue`) VALUES

                                                                                          (1, 1, 1),
                                                                                          (2, 2, 1),
                                                                                          (3, 3, 1),
                                                                                          (5, 5, 1),
                                                                                          (6, 6, 1),
                                                                                          (7, 7, 1),
                                                                                          (8, 8, 1),
                                                                                          (9, 9, 1),
                                                                                          (10, 10, 1),
                                                                                          (11, 11, 1),
                                                                                          (12, 12, 1),
                                                                                          (13, 13, 1),
                                                                                          (14, 14, 1),
                                                                                          (15, 15, 1),
                                                                                          (16, 16, 1),
                                                                                          (18, 18, 1),
                                                                                          (19, 19, 1),
                                                                                          (20, 20, 1),
                                                                                          (21, 21, 1),
                                                                                          (22, 22, 1),
                                                                                          (23, 23, 1),
                                                                                          (24, 24, 1),
                                                                                          (25, 25, 1),
                                                                                          (26, 55, 1),
                                                                                          (27, 10000, 1),
                                                                                          (31, 10020, 1),
                                                                                          (32, 10021, 1),
                                                                                          (33, 10024, 1),
                                                                                          (34, 10029, 1),
                                                                                          (35, 10030, 1),
                                                                                          (36, 10031, 1),
                                                                                          (38, 10040, 1),
                                                                                          (90, 69, 1),
                                                                                          (91, 39, 1),
                                                                                          (92, 10049, 1),
                                                                                          (93, 17, 1),
                                                                                          (94, 10054, 1),
                                                                                          (95, 10055, 1),
                                                                                          (96, 10059, 1),
                                                                                          (97, 10058, 1),
                                                                                          (98, 10057, 1),
                                                                                          (99, 10056, 1),
                                                                                          (188, 188, 1),
                                                                                          (22222, 22222, 1);

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

--
-- Déchargement des données de la table `cepage`
--

INSERT INTO `cepage` (`id_cepage`, `ref_pays_region_vin`, `ref_type_de_vin`, `nom`) VALUES
                                                                                        (1, 1, 1, 'Fendant'),
                                                                                        (2, 1, 1, 'Johannisberg'),
                                                                                        (3, 1, 1, 'Malvoisie (Pinot Gris),'),
                                                                                        (4, 1, 1, 'Ermitage (Marsanne Blanche),'),
                                                                                        (5, 1, 1, 'Amigne'),
                                                                                        (6, 1, 1, 'Petite Arvine'),
                                                                                        (7, 1, 1, 'Sylvaner'),
                                                                                        (8, 1, 1, 'Pinot Noir'),
                                                                                        (9, 1, 1, 'Gamay'),
                                                                                        (10, 1, 1, 'Syrah'),
                                                                                        (11, 1, 1, 'Humagne rouge'),
                                                                                        (12, 1, 1, 'Cornalin'),
                                                                                        (13, 2, 1, 'Chasselas'),
                                                                                        (14, 2, 1, 'Pinot blanc'),
                                                                                        (15, 2, 1, 'Chardonnay'),
                                                                                        (16, 2, 1, 'Pinot gris'),
                                                                                        (17, 2, 1, 'Riesling x Sylvaner'),
                                                                                        (18, 2, 1, 'Gamay'),
                                                                                        (19, 2, 1, 'Pinot noir'),
                                                                                        (20, 2, 1, 'Gamaret'),
                                                                                        (21, 2, 1, 'Garanoir'),
                                                                                        (22, 2, 1, 'Merlo'),
                                                                                        (23, 2, 1, 'Syrah'),
                                                                                        (24, 2, 1, 'Müller-Thurgau (Riesling x Syl'),
                                                                                        (25, 2, 1, 'Pinot gris (Grauburgunder)'),
                                                                                        (26, 2, 1, 'Pinot blanc (Weissburgunder)'),
                                                                                        (27, 2, 1, 'Gewürztramine'),
                                                                                        (28, 2, 1, 'Räuschling'),
                                                                                        (29, 2, 1, 'Chardonnay'),
                                                                                        (30, 2, 1, 'Pinot noir (Blauburgunder),'),
                                                                                        (31, 1, 1, 'Gamaret'),
                                                                                        (32, 1, 1, 'Garanoir'),
                                                                                        (33, 1, 1, 'Regent'),
                                                                                        (34, 1, 1, 'Chasselas'),
                                                                                        (35, 1, 1, 'Pinot blanc'),
                                                                                        (36, 1, 1, 'Aligoté'),
                                                                                        (37, 1, 1, 'Chardonnay'),
                                                                                        (38, 1, 1, 'Pinot gris'),
                                                                                        (39, 1, 1, 'Müller-Thurgau'),
                                                                                        (40, 1, 1, 'Sauvignon blan'),
                                                                                        (41, 1, 1, 'Gewürztraminer'),
                                                                                        (42, 1, 1, 'Pinot noir'),
                                                                                        (43, 1, 1, 'Gamay'),
                                                                                        (44, 1, 1, 'Gamaret'),
                                                                                        (45, 1, 1, 'Garanoir'),
                                                                                        (46, 1, 1, 'Merlot'),
                                                                                        (47, 1, 1, 'Syrah'),
                                                                                        (48, 5, 1, 'Merlot'),
                                                                                        (49, 5, 1, 'Chardonnay'),
                                                                                        (50, 5, 1, 'Chasselas'),
                                                                                        (51, 5, 1, 'Sauvignon'),
                                                                                        (52, 5, 1, 'Sémillon'),
                                                                                        (53, 5, 1, 'Merlot'),
                                                                                        (54, 5, 1, 'Cabernet Sauvignon'),
                                                                                        (55, 5, 1, 'Gamaret'),
                                                                                        (56, 67, 1, 'Chasselas'),
                                                                                        (57, 1, 1, 'Sauvignon blanc'),
                                                                                        (58, 1, 1, 'Chardonnay'),
                                                                                        (59, 1, 1, 'Pinot gris'),
                                                                                        (60, 1, 1, 'Riesling x Sylvaner'),
                                                                                        (61, 1, 1, 'Gewürztraminer'),
                                                                                        (62, 1, 1, 'Riesling x Sylvaner (Müller-Thurgau)'),
                                                                                        (63, 1, 1, 'Pinot noir'),
                                                                                        (64, 1, 1, 'Gamaret'),
                                                                                        (65, 1, 1, 'Garanoir'),
                                                                                        (66, 1, 1, 'Chasselas'),
                                                                                        (67, 1, 1, 'Sauvignon blanc'),
                                                                                        (68, 1, 1, 'Chardonnay'),
                                                                                        (69, 1, 1, 'Pinot gris'),
                                                                                        (70, 1, 1, 'Riesling'),
                                                                                        (71, 1, 1, 'Gewürztraminer'),
                                                                                        (72, 1, 1, 'Riesling x Sylvaner (Müller-Thurgau)'),
                                                                                        (73, 1, 1, 'Pinot noir'),
                                                                                        (74, 1, 1, 'Gamaret'),
                                                                                        (75, 1, 1, 'Garanoir'),
                                                                                        (76, 1, 1, 'Chasselas 63%'),
                                                                                        (77, 1, 1, 'Pinot gris'),
                                                                                        (78, 1, 1, 'Traminer '),
                                                                                        (79, 1, 1, 'Pinot noir 22 %'),
                                                                                        (80, 1, 1, 'Gamaret'),
                                                                                        (81, 1, 1, 'Garanoir'),
                                                                                        (82, 2, 1, 'Alsace Chasselas (Gutedel)'),
                                                                                        (83, 2, 1, 'Alsace Edelzwicker'),
                                                                                        (84, 2, 1, 'Alsace Grand Cru'),
                                                                                        (85, 2, 1, 'Alsace Grand Cru Altenberg de Bergbieten'),
                                                                                        (86, 2, 1, 'Alsace Grand Cru Altenberg de Bergheim'),
                                                                                        (87, 2, 1, 'Alsace Grand Cru Altenberg de Wolxheim'),
                                                                                        (88, 2, 1, 'Alsace Grand Cru Brand'),
                                                                                        (89, 2, 1, 'Alsace Grand Cru Bruderthal'),
                                                                                        (90, 2, 1, 'Alsace Grand Cru Eichberg'),
                                                                                        (91, 2, 1, 'Alsace Grand Cru Engelberg'),
                                                                                        (92, 2, 1, 'Alsace Grand Cru Florimont'),
                                                                                        (93, 2, 1, 'Alsace Grand Cru Franckstein'),
                                                                                        (94, 2, 1, 'Alsace Grand Cru Froehn'),
                                                                                        (95, 2, 1, 'Alsace Grand Cru Furstentum'),
                                                                                        (96, 2, 1, 'Alsace Grand Cru Geisberg'),
                                                                                        (97, 2, 1, 'Alsace Grand Cru Gloeckelberg'),
                                                                                        (98, 2, 1, 'Alsace Grand Cru Goldert'),
                                                                                        (99, 2, 1, 'Alsace Grand Cru Hatschbourg'),
                                                                                        (100, 2, 1, 'Alsace Grand Cru Hengst'),
                                                                                        (101, 2, 1, 'Alsace Grand Cru Kanzlerberg'),
                                                                                        (102, 2, 1, 'Alsace Grand Cru Kastelberg'),
                                                                                        (103, 2, 1, 'Alsace Grand Cru Kessler'),
                                                                                        (104, 2, 1, 'Alsace Grand Cru Kirchberg de Barr'),
                                                                                        (105, 2, 1, 'Alsace Grand Cru Kirchberg de Ribeauvill'),
                                                                                        (106, 2, 1, 'Alsace Grand Cru Kitterlé'),
                                                                                        (107, 2, 1, 'Alsace Grand Cru Mambourg'),
                                                                                        (108, 2, 1, 'Alsace Grand Cru Mandelberg'),
                                                                                        (109, 2, 1, 'Alsace Grand Cru Marckrain'),
                                                                                        (110, 2, 1, 'Alsace Grand Cru Moenchberg'),
                                                                                        (111, 2, 1, 'Alsace Grand Cru Muenchberg'),
                                                                                        (112, 2, 1, 'Alsace Grand Cru Ollwiller'),
                                                                                        (113, 2, 1, 'Alsace Grand Cru Osterberg'),
                                                                                        (114, 2, 1, 'Alsace Grand Cru Pfersigberg'),
                                                                                        (115, 2, 1, 'Alsace Grand Cru Pfingstberg'),
                                                                                        (116, 2, 1, 'Alsace Grand Cru Praelatenberg'),
                                                                                        (117, 2, 1, 'Alsace Grand Cru Rangen'),
                                                                                        (118, 2, 1, 'Alsace Grand Cru Rosacker'),
                                                                                        (119, 2, 1, 'Alsace Grand Cru Saering'),
                                                                                        (120, 2, 1, 'Alsace Grand Cru Schoenenbourg'),
                                                                                        (121, 2, 1, 'Alsace Grand Cru Scholssberg'),
                                                                                        (122, 2, 1, 'Alsace Grand Cru Sommerberg'),
                                                                                        (123, 2, 1, 'Alsace Grand Cru Sonnenglanz'),
                                                                                        (124, 2, 1, 'Alsace Grand Cru Spiegel'),
                                                                                        (125, 2, 1, 'Alsace Grand Cru Sporen'),
                                                                                        (126, 2, 1, 'Alsace Grand Cru Steinert'),
                                                                                        (127, 2, 1, 'Alsace Grand Cru Steingrubler'),
                                                                                        (128, 2, 1, 'Alsace Grand Cru Steinklotz'),
                                                                                        (129, 2, 1, 'Alsace Grand Cru Vorbourg'),
                                                                                        (130, 2, 1, 'Alsace Grand Cru Wiebelberg'),
                                                                                        (131, 2, 1, 'Alsace Grand Cru Wineck-Schlossberg'),
                                                                                        (132, 2, 1, 'Alsace Grand Cru Winzenberg'),
                                                                                        (133, 2, 1, 'Alsace Grand Cru Zinnkoepflé'),
                                                                                        (134, 2, 1, 'Alsace Grand Cru Zotzenberg'),
                                                                                        (135, 2, 1, 'Alsace Muscat'),
                                                                                        (136, 2, 1, 'Alsace Pinot (Klevner)'),
                                                                                        (137, 2, 1, 'Alsace Pinot Noir'),
                                                                                        (138, 2, 1, 'Alsace Riesling'),
                                                                                        (139, 2, 1, 'Alsace Sylvaner'),
                                                                                        (140, 2, 1, 'Alsace Tokay-Pinot Gris'),
                                                                                        (141, 2, 1, 'Crémant d\'Alsace'),
(142, 2, 1, 'Brouilly'),
(143, 2, 1, 'Chénas'),
(144, 2, 1, 'Chiroubles'),
(145, 2, 1, 'Côte de Brouilly'),
(146, 2, 1, 'Fleurie'),
(147, 2, 1, 'Juliénas'),
(148, 2, 1, 'Morgon'),
(149, 2, 1, 'Moulin-à-Vent'),
(150, 2, 1, 'Régnié'),
(151, 2, 1, 'Saint-Amour'),
(152, 2, 1, 'Bordeaux Haut-Benauge'),
(153, 2, 1, 'Côtes de Bordeaux St-Macaire'),
(154, 2, 1, 'Entre-Deux-Mers'),
(155, 2, 1, 'Entre-Deux-Mers Haut-Benauge'),
(156, 2, 1, 'Graves de Vayres'),
(157, 2, 1, 'Loupiac'),
(158, 2, 1, 'Premières Côtes de Bordeaux'),
(159, 2, 1, 'Ste-Croix du Mont'),
(160, 2, 1, 'Ste-Foy Bordeaux'),
(161, 2, 1, 'Barsac'),
(162, 2, 1, 'Cadillac'),
(163, 2, 1, 'Cérons'),
(164, 2, 1, 'Graves'),
(165, 2, 1, 'Graves Supérieures'),
(166, 2, 1, 'Pessac-Léongnan'),
(167, 2, 1, 'Sauternes'),
(168, 2, 1, 'Blayais'),
(169, 2, 1, 'Bordeaux Côtes de francs'),
(170, 2, 1, 'Canon Fronsac'),
(171, 2, 1, 'Côtes de Blaye'),
(172, 2, 1, 'Côtes de Bourg'),
(173, 2, 1, 'Côtes de Castillon'),
(174, 2, 1, 'Fronsac'),
(175, 2, 1, 'Lalande de Pomerol'),
(176, 2, 1, 'Lussac St-Emilion'),
(177, 2, 1, 'Montagne St-Emilion'),
(178, 2, 1, 'Néac'),
(179, 2, 1, 'Pomerol'),
(180, 2, 1, 'Premières Côtes de Blaye'),
(181, 2, 1, 'Puisseguin St-Emilion'),
(182, 2, 1, 'St-Emilion'),
(183, 2, 1, 'St-Emilion Grand Cru'),
(184, 2, 1, 'St-Georges St-Emilion'),
(185, 2, 1, 'Haut-Médoc'),
(186, 2, 1, 'Listrac-Médoc'),
(187, 2, 1, 'Margaux'),
(188, 2, 1, 'Médoc'),
(189, 2, 1, 'Moulis-en-Médoc'),
(190, 2, 1, 'Pauillac'),
(191, 2, 1, 'St-Estèphe'),
(192, 2, 1, 'St-Julien'),
(193, 13, 1, 'Bourgogne Côtes d\'Auxerre'),
                                                                                        (194, 13, 1, 'Bourgogne-Irancy'),
                                                                                        (195, 13, 1, 'Chablis'),
                                                                                        (196, 13, 1, 'Chablis Grand Cru'),
                                                                                        (197, 13, 1, 'Chablis Premier Cru'),
                                                                                        (198, 13, 1, 'Petit Chablis'),
                                                                                        (199, 13, 1, 'Sauvignon de St-Bris'),
                                                                                        (200, 13, 1, 'Bourgogne Côte Chalonnaise'),
                                                                                        (201, 13, 1, 'Bouzeron'),
                                                                                        (202, 13, 1, 'Givry'),
                                                                                        (203, 13, 1, 'Givry Premier Cru'),
                                                                                        (204, 13, 1, 'Mercurey'),
                                                                                        (205, 13, 1, 'Mercurey Premier Cru'),
                                                                                        (206, 13, 1, 'Montagny'),
                                                                                        (207, 13, 1, 'Montagny Premier Cru'),
                                                                                        (208, 13, 1, 'Rully'),
                                                                                        (209, 13, 1, 'Rully Premier Cru'),
                                                                                        (210, 13, 1, 'Aloxe-Corton'),
                                                                                        (211, 13, 1, 'Aloxe-Corton Premier Cru'),
                                                                                        (212, 13, 1, 'Auxey-Duresses'),
                                                                                        (213, 13, 1, 'Auxey-Duresses Premier Cru'),
                                                                                        (214, 13, 1, 'Batard-Montrachet'),
                                                                                        (215, 13, 1, 'Beaune'),
                                                                                        (216, 13, 5, ''),
                                                                                        (217, 13, 1, 'Beaune'),
                                                                                        (218, 13, 1, 'Beaune Premier Cru'),
                                                                                        (219, 13, 1, 'Bienvenues Bâtard-Montrachet'),
                                                                                        (220, 13, 1, 'Blagny'),
                                                                                        (221, 13, 1, 'Blagny Premier Cru'),
                                                                                        (222, 13, 1, 'Bourgogne Hautes-Côtes de Beaune'),
                                                                                        (223, 13, 1, 'Charlemagne'),
                                                                                        (224, 13, 1, 'Chassagne-Montrachet'),
                                                                                        (225, 13, 1, 'Chassagne-Montrachet Premier Cru'),
                                                                                        (226, 13, 1, 'Chevalier-Montrachet'),
                                                                                        (227, 13, 1, 'Chorey-lès-Beaune'),
                                                                                        (228, 13, 1, 'Corton'),
                                                                                        (229, 13, 1, 'Corton-Charlemagne'),
                                                                                        (230, 13, 1, 'Côtes de Beaune'),
                                                                                        (231, 13, 1, 'Criots Bâtard-Montrachet'),
                                                                                        (232, 13, 1, 'Ladoix'),
                                                                                        (233, 13, 1, 'Ladoix Premier Cru'),
                                                                                        (234, 13, 1, 'Ladoix-Serrigny'),
                                                                                        (235, 13, 1, 'Maranges'),
                                                                                        (236, 13, 1, 'Maranges Premier Cru'),
                                                                                        (237, 13, 1, 'Meursault'),
                                                                                        (238, 13, 1, 'Meursault Premier Cru'),
                                                                                        (239, 13, 1, 'Monthélie'),
                                                                                        (240, 13, 1, 'Monthélie Premier Cru'),
                                                                                        (241, 13, 1, 'Montrachet'),
                                                                                        (242, 13, 1, 'Pernand-Vergelesses'),
                                                                                        (243, 13, 1, 'Pernand-Vergelesses Premier Cru'),
                                                                                        (244, 13, 1, 'Pommard'),
                                                                                        (245, 13, 1, 'Pommard Premier Cru'),
                                                                                        (246, 13, 1, 'Puligny-Montrachet'),
                                                                                        (247, 13, 1, 'Puligny-Montrachet Premier Cru'),
                                                                                        (248, 13, 1, 'Saint-Aubin'),
                                                                                        (249, 13, 1, 'Saint-Romain'),
                                                                                        (250, 13, 1, 'Santenay'),
                                                                                        (251, 13, 1, 'Santenay Premier Cru'),
                                                                                        (252, 13, 1, 'Savigny-lès-Beaune'),
                                                                                        (253, 13, 1, 'Savigny-lès-Beaune Premier Cru'),
                                                                                        (254, 13, 1, 'St Aubin Premier Cru'),
                                                                                        (255, 13, 1, 'Volnay'),
                                                                                        (256, 13, 1, 'Volnay Premier Cru'),
                                                                                        (257, 13, 1, 'Volnay Santenots'),
                                                                                        (258, 13, 1, 'Bonnes Mares'),
                                                                                        (259, 13, 1, 'Bourgogne Hautes-Côtes de Nuits'),
                                                                                        (260, 13, 1, 'Chambertin'),
                                                                                        (261, 13, 1, 'Chambertin-Clos de Bèze'),
                                                                                        (262, 13, 1, 'Chambolle-Musigny'),
                                                                                        (263, 13, 1, 'Chambolle-Musigny Premier Cru'),
                                                                                        (264, 13, 1, 'Chapelle-Chambertin'),
                                                                                        (265, 13, 1, 'Charmes-Chambertin'),
                                                                                        (266, 13, 1, 'Clos de la Roche'),
                                                                                        (267, 13, 1, 'Clos des Lambrays'),
                                                                                        (268, 13, 1, 'Clos de Tart'),
                                                                                        (269, 13, 1, 'Clos de Vougeot'),
                                                                                        (270, 13, 1, 'Clos Saint-Denis'),
                                                                                        (271, 13, 1, 'Echezeaux'),
                                                                                        (272, 13, 1, 'Fixin'),
                                                                                        (273, 13, 1, 'Fixin Premier Cru'),
                                                                                        (274, 13, 1, 'Gevrey-Chambertin'),
                                                                                        (275, 13, 1, 'Gevrey-Chambertin Premier Cru'),
                                                                                        (276, 13, 1, 'Grands-Echezeaux'),
                                                                                        (277, 13, 1, 'Griotte-Chambertin'),
                                                                                        (278, 13, 1, 'La Grande Rue'),
                                                                                        (279, 13, 1, 'La Romanée'),
                                                                                        (280, 13, 1, 'La Tâche'),
                                                                                        (281, 13, 1, 'Latricières-Chambertin'),
                                                                                        (282, 13, 1, 'Marsannay'),
                                                                                        (283, 13, 1, 'Marsannay Rosé'),
                                                                                        (284, 13, 1, 'Mazis-Chambertin'),
                                                                                        (285, 13, 1, 'Mazoyères-Chambertin'),
                                                                                        (286, 13, 1, 'Morey-St-Denis'),
                                                                                        (287, 13, 1, 'Morey-St-Denis Premier Cru'),
                                                                                        (288, 13, 1, 'Musigny'),
                                                                                        (289, 13, 1, 'Nuits Premier Cru ou Nuits-St-Georges Pr'),
                                                                                        (290, 13, 1, 'Nuits-Saint-Georges'),
                                                                                        (291, 13, 1, 'Richebourg'),
                                                                                        (292, 13, 1, 'Romanée-Conti'),
                                                                                        (293, 13, 1, 'Romanée-Saint-Vivant'),
                                                                                        (294, 13, 1, 'Ruchottes-Chambertin'),
                                                                                        (295, 13, 1, 'Vosne-Romanée'),
                                                                                        (296, 13, 1, 'Vosne-Romanée Premier Cru'),
                                                                                        (297, 13, 1, 'Vougeot'),
                                                                                        (298, 13, 1, 'Vougeot Premier Cru'),
                                                                                        (299, 13, 1, 'Bourgogne Côtes du Couchois'),
                                                                                        (300, 13, 1, 'Mâcon'),
                                                                                        (301, 13, 1, 'Mâcon-Supérieur'),
                                                                                        (302, 13, 1, 'Mâcon-Villages'),
                                                                                        (303, 13, 1, 'Pouilly-Fuissé'),
                                                                                        (304, 13, 1, 'Pouilly-Loché'),
                                                                                        (305, 13, 1, 'Pouilly-Vinzelles'),
                                                                                        (306, 13, 1, 'Saint-Véran'),
                                                                                        (307, 14, 1, 'Roussette de Bugey'),
                                                                                        (308, 14, 1, 'Roussette de Bugey Anglefort'),
                                                                                        (309, 14, 1, 'Roussette de Bugey Arbignieu'),
                                                                                        (310, 14, 1, 'Roussette de Bugey Montagnieu'),
                                                                                        (311, 14, 1, 'Roussette de Bugey Virieu-le-Grand'),
                                                                                        (312, 14, 1, 'Roussettte de Bugey Lagnieu'),
                                                                                        (313, 14, 1, 'Vin du Buget Montagnieu'),
                                                                                        (314, 14, 1, 'Vin du Bugey'),
                                                                                        (315, 14, 1, 'Vin du Bugey Cerdon'),
                                                                                        (316, 14, 1, 'Vin du Bugey Cerdon Mousseux'),
                                                                                        (317, 14, 1, 'Vin du Bugey Cerdon Pétillant'),
                                                                                        (318, 14, 1, 'Vin du Bugey Machuraz'),
                                                                                        (319, 14, 1, 'Vin du Bugey Manicle'),
                                                                                        (320, 14, 1, 'Vin du Bugey Montagnieu'),
                                                                                        (321, 14, 1, 'Vin du Bugey Mousseux'),
                                                                                        (322, 14, 1, 'Vin du Bugey Pétillant'),
                                                                                        (323, 14, 1, 'Vin du Bugey Virieu-le-Grand'),
                                                                                        (324, 15, 1, 'Champagne'),
                                                                                        (325, 15, 1, 'Coteaux Champenois'),
                                                                                        (326, 15, 1, 'Coteaux Champenois'),
                                                                                        (327, 15, 1, 'Rosé des Riceys'),
                                                                                        (328, 25, 1, 'Ajaccio'),
                                                                                        (329, 25, 1, 'Muscat du Cap Corse'),
                                                                                        (330, 25, 1, 'Patrimonio'),
                                                                                        (331, 25, 1, 'Vin de Corse'),
                                                                                        (332, 25, 1, 'Vin de Corse Calvi'),
                                                                                        (333, 25, 1, 'Vin de Corse Coteaux du Cap Corse'),
                                                                                        (334, 25, 1, 'Vin de Corse Figari'),
                                                                                        (335, 25, 1, 'Vin de Corse Porto-Vecchio'),
                                                                                        (336, 25, 1, 'Vin de Corse Sartène'),
                                                                                        (337, 26, 1, 'Côtes de Toul'),
                                                                                        (338, 26, 1, 'Vin de Moselle'),
                                                                                        (339, 27, 1, 'Arbois'),
                                                                                        (340, 27, 1, 'Arbois Mousseux'),
                                                                                        (341, 27, 1, 'Arbois Pupillin'),
                                                                                        (342, 27, 1, 'Château-Chalon'),
                                                                                        (343, 27, 1, 'Côtes du Jura'),
                                                                                        (344, 27, 1, 'Côtes du Jura Mousseux'),
                                                                                        (345, 27, 1, 'Crémant du Jura'),
                                                                                        (346, 27, 1, 'L\'Etoile'),
(347, 27, 1, 'L\'Etoile Mousseux'),
                                                                                        (348, 0, 0, ''),
                                                                                        (349, 16, 1, 'Blanquette de Limoux'),
                                                                                        (350, 16, 1, 'Blanquette méthode ancéstrale'),
                                                                                        (351, 16, 1, 'Clairette du Languedoc'),
                                                                                        (352, 16, 1, 'Corbières'),
                                                                                        (353, 16, 1, 'Coteaux de la Méjanelle ( La Méjanelle)'),
                                                                                        (354, 16, 1, 'Coteaux de Languedoc St-Saturnin'),
                                                                                        (355, 16, 1, 'Coteaux de St-Cristol'),
                                                                                        (356, 16, 1, 'Coteaux de Vérargues'),
                                                                                        (357, 16, 1, 'Coteaux du Languedoc'),
                                                                                        (358, 16, 1, 'Coteaux du Languedoc Cabrières'),
                                                                                        (359, 16, 1, 'Coteaux du Languedoc La Clape'),
                                                                                        (360, 16, 1, 'Coteaux du Languedoc Montpeyroux'),
                                                                                        (361, 16, 1, 'Coteaux du Languedoc Picpoul-de-Pinet'),
                                                                                        (362, 16, 1, 'Coteaux du Languedoc Pic-St-Loup'),
                                                                                        (363, 16, 1, 'Coteaux du Languedoc Quatourze'),
                                                                                        (364, 16, 1, 'Coteaux du Languedoc St-Drézéry'),
                                                                                        (365, 16, 1, 'Coteaux du Languedoc St-Georges-d\'Orques'),
(366, 16, 1, 'Côtes de la Malapère'),
(367, 16, 1, 'Côtes de Millau'),
(368, 16, 1, 'Côtes du Cabardès et de l\'Orbiel'),
                                                                                        (369, 16, 1, 'Crémant de Limoux'),
                                                                                        (370, 16, 1, 'Faugères'),
                                                                                        (371, 16, 1, 'Fitou'),
                                                                                        (372, 16, 1, 'Limoux'),
                                                                                        (373, 16, 1, 'Minervois'),
                                                                                        (374, 16, 1, 'St-Chinian'),
                                                                                        (375, 29, 1, 'Anjou'),
                                                                                        (376, 29, 1, 'Anjou Rive Droite'),
                                                                                        (377, 29, 1, 'Anjou Rive Gauche'),
                                                                                        (378, 29, 1, 'Auvergne'),
                                                                                        (379, 29, 1, 'Centre'),
                                                                                        (380, 29, 1, 'Pays Nantais'),
                                                                                        (381, 29, 1, 'Poitou'),
                                                                                        (382, 29, 1, 'Saumurois'),
                                                                                        (383, 29, 1, 'Touraine'),
                                                                                        (384, 29, 1, 'Vendée'),
                                                                                        (385, 31, 1, 'Coteaux du Lyonnais'),
                                                                                        (386, 17, 1, 'Bandol'),
                                                                                        (387, 17, 1, 'Baux de Provence'),
                                                                                        (388, 17, 1, 'Bellet'),
                                                                                        (389, 17, 1, 'Cassis'),
                                                                                        (390, 17, 1, 'Coteaux d\'Aix-en-Provence'),
(391, 17, 1, 'Coteaux du Pierrevert'),
(392, 17, 1, 'Coteaux Varois'),
(393, 17, 1, 'Côtes de Provence'),
(394, 17, 1, 'Palette'),
(395, 20, 1, 'châteaumeillant'),
(396, 20, 1, 'Châtillon-en-Diois'),
(397, 20, 1, 'Clairette de Die'),
(398, 20, 1, 'Coteaux de Die'),
(399, 20, 1, 'Crémant de Die'),
(400, 20, 1, 'Châteauneuf-du-Pape'),
(401, 20, 1, 'Clairette de Bellegarde'),
(402, 20, 1, 'Costières de Nîmes'),
(403, 20, 1, 'Coteaux du Tricastin'),
(404, 20, 1, 'Côtes du Lubéron'),
(405, 20, 1, 'Côtes du Rhône Beaumes-de-Venise'),
(406, 20, 1, 'Côtes du Rhône Cairanne'),
(407, 20, 1, 'Côtes du Rhône Chusclan'),
(408, 20, 1, 'Côtes du Rhône Laudun'),
(409, 20, 1, 'Côtes du Rhône Roaix'),
(410, 20, 1, 'Côtes du Rhône Rochegude'),
(411, 20, 1, 'Côtes du Rhône Sablet'),
(412, 20, 1, 'Côtes du Rhône Séguret'),
(413, 20, 1, 'Côtes du Rhônes Rousset-Les-Vignes'),
(414, 20, 1, 'Côtes du Rhône St-Gervais'),
(415, 20, 1, 'Côtes du Rhône St-Maurice-sur-Eygues'),
(416, 20, 1, 'Côtes du Rhône St-Pantaléon-les-Vignes'),
(417, 20, 1, 'Côtes du Rhône Valréas'),
(418, 20, 1, 'Côtes du Rhône-Villages'),
(419, 20, 1, 'Côtes du Rhône Vinsobres'),
(420, 20, 1, 'Côtes du Rhône Visan'),
(421, 20, 1, 'Côtes du Ventoux'),
(422, 30, 1, 'Côtes du Vivarais'),
(423, 20, 1, 'Côtes du Vivarais Orgnac l\'Aven'),
                                                                                        (424, 20, 1, 'Côtes du Vivarais St-Montant'),
                                                                                        (425, 20, 1, 'Côtes du Vivarais St-Remèze'),
                                                                                        (426, 20, 1, 'Gigondas'),
                                                                                        (427, 20, 1, 'Lirac'),
                                                                                        (428, 20, 1, 'Muscat de Beaumes-de-Venise'),
                                                                                        (429, 20, 1, 'Rasteau'),
                                                                                        (430, 20, 1, 'Tavel'),
                                                                                        (431, 20, 1, 'Vacqueyras'),
                                                                                        (432, 20, 1, 'Vacqueyras'),
                                                                                        (433, 20, 1, 'Château Grillet'),
                                                                                        (434, 20, 1, 'Condrieu'),
                                                                                        (435, 20, 1, 'Cornas'),
                                                                                        (436, 20, 1, 'Côte Rôtie'),
                                                                                        (437, 20, 1, 'St-joseph'),
                                                                                        (438, 20, 1, 'St-Péray'),
                                                                                        (439, 20, 1, 'St-Péray Mousseux'),
                                                                                        (440, 20, 1, 'Crozes-Hermitage'),
                                                                                        (441, 20, 1, 'Hermitage'),
                                                                                        (442, 20, 1, 'Côte du Rhône'),
                                                                                        (443, 33, 1, 'Banyuls'),
                                                                                        (444, 33, 1, 'Collioure'),
                                                                                        (445, 33, 1, 'Côtes du Roussillon'),
                                                                                        (446, 33, 1, 'Côtes du Roussillon-villages'),
                                                                                        (447, 33, 1, 'Côtes du Roussillon-villages Caramany'),
                                                                                        (448, 33, 1, 'Côtes du Roussillon-Villages Latour-de-F'),
                                                                                        (449, 33, 1, 'Maury'),
                                                                                        (450, 33, 1, 'Muscat de Rivesaltes'),
                                                                                        (451, 33, 1, 'Rivesaltes'),
                                                                                        (452, 34, 1, 'Crépy'),
                                                                                        (453, 34, 1, 'Mousseux de Savoie'),
                                                                                        (454, 34, 1, 'Pétillant de Savoie'),
                                                                                        (455, 34, 1, 'Roussette de Savoie'),
                                                                                        (456, 34, 1, 'Roussette de Savoie Frangy'),
                                                                                        (457, 34, 1, 'Roussette de Savoie Marestel'),
                                                                                        (458, 34, 1, 'Roussette de Savoie Marestel-Altesse'),
                                                                                        (459, 34, 1, 'Roussette de Savoie Monterminod'),
                                                                                        (460, 34, 1, 'Roussette de Savoie Monthoux'),
                                                                                        (461, 34, 1, 'Seyssel'),
                                                                                        (462, 34, 1, 'Seyssel Mousseux'),
                                                                                        (463, 34, 1, 'Vin de Savoie'),
                                                                                        (464, 34, 1, 'Vin de Savoie Abymes'),
                                                                                        (465, 34, 1, 'Vin de Savoie Apremont'),
                                                                                        (466, 34, 1, 'Vin de Savoie Arbin'),
                                                                                        (467, 34, 1, 'Vin de Savoie Ayze'),
                                                                                        (468, 34, 1, 'Vin de Savoie Ayze Charpignat'),
                                                                                        (469, 34, 1, 'Vin de Savoie Ayze Mousseux'),
                                                                                        (470, 34, 1, 'Vin de Savoie Ayze Pétillant'),
                                                                                        (471, 34, 1, 'Vin de Savoie Bergeron'),
                                                                                        (472, 34, 1, 'Vin de Savoie Chautagne'),
                                                                                        (473, 34, 1, 'Vin de Savoie Chignin'),
                                                                                        (474, 34, 1, 'Vin de Savoie Chignin-Bergeron'),
                                                                                        (475, 34, 1, 'Vin de Savoie Cruet'),
                                                                                        (476, 34, 1, 'Vin de Savoie Jongieux'),
                                                                                        (477, 34, 1, 'Vin de Savoie Marignan'),
                                                                                        (478, 34, 1, 'Vin de Savoie Marin'),
                                                                                        (479, 34, 1, 'Vin de Savoie Montmélian'),
                                                                                        (480, 34, 1, 'Vin de Savoie Ripaille'),
                                                                                        (481, 34, 1, 'Vin de Savoie Ste-Marie-d\'Alloix'),
(482, 34, 1, 'Vin de Savoie St-Jean-de-la-Porte'),
(483, 34, 1, 'Vin de Savoie St-Jeoire Prieuré'),
(484, 18, 1, 'Marcillac'),
(485, 18, 1, 'Vin d\'Entraygues et de Fel'),
                                                                                        (486, 18, 1, 'Vins d\'Estaing'),
(487, 18, 1, 'Bergerac'),
(488, 18, 1, 'Bergerac Sec'),
(489, 18, 1, 'Côtes de Bergerac'),
(490, 18, 1, 'côtes de Bergerac Moelleux'),
(491, 18, 1, 'Côtes de Montravel'),
(492, 18, 1, 'Haut-Montravel'),
(493, 18, 1, 'Monbazillac'),
(494, 18, 1, 'Montravel'),
(495, 18, 1, 'Pécharmant'),
(496, 18, 1, 'Rosette'),
(497, 18, 1, 'Saussignac'),
(498, 18, 1, 'Béarn'),
(499, 18, 1, 'Côtes de St-Mont'),
(500, 18, 1, 'Côtes du Brulhois'),
(501, 18, 1, 'Madiran'),
(502, 18, 1, 'Pacherenc du Vic-Bilh'),
(503, 18, 1, 'Tursan'),
(504, 18, 1, 'Bergerac'),
(505, 18, 1, 'Bergerac Sec'),
(506, 18, 1, 'Côtes de Bergerac'),
(507, 18, 1, 'côtes de Bergerac Moelleux'),
(508, 18, 1, 'Côtes de Montravel'),
(509, 18, 1, 'Haut-Montravel'),
(510, 18, 1, 'Monbazillac'),
(511, 18, 1, 'Montravel'),
(512, 18, 1, 'Pécharmant'),
(513, 18, 1, 'Rosette'),
(514, 18, 1, 'Saussignac'),
(515, 18, 1, 'Côtes du Frontonnais'),
(516, 18, 1, 'Côtes du Frontonnais-Fronton'),
(517, 18, 1, 'Côtes du Frontonnais-Villaudric'),
(518, 18, 1, 'Béarn'),
(520, 18, 1, 'Madiran'),
(521, 18, 1, 'Pacherenc du Vic-Bilh'),
(522, 18, 1, 'Côtes de St-Mont'),
(523, 18, 1, 'Tursan'),
(524, 18, 1, 'Cahors'),
(525, 18, 1, 'Côtes de Buzet (Garonne)'),
(526, 18, 1, 'Côtes de Duras'),
(527, 18, 1, 'Côtes de Marmandais'),
(528, 18, 1, 'Côtes du Brulhois'),
(529, 18, 1, 'Béarn'),
(530, 18, 1, 'Béarn Bellocq'),
(531, 18, 1, 'Irouléguy'),
(532, 18, 1, 'Jurançon'),
(533, 18, 1, 'Jurançon Sec'),
(534, 18, 1, 'Madiran'),
(535, 18, 1, 'Pacherenc du Vic-Bilh'),
(536, 18, 1, 'Côtes de Millau AOVDQS'),
(537, 18, 1, 'Gaillac'),
(538, 18, 1, 'Gaillac Doux'),
(539, 18, 1, 'Gaillac Mousseux'),
(540, 18, 1, 'Côtes du Brulhois'),
(541, 18, 1, 'Côtes du Frontonnais'),
(542, 18, 1, 'Lavilledieu AOVDQS');

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

--
-- Déchargement des données de la table `commentaire`
--

INSERT INTO `commentaire` (`id_commentaire`, `ref_article`, `ref_emetteur`, `question`, `texte`, `date`) VALUES
(1, 19, 3, 'Coucou', 'Voici un petit test', '2026-03-10 08:14:31'),
(2, 19, 1, 'Non', 'Il faut passer au magasin ...', '2026-03-10 08:28:09'),
(3, 8, 1, 'J\'aime trop', 'Test', '2026-03-10 21:54:15'),
                                                                                        (4, 32, 1, 'Intéressant', 'Ma nièce aimerait bien le dernier modèle', '2026-03-12 15:30:41'),
                                                                                        (5, 35, 1, 'Test', 'Test', '2026-03-17 10:38:49'),
                                                                                        (6, 34, 1, 'Test', 'Ceci est un test', '2026-03-21 15:27:04'),
                                                                                        (7, 19, 1, 'Nouveau', 'Encore un test pour les dates', '2026-03-30 15:56:01');

-- --------------------------------------------------------

--
-- Structure de la table `condition_livraison`
--

CREATE TABLE `condition_livraison` (
                                       `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `condition_livraison`
--

INSERT INTO `condition_livraison` (`id`) VALUES
                                             (1),
                                             (2),
                                             (3),
                                             (4);

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

--
-- Déchargement des données de la table `condition_livraison_libelle_langue`
--

INSERT INTO `condition_livraison_libelle_langue` (`ref_mode_de_livraison`, `ref_libelle`, `ref_langue`, `frais`) VALUES
                                                                                                                     (1, 10200, 1, '0'),
                                                                                                                     (2, 10201, 1, '0'),
                                                                                                                     (3, 10202, 1, '0'),
                                                                                                                     (4, 10203, 1, '0');

-- --------------------------------------------------------

--
-- Structure de la table `condition_payement_libelle_langue`
--

CREATE TABLE `condition_payement_libelle_langue` (
                                                     `ref_condition_payement` int(11) NOT NULL,
                                                     `ref_libelle` int(11) NOT NULL,
                                                     `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `condition_payement_libelle_langue`
--

INSERT INTO `condition_payement_libelle_langue` (`ref_condition_payement`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                            (1, 10100, 1),
                                                                                                            (2, 10101, 1),
                                                                                                            (3, 10102, 1),
                                                                                                            (4, 10103, 1);                                   ;;

-- --------------------------------------------------------

--
-- Structure de la table `demande_visite`
--

CREATE TABLE `demande_visite` (
                                  `id_demande` int(11) NOT NULL,
                                  `nom` varchar(40) NOT NULL,
                                  `prenom` varchar(40) NOT NULL,
                                  `adresse` varchar(100) NOT NULL,
                                  `tel_mobile` varchar(30) NOT NULL,
                                  `ref_article` int(11) NOT NULL,
                                  `ref_vendeur` int(11) NOT NULL,
                                  `commentaire` text NOT NULL,
                                  `email` varchar(100) DEFAULT NULL,
                                  `date_visite` datetime DEFAULT NULL,
                                  `date_visite_fin` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `demande_visite`
--

INSERT INTO `demande_visite` (`id_demande`, `nom`, `prenom`, `adresse`, `tel_mobile`, `ref_article`, `ref_vendeur`, `commentaire`, `email`, `date_visite`, `date_visite_fin`) VALUES
                                                                                                                                                                                  (1, 'Jaquet', 'Alexandre', 'route d\'Autafond', '+410786665544', 46, 1, 'test', 'alexjaquet@gmail.com', '2026-03-31 09:00:00', NULL),
(2, 'Jaquet', 'Alexandre', 'route d\'Autafond', '+410787776655', 46, 1, 'Test commentaire', 'alexjaquet@gmail.com', '2026-03-31 18:00:00', NULL),
                                                                                                                                                                                  (3, 'Jaquet', 'Alexandre', 'route d\'Autafond', '+410788889900', 46, 1, '', 'alexjaquet@gmail.com', '2026-03-31 17:30:00', NULL),
(4, 'Jaquet Demande de visite', 'Test', 'route d\'Autafond', '+41', 46, 1, 'Commentaire de test', 'alexjaquet@gmail.com', '2026-04-21 09:00:00', NULL),
                                                                                                                                                                                  (5, 'Dupont', 'Jean', 'route d\'Autafond 10', '+410786664433', 46, 1, 'Test', 'alexjaquet@gmail.com', '2026-04-21 08:30:00', NULL),
(6, 'Dupont', 'Pierre', 'route d\'Autafond', '+419999877', 46, 1, 'Commentare joli maison ^^', 'alexjaquet@gmail.com', '2026-04-18 09:00:00', NULL),
                                                                                                                                                                                  (7, 'Jaquet', 'Alexandre', 'route d\'Autafond', '+410787776655', 46, 1, 'Demande de rendez-vous.', 'alexjaquet@gmail.com', '2026-04-28 15:00:00', '2026-04-28 16:00:00'),
(8, 'Dupont', 'Claude', 'route d\'Autafond', '+41', 46, 1, 'Joli maison possibilité de piscine ?', 'alexjaquet@gmail.com', '2026-04-03 14:30:00', '2026-04-03 15:30:00');

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

--
-- Déchargement des données de la table `enchere`
--

INSERT INTO `enchere` (`id_enchere`, `ref_article`, `ref_enchereur`, `prix`, `date_enchere`) VALUES
                                                                                                 (5, 19, 1, '9999999999', '2026-03-10 07:46:26'),
                                                                                                 (6, 35, 1, '2000', '2026-03-12 13:23:57'),
                                                                                                 (7, 35, 1, '3000', '2026-03-13 07:35:37'),
                                                                                                 (8, 26, 1, '2000000000', '2026-03-14 10:19:10'),
                                                                                                 (10, 37, 3, '20000000', '2026-03-17 15:27:12'),
                                                                                                 (11, 38, 3, '2900', '2026-03-17 15:52:16'),
                                                                                                 (12, 37, 1, '2000000', '2026-03-17 18:03:36'),
                                                                                                 (13, 38, 1, '3000', '2026-03-17 20:19:32'),
                                                                                                 (14, 37, 1, '21222222', '2026-03-17 20:19:47'),
                                                                                                 (15, 38, 1, '3001', '2026-03-17 20:21:02'),
                                                                                                 (16, 38, 1, '3004', '2026-03-17 20:51:07'),
                                                                                                 (17, 37, 1, '21222223', '2026-03-17 20:51:22'),
                                                                                                 (18, 38, 1, '30005', '2026-03-17 21:03:15'),
                                                                                                 (19, 17, 1, '400', '2026-03-19 11:11:19'),
                                                                                                 (20, 38, 1, '2222', '2026-03-21 07:24:43'),
                                                                                                 (21, 41, 3, '12222', '2026-03-21 07:27:39'),
                                                                                                 (22, 41, 3, '222221', '2026-03-21 07:33:21'),
                                                                                                 (23, 26, 1, '1212122121', '2026-03-22 18:05:03'),
                                                                                                 (24, 43, 3, '9999999', '2026-03-22 18:25:25'),
                                                                                                 (25, 42, 3, '12222', '2026-03-23 04:24:18'),
                                                                                                 (26, 43, 3, '9999999', '2026-03-23 06:21:32'),
                                                                                                 (27, 34, 3, '23', '2026-03-23 10:47:41'),
                                                                                                 (32, 28, 1, '12121212', '2026-09-15 15:26:31');

-- --------------------------------------------------------

--
-- Structure de la table `etat`
--

CREATE TABLE `etat` (
                        `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `etat`
--

INSERT INTO `etat` (`id`) VALUES
                              (1),
                              (2);

-- --------------------------------------------------------

--
-- Structure de la table `etat_libelle_langue`
--

CREATE TABLE `etat_libelle_langue` (
                                       `ref_etat` int(11) NOT NULL,
                                       `ref_libelle` int(11) NOT NULL,
                                       `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `etat_libelle_langue`
--

INSERT INTO `etat_libelle_langue` (`ref_etat`, `ref_libelle`, `ref_langue`) VALUES
                                                                                (1, 10035, 1),
                                                                                (2, 10036, 1);

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
                                      `date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `evaluation_article`
--

INSERT INTO `evaluation_article` (`id_evaluation_article`, `ref_article`, `ref_vendeur`, `ref_acheteur`, `note`, `date`) VALUES
                                                                                                                             (2, 36, 0, 1, 10, '2026-03-18 15:11:57'),
                                                                                                                             (3, 39, 0, 1, 10, '2026-03-20 16:28:22'),
                                                                                                                             (4, 39, 0, 3, 10, '2026-03-21 08:12:43'),
                                                                                                                             (5, 40, 0, 3, 10, '2026-03-21 08:25:07'),
                                                                                                                             (6, 41, 0, 1, 10, '2026-03-21 13:49:28'),
                                                                                                                             (7, 39, 0, 1, 10, '2026-03-22 10:27:34'),
                                                                                                                             (8, 42, 0, 3, 10, '2026-03-23 05:48:56'),
                                                                                                                             (9, 42, 0, 3, 10, '2026-03-25 13:24:23');

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

--
-- Déchargement des données de la table `libelle`
--

INSERT INTO `libelle` (`id_libelle`, `libelle`) VALUES
                                                    (1, 'Habits femmes'),
                                                    (2, 'Habits hommes'),
                                                    (3, 'Habits enfants'),
                                                    (4, 'Mobilier'),
                                                    (5, 'Automobile'),
                                                    (6, 'Motos'),
                                                    (7, 'Tv / vidéo'),
                                                    (9, 'Livres'),
                                                    (11, 'Consoles de jeux'),
                                                    (12, 'Diététique'),
                                                    (13, 'Parfums'),
                                                    (14, 'Accessoires de cuisines'),
                                                    (16, 'Cosmétiques'),
                                                    (17, 'Matériels dj'),
                                                    (18, 'Collections'),
                                                    (19, 'Jouets'),
                                                    (20, 'Accessoires et outils'),
                                                    (21, 'Lingerie femme'),
                                                    (22, 'Calendrier'),
                                                    (23, 'Univers de bébé'),
                                                    (24, 'Electroménager'),
                                                    (25, 'Immobilier'),
                                                    (26, 'Jeans'),
                                                    (27, 'TShirts'),
                                                    (28, 'Jupes'),
                                                    (29, 'Robes'),
                                                    (30, 'Sacs'),
                                                    (31, 'Chaussures'),
                                                    (32, 'Chaussettes'),
                                                    (33, 'Lunettes de soleil'),
                                                    (34, 'Pulls'),
                                                    (35, 'De 0 à 3 mois'),
                                                    (36, 'De 4 à 6 mois'),
                                                    (37, 'De 7 à 12 mois'),
                                                    (38, 'Chaussures'),
                                                    (39, 'Chaussons'),
                                                    (40, 'De 0 à 3 mois'),
                                                    (41, 'Peintures'),
                                                    (42, 'Photographies'),
                                                    (43, 'Sculptures'),
                                                    (44, 'Divers'),
                                                    (47, 'Lits'),
                                                    (48, 'Canapés'),
                                                    (49, 'Armoires'),
                                                    (50, 'Meubles Tv'),
                                                    (51, 'Fauteils'),
                                                    (52, 'Berline'),
                                                    (53, 'Coupé'),
                                                    (54, '4x4'),
                                                    (55, 'Sport'),
                                                    (56, 'Hybride'),
                                                    (57, 'Eléctrique'),
                                                    (58, '50 cm3'),
                                                    (59, '125 cm3'),
                                                    (60, '250 cm3'),
                                                    (61, '500 cm3'),
                                                    (62, '750 cm3'),
                                                    (63, '1000 cm3'),
                                                    (64, 'Casques'),
                                                    (65, 'Gants'),
                                                    (66, 'Accessoires'),
                                                    (67, 'Vestes'),
                                                    (68, 'Pantalons'),
                                                    (70, 'Lecteurs dvd'),
                                                    (71, 'Chaines hifi'),
                                                    (72, 'Ecrans plats'),
                                                    (73, 'Lecteurs mp3'),
                                                    (74, 'Casques audio'),
                                                    (77, 'Pc'),
                                                    (78, 'Ordinateurs portable'),
                                                    (79, 'Mac'),
                                                    (80, 'Tablettes'),
                                                    (81, 'Souris'),
                                                    (82, 'Télécommandes'),
                                                    (83, 'Logiciels'),
                                                    (84, 'Natels'),
                                                    (85, 'Webcams'),
                                                    (86, 'Câblages'),
                                                    (87, 'Cartes mères'),
                                                    (88, 'Scanners'),
                                                    (89, 'Photocopieurs'),
                                                    (90, 'Routeurs'),
                                                    (91, 'Antennes satellite'),
                                                    (92, 'Lecteurs biométrique'),
                                                    (93, 'Lecteurs de cartes à puce'),
                                                    (94, 'Caisses enregistreuse'),
                                                    (97, 'Bagues'),
                                                    (98, 'Briquets'),
                                                    (99, 'Chainette'),
                                                    (100, 'Parure'),
                                                    (101, 'Techno'),
                                                    (102, 'Hardstyle'),
                                                    (103, 'Hardcore'),
                                                    (104, 'Trance'),
                                                    (105, 'Hard Trance'),
                                                    (106, 'House'),
                                                    (107, 'Teck House'),
                                                    (108, 'Goa'),
                                                    (109, 'Rap US'),
                                                    (110, 'R and b'),
                                                    (111, 'Reggea'),
                                                    (112, 'Dance'),
                                                    (113, 'Hits'),
                                                    (114, 'Rock'),
                                                    (115, 'HardRock'),
                                                    (116, 'Pop'),
                                                    (117, 'Folk'),
                                                    (118, 'Snowboard fille'),
                                                    (119, 'Snowboard garçon'),
                                                    (120, 'Fixation snowboard fille'),
                                                    (121, 'Fixation snowboard garçon'),
                                                    (122, 'Skate'),
                                                    (129, 'Trucks de skate'),
                                                    (130, 'Vis'),
                                                    (131, 'Roulements à billes'),
                                                    (132, 'Maillots de hockey'),
                                                    (133, 'Puck'),
                                                    (135, 'Chaussures de courses fille'),
                                                    (136, 'Chaussures de courses garçon'),
                                                    (137, 'Chaussures de trekking fille'),
                                                    (138, 'Chaussures de trekking garçon'),
                                                    (139, 'Chaussures de danse fille'),
                                                    (140, 'Chaussures de danse garçon'),
                                                    (141, 'Chaussures de basket fille'),
                                                    (142, 'Chaussures de basket garçon'),
                                                    (143, 'Chaussures de football garçon'),
                                                    (144, 'Chaussures de football fille'),
                                                    (145, 'Chaussures de rugby fille'),
                                                    (146, 'Chaussures de rugby garçon'),
                                                    (147, 'Chaussures de surf fille'),
                                                    (148, 'Chaussures de surf garçon'),
                                                    (149, 'Chaussures de snowboard fille'),
                                                    (150, 'Chaussures de snowboard garçon'),
                                                    (151, 'Chaussures de training fille'),
                                                    (152, 'Chaussures de training garçon'),
                                                    (153, 'Habits de snowboard fille'),
                                                    (154, 'Habits de snowboard garçon'),
                                                    (155, 'Habits de ski fille'),
                                                    (156, 'Habits de ski garçon'),
                                                    (157, 'Casques de ski fille'),
                                                    (158, 'Casques de ski garçon'),
                                                    (159, 'Lunettes de ski fille'),
                                                    (160, 'Lunettes de ski garçon'),
                                                    (161, 'Caméra de sport'),
                                                    (162, 'Ballon de basket'),
                                                    (163, 'Ballon de football'),
                                                    (164, 'Ballon de rugby'),
                                                    (165, 'Racket de tennis fille'),
                                                    (166, 'Racket de tennis garçon'),
                                                    (167, 'Sacoche de tennis fille'),
                                                    (168, 'Sacoche de tennis garçon'),
                                                    (169, 'Selles pour chevaux fille'),
                                                    (170, 'Selles pour chevaux garçon'),
                                                    (171, 'Bottes de cheval fille'),
                                                    (172, 'Bottes de cheval garçon'),
                                                    (173, 'Vêtements d\'équitation fille'),
(174, 'Vêtements d\'équitation garçon'),
                                                    (175, 'Action'),
                                                    (176, 'Suspense'),
                                                    (177, 'Policier'),
                                                    (178, 'Humour'),
                                                    (179, 'Tragédie'),
                                                    (180, 'Thriller'),
                                                    (181, 'Horreur'),
                                                    (182, 'Animations'),
                                                    (183, 'Séries'),
                                                    (185, 'Documentaires'),
                                                    (186, 'Dessins animés'),
                                                    (187, 'Porno'),
                                                    (188, 'Informatique'),
                                                    (189, 'Médecine'),
                                                    (190, 'Droit'),
                                                    (191, 'Mathématique'),
                                                    (192, 'Biologie'),
                                                    (193, 'Physique'),
                                                    (194, 'Langue'),
                                                    (195, 'Biologie'),
                                                    (196, 'Psychologie'),
                                                    (197, 'Psychiatrie'),
                                                    (198, 'Science cognitive'),
                                                    (199, 'Architecture'),
                                                    (200, 'Design'),
                                                    (201, 'Art'),
                                                    (202, 'Santé'),
                                                    (203, 'Nutrionnel'),
                                                    (204, 'Criminologie'),
                                                    (205, 'Science politique'),
                                                    (206, 'Urbanisme'),
                                                    (207, 'Enfants'),
                                                    (208, 'BD'),
                                                    (209, 'Mécanique'),
                                                    (210, 'Essais'),
                                                    (211, 'Essais'),
                                                    (212, 'Littérature (langues)'),
                                                    (213, 'Romans'),
                                                    (214, 'Génétique'),
                                                    (215, 'Guerre'),
                                                    (216, 'Ludique'),
                                                    (217, 'Shoot\'em up'),
(218, 'Plantes médicinales'),
(219, 'Parfums femme'),
(220, 'Parfums homme'),
(221, 'Parfums homme'),
(222, 'Piano'),
(223, 'Trompette'),
(224, 'Bariton'),
(225, 'Violoncelle'),
(226, 'Batterie'),
(227, 'Clarinette'),
(228, 'Accordéon'),
(229, 'Synthétiseur'),
(230, 'Saxophone'),
(231, 'Basse'),
(232, 'Clavecin'),
(233, 'Cornemuse'),
(234, 'Flute'),
(235, 'Harpe'),
(236, 'Contrebasse'),
(237, 'Tuba'),
(238, 'Alto'),
(239, 'Basson'),
(240, 'Cor'),
(241, 'Hautbois'),
(242, 'Violon'),
(243, 'Orgues'),
(244, 'Tables de mix'),
(245, 'Mixers'),
(246, 'Dj Cd mp3 players'),
(247, 'Dj Controllers'),
(248, 'Phono cartdridges'),
(249, 'Dj fx\'s'),
                                                    (250, 'Dj Software'),
                                                    (251, 'Dj fx\'s'),
(252, 'Casques'),
(253, 'Flycase'),
(254, 'Accessoires de dj'),
(255, 'Dj fx\'s'),
                                                    (256, 'Démaquillants et nettoyants'),
                                                    (257, 'Hydratation Multi-Climats'),
                                                    (258, 'Eclat Mat'),
                                                    (259, 'Douceur'),
                                                    (260, 'Eclat du jour'),
                                                    (261, 'Eclaircissant'),
                                                    (262, 'Aromaphytosoin'),
                                                    (263, 'Multi-Actif'),
                                                    (264, 'Multi-Regénérant'),
                                                    (265, 'Multi-Intensif'),
                                                    (266, 'Capital Lumière'),
                                                    (267, 'Sérums'),
                                                    (268, 'Exfoliants et Masques'),
                                                    (269, 'Yeux, Lèvres et Cou'),
                                                    (270, 'Essentiels'),
                                                    (271, 'AromaPhytoSoins \"Bien-Etre\"'),
                                                    (290, 'Aviation / Aéronautique'),
                                                    (291, 'Train / Chemin de fer'),
                                                    (292, 'Bistrot'),
                                                    (293, 'Briquet / Allumettes'),
                                                    (294, 'Calendrier femmes'),
                                                    (295, 'Capsules'),
                                                    (296, 'Bouchons'),
                                                    (314, 'Circuits'),
                                                    (315, 'Figurines, Statues'),
                                                    (316, 'Jeux de construction, Lego'),
                                                    (317, 'Jeux de plein air'),
                                                    (318, 'Jouets, Jeux anciens'),
                                                    (319, 'Jouets musicaux, Instruments'),
                                                    (320, 'Magie'),
                                                    (321, 'Maquettes'),
                                                    (322, 'Marionnettes'),
                                                    (323, 'Minis Univers'),
                                                    (324, 'Peluches, Doudous'),
                                                    (325, 'Peluches, Doudous'),
                                                    (326, 'Petits soldats'),
                                                    (327, 'Poker, Casino'),
                                                    (328, 'Puzzles'),
                                                    (329, 'Robots, Automates'),
                                                    (330, 'Star Wars'),
                                                    (331, 'Cartes à jouer'),
                                                    (332, 'Cartes à jouer'),
                                                    (333, 'Déguisements, Masques'),
                                                    (334, 'Jeux de rôle, de figurines'),
                                                    (335, 'Jeux de société'),
                                                    (336, 'Jeux éducatifs, Casse-tête'),
                                                    (337, 'Jeux éducatifs, Casse-tête'),
                                                    (338, 'Jeux électroniques'),
                                                    (339, 'Maquettes trains électriques'),
                                                    (340, 'Poupées'),
                                                    (341, 'Radiocommandés, Modélisme'),
                                                    (342, 'Véhicules miniatures'),
                                                    (343, 'Jeux de café'),
                                                    (345, 'Outils à main'),
                                                    (346, 'Outils de jardin'),
                                                    (347, 'Outils électriques'),
                                                    (348, 'Electronique, Composants'),
                                                    (349, 'Installation électrique'),
                                                    (350, 'Matériaux'),
                                                    (351, 'Peinture, Accessoires'),
                                                    (361, 'Cuisine: Arts de la table, Accessoires'),
                                                    (362, 'Cuisine: Casserolerie, Plats'),
                                                    (363, 'Cuisine: Meubles de cuisine'),
                                                    (364, 'Cuisine: Ustensiles'),
                                                    (365, 'Entretien, Nettoyage'),
                                                    (366, 'Linge de maison, Rideaux'),
                                                    (367, 'Luminaires'),
                                                    (368, 'Meubles'),
                                                    (369, 'Salle de bains: Accessoires'),
                                                    (370, 'Salle de bains: Meubles'),
                                                    (371, 'Cheminées, AccessoiresSalle de bains: Meubles'),
                                                    (372, 'Cuisine: Boîtes hermétiques'),
                                                    (373, 'Décoration'),
                                                    (374, 'Décoration'),
                                                    (375, 'Electroménager'),
                                                    (376, 'Jardin, Extérieur'),
                                                    (378, 'Fêtes, Occasions spéciales'),
                                                    (379, 'Sécurité, Domotique'),
                                                    (380, 'Cuisine: Ustensiles café, Thé'),
                                                    (381, 'A l\'huile'),
(382, 'A l\'eau'),
                                                    (383, 'Mate'),
                                                    (384, 'Satinée'),
                                                    (385, 'Brillante'),
                                                    (386, 'Glycéro'),
                                                    (387, 'Acrylique'),
                                                    (388, 'Lavable'),
                                                    (389, 'Lessivable'),
                                                    (390, 'Lasure'),
                                                    (391, 'Spécifique'),
                                                    (392, 'Bougies, Bougeoirs'),
                                                    (393, 'Bougies, Bougeoirs'),
                                                    (394, 'Cadres'),
                                                    (395, 'Coussins, Galettes de sièges'),
                                                    (396, 'Décorations enfants'),
                                                    (397, 'Décorations murales, Stickers'),
                                                    (398, 'Horloges, Pendules'),
                                                    (399, 'Miroirs'),
                                                    (400, 'Objets ethniques'),
                                                    (401, 'Parfums d\'intérieur'),
(403, 'Sculptures, Statues'),
(404, 'Tapis'),
(405, 'Autres objets de décoration'),
(406, 'Arrosage, Fontaines'),
(407, 'Barbecues'),
(408, 'Clôtures, Portails'),
(409, 'Décorations de jardin'),
(410, 'Eclairage, Lampes'),
(411, 'Energie renouvelable'),
(412, 'Jeux de plein air'),
(413, 'Meubles de jardin, Parasols'),
(414, 'Piscines, Accessoires'),
(415, 'Plantes, Graines, Bulbes'),
(416, 'Saunas, Bains hydromassants'),
(417, 'Serres, Accessoires'),
(418, 'Ensembles'),
(419, 'Soutiens gorges'),
(420, 'Collants, Bas'),
(421, 'Maillots de Bain 2 Pièces'),
(422, 'Maillots de Bain 1 Pièce'),
(423, 'Strings'),
(426, 'Calendrier sexy'),
(427, 'Calendrier bienfaisance'),
(428, 'Calendrier bienfaisance'),
(429, 'Porte-bébé'),
(430, 'Poussettes, Systèmes combinés'),
(431, 'Sacs à langer'),
(432, 'Sièges-auto, Vélo'),
(433, 'Jouets de 0 à 6 mois fille et garçon'),
(434, 'Jouets de 6 à 12 mois fille et garçon'),
(435, 'Jouets de 12 à 18 mois fille et garçon'),
(436, 'Jouets de 18 à 24 mois fille et garçon'),
(437, 'Chambres complètes'),
(438, 'Décorations, Veilleuses'),
(439, 'Gigoteuses, Nids d\'Anges'),
                                                    (440, 'Literie'),
                                                    (441, 'Meubles'),
                                                    (442, 'Meubles à langer'),
                                                    (443, 'Parcs'),
                                                    (444, 'Transats, Balancelles'),
                                                    (445, 'Biberons'),
                                                    (446, 'Bavoirs'),
                                                    (447, 'Sucettes'),
                                                    (448, 'Stérilisateurs'),
                                                    (449, 'Stérilisateurs'),
                                                    (450, 'Interphones'),
                                                    (451, 'Barrières de Sécurité'),
                                                    (452, 'Interphones avec Caméra'),
                                                    (453, 'Détecteurs de Température'),
                                                    (454, 'Couches'),
                                                    (455, 'Produits de Toilette'),
                                                    (456, 'Capes de Bain'),
                                                    (457, 'Thermomètres de Bain'),
                                                    (458, 'Vêtements de bébés'),
                                                    (459, 'Lave-vaisselle'),
                                                    (460, 'Lave-linge'),
                                                    (461, 'Frigo'),
                                                    (462, 'Aspirateur'),
                                                    (463, 'Four'),
                                                    (464, 'Four encastrable'),
                                                    (465, 'Thermo mix'),
                                                    (466, 'Machine à laver'),
                                                    (467, 'Micro-onde'),
                                                    (468, 'Sèche-linge'),
                                                    (469, 'Appartement'),
                                                    (470, 'Villa'),
                                                    (471, 'Loft'),
                                                    (473, 'Parcelle de terrain'),
                                                    (475, 'Châlet'),
                                                    (550, 'Trucks de skate'),
                                                    (551, 'Vis'),
                                                    (553, 'Roulements à billes'),
                                                    (554, 'Maillots de hockey'),
                                                    (555, 'Puck'),
                                                    (556, 'Puck'),
                                                    (600, 'Ski fille'),
                                                    (601, 'Ski garçon'),
                                                    (602, 'Fixation de ski fille'),
                                                    (603, 'Fixation de ski garçon'),
                                                    (604, 'Chaussures de ski fille'),
                                                    (605, 'Chaussures de ski garçon'),
                                                    (771, 'Embellir sa Peau'),
                                                    (772, 'Remodeler son Corps'),
                                                    (773, 'Grossesse'),
                                                    (774, 'Eaux De Soins'),
                                                    (775, 'Minceur et fermeté'),
                                                    (776, 'Protecteurs'),
                                                    (777, 'Après-Soleil'),
                                                    (778, 'Autobronzants'),
                                                    (779, 'Neo Pastels'),
                                                    (780, 'Teint'),
                                                    (781, 'Yeux'),
                                                    (782, 'Lèvres et Ongles'),
                                                    (783, 'Eclat Minute: Les Produits Malins'),
                                                    (784, 'Corps'),
                                                    (785, 'Nettoyage'),
                                                    (786, 'Rasage'),
                                                    (787, 'Hydratation'),
                                                    (788, 'Anti-Age'),
                                                    (789, 'S.O.S Express'),
                                                    (1052, 'Scooter des mers'),
                                                    (1094, 'Calendrier hommes'),
                                                    (1197, 'Cartes / Guides / Plans'),
                                                    (1198, 'Cartes postales'),
                                                    (1300, 'Couteaux de poche'),
                                                    (1301, 'Couture / Tricot'),
                                                    (1302, 'Diddl'),
                                                    (1303, 'Ecriture / Dessins'),
                                                    (1304, 'Fèves'),
                                                    (1305, 'Gramographe / Phonographe'),
                                                    (1306, 'Images / Statue animale'),
                                                    (1307, 'Incroyable voir étrange'),
                                                    (1308, 'Kinder'),
                                                    (1309, 'Lanterne / Lampe de poche'),
                                                    (1310, 'Lettre / Vieux papier'),
                                                    (1311, 'Militaire'),
                                                    (1312, 'Lettre / Vieux papier'),
                                                    (1313, 'Cartes de collection'),
                                                    (1372, 'Remodeler son Corps'),
                                                    (1373, 'Grossesse'),
                                                    (1374, 'Eaux De Soins'),
                                                    (1375, 'Minceur et fermeté'),
                                                    (1376, 'Protecteurs'),
                                                    (1398, 'Coquillage / Fossiles minéraux'),
                                                    (1399, 'Costumes / Vêtements d\'époque'),
(2351, 'Plomberie, Sanitaires'),
(6351, 'Plomberie, Sanitaires'),
(6352, 'Revêtements de sols '),
(6353, 'Revêtements muraux'),
(6354, 'Toiture, Isolation'),
(6355, 'Travaux du bâtiment'),
(6356, 'Vêtements de travail'),
(6357, 'Accessoires Animaux'),
(6358, 'Bricolage'),
(6359, 'Bricolage: Outils'),
(6360, 'Quincaillerie, Ferronnerie'),
(6380, 'Chauffage, Climatisation'),
(8423, 'Culottes'),
(8424, 'Caleçons'),
(8425, 'Slips'),
(9000, 'X Box'),
(9001, 'Playstation'),
(9002, 'DES'),
(9003, 'Wii'),
(9004, 'Action'),
(9005, 'Action'),
(9006, 'Sport'),
(10000, 'Vins'),
(10001, 'Vin rouge'),
(10002, 'Vin blanc'),
(10003, 'Vin rosé'),
(10004, 'Champagne'),
(10005, 'Payement comptant'),
(10006, 'Paypall'),
(10007, 'Lettres Courrier A'),
(10008, 'Lettres Courrier B'),
(10009, 'Colis Courrier A'),
(10010, 'Colis Courrier B'),
(10011, 'Recommander'),
(10012, 'Remboursement'),
(10013, 'Service d expédition ou courrier'),
(10014, 'A retirer chez le vendeur'),
(10015, 'Livraison par le fournisseur'),
(10016, 'Acheteur'),
(10017, 'Vendeur_(non-professionel)'),
(10018, 'Garage'),
(10019, 'Vendeur_professionel'),
(10020, 'Lingerie homme'),
(10021, 'Astrologie'),
(10022, 'Cartes / Tarôts'),
(10023, 'Livres'),
(10024, 'Animal'),
(10025, 'Chien'),
(10026, 'Chat'),
(10027, 'Cheval'),
(10028, 'Serpent'),
(10029, 'Montres et bijoux femme'),
(10030, 'Montres et bijoux homme'),
(10031, 'Habitat et jardin'),
(10032, 'Cd / Vinyl / Mp3'),
(10033, 'Location'),
(10034, 'Achat'),
(10035, 'Utilisé'),
(10036, 'Neuf'),
(10037, 'Cd'),
(10038, 'Vinyl'),
(10039, 'Mp3'),
(10040, 'Dvd'),
(10041, 'Essence'),
(10042, 'Diesel'),
(10043, 'Eléctrique'),
(10044, 'Hybride'),
(10045, 'Gaz'),
(10046, 'Manuelle'),
(10047, 'Automatique'),
(10049, 'Instruments de musique'),
(10050, 'Yacht'),
(10051, 'Voilier'),
(10053, 'Offshore'),
(10054, 'Bâteau'),
(10055, 'Jeux vidéos'),
(10056, 'Hockey'),
(10057, 'Snowboard'),
(10058, 'Ski'),
(10059, 'Equitation'),
(10060, 'Lcd'),
(10061, 'Plasma'),
(10100, 'Paiement comptant'),
(10101, 'PayPal'),
(10102, 'Virement bancaire'),
(10103, 'Carte de crédit'),
(10200, 'Lettres Courrier A'),
(10201, 'Colis Courrier B'),
(10202, 'À retirer chez le vendeur'),
(10203, 'Livraison à domicile'),
(11000, 'Informatique'),
(12000, 'Tv / Vidéo'),
(12001, 'Air filtration et stérilisation'),
(12002, '34'),
(12003, '35'),
(22222, 'Art et design'),
(99999, 'Fendant'),
(100000, 'Janvier'),
(100001, 'Février'),
(100002, 'Mars'),
(100003, 'Avril'),
(100004, 'Mai'),
(100005, 'Juin'),
(100006, 'Juillet'),
(100007, 'Août'),
(100008, 'Septembre'),
(100009, 'Octobre'),
(1000010, 'Novembre'),
(1000122, 'Décembre'),
(1000123, 'Twint'),
(1000124, 'Canne de Hockey proessionel\r\n'),
(1000125, 'Montres'),
(1000126, 'Boucles d\'oreille');

-- --------------------------------------------------------

--
-- Structure de la table `location_ou_achat_libelle_langue`
--

CREATE TABLE `location_ou_achat_libelle_langue` (
                                                    `ref_location_ou_achat` int(11) NOT NULL,
                                                    `ref_libelle` int(11) NOT NULL,
                                                    `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `location_ou_achat_libelle_langue`
--

INSERT INTO `location_ou_achat_libelle_langue` (`ref_location_ou_achat`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                          (1, 10033, 1),
                                                                                                          (2, 10034, 1);

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

--
-- Déchargement des données de la table `met_en_vente`
--

INSERT INTO `met_en_vente` (`ref_vendeur`, `ref_article`, `date_stock`, `page_principale`, `page_categorie`, `pack_photo`, `notre_selection`, `statut`) VALUES
                                                                                                                                                            (1, 32, '2026-03-11', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 33, '2026-03-12', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 34, '2026-03-12', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 35, '2026-03-12', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 36, '2026-03-13', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 37, '2026-03-17', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 39, '2026-03-20', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 40, '2026-03-21', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 41, '2026-03-21', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 42, '2026-03-22', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 43, '2026-03-22', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 46, '2026-03-31', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (1, 47, '2026-09-15', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (3, 38, '2026-03-17', NULL, NULL, NULL, 0, NULL),
                                                                                                                                                            (3, 44, '2026-03-26', NULL, NULL, NULL, 0, NULL);

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

--
-- Déchargement des données de la table `mois`
--

INSERT INTO `mois` (`id`) VALUES
                              (1),
                              (2),
                              (3),
                              (4),
                              (5),
                              (6),
                              (7),
                              (8),
                              (9),
                              (10),
                              (11),
                              (12);

-- --------------------------------------------------------

--
-- Structure de la table `mois_libelle_langue`
--

CREATE TABLE `mois_libelle_langue` (
                                       `ref_mois` int(11) NOT NULL,
                                       `ref_libelle` int(11) NOT NULL,
                                       `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `mois_libelle_langue`
--

INSERT INTO `mois_libelle_langue` (`ref_mois`, `ref_libelle`, `ref_langue`) VALUES
                                                                                (1, 100000, 1),
                                                                                (2, 100001, 1),
                                                                                (3, 100002, 1),
                                                                                (4, 100003, 1),
                                                                                (5, 100004, 1),
                                                                                (6, 100005, 1),
                                                                                (7, 100006, 1),
                                                                                (8, 100007, 1),
                                                                                (9, 100008, 1),
                                                                                (10, 100009, 1),
                                                                                (11, 1000010, 1),
                                                                                (12, 1000122, 1);

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
                                                          (2, 'France');
;

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
                                                                                                                                                                                                                                                                                                                            (1, 'alexjaquet', '', 'Alexandre Jaquet', '', 'test', 1212, 'Belfaux', 'Suisse', 'alexjaquet@gmail.com', '+41 78 000 00 00', 1, 1, '', NULL, 'CHF 0000-000', NULL, 'CHF 0000 0000 000', NULL, NULL, NULL, NULL, 7, NULL),
                                                                                                                                                                                                                                                                                                                            (2, 'testtest', '', 'Test', 'Test', '', 0, '', '', 'alexandre.jaquet1980@outlook.com', '', 1, 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                                                                                                                                                                                            (3, 'dev.jaquet.alexandre', '', 'Alexandre Jaquet 2', '', '', 0, '', '', 'dev.jaquet.alexandre@gmail.com', '+41 78 000 00 00', 1, 1, '', NULL, 'CHF 0000-000', NULL, 'CHF 0000 0000 000', NULL, NULL, NULL, NULL, NULL, NULL);

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
-- Structure de la table `subcategorie_libelle_langue`
--

CREATE TABLE `subcategorie_libelle_langue` (
                                               `ref_subcategorie` int(11) NOT NULL,
                                               `ref_categorie` int(11) NOT NULL,
                                               `ref_libelle` int(11) NOT NULL,
                                               `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `subcategorie_libelle_langue`
--

INSERT INTO `subcategorie_libelle_langue` (`ref_subcategorie`, `ref_categorie`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                                 (130, 12, 9, 1),
                                                                                                                 (1, 1, 26, 1),
                                                                                                                 (2, 2, 26, 1),
                                                                                                                 (26, 3, 26, 1),
                                                                                                                 (23, 2, 27, 1),
                                                                                                                 (24, 1, 27, 1),
                                                                                                                 (25, 3, 27, 1),
                                                                                                                 (7, 1, 28, 1),
                                                                                                                 (21, 3, 28, 1),
                                                                                                                 (20, 3, 29, 1),
                                                                                                                 (31, 1, 29, 1),
                                                                                                                 (10, 3, 30, 1),
                                                                                                                 (11, 2, 30, 1),
                                                                                                                 (30, 1, 30, 1),
                                                                                                                 (12, 3, 31, 1),
                                                                                                                 (13, 2, 31, 1),
                                                                                                                 (32, 1, 31, 1),
                                                                                                                 (14, 2, 32, 1),
                                                                                                                 (15, 3, 32, 1),
                                                                                                                 (33, 1, 32, 1),
                                                                                                                 (16, 2, 33, 1),
                                                                                                                 (17, 3, 33, 1),
                                                                                                                 (34, 1, 33, 1),
                                                                                                                 (18, 3, 34, 1),
                                                                                                                 (19, 2, 34, 1),
                                                                                                                 (35, 1, 34, 1),
                                                                                                                 (27, 3, 35, 1),
                                                                                                                 (28, 3, 36, 1),
                                                                                                                 (29, 3, 37, 1),
                                                                                                                 (3, 3, 39, 1),
                                                                                                                 (41, 22222, 41, 1),
                                                                                                                 (42, 22222, 42, 1),
                                                                                                                 (43, 22222, 43, 1),
                                                                                                                 (44, 22222, 44, 1),
                                                                                                                 (47, 22222, 47, 1),
                                                                                                                 (48, 22222, 48, 1),
                                                                                                                 (49, 22222, 49, 1),
                                                                                                                 (50, 22222, 50, 1),
                                                                                                                 (51, 22222, 51, 1),
                                                                                                                 (51, 5, 52, 1),
                                                                                                                 (52, 5, 53, 1),
                                                                                                                 (53, 5, 54, 1),
                                                                                                                 (54, 5, 55, 1),
                                                                                                                 (55, 5, 56, 1),
                                                                                                                 (56, 5, 57, 1),
                                                                                                                 (57, 6, 58, 1),
                                                                                                                 (58, 6, 59, 1),
                                                                                                                 (59, 6, 60, 1),
                                                                                                                 (60, 6, 61, 1),
                                                                                                                 (61, 6, 62, 1),
                                                                                                                 (62, 6, 63, 1),
                                                                                                                 (63, 6, 64, 1),
                                                                                                                 (64, 6, 65, 1),
                                                                                                                 (65, 6, 66, 1),
                                                                                                                 (66, 6, 67, 1),
                                                                                                                 (67, 6, 68, 1),
                                                                                                                 (68, 7, 70, 1),
                                                                                                                 (69, 7, 71, 1),
                                                                                                                 (70, 7, 72, 1),
                                                                                                                 (71, 7, 73, 1),
                                                                                                                 (72, 7, 74, 1),
                                                                                                                 (77, 188, 77, 1),
                                                                                                                 (78, 188, 78, 1),
                                                                                                                 (79, 188, 79, 1),
                                                                                                                 (80, 188, 80, 1),
                                                                                                                 (81, 188, 81, 1),
                                                                                                                 (82, 188, 82, 1),
                                                                                                                 (83, 188, 83, 1),
                                                                                                                 (84, 188, 84, 1),
                                                                                                                 (85, 188, 85, 1),
                                                                                                                 (86, 188, 86, 1),
                                                                                                                 (87, 188, 87, 1),
                                                                                                                 (88, 188, 88, 1),
                                                                                                                 (89, 188, 89, 1),
                                                                                                                 (90, 188, 90, 1),
                                                                                                                 (91, 188, 91, 1),
                                                                                                                 (92, 188, 92, 1),
                                                                                                                 (93, 188, 93, 1),
                                                                                                                 (94, 188, 94, 1),
                                                                                                                 (377, 35, 97, 1),
                                                                                                                 (371, 34, 98, 1),
                                                                                                                 (378, 35, 98, 1),
                                                                                                                 (372, 34, 99, 1),
                                                                                                                 (379, 35, 99, 1),
                                                                                                                 (373, 34, 100, 1),
                                                                                                                 (310, 26, 118, 1),
                                                                                                                 (553, 98, 118, 1),
                                                                                                                 (311, 26, 119, 1),
                                                                                                                 (552, 98, 119, 1),
                                                                                                                 (312, 26, 120, 1),
                                                                                                                 (555, 98, 120, 1),
                                                                                                                 (313, 26, 121, 1),
                                                                                                                 (554, 98, 121, 1),
                                                                                                                 (314, 26, 122, 1),
                                                                                                                 (315, 26, 129, 1),
                                                                                                                 (316, 26, 131, 1),
                                                                                                                 (317, 26, 132, 1),
                                                                                                                 (318, 26, 133, 1),
                                                                                                                 (319, 26, 133, 1),
                                                                                                                 (320, 26, 135, 1),
                                                                                                                 (321, 26, 136, 1),
                                                                                                                 (322, 26, 138, 1),
                                                                                                                 (324, 26, 140, 1),
                                                                                                                 (325, 26, 141, 1),
                                                                                                                 (326, 26, 142, 1),
                                                                                                                 (327, 26, 143, 1),
                                                                                                                 (328, 26, 144, 1),
                                                                                                                 (329, 26, 145, 1),
                                                                                                                 (330, 26, 146, 1),
                                                                                                                 (331, 26, 147, 1),
                                                                                                                 (332, 26, 148, 1),
                                                                                                                 (333, 26, 149, 1),
                                                                                                                 (557, 98, 149, 1),
                                                                                                                 (334, 26, 150, 1),
                                                                                                                 (556, 98, 150, 1),
                                                                                                                 (335, 26, 151, 1),
                                                                                                                 (336, 26, 152, 1),
                                                                                                                 (337, 26, 153, 1),
                                                                                                                 (559, 98, 153, 1),
                                                                                                                 (338, 26, 154, 1),
                                                                                                                 (558, 98, 154, 1),
                                                                                                                 (339, 26, 155, 1),
                                                                                                                 (572, 97, 155, 1),
                                                                                                                 (340, 26, 156, 1),
                                                                                                                 (571, 97, 156, 1),
                                                                                                                 (341, 26, 157, 1),
                                                                                                                 (570, 97, 157, 1),
                                                                                                                 (342, 26, 158, 1),
                                                                                                                 (569, 97, 158, 1),
                                                                                                                 (343, 26, 159, 1),
                                                                                                                 (568, 97, 159, 1),
                                                                                                                 (344, 26, 160, 1),
                                                                                                                 (566, 97, 160, 1),
                                                                                                                 (345, 26, 161, 1),
                                                                                                                 (567, 97, 161, 1),
                                                                                                                 (346, 26, 162, 1),
                                                                                                                 (347, 26, 163, 1),
                                                                                                                 (348, 26, 164, 1),
                                                                                                                 (349, 26, 165, 1),
                                                                                                                 (350, 26, 166, 1),
                                                                                                                 (351, 26, 167, 1),
                                                                                                                 (352, 26, 168, 1),
                                                                                                                 (353, 26, 169, 1),
                                                                                                                 (578, 96, 169, 1),
                                                                                                                 (354, 26, 170, 1),
                                                                                                                 (577, 96, 170, 1),
                                                                                                                 (355, 26, 171, 1),
                                                                                                                 (576, 96, 171, 1),
                                                                                                                 (356, 26, 172, 1),
                                                                                                                 (575, 96, 172, 1),
                                                                                                                 (357, 26, 173, 1),
                                                                                                                 (574, 96, 173, 1),
                                                                                                                 (358, 26, 174, 1),
                                                                                                                 (573, 96, 174, 1),
                                                                                                                 (537, 38, 175, 1),
                                                                                                                 (538, 38, 176, 1),
                                                                                                                 (539, 38, 177, 1),
                                                                                                                 (540, 38, 178, 1),
                                                                                                                 (541, 38, 179, 1),
                                                                                                                 (542, 38, 180, 1),
                                                                                                                 (543, 38, 181, 1),
                                                                                                                 (544, 38, 182, 1),
                                                                                                                 (545, 38, 183, 1),
                                                                                                                 (547, 38, 185, 1),
                                                                                                                 (548, 38, 186, 1),
                                                                                                                 (549, 38, 187, 1),
                                                                                                                 (93, 9, 188, 1),
                                                                                                                 (94, 9, 189, 1),
                                                                                                                 (95, 9, 190, 1),
                                                                                                                 (96, 9, 191, 1),
                                                                                                                 (97, 9, 192, 1),
                                                                                                                 (98, 9, 193, 1),
                                                                                                                 (99, 9, 194, 1),
                                                                                                                 (100, 9, 195, 1),
                                                                                                                 (101, 9, 196, 1),
                                                                                                                 (102, 9, 197, 1),
                                                                                                                 (103, 9, 198, 1),
                                                                                                                 (104, 9, 199, 1),
                                                                                                                 (105, 9, 201, 1),
                                                                                                                 (106, 9, 202, 1),
                                                                                                                 (107, 9, 203, 1),
                                                                                                                 (108, 9, 204, 1),
                                                                                                                 (109, 9, 205, 1),
                                                                                                                 (110, 9, 206, 1),
                                                                                                                 (111, 9, 207, 1),
                                                                                                                 (112, 9, 208, 1),
                                                                                                                 (113, 9, 208, 1),
                                                                                                                 (114, 9, 209, 1),
                                                                                                                 (116, 9, 211, 1),
                                                                                                                 (117, 9, 212, 1),
                                                                                                                 (118, 9, 213, 1),
                                                                                                                 (119, 9, 214, 1),
                                                                                                                 (126, 95, 215, 1),
                                                                                                                 (127, 95, 216, 1),
                                                                                                                 (128, 95, 217, 1),
                                                                                                                 (129, 12, 218, 1),
                                                                                                                 (131, 13, 219, 1),
                                                                                                                 (132, 13, 220, 1),
                                                                                                                 (138, 15, 222, 1),
                                                                                                                 (443, 92, 222, 1),
                                                                                                                 (139, 15, 223, 1),
                                                                                                                 (444, 92, 223, 1),
                                                                                                                 (140, 15, 224, 1),
                                                                                                                 (445, 92, 224, 1),
                                                                                                                 (141, 15, 225, 1),
                                                                                                                 (143, 15, 225, 1),
                                                                                                                 (446, 92, 225, 1),
                                                                                                                 (142, 15, 226, 1),
                                                                                                                 (144, 15, 226, 1),
                                                                                                                 (145, 15, 226, 1),
                                                                                                                 (447, 92, 226, 1),
                                                                                                                 (146, 15, 227, 1),
                                                                                                                 (147, 15, 227, 1),
                                                                                                                 (151, 15, 227, 1),
                                                                                                                 (448, 92, 227, 1),
                                                                                                                 (148, 15, 228, 1),
                                                                                                                 (152, 15, 228, 1),
                                                                                                                 (449, 92, 228, 1),
                                                                                                                 (149, 15, 229, 1),
                                                                                                                 (153, 15, 229, 1),
                                                                                                                 (450, 92, 229, 1),
                                                                                                                 (150, 15, 230, 1),
                                                                                                                 (154, 15, 230, 1),
                                                                                                                 (451, 92, 230, 1),
                                                                                                                 (155, 15, 231, 1),
                                                                                                                 (452, 92, 231, 1),
                                                                                                                 (156, 15, 232, 1),
                                                                                                                 (453, 92, 232, 1),
                                                                                                                 (157, 15, 233, 1),
                                                                                                                 (454, 92, 233, 1),
                                                                                                                 (158, 15, 234, 1),
                                                                                                                 (455, 92, 234, 1),
                                                                                                                 (159, 15, 235, 1),
                                                                                                                 (456, 92, 235, 1),
                                                                                                                 (160, 15, 236, 1),
                                                                                                                 (457, 92, 236, 1),
                                                                                                                 (161, 15, 237, 1),
                                                                                                                 (458, 92, 237, 1),
                                                                                                                 (162, 15, 238, 1),
                                                                                                                 (459, 92, 238, 1),
                                                                                                                 (163, 15, 239, 1),
                                                                                                                 (164, 15, 239, 1),
                                                                                                                 (460, 92, 239, 1),
                                                                                                                 (165, 15, 240, 1),
                                                                                                                 (461, 92, 240, 1),
                                                                                                                 (166, 15, 241, 1),
                                                                                                                 (462, 92, 241, 1),
                                                                                                                 (167, 15, 242, 1),
                                                                                                                 (463, 92, 242, 1),
                                                                                                                 (168, 15, 243, 1),
                                                                                                                 (464, 92, 243, 1),
                                                                                                                 (169, 15, 244, 1),
                                                                                                                 (465, 93, 244, 1),
                                                                                                                 (170, 15, 245, 1),
                                                                                                                 (466, 93, 245, 1),
                                                                                                                 (171, 15, 246, 1),
                                                                                                                 (467, 93, 246, 1),
                                                                                                                 (172, 15, 247, 1),
                                                                                                                 (468, 93, 247, 1),
                                                                                                                 (173, 15, 248, 1),
                                                                                                                 (469, 93, 248, 1),
                                                                                                                 (174, 15, 249, 1),
                                                                                                                 (470, 93, 249, 1),
                                                                                                                 (175, 15, 250, 1),
                                                                                                                 (471, 93, 250, 1),
                                                                                                                 (176, 15, 251, 1),
                                                                                                                 (472, 93, 251, 1),
                                                                                                                 (177, 15, 252, 1),
                                                                                                                 (473, 93, 252, 1),
                                                                                                                 (178, 15, 253, 1),
                                                                                                                 (474, 93, 253, 1),
                                                                                                                 (179, 15, 254, 1),
                                                                                                                 (475, 93, 254, 1),
                                                                                                                 (180, 15, 255, 1),
                                                                                                                 (476, 93, 255, 1),
                                                                                                                 (200, 18, 290, 1),
                                                                                                                 (477, 18, 290, 1),
                                                                                                                 (201, 18, 291, 1),
                                                                                                                 (478, 18, 291, 1),
                                                                                                                 (202, 18, 292, 1),
                                                                                                                 (479, 18, 292, 1),
                                                                                                                 (203, 18, 293, 1),
                                                                                                                 (480, 18, 293, 1),
                                                                                                                 (204, 18, 294, 1),
                                                                                                                 (481, 18, 294, 1),
                                                                                                                 (205, 18, 295, 1),
                                                                                                                 (482, 18, 295, 1),
                                                                                                                 (206, 18, 296, 1),
                                                                                                                 (483, 18, 296, 1),
                                                                                                                 (207, 18, 314, 1),
                                                                                                                 (501, 18, 314, 1),
                                                                                                                 (208, 18, 315, 1),
                                                                                                                 (502, 18, 315, 1),
                                                                                                                 (209, 18, 316, 1),
                                                                                                                 (503, 18, 316, 1),
                                                                                                                 (210, 18, 317, 1),
                                                                                                                 (504, 18, 317, 1),
                                                                                                                 (211, 18, 318, 1),
                                                                                                                 (213, 18, 318, 1),
                                                                                                                 (505, 18, 318, 1),
                                                                                                                 (212, 18, 319, 1),
                                                                                                                 (214, 18, 319, 1),
                                                                                                                 (506, 18, 319, 1),
                                                                                                                 (215, 18, 320, 1),
                                                                                                                 (507, 18, 320, 1),
                                                                                                                 (216, 18, 321, 1),
                                                                                                                 (508, 18, 321, 1),
                                                                                                                 (217, 18, 322, 1),
                                                                                                                 (509, 18, 322, 1),
                                                                                                                 (218, 18, 323, 1),
                                                                                                                 (510, 18, 323, 1),
                                                                                                                 (511, 18, 324, 1),
                                                                                                                 (220, 18, 325, 1),
                                                                                                                 (512, 18, 325, 1),
                                                                                                                 (221, 18, 326, 1),
                                                                                                                 (513, 18, 326, 1),
                                                                                                                 (222, 18, 327, 1),
                                                                                                                 (514, 18, 327, 1),
                                                                                                                 (223, 18, 328, 1),
                                                                                                                 (515, 18, 328, 1),
                                                                                                                 (224, 18, 329, 1),
                                                                                                                 (516, 18, 329, 1),
                                                                                                                 (225, 18, 330, 1),
                                                                                                                 (517, 18, 330, 1),
                                                                                                                 (226, 19, 331, 1),
                                                                                                                 (518, 18, 331, 1),
                                                                                                                 (227, 19, 332, 1),
                                                                                                                 (519, 18, 332, 1),
                                                                                                                 (228, 19, 333, 1),
                                                                                                                 (520, 18, 333, 1),
                                                                                                                 (229, 19, 334, 1),
                                                                                                                 (521, 18, 334, 1),
                                                                                                                 (230, 19, 335, 1),
                                                                                                                 (522, 18, 335, 1),
                                                                                                                 (231, 19, 336, 1),
                                                                                                                 (523, 18, 336, 1),
                                                                                                                 (232, 19, 337, 1),
                                                                                                                 (233, 19, 338, 1),
                                                                                                                 (524, 18, 338, 1),
                                                                                                                 (234, 19, 339, 1),
                                                                                                                 (525, 18, 339, 1),
                                                                                                                 (235, 19, 340, 1),
                                                                                                                 (526, 18, 340, 1),
                                                                                                                 (236, 19, 341, 1),
                                                                                                                 (527, 18, 341, 1),
                                                                                                                 (237, 19, 342, 1),
                                                                                                                 (528, 18, 342, 1),
                                                                                                                 (238, 19, 343, 1),
                                                                                                                 (529, 18, 343, 1),
                                                                                                                 (239, 20, 345, 1),
                                                                                                                 (380, 36, 345, 1),
                                                                                                                 (240, 20, 346, 1),
                                                                                                                 (381, 36, 346, 1),
                                                                                                                 (241, 20, 347, 1),
                                                                                                                 (382, 36, 347, 1),
                                                                                                                 (242, 20, 348, 1),
                                                                                                                 (383, 36, 348, 1),
                                                                                                                 (243, 20, 349, 1),
                                                                                                                 (384, 36, 349, 1),
                                                                                                                 (244, 20, 350, 1),
                                                                                                                 (385, 36, 350, 1),
                                                                                                                 (245, 20, 351, 1),
                                                                                                                 (386, 36, 351, 1),
                                                                                                                 (133, 14, 361, 1),
                                                                                                                 (387, 36, 361, 1),
                                                                                                                 (134, 14, 362, 1),
                                                                                                                 (388, 36, 362, 1),
                                                                                                                 (135, 14, 363, 1),
                                                                                                                 (136, 14, 364, 1),
                                                                                                                 (137, 14, 365, 1),
                                                                                                                 (256, 21, 418, 1),
                                                                                                                 (257, 21, 419, 1),
                                                                                                                 (258, 21, 420, 1),
                                                                                                                 (259, 21, 421, 1),
                                                                                                                 (260, 21, 422, 1),
                                                                                                                 (261, 21, 423, 1),
                                                                                                                 (262, 22, 426, 1),
                                                                                                                 (263, 22, 427, 1),
                                                                                                                 (262, 23, 429, 1),
                                                                                                                 (263, 23, 430, 1),
                                                                                                                 (264, 23, 431, 1),
                                                                                                                 (265, 23, 432, 1),
                                                                                                                 (266, 23, 433, 1),
                                                                                                                 (267, 23, 434, 1),
                                                                                                                 (268, 23, 435, 1),
                                                                                                                 (269, 23, 436, 1),
                                                                                                                 (270, 23, 437, 1),
                                                                                                                 (271, 23, 438, 1),
                                                                                                                 (272, 23, 439, 1),
                                                                                                                 (273, 23, 440, 1),
                                                                                                                 (274, 23, 441, 1),
                                                                                                                 (275, 23, 442, 1),
                                                                                                                 (276, 23, 443, 1),
                                                                                                                 (277, 23, 444, 1),
                                                                                                                 (278, 23, 445, 1),
                                                                                                                 (279, 23, 446, 1),
                                                                                                                 (280, 23, 447, 1),
                                                                                                                 (281, 23, 448, 1),
                                                                                                                 (282, 23, 449, 1),
                                                                                                                 (283, 23, 450, 1),
                                                                                                                 (284, 23, 451, 1),
                                                                                                                 (285, 23, 452, 1),
                                                                                                                 (286, 23, 453, 1),
                                                                                                                 (287, 23, 454, 1),
                                                                                                                 (289, 23, 454, 1),
                                                                                                                 (288, 23, 455, 1),
                                                                                                                 (290, 23, 455, 1),
                                                                                                                 (291, 23, 456, 1),
                                                                                                                 (292, 23, 458, 1),
                                                                                                                 (293, 24, 459, 1),
                                                                                                                 (294, 24, 460, 1),
                                                                                                                 (295, 24, 461, 1),
                                                                                                                 (296, 24, 462, 1),
                                                                                                                 (297, 24, 463, 1),
                                                                                                                 (298, 24, 464, 1),
                                                                                                                 (299, 24, 465, 1),
                                                                                                                 (300, 24, 466, 1),
                                                                                                                 (301, 24, 467, 1),
                                                                                                                 (302, 24, 468, 1),
                                                                                                                 (303, 25, 469, 1),
                                                                                                                 (304, 25, 470, 1),
                                                                                                                 (305, 25, 471, 1),
                                                                                                                 (307, 25, 473, 1),
                                                                                                                 (309, 25, 475, 1),
                                                                                                                 (550, 99, 554, 1),
                                                                                                                 (551, 99, 555, 1),
                                                                                                                 (565, 97, 600, 1),
                                                                                                                 (564, 97, 601, 1),
                                                                                                                 (563, 97, 602, 1),
                                                                                                                 (562, 97, 603, 1),
                                                                                                                 (561, 97, 604, 1),
                                                                                                                 (560, 97, 605, 1),
                                                                                                                 (181, 16, 771, 1),
                                                                                                                 (182, 16, 772, 1),
                                                                                                                 (183, 16, 773, 1),
                                                                                                                 (184, 16, 774, 1),
                                                                                                                 (185, 16, 775, 1),
                                                                                                                 (186, 16, 776, 1),
                                                                                                                 (187, 16, 777, 1),
                                                                                                                 (188, 16, 778, 1),
                                                                                                                 (189, 16, 779, 1),
                                                                                                                 (190, 16, 780, 1),
                                                                                                                 (191, 16, 781, 1),
                                                                                                                 (192, 16, 782, 1),
                                                                                                                 (193, 16, 783, 1),
                                                                                                                 (194, 16, 784, 1),
                                                                                                                 (195, 16, 785, 1),
                                                                                                                 (196, 16, 786, 1),
                                                                                                                 (197, 16, 787, 1),
                                                                                                                 (198, 16, 788, 1),
                                                                                                                 (199, 16, 789, 1),
                                                                                                                 (246, 20, 6352, 1),
                                                                                                                 (247, 20, 6353, 1),
                                                                                                                 (248, 20, 6354, 1),
                                                                                                                 (249, 20, 6355, 1),
                                                                                                                 (250, 20, 6356, 1),
                                                                                                                 (251, 20, 6357, 1),
                                                                                                                 (252, 20, 6358, 1),
                                                                                                                 (253, 20, 6359, 1),
                                                                                                                 (254, 20, 6360, 1),
                                                                                                                 (255, 20, 6380, 1),
                                                                                                                 (361, 31, 8424, 1),
                                                                                                                 (362, 31, 8425, 1),
                                                                                                                 (120, 11, 9000, 1),
                                                                                                                 (121, 11, 9001, 1),
                                                                                                                 (122, 11, 9002, 1),
                                                                                                                 (123, 11, 9003, 1),
                                                                                                                 (124, 11, 9005, 1),
                                                                                                                 (125, 95, 9006, 1),
                                                                                                                 (1, 27, 10001, 1),
                                                                                                                 (2, 27, 10002, 1),
                                                                                                                 (3, 27, 10003, 1),
                                                                                                                 (4, 27, 10004, 1),
                                                                                                                 (363, 32, 10022, 1),
                                                                                                                 (364, 32, 10023, 1),
                                                                                                                 (365, 33, 10025, 1),
                                                                                                                 (366, 33, 10026, 1),
                                                                                                                 (367, 33, 10027, 1),
                                                                                                                 (368, 33, 10028, 1),
                                                                                                                 (999999, 35, 10030, 1),
                                                                                                                 (442, 41, 10048, 1),
                                                                                                                 (531, 94, 10050, 1),
                                                                                                                 (532, 94, 10051, 1),
                                                                                                                 (536, 94, 10053, 1),
                                                                                                                 (99999, 99, 1000124, 1),
                                                                                                                 (0, 34, 1000125, 1),
                                                                                                                 (10000, 34, 1000125, 1),
                                                                                                                 (10001, 34, 1000126, 1),
                                                                                                                 (1000000, 35, 1000126, 1);

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

--
-- Déchargement des données de la table `transaction`
--

INSERT INTO `transaction` (`id_transaction`, `ref_acheteur`, `ref_vendeur`, `date`, `prix`, `validate`, `ref_article`) VALUES
                                                                                                                           (4, 3, 0, '2026-03-09 17:41:55', '9087532', 0, 15),
                                                                                                                           (5, 1, 1, '2026-03-15 11:24:55', '1000', 0, 32),
                                                                                                                           (6, 1, 1, '2026-03-17 08:19:23', '2990', 0, 36),
                                                                                                                           (7, 1, 1, '2026-03-17 10:20:46', '12', 0, 33),
                                                                                                                           (8, 1, 0, '2026-03-19 11:23:32', '150', 0, 27),
                                                                                                                           (9, 1, 0, '2026-03-19 11:38:07', '150', 0, 27),
                                                                                                                           (10, 1, 0, '2026-03-19 11:40:58', '150', 0, 27),
                                                                                                                           (11, 1, 0, '2026-03-19 11:43:47', '150', 0, 27),
                                                                                                                           (12, 1, 0, '2026-03-19 11:46:36', '150', 0, 27),
                                                                                                                           (13, 1, 0, '2026-03-19 11:48:56', '150', 0, 27),
                                                                                                                           (14, 1, 0, '2026-03-19 11:51:29', '150', 0, 27),
                                                                                                                           (15, 1, 0, '2026-03-19 11:55:58', '150', 0, 27),
                                                                                                                           (16, 1, 0, '2026-03-19 12:03:22', '150', 0, 27),
                                                                                                                           (17, 1, 1, '2026-03-19 12:06:47', '2990', 0, 36),
                                                                                                                           (18, 1, 1, '2026-03-19 16:07:15', '2990', 0, 36),
                                                                                                                           (19, 1, 1, '2026-03-20 10:17:14', '2990', 0, 36),
                                                                                                                           (20, 1, 1, '2026-03-20 14:03:15', '2990', 0, 36);

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

--
-- Déchargement des données de la table `type_de_jeux`
--

INSERT INTO `type_de_jeux` (`id`) VALUES
                                      (1),
                                      (2),
                                      (3);

-- --------------------------------------------------------

--
-- Structure de la table `type_de_jeux_libelle_langue`
--

CREATE TABLE `type_de_jeux_libelle_langue` (
                                               `ref_type_de_jeux` int(11) NOT NULL,
                                               `ref_libelle` int(11) NOT NULL,
                                               `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_de_jeux_libelle_langue`
--

INSERT INTO `type_de_jeux_libelle_langue` (`ref_type_de_jeux`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                (1, 9004, 1),
                                                                                                (2, 9005, 1),
                                                                                                (3, 9006, 1);

-- --------------------------------------------------------

--
-- Structure de la table `type_de_vin_libelle_langue`
--

CREATE TABLE `type_de_vin_libelle_langue` (
                                              `ref_type_de_vin` int(11) NOT NULL,
                                              `ref_libelle` int(11) NOT NULL,
                                              `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_de_vin_libelle_langue`
--

INSERT INTO `type_de_vin_libelle_langue` (`ref_type_de_vin`, `ref_libelle`, `ref_langue`) VALUES
                                                                                              (1, 10001, 1),
                                                                                              (2, 10002, 1),
                                                                                              (3, 10003, 1),
                                                                                              (4, 10004, 1);

-- --------------------------------------------------------

--
-- Structure de la table `type_ecran`
--

CREATE TABLE `type_ecran` (
                              `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_ecran`
--

INSERT INTO `type_ecran` (`id`) VALUES
                                    (1),
                                    (2);

-- --------------------------------------------------------

--
-- Structure de la table `type_ecran_libelle_langue`
--

CREATE TABLE `type_ecran_libelle_langue` (
                                             `ref_type_ecran` int(11) NOT NULL,
                                             `ref_libelle` int(11) NOT NULL,
                                             `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_ecran_libelle_langue`
--

INSERT INTO `type_ecran_libelle_langue` (`ref_type_ecran`, `ref_libelle`, `ref_langue`) VALUES
                                                                                            (1, 10060, 1),
                                                                                            (2, 10061, 1);

-- --------------------------------------------------------

--
-- Structure de la table `type_essence`
--

CREATE TABLE `type_essence` (
                                `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_essence`
--

INSERT INTO `type_essence` (`id`) VALUES
                                      (1),
                                      (2),
                                      (3);

-- --------------------------------------------------------

--
-- Structure de la table `type_essence_libelle_langue`
--

CREATE TABLE `type_essence_libelle_langue` (
                                               `ref_type_essence` int(11) NOT NULL,
                                               `ref_libelle` int(11) NOT NULL,
                                               `ref_langue` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `type_essence_libelle_langue`
--

INSERT INTO `type_essence_libelle_langue` (`ref_type_essence`, `ref_libelle`, `ref_langue`) VALUES
                                                                                                (1, 10041, 1),
                                                                                                (2, 10042, 1),
                                                                                                (3, 10043, 1);

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
    ADD PRIMARY KEY (`id_article`),
  ADD KEY `fk_article_genre` (`ref_genre`),
  ADD KEY `fk_article_categorie` (`ref_categorie`),
  ADD KEY `fk_article_depot` (`ref_depot`),
  ADD KEY `fk_article_taille` (`ref_taille`),
  ADD KEY `fk_article_payement` (`ref_condition_payement`),
  ADD KEY `fk_article_livraison` (`ref_mode_de_livraison`),
  ADD KEY `fk_article_type_ecran` (`ref_type_ecran`),
  ADD KEY `fk_article_essence` (`essence_ou_diesel`),
  ADD KEY `fk_article_type_jeux` (`ref_type_de_jeux`),
  ADD KEY `fk_article_cepage` (`ref_cepage`),
  ADD KEY `fk_article_region_vin` (`ref_pays_region_vin`),
  ADD KEY `fk_article_type_vin` (`ref_type_de_vin`),
  ADD KEY `fk_article_etat` (`ref_etat`),
  ADD KEY `fk_article_canton` (`ref_canton`),
  ADD KEY `fk_article_boite` (`ref_boite_de_vitesse`),
  ADD KEY `fk_article_departement` (`ref_departement`);

--
-- Index pour la table `article_ip`
--
ALTER TABLE `article_ip`
    ADD PRIMARY KEY (`ref_article`);

--
-- Index pour la table `article_visite`
--
ALTER TABLE `article_visite`
    ADD PRIMARY KEY (`id_visite`),
  ADD KEY `fk_av_personne` (`ref_personne`),
  ADD KEY `idx_av_article` (`ref_article`);

--
-- Index pour la table `a_livre`
--
ALTER TABLE `a_livre`
    ADD PRIMARY KEY (`id_a_livre`),
  ADD KEY `fk_al_article` (`ref_article`),
  ADD KEY `fk_al_acheteur` (`ref_acheteur`),
  ADD KEY `fk_al_vendeur` (`ref_vendeur`),
  ADD KEY `fk_al_livraison` (`ref_mode_de_livraison`);

--
-- Index pour la table `a_paye`
--
ALTER TABLE `a_paye`
    ADD PRIMARY KEY (`id_a_paye`),
  ADD KEY `fk_ap_article` (`ref_article`),
  ADD KEY `fk_ap_vendeur` (`ref_vendeur`),
  ADD KEY `fk_ap_acheteur` (`ref_acheteur`),
  ADD KEY `fk_ap_enchere` (`ref_enchere`),
  ADD KEY `fk_ap_payement` (`ref_condition_payement`),
  ADD KEY `fk_ap_livraison` (`ref_mode_de_livraison`),
  ADD KEY `fk_ap_canton` (`ref_canton`);

--
-- Index pour la table `boite_de_vitesse`
--
ALTER TABLE `boite_de_vitesse`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `boite_de_vitesse_libelle_langue`
--
ALTER TABLE `boite_de_vitesse_libelle_langue`
    ADD PRIMARY KEY (`ref_boite_de_vitesse`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_bdvll_libelle` (`ref_libelle`),
  ADD KEY `fk_bdvll_langue` (`ref_langue`);

--
-- Index pour la table `boutique`
--
ALTER TABLE `boutique`
    ADD PRIMARY KEY (`id_boutique`),
  ADD KEY `fk_boutique_personne` (`ref_personne`);

--
-- Index pour la table `boutique_a_categorie`
--
ALTER TABLE `boutique_a_categorie`
    ADD PRIMARY KEY (`ref_boutique`,`ref_categorie`),
  ADD KEY `fk_bac_categorie` (`ref_categorie`);

--
-- Index pour la table `canton_fr`
--
ALTER TABLE `canton_fr`
    ADD PRIMARY KEY (`id_canton`);

--
-- Index pour la table `categorie_libelle_langue`
--
ALTER TABLE `categorie_libelle_langue` 
  ADD PRIMARY KEY (`ref_categorie`),
  ADD KEY `fk_cat_ll_langue` (`ref_langue`),
  ADD KEY `fk_cat_ll_libelle_categorie_libelle_langue` (`ref_libelle`);

--
-- Index pour la table `cepage`
--
ALTER TABLE `cepage`
    ADD PRIMARY KEY (`id_cepage`),
  ADD KEY `fk_cepage_region` (`ref_pays_region_vin`),
  ADD KEY `fk_cepage_type` (`ref_type_de_vin`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
    ADD PRIMARY KEY (`id_commande`),
  ADD KEY `fk_commande_client` (`client_ref`),
  ADD KEY `fk_commande_vendeur` (`ref_vendeur`),
  ADD KEY `fk_commande_payement` (`ref_mode_de_payement`),
  ADD KEY `fk_commande_livraison` (`ref_mode_de_livraison`);

--
-- Index pour la table `commande_article`
--
ALTER TABLE `commande_article`
    ADD PRIMARY KEY (`ref_commande`,`ref_article`,`date_payement`),
  ADD KEY `fk_ca_article` (`ref_article`);

--
-- Index pour la table `commentaire`
--
ALTER TABLE `commentaire`
    ADD PRIMARY KEY (`id_commentaire`),
  ADD KEY `fk_commentaire_article` (`ref_article`),
  ADD KEY `fk_commentaire_emetteur` (`ref_emetteur`);

--
-- Index pour la table `condition_livraison`
--
ALTER TABLE `condition_livraison`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `condition_livraison_libelle_langue`
--
ALTER TABLE `condition_livraison_libelle_langue`
    ADD PRIMARY KEY (`ref_mode_de_livraison`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_cll_libelle` (`ref_libelle`),
  ADD KEY `fk_cll_langue` (`ref_langue`);


--
-- Index pour la table `demande_visite`
--
ALTER TABLE `demande_visite`
    ADD PRIMARY KEY (`id_demande`),
  ADD KEY `fk_dv_article` (`ref_article`),
  ADD KEY `fk_dv_vendeur` (`ref_vendeur`);

--
-- Index pour la table `departement`
--
ALTER TABLE `departement`
    ADD PRIMARY KEY (`id_departement`,`code`);

--
-- Index pour la table `depot`
--
ALTER TABLE `depot`
    ADD PRIMARY KEY (`id_depot`,`ref_responsable`),
  ADD KEY `fk_depot_responsable` (`ref_responsable`);

--
-- Index pour la table `enchere`
ALTER TABLE `enchere` 
  ADD PRIMARY KEY (`id_enchere`),
  ADD UNIQUE KEY `uq_id_enchere` (`id_enchere`), -- Add this line
  ADD KEY `fk_enchere_article` (`ref_article`),
  ADD KEY `fk_enchere_enchereur` (`ref_enchereur`);

--
-- Index pour la table `etat`
--
ALTER TABLE `etat`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `etat_libelle_langue`
--
ALTER TABLE `etat_libelle_langue`
    ADD PRIMARY KEY (`ref_etat`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_ell_libelle` (`ref_libelle`),
  ADD KEY `fk_ell_langue` (`ref_langue`);

--
-- Index pour la table `evaluation_achat`
--
ALTER TABLE `evaluation_achat`
    ADD PRIMARY KEY (`id_evaluation_achat`),
  ADD KEY `fk_ea_vendeur` (`ref_vendeur`),
  ADD KEY `fk_ea_acheteur` (`ref_acheteur`),
  ADD KEY `fk_ea_article` (`ref_article`);

--
-- Index pour la table `evaluation_article`
--
ALTER TABLE `evaluation_article`
    ADD PRIMARY KEY (`id_evaluation_article`),
  ADD KEY `fk_eart_vendeur` (`ref_vendeur`),
  ADD KEY `fk_eart_acheteur` (`ref_acheteur`),
  ADD KEY `fk_eart_article` (`ref_article`);

--
-- Index pour la table `evaluation_vente`
--
ALTER TABLE `evaluation_vente`
    ADD PRIMARY KEY (`id_evaluation_vente`),
  ADD KEY `fk_ev_vendeur` (`ref_vendeur`),
  ADD KEY `fk_ev_acheteur` (`ref_acheteur`),
  ADD KEY `fk_ev_article` (`ref_article`);

--
-- Index pour la table `genre`
--
ALTER TABLE `genre`
    ADD PRIMARY KEY (`id_genre`);

--
-- Index pour la table `langue`
--
ALTER TABLE `langue` 
  ADD PRIMARY KEY (`id_langue`),
  ADD UNIQUE KEY `uq_id_langue` (`id_langue`), -- <-- ADD THIS EXACT LINE
  ADD KEY `fk_bdvll_langue` (`id_langue`);
--
-- Index pour la table `libelle`
--
ALTER TABLE `libelle` 
  ADD PRIMARY KEY (`id_libelle`),
  ADD UNIQUE KEY `uq_id_libelle` (`id_libelle`); -- ADD THIS LINE

--
-- Index pour la table `location_ou_achat_libelle_langue`
--
ALTER TABLE `location_ou_achat_libelle_langue`
    ADD PRIMARY KEY (`ref_location_ou_achat`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_loall_libelle` (`ref_libelle`),
  ADD KEY `fk_loall_langue` (`ref_langue`);

--
-- Index pour la table `main_categorie_libelle_langue`
--
ALTER TABLE `main_categorie_libelle_langue`
    ADD PRIMARY KEY (`ref_main_categorie`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_mcat_ll_langue` (`ref_langue`),
  ADD KEY `fk_mcat_ll_libelle` (`ref_libelle`);

--
-- Index pour la table `met_en_vente`
--
ALTER TABLE `met_en_vente`
    ADD PRIMARY KEY (`ref_vendeur`,`ref_article`),
  ADD KEY `fk_mev_article` (`ref_article`);

--
-- Index pour la table `mode_de_payement`
--
ALTER TABLE `mode_de_payement`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `mode_de_payement_libelle_langue`
--
ALTER TABLE `mode_de_payement_libelle_langue`
    ADD PRIMARY KEY (`ref_mode_de_payement`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_mdpll_libelle` (`ref_libelle`),
  ADD KEY `fk_mdpll_langue` (`ref_langue`);

--
-- Index pour la table `mois`
--
ALTER TABLE `mois`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `mois_libelle_langue`
--
ALTER TABLE `mois_libelle_langue`
    ADD PRIMARY KEY (`ref_mois`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_mll_libelle` (`ref_libelle`),
  ADD KEY `fk_mll_langue` (`ref_langue`);

--
-- Index pour la table `pays`
--
ALTER TABLE `pays` 
  ADD PRIMARY KEY (`id_pays`),
  ADD UNIQUE KEY `uq_id_pays` (`id_pays`); -- ADD THIS LINE

--
-- Index pour la table `pays_present`
--
ALTER TABLE `pays_present` 
  ADD PRIMARY KEY (`id_pays_present`),
  ADD UNIQUE KEY `uq_id_pays_present` (`id_pays_present`); -- ADD THIS LINE

--
-- Index pour la table `pays_region_vin`
--
ALTER TABLE `pays_region_vin`
    ADD PRIMARY KEY (`id_pays_region_vin`),
  ADD KEY `fk_pays_region_vin_pays` (`ref_pays`),
  ADD KEY `fk_pays_region_vin_parent` (`parent_id`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
    ADD PRIMARY KEY (`id_personne`),
  ADD UNIQUE KEY `uq_personne_nom_utilisateur` (`nom_utilisateur`),
  ADD UNIQUE KEY `uq_personne_email` (`email`),
  ADD KEY `fk_personne_canton` (`ref_canton`);

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
    ADD PRIMARY KEY (`ref_publication_option`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_poll_libelle` (`ref_libelle`),
  ADD KEY `fk_poll_langue` (`ref_langue`);

--
-- Index pour la table `recherche`
--
ALTER TABLE `recherche`
    ADD PRIMARY KEY (`id_recherche`),
  ADD KEY `fk_recherche_categorie` (`ref_categorie`);

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
    ADD PRIMARY KEY (`ref_statut`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_sll_libelle` (`ref_libelle`),
  ADD KEY `fk_sll_langue` (`ref_langue`);

--
-- Index pour la table `subcategorie_libelle_langue`
--
ALTER TABLE `subcategorie_libelle_langue`
    ADD PRIMARY KEY (`ref_subcategorie`,`ref_categorie`,`ref_langue`),
  ADD KEY `fk_subcat_ll_langue` (`ref_langue`),
  ADD KEY `fk_subcat_ll_libelle` (`ref_libelle`),
  ADD KEY `fk_subcat_ll_categorie` (`ref_categorie`);

--
-- Index pour la table `taille_libelle_langue`
--
ALTER TABLE `taille_libelle_langue`
    ADD PRIMARY KEY (`ref_taille`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tall_libelle` (`ref_libelle`),
  ADD KEY `fk_tall_langue` (`ref_langue`);

--
-- Index pour la table `temps`
--
ALTER TABLE `temps`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `temps_libelle_langue`
--
ALTER TABLE `temps_libelle_langue`
    ADD PRIMARY KEY (`ref_temps`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tll_libelle` (`ref_libelle`),
  ADD KEY `fk_tll_langue` (`ref_langue`);

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
    ADD PRIMARY KEY (`id_transaction`),
  ADD KEY `fk_tx_acheteur` (`ref_acheteur`),
  ADD KEY `fk_tx_vendeur` (`ref_vendeur`),
  ADD KEY `fk_tx_article` (`ref_article`);

--
-- Index pour la table `type_de_compte`
--
ALTER TABLE `type_de_compte`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_de_compte_libelle_langue`
--
ALTER TABLE `type_de_compte_libelle_langue`
    ADD PRIMARY KEY (`ref_type_de_compte`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tdcll_libelle` (`ref_libelle`),
  ADD KEY `fk_tdcll_langue` (`ref_langue`);

--
-- Index pour la table `type_de_jeux`
--
ALTER TABLE `type_de_jeux`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_de_jeux_libelle_langue`
--
ALTER TABLE `type_de_jeux_libelle_langue`
    ADD PRIMARY KEY (`ref_type_de_jeux`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tdjll_libelle` (`ref_libelle`),
  ADD KEY `fk_tdjll_langue` (`ref_langue`);

--
-- Index pour la table `type_de_vin_libelle_langue`
--
ALTER TABLE `type_de_vin_libelle_langue`
    ADD PRIMARY KEY (`ref_type_de_vin`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tdvll_libelle` (`ref_libelle`),
  ADD KEY `fk_tdvll_langue` (`ref_langue`);

--
-- Index pour la table `type_ecran`
--
ALTER TABLE `type_ecran`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_ecran_libelle_langue`
--
ALTER TABLE `type_ecran_libelle_langue`
    ADD PRIMARY KEY (`ref_type_ecran`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tell_libelle` (`ref_libelle`),
  ADD KEY `fk_tell_langue` (`ref_langue`);

--
-- Index pour la table `type_essence`
--
ALTER TABLE `type_essence`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_essence_libelle_langue`
--
ALTER TABLE `type_essence_libelle_langue`
    ADD PRIMARY KEY (`ref_type_essence`,`ref_libelle`,`ref_langue`),
  ADD KEY `fk_tess_libelle` (`ref_libelle`),
  ADD KEY `fk_tess_langue` (`ref_langue`);

--
-- Index pour la table `visiteur`
--
ALTER TABLE `visiteur`
    ADD PRIMARY KEY (`ref_personne`,`ref_visiteur`),
  ADD KEY `fk_visiteur_visiteur` (`ref_visiteur`);

--
-- Index pour la table `wish`
--
ALTER TABLE `wish`
    ADD PRIMARY KEY (`id_wish`),
  ADD KEY `fk_wish_list` (`ref_wish_list`),
  ADD KEY `fk_wish_user` (`ref_user`),
  ADD KEY `fk_wish_categorie` (`ref_categorie`),
  ADD KEY `fk_wish_payement` (`ref_condition_payement`),
  ADD KEY `fk_wish_livraison` (`ref_mode_de_livraison`),
  ADD KEY `fk_wish_canton` (`ref_canton`);

--
-- Index pour la table `wish_list`
--
ALTER TABLE `wish_list`
    ADD PRIMARY KEY (`id_wish_list`),
  ADD KEY `fk_wl_user` (`ref_user`);

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
    MODIFY `id_article` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT pour la table `article_visite`
--
ALTER TABLE `article_visite`
    MODIFY `id_visite` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `a_livre`
--
ALTER TABLE `a_livre`
    MODIFY `id_a_livre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;

--
-- AUTO_INCREMENT pour la table `a_paye`
--
ALTER TABLE `a_paye`
    MODIFY `id_a_paye` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

--
-- AUTO_INCREMENT pour la table `boutique`
--
ALTER TABLE `boutique`
    MODIFY `id_boutique` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `canton_fr`
--
ALTER TABLE `canton_fr`
    MODIFY `id_canton` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT pour la table `cepage`
--
ALTER TABLE `cepage`
    MODIFY `id_cepage` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=543;

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
    MODIFY `id_commande` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `commentaire`
--
ALTER TABLE `commentaire`
    MODIFY `id_commentaire` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `demande_visite`
--
ALTER TABLE `demande_visite`
    MODIFY `id_demande` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `departement`
--
ALTER TABLE `departement`
    MODIFY `id_departement` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `enchere`
--
ALTER TABLE `enchere`
    MODIFY `id_enchere` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT pour la table `evaluation_achat`
--
ALTER TABLE `evaluation_achat`
    MODIFY `id_evaluation_achat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `evaluation_article`
--
ALTER TABLE `evaluation_article`
    MODIFY `id_evaluation_article` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

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
-- AUTO_INCREMENT pour la table `mode_de_payement`
--
ALTER TABLE `mode_de_payement`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `mois`
--
ALTER TABLE `mois`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

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
    MODIFY `id_personne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

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
-- AUTO_INCREMENT pour la table `temps`
--
ALTER TABLE `temps`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `transaction`
--
ALTER TABLE `transaction`
    MODIFY `id_transaction` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

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
-- Contraintes pour la table `article`
--
ALTER TABLE `article`
    ADD CONSTRAINT `fk_article_boite` FOREIGN KEY (`ref_boite_de_vitesse`) REFERENCES `boite_de_vitesse` (`id`),
  ADD CONSTRAINT `fk_article_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  ADD CONSTRAINT `fk_article_cepage` FOREIGN KEY (`ref_cepage`) REFERENCES `cepage` (`id_cepage`),
  ADD CONSTRAINT `fk_article_essence` FOREIGN KEY (`essence_ou_diesel`) REFERENCES `type_essence` (`id`),
  ADD CONSTRAINT `fk_article_etat` FOREIGN KEY (`ref_etat`) REFERENCES `etat` (`id`),
  ADD CONSTRAINT `fk_article_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  ADD CONSTRAINT `fk_article_region_vin` FOREIGN KEY (`ref_pays_region_vin`) REFERENCES `pays_region_vin` (`id_pays_region_vin`),
  ADD CONSTRAINT `fk_article_type_ecran` FOREIGN KEY (`ref_type_ecran`) REFERENCES `type_ecran` (`id`),
  ADD CONSTRAINT `fk_article_type_jeux` FOREIGN KEY (`ref_type_de_jeux`) REFERENCES `type_de_jeux` (`id`);

--
-- Contraintes pour la table `article_ip`
--
ALTER TABLE `article_ip`
    ADD CONSTRAINT `fk_article_ip_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`);

--
-- Contraintes pour la table `article_visite`
--
ALTER TABLE `article_visite`
    ADD CONSTRAINT `fk_av_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_av_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `a_livre`
--
ALTER TABLE `a_livre`
    ADD CONSTRAINT `fk_al_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_al_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_al_enchere_article` FOREIGN KEY (`ref_enchere`) REFERENCES `enchere` (`id_enchere`),
  ADD CONSTRAINT `fk_al_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  ADD CONSTRAINT `fk_al_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `a_paye`
--
ALTER TABLE `a_paye`
    ADD CONSTRAINT `fk_ap_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_ap_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_ap_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  ADD CONSTRAINT `fk_ap_enchere` FOREIGN KEY (`ref_enchere`) REFERENCES `enchere` (`id_enchere`),
  ADD CONSTRAINT `fk_ap_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  ADD CONSTRAINT `fk_ap_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `boite_de_vitesse_libelle_langue`
--
ALTER TABLE `boite_de_vitesse_libelle_langue`
    ADD CONSTRAINT `fk_bdvll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_bdvll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_bdvll_type` FOREIGN KEY (`ref_boite_de_vitesse`) REFERENCES `boite_de_vitesse` (`id`);

--
-- Contraintes pour la table `boutique`
--
ALTER TABLE `boutique`
    ADD CONSTRAINT `fk_boutique_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `boutique_a_categorie`
--
ALTER TABLE `boutique_a_categorie`
    ADD CONSTRAINT `fk_bac_boutique` FOREIGN KEY (`ref_boutique`) REFERENCES `boutique` (`id_boutique`),
    ADD CONSTRAINT `fk_bac_categorie_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie_libelle_langue` (`ref_categorie`);

--
-- Contraintes pour la table `categorie_libelle_langue`
--
-- LOCATE THIS BLOCK IN YOUR SCRIPT:
ALTER TABLE `categorie_libelle_langue` 

  ADD UNIQUE KEY `uq_ref_categorie` (`ref_categorie`), -- ADD THIS EXACT LINE
  ADD KEY `fk_cat_ll_langue_categorie_langue` (`ref_langue`),
  ADD KEY `fk_cat_ll_libelle` (`ref_libelle`);

--
-- Contraintes pour la table `cepage`
--
ALTER TABLE `cepage`
    ADD CONSTRAINT `fk_cepage_region` FOREIGN KEY (`ref_pays_region_vin`) REFERENCES `pays_region_vin` (`id_pays_region_vin`);

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
    ADD CONSTRAINT `fk_commande_client` FOREIGN KEY (`client_ref`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_commande_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  ADD CONSTRAINT `fk_commande_payement` FOREIGN KEY (`ref_mode_de_payement`) REFERENCES `mode_de_payement` (`id`),
  ADD CONSTRAINT `fk_commande_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `commande_article`
--
ALTER TABLE `commande_article`
    ADD CONSTRAINT `fk_ca_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_ca_commande` FOREIGN KEY (`ref_commande`) REFERENCES `commande` (`id_commande`);

--
-- Contraintes pour la table `commentaire`
--
ALTER TABLE `commentaire`
    ADD CONSTRAINT `fk_commentaire_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_commentaire_emetteur` FOREIGN KEY (`ref_emetteur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `condition_livraison_libelle_langue`
--
ALTER TABLE `condition_livraison_libelle_langue`
    ADD CONSTRAINT `fk_cll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_cll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_cll_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`);

--
-- Contraintes pour la table `condition_payement_libelle_langue`
--
-- LOCATE THIS BLOCK IN YOUR SCRIPT:
ALTER TABLE `condition_payement_libelle_langue` 
  ADD PRIMARY KEY (`ref_condition_payement`),
  ADD UNIQUE KEY `uq_ref_condition_payement` (`ref_condition_payement`), -- ADD THIS LINE
  ADD KEY `fk_cpl_libelle` (`ref_libelle`),
  ADD KEY `fk_cpl_langue` (`ref_langue`);

--
-- Contraintes pour la table `demande_visite`
--
ALTER TABLE `demande_visite`
    ADD CONSTRAINT `fk_dv_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_dv_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `depot`
--
ALTER TABLE `depot`
    ADD CONSTRAINT `fk_depot_responsable` FOREIGN KEY (`ref_responsable`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `enchere`
--
ALTER TABLE `enchere`
    ADD CONSTRAINT `fk_enchere_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_enchere_enchereur` FOREIGN KEY (`ref_enchereur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `etat_libelle_langue`
--
ALTER TABLE `etat_libelle_langue`
    ADD CONSTRAINT `fk_ell_etat` FOREIGN KEY (`ref_etat`) REFERENCES `etat` (`id`),
  ADD CONSTRAINT `fk_ell_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_ell_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `evaluation_achat`
--
ALTER TABLE `evaluation_achat`
    ADD CONSTRAINT `fk_ea_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_ea_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_ea_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `evaluation_article`
--
ALTER TABLE `evaluation_article`
    ADD CONSTRAINT `fk_eart_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_eart_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`);

--
-- Contraintes pour la table `evaluation_vente`
--
ALTER TABLE `evaluation_vente`
    ADD CONSTRAINT `fk_ev_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_ev_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_ev_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `location_ou_achat_libelle_langue`
--
ALTER TABLE `location_ou_achat_libelle_langue`
    ADD CONSTRAINT `fk_loall_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_loall_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `main_categorie_libelle_langue`
--
ALTER TABLE `main_categorie_libelle_langue`
    ADD CONSTRAINT `fk_mcat_ll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_mcat_ll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `met_en_vente`
--
ALTER TABLE `met_en_vente`
    ADD CONSTRAINT `fk_mev_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  ADD CONSTRAINT `fk_mev_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `mode_de_payement_libelle_langue`
--
ALTER TABLE `mode_de_payement_libelle_langue`
    ADD CONSTRAINT `fk_mdpll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_mdpll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_mdpll_type` FOREIGN KEY (`ref_mode_de_payement`) REFERENCES `mode_de_payement` (`id`);

--
-- Contraintes pour la table `mois_libelle_langue`
--
ALTER TABLE `mois_libelle_langue`
    ADD CONSTRAINT `fk_mll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_mll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_mll_mois` FOREIGN KEY (`ref_mois`) REFERENCES `mois` (`id`);

--
-- Contraintes pour la table `pays_region_vin`
--
ALTER TABLE `pays_region_vin`
    ADD CONSTRAINT `fk_pays_region_vin_parent` FOREIGN KEY (`parent_id`) REFERENCES `pays_region_vin` (`id_pays_region_vin`),
  ADD CONSTRAINT `fk_pays_region_vin_pays` FOREIGN KEY (`ref_pays`) REFERENCES `pays` (`id_pays`);

--
-- Contraintes pour la table `personne`
--
ALTER TABLE `personne`
    ADD CONSTRAINT `fk_personne_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`);

--
-- Contraintes pour la table `publication_option_libelle_langue`
--
ALTER TABLE `publication_option_libelle_langue`
    ADD CONSTRAINT `fk_poll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_poll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_poll_option` FOREIGN KEY (`ref_publication_option`) REFERENCES `publication_option` (`id`);

--
-- Contraintes pour la table `recherche`
--
ALTER TABLE `recherche`
    ADD CONSTRAINT `fk_recherche_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie_libelle_langue` (`ref_categorie`);

--
-- Contraintes pour la table `statut_libelle_langue`
--
ALTER TABLE `statut_libelle_langue`
    ADD CONSTRAINT `fk_sll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_sll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `subcategorie_libelle_langue`
--
ALTER TABLE `subcategorie_libelle_langue`
    ADD CONSTRAINT `fk_subcat_ll_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie_libelle_langue` (`ref_categorie`),
  ADD CONSTRAINT `fk_subcat_ll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_subcat_ll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `taille_libelle_langue`
--
ALTER TABLE `taille_libelle_langue`
    ADD CONSTRAINT `fk_tall_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tall_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `temps_libelle_langue`
--
ALTER TABLE `temps_libelle_langue`
    ADD CONSTRAINT `fk_tll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_tll_temps` FOREIGN KEY (`ref_temps`) REFERENCES `temps` (`id`);

--
-- Contraintes pour la table `type_de_compte_libelle_langue`
--
ALTER TABLE `type_de_compte_libelle_langue`
    ADD CONSTRAINT `fk_tdcll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tdcll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_tdcll_type` FOREIGN KEY (`ref_type_de_compte`) REFERENCES `type_de_compte` (`id`);

--
-- Contraintes pour la table `type_de_jeux_libelle_langue`
--
ALTER TABLE `type_de_jeux_libelle_langue`
    ADD CONSTRAINT `fk_tdjll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tdjll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_tdjll_type` FOREIGN KEY (`ref_type_de_jeux`) REFERENCES `type_de_jeux` (`id`);

--
-- Contraintes pour la table `type_de_vin_libelle_langue`
--
ALTER TABLE `type_de_vin_libelle_langue`
    ADD CONSTRAINT `fk_tdvll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tdvll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`);

--
-- Contraintes pour la table `type_ecran_libelle_langue`
--
ALTER TABLE `type_ecran_libelle_langue`
    ADD CONSTRAINT `fk_tell_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tell_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_tell_type` FOREIGN KEY (`ref_type_ecran`) REFERENCES `type_ecran` (`id`);

--
-- Contraintes pour la table `type_essence_libelle_langue`
--
ALTER TABLE `type_essence_libelle_langue`
    ADD CONSTRAINT `fk_tess_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  ADD CONSTRAINT `fk_tess_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  ADD CONSTRAINT `fk_tess_type` FOREIGN KEY (`ref_type_essence`) REFERENCES `type_essence` (`id`);

--
-- Contraintes pour la table `visiteur`
--
ALTER TABLE `visiteur`
    ADD CONSTRAINT `fk_visiteur_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`),
  ADD CONSTRAINT `fk_visiteur_visiteur` FOREIGN KEY (`ref_visiteur`) REFERENCES `personne` (`id_personne`);

--
-- Contraintes pour la table `wish`
--
ALTER TABLE `wish`
    ADD CONSTRAINT `fk_wish_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  ADD CONSTRAINT `fk_wish_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie_libelle_langue` (`ref_categorie`),
  ADD CONSTRAINT `fk_wish_list` FOREIGN KEY (`ref_wish_list`) REFERENCES `wish_list` (`id_wish_list`),
  ADD CONSTRAINT `fk_wish_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  ADD CONSTRAINT `fk_wish_payement` FOREIGN KEY (`ref_condition_payement`) REFERENCES `condition_payement_libelle_langue` (`ref_condition_payement`),
  ADD CONSTRAINT `fk_wish_user` FOREIGN KEY (`ref_user`) REFERENCES `personne` (`id_personne`);

ALTER TABLE article
    ADD COLUMN entrupy_customer_item_id VARCHAR(64)  NULL,
    ADD COLUMN entrupy_status           VARCHAR(32)  NULL,   -- pending / authentic / unidentified / invalid / not_supported
    ADD COLUMN entrupy_id               VARCHAR(64)  NULL,   -- id Entrupy de l'authentification
    ADD COLUMN entrupy_certificate_url  VARCHAR(255) NULL,
    ADD COLUMN entrupy_updated_at       TIMESTAMP    NULL;

CREATE UNIQUE INDEX ux_article_entrupy_customer_item_id
    ON article (entrupy_customer_item_id);

-- Renommez ce fichier selon votre convention Flyway existante
-- (ex: V28__add_article_video_path.sql — à ajuster selon le numéro déjà
-- utilisé pour V__add_entrupy_authentication_fields.sql, celle-ci doit
-- suivre juste après)

ALTER TABLE article
    ADD COLUMN video_path VARCHAR(255) NULL;

--
-- Contraintes pour la table `wish_list`
--

ALTER TABLE `wish_list`
    ADD CONSTRAINT `fk_wl_user` FOREIGN KEY (`ref_user`) REFERENCES `personne` (`id_personne`);
SET FOREIGN_KEY_CHECKS = 1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
