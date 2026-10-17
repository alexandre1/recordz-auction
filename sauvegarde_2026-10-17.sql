-- MySQL dump 10.13  Distrib 9.1.0, for Linux (x86_64)
--
-- Host: localhost    Database: new_db_recordzv3
-- ------------------------------------------------------
-- Server version	9.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `a_livre`
--

DROP TABLE IF EXISTS `a_livre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a_livre` (
  `id_a_livre` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_acheteur` int NOT NULL,
  `ref_vendeur` int NOT NULL,
  `ref_enchere` int DEFAULT NULL,
  `date_achat` datetime DEFAULT NULL,
  `ref_mode_de_livraison` int DEFAULT NULL,
  `ref_statut` int NOT NULL,
  `quantite` int DEFAULT NULL,
  `montant` varchar(10) DEFAULT NULL,
  `date_reception` date DEFAULT NULL,
  PRIMARY KEY (`id_a_livre`),
  KEY `fk_al_article` (`ref_article`),
  KEY `fk_al_acheteur` (`ref_acheteur`),
  KEY `fk_al_vendeur` (`ref_vendeur`),
  KEY `fk_al_livraison` (`ref_mode_de_livraison`),
  KEY `fk_al_enchere_article` (`ref_enchere`),
  CONSTRAINT `fk_al_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_al_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_al_enchere_article` FOREIGN KEY (`ref_enchere`) REFERENCES `enchere` (`id_enchere`),
  CONSTRAINT `fk_al_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  CONSTRAINT `fk_al_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_livre`
--

LOCK TABLES `a_livre` WRITE;
/*!40000 ALTER TABLE `a_livre` DISABLE KEYS */;
INSERT INTO `a_livre` VALUES (1,2,3,1,NULL,'2026-10-14 21:20:07',4,12,1,'600.0','2026-10-14'),(2,1,3,1,NULL,'2026-10-16 16:02:51',3,12,NULL,'12.0',NULL),(3,2,3,1,NULL,'2026-10-14 21:20:15',4,12,1,'600.0','2026-10-14'),(4,2,3,1,NULL,'2026-10-14 20:57:53',4,12,1,'600.0','2026-10-14'),(5,2,3,1,NULL,'2026-10-14 22:07:25',4,12,1,'600.0','2026-10-15'),(6,4,3,1,NULL,'2026-10-15 13:06:29',2,12,NULL,'1212.0',NULL),(7,4,3,1,NULL,'2026-10-15 13:02:05',2,12,NULL,'1212.0',NULL),(8,4,3,1,NULL,'2026-10-15 13:53:16',2,12,1,'1212.0','2026-10-15'),(9,4,3,1,NULL,'2026-10-15 13:53:26',2,12,1,'1212.0','2026-10-15'),(10,4,3,1,NULL,'2026-10-15 14:18:42',2,12,1,'1212.0','2026-10-15'),(11,1,3,1,NULL,'2026-10-16 16:02:51',3,12,NULL,'12.0',NULL),(12,1,3,1,NULL,'2026-10-16 16:02:51',3,12,NULL,'12.0',NULL),(13,1,3,1,NULL,'2026-10-16 16:02:51',3,12,NULL,'12.0',NULL),(14,7,1,1,NULL,'2026-10-16 16:02:45',3,12,NULL,'1200000.0',NULL),(15,7,1,1,NULL,'2026-10-16 16:30:16',3,12,NULL,'1200000.0',NULL),(16,7,1,1,NULL,'2026-10-16 16:43:08',3,12,NULL,'1200000.0',NULL),(17,7,1,1,NULL,'2026-10-16 16:43:13',3,12,NULL,'1200000.0',NULL),(18,7,1,1,NULL,'2026-10-16 16:43:17',3,12,NULL,'1200000.0',NULL),(19,7,1,1,NULL,'2026-10-16 17:35:32',3,12,NULL,'1200000.0',NULL),(20,7,1,1,NULL,'2026-10-16 18:38:49',3,12,NULL,'1200000.0',NULL),(21,7,1,1,NULL,'2026-10-17 11:25:07',3,12,NULL,'1200000.0',NULL),(22,7,1,1,NULL,'2026-10-16 17:52:13',3,8,NULL,'1200000.0',NULL),(23,7,3,1,NULL,'2026-10-16 20:24:39',3,12,NULL,'1200000.0',NULL),(24,7,3,1,NULL,'2026-10-16 20:35:35',3,12,NULL,'1200000.0',NULL),(25,7,3,1,NULL,'2026-10-16 20:35:41',3,12,NULL,'1200000.0',NULL),(26,7,3,1,NULL,'2026-10-16 20:35:46',3,12,NULL,'1200000.0',NULL),(27,7,3,1,NULL,'2026-10-16 20:35:49',3,12,NULL,'1200000.0',NULL),(28,7,3,1,NULL,'2026-10-16 20:35:54',3,12,NULL,'1200000.0',NULL);
/*!40000 ALTER TABLE `a_livre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_paye`
--

DROP TABLE IF EXISTS `a_paye`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `a_paye` (
  `id_a_paye` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_vendeur` int NOT NULL,
  `ref_acheteur` int NOT NULL,
  `ref_enchere` int DEFAULT NULL,
  `ref_condition_payement` int NOT NULL,
  `ref_mode_de_livraison` int NOT NULL,
  `ref_statut` int NOT NULL,
  `montant` decimal(10,0) NOT NULL,
  `date_fermeture_enchere` datetime DEFAULT NULL,
  `date_payement` datetime DEFAULT NULL,
  `date_echeance` datetime DEFAULT NULL,
  `rappel_1` date DEFAULT NULL,
  `rappel_2` date DEFAULT NULL,
  `rappel_3` date DEFAULT NULL,
  `quantite` int NOT NULL DEFAULT '1',
  `ref_canton` int DEFAULT NULL,
  PRIMARY KEY (`id_a_paye`),
  KEY `fk_ap_article` (`ref_article`),
  KEY `fk_ap_vendeur` (`ref_vendeur`),
  KEY `fk_ap_acheteur` (`ref_acheteur`),
  KEY `fk_ap_enchere` (`ref_enchere`),
  KEY `fk_ap_payement` (`ref_condition_payement`),
  KEY `fk_ap_livraison` (`ref_mode_de_livraison`),
  KEY `fk_ap_canton` (`ref_canton`),
  CONSTRAINT `fk_ap_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_ap_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_ap_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  CONSTRAINT `fk_ap_enchere` FOREIGN KEY (`ref_enchere`) REFERENCES `enchere` (`id_enchere`),
  CONSTRAINT `fk_ap_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  CONSTRAINT `fk_ap_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_paye`
--

LOCK TABLES `a_paye` WRITE;
/*!40000 ALTER TABLE `a_paye` DISABLE KEYS */;
INSERT INTO `a_paye` VALUES (1,2,1,3,NULL,5,4,14,600,'2026-10-14 21:33:05',NULL,NULL,NULL,NULL,NULL,1,NULL),(2,1,1,3,NULL,4,3,14,12,'2026-10-14 18:43:42',NULL,NULL,NULL,NULL,NULL,1,NULL),(3,2,1,3,NULL,5,4,14,600,'2026-10-14 21:33:05',NULL,NULL,NULL,NULL,NULL,1,NULL),(4,2,1,3,NULL,5,4,14,600,'2026-10-14 21:33:05',NULL,NULL,NULL,NULL,NULL,1,NULL),(5,2,1,3,NULL,5,4,14,600,'2026-10-14 21:33:05',NULL,NULL,NULL,NULL,NULL,1,NULL),(6,2,1,3,NULL,5,4,14,600,'2026-10-14 22:08:03',NULL,NULL,NULL,NULL,NULL,1,NULL),(7,2,1,3,NULL,5,4,8,600,NULL,NULL,NULL,NULL,NULL,NULL,1,7),(8,4,1,3,NULL,5,2,14,1212,'2026-10-15 13:07:27',NULL,NULL,NULL,NULL,NULL,1,NULL),(9,4,1,3,NULL,5,2,14,1212,'2026-10-15 13:07:36',NULL,NULL,NULL,NULL,NULL,1,NULL),(10,4,1,3,NULL,5,2,14,1212,'2026-10-15 13:54:18',NULL,NULL,NULL,NULL,NULL,1,NULL),(11,4,1,3,NULL,5,2,14,1212,'2026-10-15 13:54:26',NULL,NULL,NULL,NULL,NULL,1,NULL),(12,4,1,3,NULL,5,2,14,1212,'2026-10-15 14:20:47',NULL,NULL,NULL,NULL,NULL,1,NULL),(13,1,1,1,NULL,4,3,14,12,NULL,NULL,NULL,NULL,NULL,NULL,1,7),(14,7,1,1,NULL,3,3,14,1200000,NULL,NULL,NULL,NULL,NULL,NULL,1,7),(15,7,1,1,NULL,3,3,14,1200000,NULL,NULL,NULL,NULL,NULL,NULL,1,7),(16,7,1,3,NULL,3,3,14,1200000,'2026-10-16 16:29:53',NULL,NULL,NULL,NULL,NULL,1,NULL),(17,7,1,3,NULL,3,3,14,1200000,'2026-10-16 16:41:40',NULL,NULL,NULL,NULL,NULL,1,NULL),(18,7,1,3,NULL,3,3,14,1200000,'2026-10-16 16:41:51',NULL,NULL,NULL,NULL,NULL,1,NULL),(19,7,1,3,NULL,3,3,14,1200000,'2026-10-16 16:41:59',NULL,NULL,NULL,NULL,NULL,1,NULL),(20,7,1,1,NULL,3,3,14,1200000,'2026-10-16 20:22:55',NULL,NULL,NULL,NULL,NULL,1,7),(21,7,1,1,NULL,3,3,14,1200000,'2026-10-16 20:23:12',NULL,NULL,NULL,NULL,NULL,1,7),(22,7,1,1,NULL,3,3,14,1200000,'2026-10-16 20:23:26',NULL,NULL,NULL,NULL,NULL,1,7),(23,7,1,1,NULL,3,3,14,1200000,'2026-10-16 20:23:36',NULL,NULL,NULL,NULL,NULL,1,7),(24,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:23:44',NULL,NULL,NULL,NULL,NULL,1,NULL),(25,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:23:52',NULL,NULL,NULL,NULL,NULL,1,NULL),(26,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:48:56',NULL,NULL,NULL,NULL,NULL,1,NULL),(27,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:49:05',NULL,NULL,NULL,NULL,NULL,1,NULL),(28,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:49:14',NULL,NULL,NULL,NULL,NULL,1,NULL),(29,7,1,3,NULL,3,3,14,1200000,'2026-10-16 20:49:22',NULL,NULL,NULL,NULL,NULL,1,NULL),(30,7,1,1,NULL,3,3,8,1200000,NULL,NULL,NULL,NULL,NULL,NULL,1,7);
/*!40000 ALTER TABLE `a_paye` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(1024) DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_app_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id_article` int NOT NULL AUTO_INCREMENT,
  `auteur` varchar(40) DEFAULT NULL,
  `marque` varchar(40) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `label` text NOT NULL,
  `prix` double NOT NULL,
  `prix_achat` decimal(10,0) DEFAULT NULL,
  `pochette` varchar(100) NOT NULL DEFAULT '',
  `presound` varchar(100) NOT NULL DEFAULT '',
  `ref_genre` int NOT NULL DEFAULT '0',
  `etat` int NOT NULL,
  `ref_categorie` int NOT NULL,
  `ref_subcategorie` int DEFAULT NULL,
  `owned` int NOT NULL,
  `ref_statut` int NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ref_depot` int NOT NULL DEFAULT '9',
  `ref_taille` int DEFAULT NULL,
  `ref_couleur` int DEFAULT NULL,
  `enchere` int NOT NULL,
  `ref_condition_payement` int NOT NULL,
  `ref_mode_de_livraison` int NOT NULL,
  `enchere_date_debut` varchar(40) DEFAULT NULL,
  `enchere_date_fin` varchar(40) DEFAULT NULL,
  `vendu` int NOT NULL DEFAULT '0',
  `ref_type_ecran` int DEFAULT NULL,
  `dimension` int DEFAULT NULL,
  `poids` int DEFAULT NULL,
  `nb_porte` int DEFAULT NULL,
  `nb_cheveaux` int DEFAULT NULL,
  `nb_km` int DEFAULT NULL,
  `premiere_immatriculation` varchar(20) DEFAULT NULL,
  `annee` int DEFAULT NULL,
  `options` text,
  `essence_ou_diesel` int DEFAULT NULL,
  `nb_piece` int DEFAULT NULL,
  `surface_habitable` int DEFAULT NULL,
  `superficie_terrain` varchar(40) DEFAULT NULL,
  `annee_construction` varchar(40) DEFAULT NULL,
  `visites` int DEFAULT '0',
  `nbr_enchere` int DEFAULT '0',
  `lang` varchar(2) NOT NULL DEFAULT 'fr',
  `ref_canton` int DEFAULT NULL,
  `lieu` varchar(50) DEFAULT NULL,
  `adresse` varchar(100) DEFAULT NULL,
  `npa` varchar(4) DEFAULT NULL,
  `ref_location_ou_achat` int DEFAULT NULL,
  `ref_departement` int DEFAULT NULL,
  `ref_pays` int DEFAULT NULL,
  `ref_boite_de_vitesse` int DEFAULT NULL,
  `clima` tinyint DEFAULT NULL,
  `processeur` decimal(10,2) DEFAULT NULL,
  `ram` varchar(10) DEFAULT NULL,
  `disque_dur` varchar(10) DEFAULT NULL,
  `quantite` int DEFAULT NULL,
  `ref_provenance` tinyint DEFAULT NULL,
  `longueur` int DEFAULT NULL,
  `largeur` int DEFAULT NULL,
  `consomation` int DEFAULT NULL,
  `acteurs` varchar(100) DEFAULT NULL,
  `duree` varbinary(10) DEFAULT NULL,
  `realisateur` varchar(40) DEFAULT NULL,
  `taille` varchar(40) DEFAULT NULL,
  `ref_type_de_jeux` int DEFAULT NULL,
  `ref_cepage` int DEFAULT NULL,
  `ref_pays_region_vin` int DEFAULT NULL,
  `ref_type_de_vin` int DEFAULT NULL,
  `ref_etat` int DEFAULT NULL,
  `frais_livraison` decimal(2,2) DEFAULT NULL,
  `wat` int DEFAULT NULL,
  `nb_cylindre` int DEFAULT NULL,
  `pub` int DEFAULT NULL,
  `pub_date_start` varchar(40) DEFAULT NULL,
  `pub_date_end` varchar(40) DEFAULT NULL,
  `tx` varchar(20) DEFAULT NULL,
  `link_youtube` varchar(500) NOT NULL DEFAULT '',
  `pochette2` varchar(100) DEFAULT NULL,
  `entrupy_customer_item_id` varchar(64) DEFAULT NULL,
  `entrupy_status` varchar(32) DEFAULT NULL,
  `entrupy_id` varchar(64) DEFAULT NULL,
  `entrupy_certificate_url` varchar(255) DEFAULT NULL,
  `entrupy_updated_at` timestamp NULL DEFAULT NULL,
  `video_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_article`),
  UNIQUE KEY `ux_article_entrupy_customer_item_id` (`entrupy_customer_item_id`),
  KEY `fk_article_genre` (`ref_genre`),
  KEY `fk_article_categorie` (`ref_categorie`),
  KEY `fk_article_depot` (`ref_depot`),
  KEY `fk_article_taille` (`ref_taille`),
  KEY `fk_article_payement` (`ref_condition_payement`),
  KEY `fk_article_livraison` (`ref_mode_de_livraison`),
  KEY `fk_article_type_ecran` (`ref_type_ecran`),
  KEY `fk_article_essence` (`essence_ou_diesel`),
  KEY `fk_article_type_jeux` (`ref_type_de_jeux`),
  KEY `fk_article_cepage` (`ref_cepage`),
  KEY `fk_article_region_vin` (`ref_pays_region_vin`),
  KEY `fk_article_type_vin` (`ref_type_de_vin`),
  KEY `fk_article_etat` (`ref_etat`),
  KEY `fk_article_canton` (`ref_canton`),
  KEY `fk_article_boite` (`ref_boite_de_vitesse`),
  KEY `fk_article_departement` (`ref_departement`),
  CONSTRAINT `fk_article_boite` FOREIGN KEY (`ref_boite_de_vitesse`) REFERENCES `boite_de_vitesse` (`id`),
  CONSTRAINT `fk_article_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  CONSTRAINT `fk_article_cepage` FOREIGN KEY (`ref_cepage`) REFERENCES `cepage` (`id_cepage`),
  CONSTRAINT `fk_article_essence` FOREIGN KEY (`essence_ou_diesel`) REFERENCES `type_essence` (`id`),
  CONSTRAINT `fk_article_etat` FOREIGN KEY (`ref_etat`) REFERENCES `etat` (`id`),
  CONSTRAINT `fk_article_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  CONSTRAINT `fk_article_region_vin` FOREIGN KEY (`ref_pays_region_vin`) REFERENCES `pays_region_vin` (`id_pays_region_vin`),
  CONSTRAINT `fk_article_type_ecran` FOREIGN KEY (`ref_type_ecran`) REFERENCES `type_ecran` (`id`),
  CONSTRAINT `fk_article_type_jeux` FOREIGN KEY (`ref_type_de_jeux`) REFERENCES `type_de_jeux` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,NULL,'test vin','Vin test','Test',12,NULL,'article_1.jpg','',0,1,27,2,1,1,'2026-10-13 00:00:00',9,NULL,NULL,0,4,3,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,37,0,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'75',NULL,88,1,2,2,NULL,NULL,NULL,1,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,NULL,'Karl Yala','Test Karl','Karl test',600,NULL,'article_2.png','',0,1,1,7,1,2,'2026-10-13 00:00:00',9,NULL,NULL,0,5,4,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,28,0,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'36',NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,'',NULL,'CHIC-2','pending',NULL,NULL,NULL,NULL),(3,NULL,'Gucci','Nouveau test','Couou',1212,NULL,'article_3.jpg','',0,1,1,35,1,2,'2026-10-14 00:00:00',9,NULL,NULL,0,5,4,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,11,0,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,'CHIC-3','pending',NULL,NULL,NULL,NULL),(4,NULL,'Omega','Test','Test',1212,NULL,'article_4.jpg','',0,1,35,999999,1,2,'2026-10-15 00:00:00',9,NULL,NULL,0,5,2,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,57,0,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,95,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,'',NULL,'CHIC-4','pending',NULL,NULL,NULL,NULL),(5,NULL,'Jack & Johns','Nouveau test','Avec enchere',9999,NULL,'article_5.webp','',0,1,2,2,1,2,'2026-10-15 00:00:00',9,NULL,NULL,1,1,3,'2026-10-15 14:28','2026-11-14 14:28',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,20,2,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'38',NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,'CHIC-5','pending',NULL,NULL,NULL,NULL),(6,NULL,'Test marque','Test sous cat','test sous cat',11212,NULL,'article_6.webp','',0,1,2,2,1,2,'2026-10-16 00:00:00',9,NULL,NULL,0,5,4,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,'fr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,100,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'38',NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,'CHIC-6','pending',NULL,NULL,NULL,NULL),(7,NULL,'','Villa','Test immo',1200000,NULL,'article_7.jpg','',0,1,25,304,1,1,'2026-10-16 00:00:00',9,NULL,NULL,0,3,3,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,10,900,'2000','2026',36,0,'fr',7,'Belfaux','rte de devine','1782',2,NULL,1,NULL,NULL,NULL,NULL,NULL,103,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,NULL,1,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_ip`
--

DROP TABLE IF EXISTS `article_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_ip` (
  `ref_article` int NOT NULL,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY (`ref_article`),
  CONSTRAINT `fk_article_ip_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_ip`
--

LOCK TABLES `article_ip` WRITE;
/*!40000 ALTER TABLE `article_ip` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_ip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_visite`
--

DROP TABLE IF EXISTS `article_visite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_visite` (
  `id_visite` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_personne` int NOT NULL,
  `nom` varchar(40) NOT NULL,
  `prenom` varchar(40) NOT NULL,
  `adresse` text,
  `ville` varchar(40) DEFAULT NULL,
  `npa` int DEFAULT NULL,
  `no_prive` varchar(40) DEFAULT NULL,
  `no_mobile` int DEFAULT NULL,
  `commentaire` text,
  `par_email` int DEFAULT NULL,
  `par_telephone` int DEFAULT NULL,
  `par_mobile` int DEFAULT NULL,
  `email` varchar(40) NOT NULL,
  `date_visite` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_visite`),
  KEY `fk_av_personne` (`ref_personne`),
  KEY `idx_av_article` (`ref_article`),
  CONSTRAINT `fk_av_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_av_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_visite`
--

LOCK TABLES `article_visite` WRITE;
/*!40000 ALTER TABLE `article_visite` DISABLE KEYS */;
INSERT INTO `article_visite` VALUES (1,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:05:40'),(2,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:13:05'),(3,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:13:05'),(4,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:19:07'),(5,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:19:07'),(6,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:24:35'),(7,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:24:35'),(8,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:30:08'),(9,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:30:09'),(10,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 14:31:54'),(11,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 15:57:56'),(12,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-13 15:57:56'),(13,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 10:44:08'),(14,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 10:44:08'),(15,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 10:45:55'),(16,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 10:46:20'),(17,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 10:46:20'),(18,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 11:04:05'),(19,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 13:01:16'),(20,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 13:01:16'),(21,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 13:01:46'),(22,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 13:01:46'),(23,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 13:21:37'),(24,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 13:21:38'),(25,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 16:34:37'),(26,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 16:34:37'),(27,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 16:34:41'),(28,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:51:40'),(29,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:51:40'),(30,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 16:52:49'),(31,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:53:32'),(32,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:53:32'),(33,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:53:52'),(34,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 16:53:52'),(35,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-14 18:18:33'),(36,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 19:42:44'),(37,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-14 19:42:45'),(38,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 09:59:02'),(39,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 09:59:33'),(40,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 09:59:33'),(41,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:01:34'),(42,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:01:34'),(43,2,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:09:11'),(44,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:44:34'),(45,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:44:34'),(46,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:45:49'),(47,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:45:49'),(48,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:51:10'),(49,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 11:51:10'),(50,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:52:34'),(51,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:52:35'),(52,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:52:45'),(53,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 11:52:56'),(54,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:18:24'),(55,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:18:24'),(56,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 12:28:01'),(57,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:49:35'),(58,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:49:36'),(59,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:49:54'),(60,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 12:52:34'),(61,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 13:06:39'),(62,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 13:12:27'),(63,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 13:12:27'),(64,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 13:12:40'),(65,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-15 13:12:40'),(66,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:09:49'),(67,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:09:50'),(68,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:14:02'),(69,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:14:02'),(70,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:14:30'),(71,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:15:10'),(72,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:17:51'),(73,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:18:23'),(74,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:21:27'),(75,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:22:53'),(76,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:24:17'),(77,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:24:35'),(78,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:24:44'),(79,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:24:58'),(80,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:25:16'),(81,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:27:47'),(82,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:28:23'),(83,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 15:31:03'),(84,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:05:13'),(85,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:19:39'),(86,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:19:40'),(87,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:19:59'),(88,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:19:59'),(89,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:20:02'),(90,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:20:04'),(91,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:20:10'),(92,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:20:10'),(93,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 17:20:46'),(94,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:03:24'),(95,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:03:27'),(96,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:08:05'),(97,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:08:05'),(98,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:08:25'),(99,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:08:25'),(100,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:10:11'),(101,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:10:11'),(102,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:11:14'),(103,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:11:15'),(104,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:21:01'),(105,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:21:01'),(106,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:30:05'),(107,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:30:07'),(108,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:34:18'),(109,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:34:20'),(110,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:34:49'),(111,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 18:34:49'),(112,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 19:08:20'),(113,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 19:08:20'),(114,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 19:08:59'),(115,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-15 19:08:59'),(116,4,4,'Plancherel','Laetitia',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'laetitia.plancherel98@gmail.com','2026-10-15 19:36:30'),(117,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:27'),(118,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:27'),(119,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:35'),(120,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:35'),(121,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:42'),(122,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:57:43'),(123,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:58:57'),(124,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 09:58:57'),(125,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:29:17'),(126,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:29:17'),(127,6,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:31:09'),(128,6,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:16'),(129,6,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:16'),(130,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:24'),(131,5,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:24'),(132,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:29'),(133,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:29'),(134,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:34'),(135,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:43:34'),(136,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 10:52:10'),(137,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 11:03:56'),(138,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 11:04:07'),(139,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 11:04:07'),(140,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 11:10:47'),(141,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 11:10:47'),(142,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:23'),(143,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:23'),(144,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:32'),(145,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:32'),(146,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:37'),(147,4,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:37'),(148,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:42'),(149,1,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:42'),(150,6,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:46'),(151,6,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:46'),(152,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:52'),(153,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:51:52'),(154,5,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 12:52:31'),(155,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 13:45:53'),(156,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:28:52'),(157,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:28:52'),(158,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:40:14'),(159,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:40:22'),(160,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:40:29'),(161,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 14:40:35'),(162,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 15:34:25'),(163,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 15:34:38'),(164,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 15:51:37'),(165,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 15:51:44'),(166,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 15:51:52'),(167,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:21:33'),(168,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:21:41'),(169,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:30:32'),(170,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:30:38'),(171,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:30:44'),(172,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:34:43'),(173,7,3,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'dev.jaquet.alexandre@gmail.com','2026-10-16 18:34:54'),(174,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 19:10:47'),(175,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 19:10:47'),(176,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-16 19:17:40'),(177,2,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 09:24:53'),(178,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 10:01:27'),(179,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 10:01:27'),(180,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 10:06:13'),(181,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 10:22:06'),(182,4,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 10:22:06'),(183,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 12:50:12'),(184,1,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 12:50:12'),(185,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 13:56:47'),(186,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 13:56:47'),(187,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 13:56:51'),(188,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 13:57:53'),(189,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 15:07:11'),(190,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 15:07:11'),(191,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 15:07:19'),(192,3,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 16:09:04'),(193,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 16:19:03'),(194,7,1,'Jaquet','Alexandre',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'alexjaquet@gmail.com','2026-10-17 16:19:04');
/*!40000 ALTER TABLE `article_visite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boite_de_vitesse`
--

DROP TABLE IF EXISTS `boite_de_vitesse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boite_de_vitesse` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boite_de_vitesse`
--

LOCK TABLES `boite_de_vitesse` WRITE;
/*!40000 ALTER TABLE `boite_de_vitesse` DISABLE KEYS */;
INSERT INTO `boite_de_vitesse` VALUES (1),(2),(3);
/*!40000 ALTER TABLE `boite_de_vitesse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boite_de_vitesse_libelle_langue`
--

DROP TABLE IF EXISTS `boite_de_vitesse_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boite_de_vitesse_libelle_langue` (
  `ref_boite_de_vitesse` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_boite_de_vitesse`,`ref_libelle`,`ref_langue`),
  KEY `fk_bdvll_libelle` (`ref_libelle`),
  KEY `fk_bdvll_langue` (`ref_langue`),
  CONSTRAINT `fk_bdvll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_bdvll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_bdvll_type` FOREIGN KEY (`ref_boite_de_vitesse`) REFERENCES `boite_de_vitesse` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boite_de_vitesse_libelle_langue`
--

LOCK TABLES `boite_de_vitesse_libelle_langue` WRITE;
/*!40000 ALTER TABLE `boite_de_vitesse_libelle_langue` DISABLE KEYS */;
INSERT INTO `boite_de_vitesse_libelle_langue` VALUES (1,10046,1),(2,10047,1),(1,30010046,3),(2,30010047,3),(1,40010046,2),(2,40010047,2);
/*!40000 ALTER TABLE `boite_de_vitesse_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boutique`
--

DROP TABLE IF EXISTS `boutique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boutique` (
  `id_boutique` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(40) NOT NULL,
  `date_creation` date NOT NULL,
  `ref_personne` int NOT NULL,
  `ref_main_categorie` int NOT NULL,
  `login` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_boutique`),
  KEY `fk_boutique_personne` (`ref_personne`),
  CONSTRAINT `fk_boutique_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boutique`
--

LOCK TABLES `boutique` WRITE;
/*!40000 ALTER TABLE `boutique` DISABLE KEYS */;
/*!40000 ALTER TABLE `boutique` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boutique_a_categorie`
--

DROP TABLE IF EXISTS `boutique_a_categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boutique_a_categorie` (
  `ref_boutique` int NOT NULL,
  `ref_categorie` int NOT NULL,
  PRIMARY KEY (`ref_boutique`,`ref_categorie`),
  KEY `fk_bac_categorie` (`ref_categorie`),
  CONSTRAINT `fk_bac_boutique` FOREIGN KEY (`ref_boutique`) REFERENCES `boutique` (`id_boutique`),
  CONSTRAINT `fk_bac_categorie_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie` (`id_categorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boutique_a_categorie`
--

LOCK TABLES `boutique_a_categorie` WRITE;
/*!40000 ALTER TABLE `boutique_a_categorie` DISABLE KEYS */;
/*!40000 ALTER TABLE `boutique_a_categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `canton_fr`
--

DROP TABLE IF EXISTS `canton_fr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `canton_fr` (
  `id_canton` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(60) NOT NULL,
  PRIMARY KEY (`id_canton`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `canton_fr`
--

LOCK TABLES `canton_fr` WRITE;
/*!40000 ALTER TABLE `canton_fr` DISABLE KEYS */;
INSERT INTO `canton_fr` VALUES (1,'Appenzell Rhodes-Extérieures - (AR)'),(2,'Appenzell Rhodes-Intérieures - (AI)'),(3,'Argovie - (AG)'),(4,'Berne - (BE)'),(5,'Bâle-Campagne - (BL)'),(6,'Bâle-Ville - (BS)'),(7,'Fribourg - (FR)'),(8,'Genève - (GE)'),(9,'Glaris - (GL)'),(10,'Grisons - (GR)'),(11,'Jura - (JU)'),(12,'Lucerne - (LU)'),(13,'Neuchâtel - (NE)'),(14,'\r\nNidwald - (NW)'),(15,'Obwald - (OW)'),(16,'Schaffhouse - (SH)'),(17,'Schwyz - (SZ)'),(18,'Soleure - (SO)'),(19,'St-Gall - (SG)'),(20,'Tessin - (TI)'),(21,'Thurgovie - (TG)'),(22,'Uri - (UR)'),(23,'Valais - (VS)'),(24,'Vaud - (VD)'),(25,'Zoug - (ZG)'),(26,'Zurich - (ZH)');
/*!40000 ALTER TABLE `canton_fr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorie`
--

DROP TABLE IF EXISTS `categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorie` (
  `id_categorie` int NOT NULL,
  PRIMARY KEY (`id_categorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorie`
--

LOCK TABLES `categorie` WRITE;
/*!40000 ALTER TABLE `categorie` DISABLE KEYS */;
INSERT INTO `categorie` VALUES (1),(2),(3),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(18),(19),(20),(21),(22),(23),(24),(25),(26),(27),(31),(32),(33),(34),(35),(36),(38),(41),(90),(91),(92),(93),(94),(95),(96),(97),(98),(99),(188),(22222);
/*!40000 ALTER TABLE `categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorie_libelle_langue`
--

DROP TABLE IF EXISTS `categorie_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorie_libelle_langue` (
  `ref_categorie` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_categorie`,`ref_langue`),
  KEY `fk_cat_ll_langue` (`ref_langue`),
  KEY `fk_cat_ll_libelle_categorie_libelle_langue` (`ref_libelle`),
  KEY `fk_cat_ll_langue_categorie_langue` (`ref_langue`),
  KEY `fk_cat_ll_libelle` (`ref_libelle`),
  CONSTRAINT `fk_catll_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie` (`id_categorie`),
  CONSTRAINT `fk_catll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorie_libelle_langue`
--

LOCK TABLES `categorie_libelle_langue` WRITE;
/*!40000 ALTER TABLE `categorie_libelle_langue` DISABLE KEYS */;
INSERT INTO `categorie_libelle_langue` VALUES (1,1,1),(2,2,1),(3,3,1),(5,5,1),(6,6,1),(7,7,1),(8,8,1),(9,9,1),(10,10,1),(11,11,1),(12,12,1),(13,13,1),(14,14,1),(15,15,1),(16,16,1),(93,17,1),(18,18,1),(19,19,1),(20,20,1),(21,21,1),(22,22,1),(23,23,1),(24,24,1),(25,25,1),(91,39,1),(26,55,1),(90,69,1),(188,188,1),(27,10000,1),(31,10020,1),(32,10021,1),(33,10024,1),(34,10029,1),(35,10030,1),(36,10031,1),(38,10040,1),(92,10049,1),(94,10054,1),(95,10055,1),(99,10056,1),(98,10057,1),(97,10058,1),(96,10059,1),(22222,22222,1),(1,10000001,3),(2,10000002,3),(3,10000003,3),(5,10000005,3),(6,10000006,3),(7,10000007,3),(9,10000009,3),(11,10000011,3),(12,10000012,3),(13,10000013,3),(14,10000014,3),(16,10000016,3),(93,10000017,3),(18,10000018,3),(19,10000019,3),(20,10000020,3),(21,10000021,3),(22,10000022,3),(23,10000023,3),(24,10000024,3),(25,10000025,3),(91,10000039,3),(188,10000188,3),(27,10010000,3),(31,10010020,3),(32,10010021,3),(33,10010024,3),(34,10010029,3),(35,10010030,3),(36,10010031,3),(38,10010040,3),(92,10010049,3),(94,10010054,3),(95,10010055,3),(99,10010056,3),(98,10010057,3),(97,10010058,3),(96,10010059,3),(22222,10022222,3),(1,20000001,2),(2,20000002,2),(3,20000003,2),(5,20000005,2),(6,20000006,2),(7,20000007,2),(9,20000009,2),(11,20000011,2),(12,20000012,2),(13,20000013,2),(14,20000014,2),(16,20000016,2),(93,20000017,2),(18,20000018,2),(19,20000019,2),(20,20000020,2),(21,20000021,2),(22,20000022,2),(23,20000023,2),(24,20000024,2),(25,20000025,2),(91,20000039,2),(188,20000188,2),(27,20010000,2),(31,20010020,2),(32,20010021,2),(33,20010024,2),(34,20010029,2),(35,20010030,2),(36,20010031,2),(38,20010040,2),(92,20010049,2),(94,20010054,2),(95,20010055,2),(99,20010056,2),(98,20010057,2),(97,20010058,2),(96,20010059,2),(22222,20022222,2),(26,90000001,3),(26,90000002,2);
/*!40000 ALTER TABLE `categorie_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cepage`
--

DROP TABLE IF EXISTS `cepage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cepage` (
  `id_cepage` int NOT NULL AUTO_INCREMENT,
  `ref_pays_region_vin` int NOT NULL,
  `ref_type_de_vin` int NOT NULL,
  `nom` varchar(50) NOT NULL,
  PRIMARY KEY (`id_cepage`),
  KEY `fk_cepage_region` (`ref_pays_region_vin`),
  KEY `fk_cepage_type` (`ref_type_de_vin`),
  CONSTRAINT `fk_cepage_region` FOREIGN KEY (`ref_pays_region_vin`) REFERENCES `pays_region_vin` (`id_pays_region_vin`)
) ENGINE=InnoDB AUTO_INCREMENT=543 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cepage`
--

LOCK TABLES `cepage` WRITE;
/*!40000 ALTER TABLE `cepage` DISABLE KEYS */;
INSERT INTO `cepage` VALUES (1,1,1,'Fendant'),(2,1,1,'Johannisberg'),(3,1,1,'Malvoisie (Pinot Gris),'),(4,1,1,'Ermitage (Marsanne Blanche),'),(5,1,1,'Amigne'),(6,1,1,'Petite Arvine'),(7,1,1,'Sylvaner'),(8,1,1,'Pinot Noir'),(9,1,1,'Gamay'),(10,1,1,'Syrah'),(11,1,1,'Humagne rouge'),(12,1,1,'Cornalin'),(13,2,1,'Chasselas'),(14,2,1,'Pinot blanc'),(15,2,1,'Chardonnay'),(16,2,1,'Pinot gris'),(17,2,1,'Riesling x Sylvaner'),(18,2,1,'Gamay'),(19,2,1,'Pinot noir'),(20,2,1,'Gamaret'),(21,2,1,'Garanoir'),(22,2,1,'Merlo'),(23,2,1,'Syrah'),(24,2,1,'Müller-Thurgau (Riesling x Syl'),(25,2,1,'Pinot gris (Grauburgunder)'),(26,2,1,'Pinot blanc (Weissburgunder)'),(27,2,1,'Gewürztramine'),(28,2,1,'Räuschling'),(29,2,1,'Chardonnay'),(30,2,1,'Pinot noir (Blauburgunder),'),(31,1,1,'Gamaret'),(32,1,1,'Garanoir'),(33,1,1,'Regent'),(34,1,1,'Chasselas'),(35,1,1,'Pinot blanc'),(36,1,1,'Aligoté'),(37,1,1,'Chardonnay'),(38,1,1,'Pinot gris'),(39,1,1,'Müller-Thurgau'),(40,1,1,'Sauvignon blan'),(41,1,1,'Gewürztraminer'),(42,1,1,'Pinot noir'),(43,1,1,'Gamay'),(44,1,1,'Gamaret'),(45,1,1,'Garanoir'),(46,1,1,'Merlot'),(47,1,1,'Syrah'),(48,5,1,'Merlot'),(49,5,1,'Chardonnay'),(50,5,1,'Chasselas'),(51,5,1,'Sauvignon'),(52,5,1,'Sémillon'),(53,5,1,'Merlot'),(54,5,1,'Cabernet Sauvignon'),(55,5,1,'Gamaret'),(56,67,1,'Chasselas'),(57,1,1,'Sauvignon blanc'),(58,1,1,'Chardonnay'),(59,1,1,'Pinot gris'),(60,1,1,'Riesling x Sylvaner'),(61,1,1,'Gewürztraminer'),(62,1,1,'Riesling x Sylvaner (Müller-Thurgau)'),(63,1,1,'Pinot noir'),(64,1,1,'Gamaret'),(65,1,1,'Garanoir'),(66,1,1,'Chasselas'),(67,1,1,'Sauvignon blanc'),(68,1,1,'Chardonnay'),(69,1,1,'Pinot gris'),(70,1,1,'Riesling'),(71,1,1,'Gewürztraminer'),(72,1,1,'Riesling x Sylvaner (Müller-Thurgau)'),(73,1,1,'Pinot noir'),(74,1,1,'Gamaret'),(75,1,1,'Garanoir'),(76,1,1,'Chasselas 63%'),(77,1,1,'Pinot gris'),(78,1,1,'Traminer '),(79,1,1,'Pinot noir 22 %'),(80,1,1,'Gamaret'),(81,1,1,'Garanoir'),(82,2,1,'Alsace Chasselas (Gutedel)'),(83,2,1,'Alsace Edelzwicker'),(84,2,1,'Alsace Grand Cru'),(85,2,1,'Alsace Grand Cru Altenberg de Bergbieten'),(86,2,1,'Alsace Grand Cru Altenberg de Bergheim'),(87,2,1,'Alsace Grand Cru Altenberg de Wolxheim'),(88,2,1,'Alsace Grand Cru Brand'),(89,2,1,'Alsace Grand Cru Bruderthal'),(90,2,1,'Alsace Grand Cru Eichberg'),(91,2,1,'Alsace Grand Cru Engelberg'),(92,2,1,'Alsace Grand Cru Florimont'),(93,2,1,'Alsace Grand Cru Franckstein'),(94,2,1,'Alsace Grand Cru Froehn'),(95,2,1,'Alsace Grand Cru Furstentum'),(96,2,1,'Alsace Grand Cru Geisberg'),(97,2,1,'Alsace Grand Cru Gloeckelberg'),(98,2,1,'Alsace Grand Cru Goldert'),(99,2,1,'Alsace Grand Cru Hatschbourg'),(100,2,1,'Alsace Grand Cru Hengst'),(101,2,1,'Alsace Grand Cru Kanzlerberg'),(102,2,1,'Alsace Grand Cru Kastelberg'),(103,2,1,'Alsace Grand Cru Kessler'),(104,2,1,'Alsace Grand Cru Kirchberg de Barr'),(105,2,1,'Alsace Grand Cru Kirchberg de Ribeauvill'),(106,2,1,'Alsace Grand Cru Kitterlé'),(107,2,1,'Alsace Grand Cru Mambourg'),(108,2,1,'Alsace Grand Cru Mandelberg'),(109,2,1,'Alsace Grand Cru Marckrain'),(110,2,1,'Alsace Grand Cru Moenchberg'),(111,2,1,'Alsace Grand Cru Muenchberg'),(112,2,1,'Alsace Grand Cru Ollwiller'),(113,2,1,'Alsace Grand Cru Osterberg'),(114,2,1,'Alsace Grand Cru Pfersigberg'),(115,2,1,'Alsace Grand Cru Pfingstberg'),(116,2,1,'Alsace Grand Cru Praelatenberg'),(117,2,1,'Alsace Grand Cru Rangen'),(118,2,1,'Alsace Grand Cru Rosacker'),(119,2,1,'Alsace Grand Cru Saering'),(120,2,1,'Alsace Grand Cru Schoenenbourg'),(121,2,1,'Alsace Grand Cru Scholssberg'),(122,2,1,'Alsace Grand Cru Sommerberg'),(123,2,1,'Alsace Grand Cru Sonnenglanz'),(124,2,1,'Alsace Grand Cru Spiegel'),(125,2,1,'Alsace Grand Cru Sporen'),(126,2,1,'Alsace Grand Cru Steinert'),(127,2,1,'Alsace Grand Cru Steingrubler'),(128,2,1,'Alsace Grand Cru Steinklotz'),(129,2,1,'Alsace Grand Cru Vorbourg'),(130,2,1,'Alsace Grand Cru Wiebelberg'),(131,2,1,'Alsace Grand Cru Wineck-Schlossberg'),(132,2,1,'Alsace Grand Cru Winzenberg'),(133,2,1,'Alsace Grand Cru Zinnkoepflé'),(134,2,1,'Alsace Grand Cru Zotzenberg'),(135,2,1,'Alsace Muscat'),(136,2,1,'Alsace Pinot (Klevner)'),(137,2,1,'Alsace Pinot Noir'),(138,2,1,'Alsace Riesling'),(139,2,1,'Alsace Sylvaner'),(140,2,1,'Alsace Tokay-Pinot Gris'),(141,2,1,'Crémant d\'Alsace'),(142,2,1,'Brouilly'),(143,2,1,'Chénas'),(144,2,1,'Chiroubles'),(145,2,1,'Côte de Brouilly'),(146,2,1,'Fleurie'),(147,2,1,'Juliénas'),(148,2,1,'Morgon'),(149,2,1,'Moulin-à-Vent'),(150,2,1,'Régnié'),(151,2,1,'Saint-Amour'),(152,2,1,'Bordeaux Haut-Benauge'),(153,2,1,'Côtes de Bordeaux St-Macaire'),(154,2,1,'Entre-Deux-Mers'),(155,2,1,'Entre-Deux-Mers Haut-Benauge'),(156,2,1,'Graves de Vayres'),(157,2,1,'Loupiac'),(158,2,1,'Premières Côtes de Bordeaux'),(159,2,1,'Ste-Croix du Mont'),(160,2,1,'Ste-Foy Bordeaux'),(161,2,1,'Barsac'),(162,2,1,'Cadillac'),(163,2,1,'Cérons'),(164,2,1,'Graves'),(165,2,1,'Graves Supérieures'),(166,2,1,'Pessac-Léongnan'),(167,2,1,'Sauternes'),(168,2,1,'Blayais'),(169,2,1,'Bordeaux Côtes de francs'),(170,2,1,'Canon Fronsac'),(171,2,1,'Côtes de Blaye'),(172,2,1,'Côtes de Bourg'),(173,2,1,'Côtes de Castillon'),(174,2,1,'Fronsac'),(175,2,1,'Lalande de Pomerol'),(176,2,1,'Lussac St-Emilion'),(177,2,1,'Montagne St-Emilion'),(178,2,1,'Néac'),(179,2,1,'Pomerol'),(180,2,1,'Premières Côtes de Blaye'),(181,2,1,'Puisseguin St-Emilion'),(182,2,1,'St-Emilion'),(183,2,1,'St-Emilion Grand Cru'),(184,2,1,'St-Georges St-Emilion'),(185,2,1,'Haut-Médoc'),(186,2,1,'Listrac-Médoc'),(187,2,1,'Margaux'),(188,2,1,'Médoc'),(189,2,1,'Moulis-en-Médoc'),(190,2,1,'Pauillac'),(191,2,1,'St-Estèphe'),(192,2,1,'St-Julien'),(193,13,1,'Bourgogne Côtes d\'Auxerre'),(194,13,1,'Bourgogne-Irancy'),(195,13,1,'Chablis'),(196,13,1,'Chablis Grand Cru'),(197,13,1,'Chablis Premier Cru'),(198,13,1,'Petit Chablis'),(199,13,1,'Sauvignon de St-Bris'),(200,13,1,'Bourgogne Côte Chalonnaise'),(201,13,1,'Bouzeron'),(202,13,1,'Givry'),(203,13,1,'Givry Premier Cru'),(204,13,1,'Mercurey'),(205,13,1,'Mercurey Premier Cru'),(206,13,1,'Montagny'),(207,13,1,'Montagny Premier Cru'),(208,13,1,'Rully'),(209,13,1,'Rully Premier Cru'),(210,13,1,'Aloxe-Corton'),(211,13,1,'Aloxe-Corton Premier Cru'),(212,13,1,'Auxey-Duresses'),(213,13,1,'Auxey-Duresses Premier Cru'),(214,13,1,'Batard-Montrachet'),(215,13,1,'Beaune'),(216,13,5,''),(217,13,1,'Beaune'),(218,13,1,'Beaune Premier Cru'),(219,13,1,'Bienvenues Bâtard-Montrachet'),(220,13,1,'Blagny'),(221,13,1,'Blagny Premier Cru'),(222,13,1,'Bourgogne Hautes-Côtes de Beaune'),(223,13,1,'Charlemagne'),(224,13,1,'Chassagne-Montrachet'),(225,13,1,'Chassagne-Montrachet Premier Cru'),(226,13,1,'Chevalier-Montrachet'),(227,13,1,'Chorey-lès-Beaune'),(228,13,1,'Corton'),(229,13,1,'Corton-Charlemagne'),(230,13,1,'Côtes de Beaune'),(231,13,1,'Criots Bâtard-Montrachet'),(232,13,1,'Ladoix'),(233,13,1,'Ladoix Premier Cru'),(234,13,1,'Ladoix-Serrigny'),(235,13,1,'Maranges'),(236,13,1,'Maranges Premier Cru'),(237,13,1,'Meursault'),(238,13,1,'Meursault Premier Cru'),(239,13,1,'Monthélie'),(240,13,1,'Monthélie Premier Cru'),(241,13,1,'Montrachet'),(242,13,1,'Pernand-Vergelesses'),(243,13,1,'Pernand-Vergelesses Premier Cru'),(244,13,1,'Pommard'),(245,13,1,'Pommard Premier Cru'),(246,13,1,'Puligny-Montrachet'),(247,13,1,'Puligny-Montrachet Premier Cru'),(248,13,1,'Saint-Aubin'),(249,13,1,'Saint-Romain'),(250,13,1,'Santenay'),(251,13,1,'Santenay Premier Cru'),(252,13,1,'Savigny-lès-Beaune'),(253,13,1,'Savigny-lès-Beaune Premier Cru'),(254,13,1,'St Aubin Premier Cru'),(255,13,1,'Volnay'),(256,13,1,'Volnay Premier Cru'),(257,13,1,'Volnay Santenots'),(258,13,1,'Bonnes Mares'),(259,13,1,'Bourgogne Hautes-Côtes de Nuits'),(260,13,1,'Chambertin'),(261,13,1,'Chambertin-Clos de Bèze'),(262,13,1,'Chambolle-Musigny'),(263,13,1,'Chambolle-Musigny Premier Cru'),(264,13,1,'Chapelle-Chambertin'),(265,13,1,'Charmes-Chambertin'),(266,13,1,'Clos de la Roche'),(267,13,1,'Clos des Lambrays'),(268,13,1,'Clos de Tart'),(269,13,1,'Clos de Vougeot'),(270,13,1,'Clos Saint-Denis'),(271,13,1,'Echezeaux'),(272,13,1,'Fixin'),(273,13,1,'Fixin Premier Cru'),(274,13,1,'Gevrey-Chambertin'),(275,13,1,'Gevrey-Chambertin Premier Cru'),(276,13,1,'Grands-Echezeaux'),(277,13,1,'Griotte-Chambertin'),(278,13,1,'La Grande Rue'),(279,13,1,'La Romanée'),(280,13,1,'La Tâche'),(281,13,1,'Latricières-Chambertin'),(282,13,1,'Marsannay'),(283,13,1,'Marsannay Rosé'),(284,13,1,'Mazis-Chambertin'),(285,13,1,'Mazoyères-Chambertin'),(286,13,1,'Morey-St-Denis'),(287,13,1,'Morey-St-Denis Premier Cru'),(288,13,1,'Musigny'),(289,13,1,'Nuits Premier Cru ou Nuits-St-Georges Pr'),(290,13,1,'Nuits-Saint-Georges'),(291,13,1,'Richebourg'),(292,13,1,'Romanée-Conti'),(293,13,1,'Romanée-Saint-Vivant'),(294,13,1,'Ruchottes-Chambertin'),(295,13,1,'Vosne-Romanée'),(296,13,1,'Vosne-Romanée Premier Cru'),(297,13,1,'Vougeot'),(298,13,1,'Vougeot Premier Cru'),(299,13,1,'Bourgogne Côtes du Couchois'),(300,13,1,'Mâcon'),(301,13,1,'Mâcon-Supérieur'),(302,13,1,'Mâcon-Villages'),(303,13,1,'Pouilly-Fuissé'),(304,13,1,'Pouilly-Loché'),(305,13,1,'Pouilly-Vinzelles'),(306,13,1,'Saint-Véran'),(307,14,1,'Roussette de Bugey'),(308,14,1,'Roussette de Bugey Anglefort'),(309,14,1,'Roussette de Bugey Arbignieu'),(310,14,1,'Roussette de Bugey Montagnieu'),(311,14,1,'Roussette de Bugey Virieu-le-Grand'),(312,14,1,'Roussettte de Bugey Lagnieu'),(313,14,1,'Vin du Buget Montagnieu'),(314,14,1,'Vin du Bugey'),(315,14,1,'Vin du Bugey Cerdon'),(316,14,1,'Vin du Bugey Cerdon Mousseux'),(317,14,1,'Vin du Bugey Cerdon Pétillant'),(318,14,1,'Vin du Bugey Machuraz'),(319,14,1,'Vin du Bugey Manicle'),(320,14,1,'Vin du Bugey Montagnieu'),(321,14,1,'Vin du Bugey Mousseux'),(322,14,1,'Vin du Bugey Pétillant'),(323,14,1,'Vin du Bugey Virieu-le-Grand'),(324,15,1,'Champagne'),(325,15,1,'Coteaux Champenois'),(326,15,1,'Coteaux Champenois'),(327,15,1,'Rosé des Riceys'),(328,25,1,'Ajaccio'),(329,25,1,'Muscat du Cap Corse'),(330,25,1,'Patrimonio'),(331,25,1,'Vin de Corse'),(332,25,1,'Vin de Corse Calvi'),(333,25,1,'Vin de Corse Coteaux du Cap Corse'),(334,25,1,'Vin de Corse Figari'),(335,25,1,'Vin de Corse Porto-Vecchio'),(336,25,1,'Vin de Corse Sartène'),(337,26,1,'Côtes de Toul'),(338,26,1,'Vin de Moselle'),(339,27,1,'Arbois'),(340,27,1,'Arbois Mousseux'),(341,27,1,'Arbois Pupillin'),(342,27,1,'Château-Chalon'),(343,27,1,'Côtes du Jura'),(344,27,1,'Côtes du Jura Mousseux'),(345,27,1,'Crémant du Jura'),(346,27,1,'L\'Etoile'),(347,27,1,'L\'Etoile Mousseux'),(348,0,0,''),(349,16,1,'Blanquette de Limoux'),(350,16,1,'Blanquette méthode ancéstrale'),(351,16,1,'Clairette du Languedoc'),(352,16,1,'Corbières'),(353,16,1,'Coteaux de la Méjanelle ( La Méjanelle)'),(354,16,1,'Coteaux de Languedoc St-Saturnin'),(355,16,1,'Coteaux de St-Cristol'),(356,16,1,'Coteaux de Vérargues'),(357,16,1,'Coteaux du Languedoc'),(358,16,1,'Coteaux du Languedoc Cabrières'),(359,16,1,'Coteaux du Languedoc La Clape'),(360,16,1,'Coteaux du Languedoc Montpeyroux'),(361,16,1,'Coteaux du Languedoc Picpoul-de-Pinet'),(362,16,1,'Coteaux du Languedoc Pic-St-Loup'),(363,16,1,'Coteaux du Languedoc Quatourze'),(364,16,1,'Coteaux du Languedoc St-Drézéry'),(365,16,1,'Coteaux du Languedoc St-Georges-d\'Orques'),(366,16,1,'Côtes de la Malapère'),(367,16,1,'Côtes de Millau'),(368,16,1,'Côtes du Cabardès et de l\'Orbiel'),(369,16,1,'Crémant de Limoux'),(370,16,1,'Faugères'),(371,16,1,'Fitou'),(372,16,1,'Limoux'),(373,16,1,'Minervois'),(374,16,1,'St-Chinian'),(375,29,1,'Anjou'),(376,29,1,'Anjou Rive Droite'),(377,29,1,'Anjou Rive Gauche'),(378,29,1,'Auvergne'),(379,29,1,'Centre'),(380,29,1,'Pays Nantais'),(381,29,1,'Poitou'),(382,29,1,'Saumurois'),(383,29,1,'Touraine'),(384,29,1,'Vendée'),(385,31,1,'Coteaux du Lyonnais'),(386,17,1,'Bandol'),(387,17,1,'Baux de Provence'),(388,17,1,'Bellet'),(389,17,1,'Cassis'),(390,17,1,'Coteaux d\'Aix-en-Provence'),(391,17,1,'Coteaux du Pierrevert'),(392,17,1,'Coteaux Varois'),(393,17,1,'Côtes de Provence'),(394,17,1,'Palette'),(395,20,1,'châteaumeillant'),(396,20,1,'Châtillon-en-Diois'),(397,20,1,'Clairette de Die'),(398,20,1,'Coteaux de Die'),(399,20,1,'Crémant de Die'),(400,20,1,'Châteauneuf-du-Pape'),(401,20,1,'Clairette de Bellegarde'),(402,20,1,'Costières de Nîmes'),(403,20,1,'Coteaux du Tricastin'),(404,20,1,'Côtes du Lubéron'),(405,20,1,'Côtes du Rhône Beaumes-de-Venise'),(406,20,1,'Côtes du Rhône Cairanne'),(407,20,1,'Côtes du Rhône Chusclan'),(408,20,1,'Côtes du Rhône Laudun'),(409,20,1,'Côtes du Rhône Roaix'),(410,20,1,'Côtes du Rhône Rochegude'),(411,20,1,'Côtes du Rhône Sablet'),(412,20,1,'Côtes du Rhône Séguret'),(413,20,1,'Côtes du Rhônes Rousset-Les-Vignes'),(414,20,1,'Côtes du Rhône St-Gervais'),(415,20,1,'Côtes du Rhône St-Maurice-sur-Eygues'),(416,20,1,'Côtes du Rhône St-Pantaléon-les-Vignes'),(417,20,1,'Côtes du Rhône Valréas'),(418,20,1,'Côtes du Rhône-Villages'),(419,20,1,'Côtes du Rhône Vinsobres'),(420,20,1,'Côtes du Rhône Visan'),(421,20,1,'Côtes du Ventoux'),(422,30,1,'Côtes du Vivarais'),(423,20,1,'Côtes du Vivarais Orgnac l\'Aven'),(424,20,1,'Côtes du Vivarais St-Montant'),(425,20,1,'Côtes du Vivarais St-Remèze'),(426,20,1,'Gigondas'),(427,20,1,'Lirac'),(428,20,1,'Muscat de Beaumes-de-Venise'),(429,20,1,'Rasteau'),(430,20,1,'Tavel'),(431,20,1,'Vacqueyras'),(432,20,1,'Vacqueyras'),(433,20,1,'Château Grillet'),(434,20,1,'Condrieu'),(435,20,1,'Cornas'),(436,20,1,'Côte Rôtie'),(437,20,1,'St-joseph'),(438,20,1,'St-Péray'),(439,20,1,'St-Péray Mousseux'),(440,20,1,'Crozes-Hermitage'),(441,20,1,'Hermitage'),(442,20,1,'Côte du Rhône'),(443,33,1,'Banyuls'),(444,33,1,'Collioure'),(445,33,1,'Côtes du Roussillon'),(446,33,1,'Côtes du Roussillon-villages'),(447,33,1,'Côtes du Roussillon-villages Caramany'),(448,33,1,'Côtes du Roussillon-Villages Latour-de-F'),(449,33,1,'Maury'),(450,33,1,'Muscat de Rivesaltes'),(451,33,1,'Rivesaltes'),(452,34,1,'Crépy'),(453,34,1,'Mousseux de Savoie'),(454,34,1,'Pétillant de Savoie'),(455,34,1,'Roussette de Savoie'),(456,34,1,'Roussette de Savoie Frangy'),(457,34,1,'Roussette de Savoie Marestel'),(458,34,1,'Roussette de Savoie Marestel-Altesse'),(459,34,1,'Roussette de Savoie Monterminod'),(460,34,1,'Roussette de Savoie Monthoux'),(461,34,1,'Seyssel'),(462,34,1,'Seyssel Mousseux'),(463,34,1,'Vin de Savoie'),(464,34,1,'Vin de Savoie Abymes'),(465,34,1,'Vin de Savoie Apremont'),(466,34,1,'Vin de Savoie Arbin'),(467,34,1,'Vin de Savoie Ayze'),(468,34,1,'Vin de Savoie Ayze Charpignat'),(469,34,1,'Vin de Savoie Ayze Mousseux'),(470,34,1,'Vin de Savoie Ayze Pétillant'),(471,34,1,'Vin de Savoie Bergeron'),(472,34,1,'Vin de Savoie Chautagne'),(473,34,1,'Vin de Savoie Chignin'),(474,34,1,'Vin de Savoie Chignin-Bergeron'),(475,34,1,'Vin de Savoie Cruet'),(476,34,1,'Vin de Savoie Jongieux'),(477,34,1,'Vin de Savoie Marignan'),(478,34,1,'Vin de Savoie Marin'),(479,34,1,'Vin de Savoie Montmélian'),(480,34,1,'Vin de Savoie Ripaille'),(481,34,1,'Vin de Savoie Ste-Marie-d\'Alloix'),(482,34,1,'Vin de Savoie St-Jean-de-la-Porte'),(483,34,1,'Vin de Savoie St-Jeoire Prieuré'),(484,18,1,'Marcillac'),(485,18,1,'Vin d\'Entraygues et de Fel'),(486,18,1,'Vins d\'Estaing'),(487,18,1,'Bergerac'),(488,18,1,'Bergerac Sec'),(489,18,1,'Côtes de Bergerac'),(490,18,1,'côtes de Bergerac Moelleux'),(491,18,1,'Côtes de Montravel'),(492,18,1,'Haut-Montravel'),(493,18,1,'Monbazillac'),(494,18,1,'Montravel'),(495,18,1,'Pécharmant'),(496,18,1,'Rosette'),(497,18,1,'Saussignac'),(498,18,1,'Béarn'),(499,18,1,'Côtes de St-Mont'),(500,18,1,'Côtes du Brulhois'),(501,18,1,'Madiran'),(502,18,1,'Pacherenc du Vic-Bilh'),(503,18,1,'Tursan'),(504,18,1,'Bergerac'),(505,18,1,'Bergerac Sec'),(506,18,1,'Côtes de Bergerac'),(507,18,1,'côtes de Bergerac Moelleux'),(508,18,1,'Côtes de Montravel'),(509,18,1,'Haut-Montravel'),(510,18,1,'Monbazillac'),(511,18,1,'Montravel'),(512,18,1,'Pécharmant'),(513,18,1,'Rosette'),(514,18,1,'Saussignac'),(515,18,1,'Côtes du Frontonnais'),(516,18,1,'Côtes du Frontonnais-Fronton'),(517,18,1,'Côtes du Frontonnais-Villaudric'),(518,18,1,'Béarn'),(520,18,1,'Madiran'),(521,18,1,'Pacherenc du Vic-Bilh'),(522,18,1,'Côtes de St-Mont'),(523,18,1,'Tursan'),(524,18,1,'Cahors'),(525,18,1,'Côtes de Buzet (Garonne)'),(526,18,1,'Côtes de Duras'),(527,18,1,'Côtes de Marmandais'),(528,18,1,'Côtes du Brulhois'),(529,18,1,'Béarn'),(530,18,1,'Béarn Bellocq'),(531,18,1,'Irouléguy'),(532,18,1,'Jurançon'),(533,18,1,'Jurançon Sec'),(534,18,1,'Madiran'),(535,18,1,'Pacherenc du Vic-Bilh'),(536,18,1,'Côtes de Millau AOVDQS'),(537,18,1,'Gaillac'),(538,18,1,'Gaillac Doux'),(539,18,1,'Gaillac Mousseux'),(540,18,1,'Côtes du Brulhois'),(541,18,1,'Côtes du Frontonnais'),(542,18,1,'Lavilledieu AOVDQS');
/*!40000 ALTER TABLE `cepage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commande`
--

DROP TABLE IF EXISTS `commande`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commande` (
  `id_commande` int NOT NULL AUTO_INCREMENT,
  `session_id` char(32) NOT NULL,
  `client_ref` int NOT NULL,
  `date` date NOT NULL,
  `ref_mode_de_payement` int NOT NULL,
  `date_payement` date DEFAULT NULL,
  `ref_vendeur` int NOT NULL,
  `ref_statut` int NOT NULL,
  `ref_mode_de_livraison` int NOT NULL,
  PRIMARY KEY (`id_commande`),
  KEY `fk_commande_client` (`client_ref`),
  KEY `fk_commande_vendeur` (`ref_vendeur`),
  KEY `fk_commande_payement` (`ref_mode_de_payement`),
  KEY `fk_commande_livraison` (`ref_mode_de_livraison`),
  CONSTRAINT `fk_commande_client` FOREIGN KEY (`client_ref`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_commande_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  CONSTRAINT `fk_commande_payement` FOREIGN KEY (`ref_mode_de_payement`) REFERENCES `mode_de_payement` (`id`),
  CONSTRAINT `fk_commande_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commande`
--

LOCK TABLES `commande` WRITE;
/*!40000 ALTER TABLE `commande` DISABLE KEYS */;
/*!40000 ALTER TABLE `commande` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commande_article`
--

DROP TABLE IF EXISTS `commande_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commande_article` (
  `ref_commande` int NOT NULL,
  `ref_article` int NOT NULL,
  `date_payement` date NOT NULL,
  PRIMARY KEY (`ref_commande`,`ref_article`,`date_payement`),
  KEY `fk_ca_article` (`ref_article`),
  CONSTRAINT `fk_ca_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_ca_commande` FOREIGN KEY (`ref_commande`) REFERENCES `commande` (`id_commande`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commande_article`
--

LOCK TABLES `commande_article` WRITE;
/*!40000 ALTER TABLE `commande_article` DISABLE KEYS */;
/*!40000 ALTER TABLE `commande_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commentaire`
--

DROP TABLE IF EXISTS `commentaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commentaire` (
  `id_commentaire` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_emetteur` int NOT NULL,
  `question` varchar(500) NOT NULL,
  `texte` text NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id_commentaire`),
  KEY `fk_commentaire_article` (`ref_article`),
  KEY `fk_commentaire_emetteur` (`ref_emetteur`),
  CONSTRAINT `fk_commentaire_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_commentaire_emetteur` FOREIGN KEY (`ref_emetteur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commentaire`
--

LOCK TABLES `commentaire` WRITE;
/*!40000 ALTER TABLE `commentaire` DISABLE KEYS */;
INSERT INTO `commentaire` VALUES (1,4,1,'Test','Test','2026-10-15 11:51:32'),(2,4,1,'Coucou','Test','2026-10-15 15:20:53'),(3,7,1,'New','New','2026-10-17 13:57:40');
/*!40000 ALTER TABLE `commentaire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condition_livraison`
--

DROP TABLE IF EXISTS `condition_livraison`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condition_livraison` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condition_livraison`
--

LOCK TABLES `condition_livraison` WRITE;
/*!40000 ALTER TABLE `condition_livraison` DISABLE KEYS */;
INSERT INTO `condition_livraison` VALUES (1),(2),(3),(4);
/*!40000 ALTER TABLE `condition_livraison` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condition_livraison_libelle_langue`
--

DROP TABLE IF EXISTS `condition_livraison_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condition_livraison_libelle_langue` (
  `ref_mode_de_livraison` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  `frais` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ref_mode_de_livraison`,`ref_libelle`,`ref_langue`),
  KEY `fk_cll_libelle` (`ref_libelle`),
  KEY `fk_cll_langue` (`ref_langue`),
  CONSTRAINT `fk_cll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_cll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_cll_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condition_livraison_libelle_langue`
--

LOCK TABLES `condition_livraison_libelle_langue` WRITE;
/*!40000 ALTER TABLE `condition_livraison_libelle_langue` DISABLE KEYS */;
INSERT INTO `condition_livraison_libelle_langue` VALUES (1,10200,1,0),(1,30010200,3,0),(1,40010200,2,0),(2,10201,1,0),(2,30010201,3,0),(2,40010201,2,0),(3,10202,1,0),(3,30010202,3,0),(3,40010202,2,0),(4,10203,1,0),(4,30010203,3,0),(4,40010203,2,0);
/*!40000 ALTER TABLE `condition_livraison_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condition_payement_libelle_langue`
--

DROP TABLE IF EXISTS `condition_payement_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condition_payement_libelle_langue` (
  `ref_condition_payement` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_condition_payement`,`ref_langue`),
  KEY `fk_cpl_libelle` (`ref_libelle`),
  KEY `fk_cpl_langue` (`ref_langue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condition_payement_libelle_langue`
--

LOCK TABLES `condition_payement_libelle_langue` WRITE;
/*!40000 ALTER TABLE `condition_payement_libelle_langue` DISABLE KEYS */;
INSERT INTO `condition_payement_libelle_langue` VALUES (1,10100,1),(2,10101,1),(3,10102,1),(4,10103,1),(5,1000123,1),(1,30010100,3),(2,30010101,3),(3,30010102,3),(4,30010103,3),(5,31000123,3),(1,40010100,2),(2,40010101,2),(3,40010102,2),(4,40010103,2),(5,41000123,2);
/*!40000 ALTER TABLE `condition_payement_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condition_payement_ref`
--

DROP TABLE IF EXISTS `condition_payement_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condition_payement_ref` (
  `ref_condition_payement` int NOT NULL,
  PRIMARY KEY (`ref_condition_payement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condition_payement_ref`
--

LOCK TABLES `condition_payement_ref` WRITE;
/*!40000 ALTER TABLE `condition_payement_ref` DISABLE KEYS */;
INSERT INTO `condition_payement_ref` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `condition_payement_ref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `demande_visite`
--

DROP TABLE IF EXISTS `demande_visite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demande_visite` (
  `id_demande` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(40) NOT NULL,
  `prenom` varchar(40) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `tel_mobile` varchar(30) NOT NULL,
  `ref_article` int NOT NULL,
  `ref_vendeur` int NOT NULL,
  `commentaire` text NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `date_visite` datetime DEFAULT NULL,
  `date_visite_fin` datetime DEFAULT NULL,
  PRIMARY KEY (`id_demande`),
  KEY `fk_dv_article` (`ref_article`),
  KEY `fk_dv_vendeur` (`ref_vendeur`),
  CONSTRAINT `fk_dv_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_dv_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demande_visite`
--

LOCK TABLES `demande_visite` WRITE;
/*!40000 ALTER TABLE `demande_visite` DISABLE KEYS */;
INSERT INTO `demande_visite` VALUES (1,'Jaquet','Alexandre','route d\'Autafond','+410786665544',46,1,'test','alexjaquet@gmail.com','2026-03-31 09:00:00',NULL),(2,'Jaquet','Alexandre','route d\'Autafond','+410787776655',46,1,'Test commentaire','alexjaquet@gmail.com','2026-03-31 18:00:00',NULL),(3,'Jaquet','Alexandre','route d\'Autafond','+410788889900',46,1,'','alexjaquet@gmail.com','2026-03-31 17:30:00',NULL),(4,'Jaquet Demande de visite','Test','route d\'Autafond','+41',46,1,'Commentaire de test','alexjaquet@gmail.com','2026-04-21 09:00:00',NULL),(5,'Dupont','Jean','route d\'Autafond 10','+410786664433',46,1,'Test','alexjaquet@gmail.com','2026-04-21 08:30:00',NULL),(6,'Dupont','Pierre','route d\'Autafond','+419999877',46,1,'Commentare joli maison ^^','alexjaquet@gmail.com','2026-04-18 09:00:00',NULL),(7,'Jaquet','Alexandre','route d\'Autafond','+410787776655',46,1,'Demande de rendez-vous.','alexjaquet@gmail.com','2026-04-28 15:00:00','2026-04-28 16:00:00'),(8,'Dupont','Claude','route d\'Autafond','+41',46,1,'Joli maison possibilité de piscine ?','alexjaquet@gmail.com','2026-04-03 14:30:00','2026-04-03 15:30:00'),(9,'Jaquet','Alexandre','route de devin','+41000000000',7,1,'Test commentaire','alexjaquet@gmail.com','2026-10-16 16:00:00','2026-10-16 17:00:00'),(10,'Jaquet','Alexandre','route du Devin','+41000000000',7,1,'Test comment','alexjaquet@gmail.com','2026-10-17 16:00:00','2026-10-17 17:00:00');
/*!40000 ALTER TABLE `demande_visite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departement`
--

DROP TABLE IF EXISTS `departement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departement` (
  `id_departement` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `code` varchar(3) NOT NULL,
  PRIMARY KEY (`id_departement`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departement`
--

LOCK TABLES `departement` WRITE;
/*!40000 ALTER TABLE `departement` DISABLE KEYS */;
/*!40000 ALTER TABLE `departement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `depot`
--

DROP TABLE IF EXISTS `depot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `depot` (
  `id_depot` int NOT NULL,
  `nom` varchar(50) NOT NULL,
  `adresse` varchar(300) NOT NULL,
  `ville` varchar(150) NOT NULL,
  `npa` int NOT NULL,
  `ref_pays` int NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `ref_responsable` int NOT NULL,
  PRIMARY KEY (`id_depot`,`ref_responsable`),
  KEY `fk_depot_responsable` (`ref_responsable`),
  CONSTRAINT `fk_depot_responsable` FOREIGN KEY (`ref_responsable`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `depot`
--

LOCK TABLES `depot` WRITE;
/*!40000 ALTER TABLE `depot` DISABLE KEYS */;
/*!40000 ALTER TABLE `depot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enchere`
--

DROP TABLE IF EXISTS `enchere`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enchere` (
  `id_enchere` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_enchereur` int NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  `date_enchere` datetime NOT NULL,
  PRIMARY KEY (`id_enchere`),
  UNIQUE KEY `uq_id_enchere` (`id_enchere`),
  KEY `fk_enchere_article` (`ref_article`),
  KEY `fk_enchere_enchereur` (`ref_enchereur`),
  CONSTRAINT `fk_enchere_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_enchere_enchereur` FOREIGN KEY (`ref_enchereur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enchere`
--

LOCK TABLES `enchere` WRITE;
/*!40000 ALTER TABLE `enchere` DISABLE KEYS */;
INSERT INTO `enchere` VALUES (1,5,3,10000,'2026-10-15 12:49:47'),(2,5,3,11000,'2026-10-16 12:52:23');
/*!40000 ALTER TABLE `enchere` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etat`
--

DROP TABLE IF EXISTS `etat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etat` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etat`
--

LOCK TABLES `etat` WRITE;
/*!40000 ALTER TABLE `etat` DISABLE KEYS */;
INSERT INTO `etat` VALUES (1),(2);
/*!40000 ALTER TABLE `etat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etat_libelle_langue`
--

DROP TABLE IF EXISTS `etat_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etat_libelle_langue` (
  `ref_etat` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_etat`,`ref_libelle`,`ref_langue`),
  KEY `fk_ell_libelle` (`ref_libelle`),
  KEY `fk_ell_langue` (`ref_langue`),
  CONSTRAINT `fk_ell_etat` FOREIGN KEY (`ref_etat`) REFERENCES `etat` (`id`),
  CONSTRAINT `fk_ell_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_ell_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etat_libelle_langue`
--

LOCK TABLES `etat_libelle_langue` WRITE;
/*!40000 ALTER TABLE `etat_libelle_langue` DISABLE KEYS */;
INSERT INTO `etat_libelle_langue` VALUES (1,10035,1),(2,10036,1),(1,30010035,3),(2,30010036,3),(1,40010035,2),(2,40010036,2);
/*!40000 ALTER TABLE `etat_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_achat`
--

DROP TABLE IF EXISTS `evaluation_achat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_achat` (
  `id_evaluation_achat` int NOT NULL AUTO_INCREMENT,
  `ref_vendeur` int NOT NULL,
  `ref_acheteur` int NOT NULL,
  `ref_article` int NOT NULL,
  `note` tinyint NOT NULL,
  `commentaire` text NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id_evaluation_achat`),
  KEY `fk_ea_vendeur` (`ref_vendeur`),
  KEY `fk_ea_acheteur` (`ref_acheteur`),
  KEY `fk_ea_article` (`ref_article`),
  CONSTRAINT `fk_ea_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_ea_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_ea_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_achat`
--

LOCK TABLES `evaluation_achat` WRITE;
/*!40000 ALTER TABLE `evaluation_achat` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation_achat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_article`
--

DROP TABLE IF EXISTS `evaluation_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_article` (
  `id_evaluation_article` int NOT NULL AUTO_INCREMENT,
  `ref_article` int NOT NULL,
  `ref_vendeur` int NOT NULL,
  `ref_acheteur` int NOT NULL,
  `note` int NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_evaluation_article`),
  KEY `fk_eart_vendeur` (`ref_vendeur`),
  KEY `fk_eart_acheteur` (`ref_acheteur`),
  KEY `fk_eart_article` (`ref_article`),
  CONSTRAINT `fk_eart_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_eart_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_article`
--

LOCK TABLES `evaluation_article` WRITE;
/*!40000 ALTER TABLE `evaluation_article` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_vente`
--

DROP TABLE IF EXISTS `evaluation_vente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_vente` (
  `id_evaluation_vente` int NOT NULL AUTO_INCREMENT,
  `ref_vendeur` int NOT NULL,
  `ref_acheteur` int NOT NULL,
  `ref_article` int NOT NULL,
  `note` tinyint NOT NULL,
  `commentaire` text NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id_evaluation_vente`),
  KEY `fk_ev_vendeur` (`ref_vendeur`),
  KEY `fk_ev_acheteur` (`ref_acheteur`),
  KEY `fk_ev_article` (`ref_article`),
  CONSTRAINT `fk_ev_acheteur` FOREIGN KEY (`ref_acheteur`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_ev_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_ev_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_vente`
--

LOCK TABLES `evaluation_vente` WRITE;
/*!40000 ALTER TABLE `evaluation_vente` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation_vente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `id_genre` int NOT NULL AUTO_INCREMENT,
  `genre` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`id_genre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `langue`
--

DROP TABLE IF EXISTS `langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `langue` (
  `id_langue` int NOT NULL,
  `nom` varchar(10) NOT NULL,
  `key` varchar(2) NOT NULL,
  PRIMARY KEY (`id_langue`),
  UNIQUE KEY `uq_id_langue` (`id_langue`),
  KEY `fk_bdvll_langue` (`id_langue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `langue`
--

LOCK TABLES `langue` WRITE;
/*!40000 ALTER TABLE `langue` DISABLE KEYS */;
INSERT INTO `langue` VALUES (1,'Français','FR'),(2,'Deutsch','DE'),(3,'English','EN');
/*!40000 ALTER TABLE `langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libelle`
--

DROP TABLE IF EXISTS `libelle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libelle` (
  `id_libelle` int NOT NULL,
  `libelle` varchar(500) NOT NULL,
  PRIMARY KEY (`id_libelle`),
  UNIQUE KEY `uq_id_libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libelle`
--

LOCK TABLES `libelle` WRITE;
/*!40000 ALTER TABLE `libelle` DISABLE KEYS */;
INSERT INTO `libelle` VALUES (1,'Habits femmes'),(2,'Habits hommes'),(3,'Habits enfants'),(4,'Mobilier'),(5,'Automobile'),(6,'Motos'),(7,'Tv / vidéo'),(9,'Livres'),(11,'Consoles de jeux'),(12,'Diététique'),(13,'Parfums'),(14,'Accessoires de cuisines'),(16,'Cosmétiques'),(17,'Matériels dj'),(18,'Collections'),(19,'Jouets'),(20,'Accessoires et outils'),(21,'Lingerie femme'),(22,'Calendrier'),(23,'Univers de bébé'),(24,'Electroménager'),(25,'Immobilier'),(26,'Jeans'),(27,'TShirts'),(28,'Jupes'),(29,'Robes'),(30,'Sacs'),(31,'Chaussures'),(32,'Chaussettes'),(33,'Lunettes de soleil'),(34,'Pulls'),(35,'De 0 à 3 mois'),(36,'De 4 à 6 mois'),(37,'De 7 à 12 mois'),(38,'Chaussures'),(39,'Chaussons'),(40,'De 0 à 3 mois'),(41,'Peintures'),(42,'Photographies'),(43,'Sculptures'),(44,'Divers'),(47,'Lits'),(48,'Canapés'),(49,'Armoires'),(50,'Meubles Tv'),(51,'Fauteils'),(52,'Berline'),(53,'Coupé'),(54,'4x4'),(55,'Sport'),(56,'Hybride'),(57,'Eléctrique'),(58,'50 cm3'),(59,'125 cm3'),(60,'250 cm3'),(61,'500 cm3'),(62,'750 cm3'),(63,'1000 cm3'),(64,'Casques'),(65,'Gants'),(66,'Accessoires'),(67,'Vestes'),(68,'Pantalons'),(70,'Lecteurs dvd'),(71,'Chaines hifi'),(72,'Ecrans plats'),(73,'Lecteurs mp3'),(74,'Casques audio'),(77,'Pc'),(78,'Ordinateurs portable'),(79,'Mac'),(80,'Tablettes'),(81,'Souris'),(82,'Télécommandes'),(83,'Logiciels'),(84,'Natels'),(85,'Webcams'),(86,'Câblages'),(87,'Cartes mères'),(88,'Scanners'),(89,'Photocopieurs'),(90,'Routeurs'),(91,'Antennes satellite'),(92,'Lecteurs biométrique'),(93,'Lecteurs de cartes à puce'),(94,'Caisses enregistreuse'),(97,'Bagues'),(98,'Briquets'),(99,'Chainette'),(100,'Parure'),(101,'Techno'),(102,'Hardstyle'),(103,'Hardcore'),(104,'Trance'),(105,'Hard Trance'),(106,'House'),(107,'Teck House'),(108,'Goa'),(109,'Rap US'),(110,'R and b'),(111,'Reggea'),(112,'Dance'),(113,'Hits'),(114,'Rock'),(115,'HardRock'),(116,'Pop'),(117,'Folk'),(118,'Snowboard fille'),(119,'Snowboard garçon'),(120,'Fixation snowboard fille'),(121,'Fixation snowboard garçon'),(122,'Skate'),(129,'Trucks de skate'),(130,'Vis'),(131,'Roulements à billes'),(132,'Maillots de hockey'),(133,'Puck'),(135,'Chaussures de courses fille'),(136,'Chaussures de courses garçon'),(137,'Chaussures de trekking fille'),(138,'Chaussures de trekking garçon'),(139,'Chaussures de danse fille'),(140,'Chaussures de danse garçon'),(141,'Chaussures de basket fille'),(142,'Chaussures de basket garçon'),(143,'Chaussures de football garçon'),(144,'Chaussures de football fille'),(145,'Chaussures de rugby fille'),(146,'Chaussures de rugby garçon'),(147,'Chaussures de surf fille'),(148,'Chaussures de surf garçon'),(149,'Chaussures de snowboard fille'),(150,'Chaussures de snowboard garçon'),(151,'Chaussures de training fille'),(152,'Chaussures de training garçon'),(153,'Habits de snowboard fille'),(154,'Habits de snowboard garçon'),(155,'Habits de ski fille'),(156,'Habits de ski garçon'),(157,'Casques de ski fille'),(158,'Casques de ski garçon'),(159,'Lunettes de ski fille'),(160,'Lunettes de ski garçon'),(161,'Caméra de sport'),(162,'Ballon de basket'),(163,'Ballon de football'),(164,'Ballon de rugby'),(165,'Racket de tennis fille'),(166,'Racket de tennis garçon'),(167,'Sacoche de tennis fille'),(168,'Sacoche de tennis garçon'),(169,'Selles pour chevaux fille'),(170,'Selles pour chevaux garçon'),(171,'Bottes de cheval fille'),(172,'Bottes de cheval garçon'),(173,'Vêtements d\'équitation fille'),(174,'Vêtements d\'équitation garçon'),(175,'Action'),(176,'Suspense'),(177,'Policier'),(178,'Humour'),(179,'Tragédie'),(180,'Thriller'),(181,'Horreur'),(182,'Animations'),(183,'Séries'),(185,'Documentaires'),(186,'Dessins animés'),(187,'Porno'),(188,'Informatique'),(189,'Médecine'),(190,'Droit'),(191,'Mathématique'),(192,'Biologie'),(193,'Physique'),(194,'Langue'),(195,'Biologie'),(196,'Psychologie'),(197,'Psychiatrie'),(198,'Science cognitive'),(199,'Architecture'),(200,'Design'),(201,'Art'),(202,'Santé'),(203,'Nutrionnel'),(204,'Criminologie'),(205,'Science politique'),(206,'Urbanisme'),(207,'Enfants'),(208,'BD'),(209,'Mécanique'),(210,'Essais'),(211,'Essais'),(212,'Littérature (langues)'),(213,'Romans'),(214,'Génétique'),(215,'Guerre'),(216,'Ludique'),(217,'Shoot\'em up'),(218,'Plantes médicinales'),(219,'Parfums femme'),(220,'Parfums homme'),(221,'Parfums homme'),(222,'Piano'),(223,'Trompette'),(224,'Bariton'),(225,'Violoncelle'),(226,'Batterie'),(227,'Clarinette'),(228,'Accordéon'),(229,'Synthétiseur'),(230,'Saxophone'),(231,'Basse'),(232,'Clavecin'),(233,'Cornemuse'),(234,'Flute'),(235,'Harpe'),(236,'Contrebasse'),(237,'Tuba'),(238,'Alto'),(239,'Basson'),(240,'Cor'),(241,'Hautbois'),(242,'Violon'),(243,'Orgues'),(244,'Tables de mix'),(245,'Mixers'),(246,'Dj Cd mp3 players'),(247,'Dj Controllers'),(248,'Phono cartdridges'),(249,'Dj fx\'s'),(250,'Dj Software'),(251,'Dj fx\'s'),(252,'Casques'),(253,'Flycase'),(254,'Accessoires de dj'),(255,'Dj fx\'s'),(256,'Démaquillants et nettoyants'),(257,'Hydratation Multi-Climats'),(258,'Eclat Mat'),(259,'Douceur'),(260,'Eclat du jour'),(261,'Eclaircissant'),(262,'Aromaphytosoin'),(263,'Multi-Actif'),(264,'Multi-Regénérant'),(265,'Multi-Intensif'),(266,'Capital Lumière'),(267,'Sérums'),(268,'Exfoliants et Masques'),(269,'Yeux, Lèvres et Cou'),(270,'Essentiels'),(271,'AromaPhytoSoins \"Bien-Etre\"'),(290,'Aviation / Aéronautique'),(291,'Train / Chemin de fer'),(292,'Bistrot'),(293,'Briquet / Allumettes'),(294,'Calendrier femmes'),(295,'Capsules'),(296,'Bouchons'),(314,'Circuits'),(315,'Figurines, Statues'),(316,'Jeux de construction, Lego'),(317,'Jeux de plein air'),(318,'Jouets, Jeux anciens'),(319,'Jouets musicaux, Instruments'),(320,'Magie'),(321,'Maquettes'),(322,'Marionnettes'),(323,'Minis Univers'),(324,'Peluches, Doudous'),(325,'Peluches, Doudous'),(326,'Petits soldats'),(327,'Poker, Casino'),(328,'Puzzles'),(329,'Robots, Automates'),(330,'Star Wars'),(331,'Cartes à jouer'),(332,'Cartes à jouer'),(333,'Déguisements, Masques'),(334,'Jeux de rôle, de figurines'),(335,'Jeux de société'),(336,'Jeux éducatifs, Casse-tête'),(337,'Jeux éducatifs, Casse-tête'),(338,'Jeux électroniques'),(339,'Maquettes trains électriques'),(340,'Poupées'),(341,'Radiocommandés, Modélisme'),(342,'Véhicules miniatures'),(343,'Jeux de café'),(345,'Outils à main'),(346,'Outils de jardin'),(347,'Outils électriques'),(348,'Electronique, Composants'),(349,'Installation électrique'),(350,'Matériaux'),(351,'Peinture, Accessoires'),(361,'Cuisine: Arts de la table, Accessoires'),(362,'Cuisine: Casserolerie, Plats'),(363,'Cuisine: Meubles de cuisine'),(364,'Cuisine: Ustensiles'),(365,'Entretien, Nettoyage'),(366,'Linge de maison, Rideaux'),(367,'Luminaires'),(368,'Meubles'),(369,'Salle de bains: Accessoires'),(370,'Salle de bains: Meubles'),(371,'Cheminées, AccessoiresSalle de bains: Meubles'),(372,'Cuisine: Boîtes hermétiques'),(373,'Décoration'),(374,'Décoration'),(375,'Electroménager'),(376,'Jardin, Extérieur'),(378,'Fêtes, Occasions spéciales'),(379,'Sécurité, Domotique'),(380,'Cuisine: Ustensiles café, Thé'),(381,'A l\'huile'),(382,'A l\'eau'),(383,'Mate'),(384,'Satinée'),(385,'Brillante'),(386,'Glycéro'),(387,'Acrylique'),(388,'Lavable'),(389,'Lessivable'),(390,'Lasure'),(391,'Spécifique'),(392,'Bougies, Bougeoirs'),(393,'Bougies, Bougeoirs'),(394,'Cadres'),(395,'Coussins, Galettes de sièges'),(396,'Décorations enfants'),(397,'Décorations murales, Stickers'),(398,'Horloges, Pendules'),(399,'Miroirs'),(400,'Objets ethniques'),(401,'Parfums d\'intérieur'),(403,'Sculptures, Statues'),(404,'Tapis'),(405,'Autres objets de décoration'),(406,'Arrosage, Fontaines'),(407,'Barbecues'),(408,'Clôtures, Portails'),(409,'Décorations de jardin'),(410,'Eclairage, Lampes'),(411,'Energie renouvelable'),(412,'Jeux de plein air'),(413,'Meubles de jardin, Parasols'),(414,'Piscines, Accessoires'),(415,'Plantes, Graines, Bulbes'),(416,'Saunas, Bains hydromassants'),(417,'Serres, Accessoires'),(418,'Ensembles'),(419,'Soutiens gorges'),(420,'Collants, Bas'),(421,'Maillots de Bain 2 Pièces'),(422,'Maillots de Bain 1 Pièce'),(423,'Strings'),(426,'Calendrier sexy'),(427,'Calendrier bienfaisance'),(428,'Calendrier bienfaisance'),(429,'Porte-bébé'),(430,'Poussettes, Systèmes combinés'),(431,'Sacs à langer'),(432,'Sièges-auto, Vélo'),(433,'Jouets de 0 à 6 mois fille et garçon'),(434,'Jouets de 6 à 12 mois fille et garçon'),(435,'Jouets de 12 à 18 mois fille et garçon'),(436,'Jouets de 18 à 24 mois fille et garçon'),(437,'Chambres complètes'),(438,'Décorations, Veilleuses'),(439,'Gigoteuses, Nids d\'Anges'),(440,'Literie'),(441,'Meubles'),(442,'Meubles à langer'),(443,'Parcs'),(444,'Transats, Balancelles'),(445,'Biberons'),(446,'Bavoirs'),(447,'Sucettes'),(448,'Stérilisateurs'),(449,'Stérilisateurs'),(450,'Interphones'),(451,'Barrières de Sécurité'),(452,'Interphones avec Caméra'),(453,'Détecteurs de Température'),(454,'Couches'),(455,'Produits de Toilette'),(456,'Capes de Bain'),(457,'Thermomètres de Bain'),(458,'Vêtements de bébés'),(459,'Lave-vaisselle'),(460,'Lave-linge'),(461,'Frigo'),(462,'Aspirateur'),(463,'Four'),(464,'Four encastrable'),(465,'Thermo mix'),(466,'Machine à laver'),(467,'Micro-onde'),(468,'Sèche-linge'),(469,'Appartement'),(470,'Villa'),(471,'Loft'),(473,'Parcelle de terrain'),(475,'Châlet'),(550,'Trucks de skate'),(551,'Vis'),(553,'Roulements à billes'),(554,'Maillots de hockey'),(555,'Puck'),(556,'Puck'),(600,'Ski fille'),(601,'Ski garçon'),(602,'Fixation de ski fille'),(603,'Fixation de ski garçon'),(604,'Chaussures de ski fille'),(605,'Chaussures de ski garçon'),(771,'Embellir sa Peau'),(772,'Remodeler son Corps'),(773,'Grossesse'),(774,'Eaux De Soins'),(775,'Minceur et fermeté'),(776,'Protecteurs'),(777,'Après-Soleil'),(778,'Autobronzants'),(779,'Neo Pastels'),(780,'Teint'),(781,'Yeux'),(782,'Lèvres et Ongles'),(783,'Eclat Minute: Les Produits Malins'),(784,'Corps'),(785,'Nettoyage'),(786,'Rasage'),(787,'Hydratation'),(788,'Anti-Age'),(789,'S.O.S Express'),(1052,'Scooter des mers'),(1094,'Calendrier hommes'),(1197,'Cartes / Guides / Plans'),(1198,'Cartes postales'),(1300,'Couteaux de poche'),(1301,'Couture / Tricot'),(1302,'Diddl'),(1303,'Ecriture / Dessins'),(1304,'Fèves'),(1305,'Gramographe / Phonographe'),(1306,'Images / Statue animale'),(1307,'Incroyable voir étrange'),(1308,'Kinder'),(1309,'Lanterne / Lampe de poche'),(1310,'Lettre / Vieux papier'),(1311,'Militaire'),(1312,'Lettre / Vieux papier'),(1313,'Cartes de collection'),(1372,'Remodeler son Corps'),(1373,'Grossesse'),(1374,'Eaux De Soins'),(1375,'Minceur et fermeté'),(1376,'Protecteurs'),(1398,'Coquillage / Fossiles minéraux'),(1399,'Costumes / Vêtements d\'époque'),(2351,'Plomberie, Sanitaires'),(6351,'Plomberie, Sanitaires'),(6352,'Revêtements de sols '),(6353,'Revêtements muraux'),(6354,'Toiture, Isolation'),(6355,'Travaux du bâtiment'),(6356,'Vêtements de travail'),(6357,'Accessoires Animaux'),(6358,'Bricolage'),(6359,'Bricolage: Outils'),(6360,'Quincaillerie, Ferronnerie'),(6380,'Chauffage, Climatisation'),(8423,'Culottes'),(8424,'Caleçons'),(8425,'Slips'),(9000,'X Box'),(9001,'Playstation'),(9002,'DES'),(9003,'Wii'),(9004,'Action'),(9005,'Action'),(9006,'Sport'),(10000,'Vins'),(10001,'Vin rouge'),(10002,'Vin blanc'),(10003,'Vin rosé'),(10004,'Champagne'),(10005,'Payement comptant'),(10006,'Paypall'),(10007,'Lettres Courrier A'),(10008,'Lettres Courrier B'),(10009,'Colis Courrier A'),(10010,'Colis Courrier B'),(10011,'Recommander'),(10012,'Remboursement'),(10013,'Service d expédition ou courrier'),(10014,'A retirer chez le vendeur'),(10015,'Livraison par le fournisseur'),(10016,'Acheteur'),(10017,'Vendeur_(non-professionel)'),(10018,'Garage'),(10019,'Vendeur_professionel'),(10020,'Lingerie homme'),(10021,'Astrologie'),(10022,'Cartes / Tarôts'),(10023,'Livres'),(10024,'Animal'),(10025,'Chien'),(10026,'Chat'),(10027,'Cheval'),(10028,'Serpent'),(10029,'Montres et bijoux femme'),(10030,'Montres et bijoux homme'),(10031,'Habitat et jardin'),(10032,'Cd / Vinyl / Mp3'),(10033,'Location'),(10034,'Achat'),(10035,'Utilisé'),(10036,'Neuf'),(10037,'Cd'),(10038,'Vinyl'),(10039,'Mp3'),(10040,'Dvd'),(10041,'Essence'),(10042,'Diesel'),(10043,'Eléctrique'),(10044,'Hybride'),(10045,'Gaz'),(10046,'Manuelle'),(10047,'Automatique'),(10049,'Instruments de musique'),(10050,'Yacht'),(10051,'Voilier'),(10053,'Offshore'),(10054,'Bâteau'),(10055,'Jeux vidéos'),(10056,'Hockey'),(10057,'Snowboard'),(10058,'Ski'),(10059,'Equitation'),(10060,'Lcd'),(10061,'Plasma'),(10100,'Paiement comptant'),(10101,'PayPal'),(10102,'Virement bancaire'),(10103,'Carte de crédit'),(10200,'Lettres Courrier A'),(10201,'Colis Courrier B'),(10202,'À retirer chez le vendeur'),(10203,'Livraison à domicile'),(11000,'Informatique'),(12000,'Tv / Vidéo'),(12001,'Air filtration et stérilisation'),(12002,'34'),(12003,'35'),(22222,'Art et design'),(99999,'Fendant'),(100000,'Janvier'),(100001,'Février'),(100002,'Mars'),(100003,'Avril'),(100004,'Mai'),(100005,'Juin'),(100006,'Juillet'),(100007,'Août'),(100008,'Septembre'),(100009,'Octobre'),(1000010,'Novembre'),(1000122,'Décembre'),(1000123,'Twint'),(1000124,'Canne de Hockey proessionel\r\n'),(1000125,'Montres'),(1000126,'Boucles d\'oreille'),(10000001,'Women\'s clothing'),(10000002,'Men\'s clothing'),(10000003,'Children\'s clothing'),(10000005,'Cars'),(10000006,'Motorcycles'),(10000007,'TV / Video'),(10000009,'Books'),(10000011,'Game consoles'),(10000012,'Diet & nutrition'),(10000013,'Perfumes'),(10000014,'Kitchen accessories'),(10000016,'Cosmetics'),(10000017,'DJ equipment'),(10000018,'Collectibles'),(10000019,'Toys'),(10000020,'Accessories and tools'),(10000021,'Women\'s lingerie'),(10000022,'Calendars'),(10000023,'Baby world'),(10000024,'Home appliances'),(10000025,'Real estate'),(10000026,'Jeans'),(10000027,'T-shirts'),(10000028,'Skirts'),(10000029,'Dresses'),(10000030,'Bags'),(10000031,'Shoes'),(10000032,'Socks'),(10000033,'Sunglasses'),(10000034,'Sweaters'),(10000035,'0 to 3 months'),(10000036,'4 to 6 months'),(10000037,'7 to 12 months'),(10000039,'Slippers'),(10000041,'Paintings'),(10000042,'Photographs'),(10000043,'Sculptures'),(10000044,'Miscellaneous'),(10000047,'Beds'),(10000048,'Sofas'),(10000049,'Wardrobes'),(10000050,'TV furniture'),(10000051,'Armchairs'),(10000052,'Saloon'),(10000053,'Coupé'),(10000054,'4x4'),(10000055,'Sports car'),(10000056,'Hybrid'),(10000057,'Electric'),(10000058,'50 cc'),(10000059,'125 cc'),(10000060,'250 cc'),(10000061,'500 cc'),(10000062,'750 cc'),(10000063,'1000 cc'),(10000064,'Helmets'),(10000065,'Gloves'),(10000066,'Accessories'),(10000067,'Jackets'),(10000068,'Trousers'),(10000070,'DVD players'),(10000071,'Hi-fi systems'),(10000072,'Flat-screen TVs'),(10000073,'MP3 players'),(10000074,'Headphones'),(10000077,'PCs'),(10000078,'Laptops'),(10000079,'Macs'),(10000080,'Tablets'),(10000081,'Mice'),(10000082,'Remote controls'),(10000083,'Software'),(10000084,'Mobile phones'),(10000085,'Webcams'),(10000086,'Cabling'),(10000087,'Motherboards'),(10000088,'Scanners'),(10000089,'Photocopiers'),(10000090,'Routers'),(10000091,'Satellite dishes'),(10000092,'Biometric readers'),(10000093,'Smart card readers'),(10000094,'Cash registers'),(10000097,'Rings'),(10000098,'Lighters'),(10000099,'Chains'),(10000100,'Jewellery sets'),(10000118,'Girls\' snowboards'),(10000119,'Boys\' snowboards'),(10000120,'Girls\' snowboard bindings'),(10000121,'Boys\' snowboard bindings'),(10000122,'Skateboards'),(10000129,'Skateboard trucks'),(10000131,'Ball bearings'),(10000132,'Ice hockey jerseys'),(10000133,'Pucks'),(10000135,'Girls\' running shoes'),(10000136,'Boys\' running shoes'),(10000138,'Boys\' trekking shoes'),(10000140,'Boys\' dance shoes'),(10000141,'Girls\' basketball shoes'),(10000142,'Boys\' basketball shoes'),(10000143,'Boys\' football boots'),(10000144,'Girls\' football boots'),(10000145,'Girls\' rugby boots'),(10000146,'Boys\' rugby boots'),(10000147,'Girls\' surf shoes'),(10000148,'Boys\' surf shoes'),(10000149,'Girls\' snowboard boots'),(10000150,'Boys\' snowboard boots'),(10000151,'Girls\' training shoes'),(10000152,'Boys\' training shoes'),(10000153,'Girls\' snowboard clothing'),(10000154,'Boys\' snowboard clothing'),(10000155,'Girls\' ski clothing'),(10000156,'Boys\' ski clothing'),(10000157,'Girls\' ski helmets'),(10000158,'Boys\' ski helmets'),(10000159,'Girls\' ski goggles'),(10000160,'Boys\' ski goggles'),(10000161,'Action cameras'),(10000162,'Basketballs'),(10000163,'Footballs'),(10000164,'Rugby balls'),(10000165,'Girls\' tennis rackets'),(10000166,'Boys\' tennis rackets'),(10000167,'Girls\' tennis bags'),(10000168,'Boys\' tennis bags'),(10000169,'Girls\' saddles'),(10000170,'Boys\' saddles'),(10000171,'Girls\' riding boots'),(10000172,'Boys\' riding boots'),(10000173,'Girls\' riding clothing'),(10000174,'Boys\' riding clothing'),(10000175,'Action'),(10000176,'Suspense'),(10000177,'Crime'),(10000178,'Comedy'),(10000179,'Tragedy'),(10000180,'Thriller'),(10000181,'Horror'),(10000182,'Animation'),(10000183,'TV series'),(10000185,'Documentaries'),(10000186,'Cartoons'),(10000187,'Adult'),(10000188,'Computing'),(10000189,'Medicine'),(10000190,'Law'),(10000191,'Mathematics'),(10000192,'Biology'),(10000193,'Physics'),(10000194,'Languages'),(10000195,'Biology'),(10000196,'Psychology'),(10000197,'Psychiatry'),(10000198,'Cognitive science'),(10000199,'Architecture'),(10000201,'Art'),(10000202,'Health'),(10000203,'Nutrition'),(10000204,'Criminology'),(10000205,'Political science'),(10000206,'Urban planning'),(10000207,'Children'),(10000208,'Comics'),(10000209,'Mechanics'),(10000211,'Essays'),(10000212,'Literature (languages)'),(10000213,'Novels'),(10000214,'Genetics'),(10000215,'War'),(10000216,'Casual'),(10000217,'Shoot \'em up'),(10000218,'Medicinal plants'),(10000219,'Women\'s perfumes'),(10000220,'Men\'s perfumes'),(10000222,'Piano'),(10000223,'Trumpet'),(10000224,'Baritone horn'),(10000225,'Cello'),(10000226,'Drums'),(10000227,'Clarinet'),(10000228,'Accordion'),(10000229,'Synthesizer'),(10000230,'Saxophone'),(10000231,'Bass guitar'),(10000232,'Harpsichord'),(10000233,'Bagpipes'),(10000234,'Flute'),(10000235,'Harp'),(10000236,'Double bass'),(10000237,'Tuba'),(10000238,'Viola'),(10000239,'Bassoon'),(10000240,'French horn'),(10000241,'Oboe'),(10000242,'Violin'),(10000243,'Organs'),(10000244,'Mixing desks'),(10000245,'Mixers'),(10000246,'DJ CD / MP3 players'),(10000247,'DJ controllers'),(10000248,'Phono cartridges'),(10000249,'DJ effects'),(10000250,'DJ software'),(10000251,'DJ effects'),(10000252,'DJ headphones'),(10000253,'Flight cases'),(10000254,'DJ accessories'),(10000255,'DJ effects'),(10000290,'Aviation'),(10000291,'Trains / Railways'),(10000292,'Bistro'),(10000293,'Lighters / Matches'),(10000294,'Women\'s calendars'),(10000295,'Capsules'),(10000296,'Corks'),(10000314,'Race tracks'),(10000315,'Figurines, statues'),(10000316,'Building sets, Lego'),(10000317,'Outdoor games'),(10000318,'Vintage toys and games'),(10000319,'Musical toys, instruments'),(10000320,'Magic'),(10000321,'Models'),(10000322,'Puppets'),(10000323,'Miniature worlds'),(10000324,'Plush toys, cuddly toys'),(10000325,'Plush toys, cuddly toys'),(10000326,'Toy soldiers'),(10000327,'Poker, casino'),(10000328,'Puzzles'),(10000329,'Robots, automatons'),(10000330,'Star Wars'),(10000331,'Playing cards'),(10000332,'Playing cards'),(10000333,'Costumes, masks'),(10000334,'Role-playing and miniature games'),(10000335,'Board games'),(10000336,'Educational games, brain teasers'),(10000337,'Educational games, brain teasers'),(10000338,'Electronic games'),(10000339,'Model railways'),(10000340,'Dolls'),(10000341,'Radio-controlled, model making'),(10000342,'Miniature vehicles'),(10000343,'Pub games'),(10000345,'Hand tools'),(10000346,'Garden tools'),(10000347,'Power tools'),(10000348,'Electronics, components'),(10000349,'Electrical installation'),(10000350,'Materials'),(10000351,'Paint, accessories'),(10000361,'Kitchen: tableware, accessories'),(10000362,'Kitchen: pots, pans and dishes'),(10000363,'Kitchen: kitchen furniture'),(10000364,'Kitchen: utensils'),(10000365,'Maintenance, cleaning'),(10000418,'Sets'),(10000419,'Bras'),(10000420,'Tights, stockings'),(10000421,'Two-piece swimsuits'),(10000422,'One-piece swimsuits'),(10000423,'Thongs'),(10000426,'Sexy calendars'),(10000427,'Charity calendars'),(10000429,'Baby carriers'),(10000430,'Strollers, travel systems'),(10000431,'Changing bags'),(10000432,'Car seats, bike seats'),(10000433,'Toys 0 to 6 months, girls and boys'),(10000434,'Toys 6 to 12 months, girls and boys'),(10000435,'Toys 12 to 18 months, girls and boys'),(10000436,'Toys 18 to 24 months, girls and boys'),(10000437,'Complete nurseries'),(10000438,'Decorations, night lights'),(10000439,'Sleeping bags, baby nests'),(10000440,'Bedding'),(10000441,'Furniture'),(10000442,'Changing units'),(10000443,'Playpens'),(10000444,'Baby bouncers, rockers'),(10000445,'Baby bottles'),(10000446,'Bibs'),(10000447,'Pacifiers'),(10000448,'Sterilisers'),(10000449,'Sterilisers'),(10000450,'Baby monitors'),(10000451,'Safety gates'),(10000452,'Video baby monitors'),(10000453,'Temperature sensors'),(10000454,'Nappies'),(10000455,'Toiletries'),(10000456,'Bath capes'),(10000458,'Baby clothing'),(10000459,'Dishwashers'),(10000460,'Washing machines'),(10000461,'Fridges'),(10000462,'Vacuum cleaners'),(10000463,'Ovens'),(10000464,'Built-in ovens'),(10000465,'Thermomix'),(10000466,'Washing machines'),(10000467,'Microwaves'),(10000468,'Tumble dryers'),(10000469,'Apartments'),(10000470,'Villas'),(10000471,'Lofts'),(10000473,'Plots of land'),(10000475,'Chalets'),(10000554,'Ice hockey jerseys'),(10000555,'Pucks'),(10000600,'Girls\' skis'),(10000601,'Boys\' skis'),(10000602,'Girls\' ski bindings'),(10000603,'Boys\' ski bindings'),(10000604,'Girls\' ski boots'),(10000605,'Boys\' ski boots'),(10000771,'Skin beautifying'),(10000772,'Body contouring'),(10000773,'Pregnancy'),(10000774,'Treatment waters'),(10000775,'Slimming and firming'),(10000776,'Sun protection'),(10000777,'After-sun'),(10000778,'Self-tanners'),(10000779,'Neo Pastels'),(10000780,'Complexion'),(10000781,'Eyes'),(10000782,'Lips and nails'),(10000783,'Instant glow: handy products'),(10000784,'Body'),(10000785,'Cleansing'),(10000786,'Shaving'),(10000787,'Hydration'),(10000788,'Anti-ageing'),(10000789,'S.O.S. Express'),(10006352,'Floor coverings'),(10006353,'Wall coverings'),(10006354,'Roofing, insulation'),(10006355,'Building work'),(10006356,'Workwear'),(10006357,'Pet accessories'),(10006358,'DIY'),(10006359,'DIY: tools'),(10006360,'Hardware, ironmongery'),(10006380,'Heating, air conditioning'),(10008424,'Boxer shorts'),(10008425,'Briefs'),(10009000,'Xbox'),(10009001,'PlayStation'),(10009002,'DES'),(10009003,'Wii'),(10009005,'Action'),(10009006,'Sports'),(10010000,'Wines'),(10010001,'Red wine'),(10010002,'White wine'),(10010003,'Rosé wine'),(10010004,'Champagne'),(10010020,'Men\'s underwear'),(10010021,'Astrology'),(10010022,'Cards / Tarot'),(10010023,'Books'),(10010024,'Pets'),(10010025,'Dogs'),(10010026,'Cats'),(10010027,'Horses'),(10010028,'Snakes'),(10010029,'Women\'s watches and jewellery'),(10010030,'Men\'s watches and jewellery'),(10010031,'Home and garden'),(10010040,'DVD'),(10010049,'Musical instruments'),(10010050,'Yachts'),(10010051,'Sailing boats'),(10010053,'Offshore boats'),(10010054,'Boats'),(10010055,'Video games'),(10010056,'Ice hockey'),(10010057,'Snowboarding'),(10010058,'Skiing'),(10010059,'Horse riding'),(10022222,'Art and design'),(11000124,'Professional ice hockey sticks'),(11000125,'Watches'),(11000126,'Earrings'),(20000001,'Damenbekleidung'),(20000002,'Herrenbekleidung'),(20000003,'Kinderbekleidung'),(20000005,'Autos'),(20000006,'Motorräder'),(20000007,'TV / Video'),(20000009,'Bücher'),(20000011,'Spielkonsolen'),(20000012,'Diät & Ernährung'),(20000013,'Parfüms'),(20000014,'Küchenzubehör'),(20000016,'Kosmetik'),(20000017,'DJ-Equipment'),(20000018,'Sammlerstücke'),(20000019,'Spielzeug'),(20000020,'Zubehör und Werkzeuge'),(20000021,'Damenwäsche'),(20000022,'Kalender'),(20000023,'Babywelt'),(20000024,'Haushaltsgeräte'),(20000025,'Immobilien'),(20000026,'Jeans'),(20000027,'T-Shirts'),(20000028,'Röcke'),(20000029,'Kleider'),(20000030,'Taschen'),(20000031,'Schuhe'),(20000032,'Socken'),(20000033,'Sonnenbrillen'),(20000034,'Pullover'),(20000035,'0 bis 3 Monate'),(20000036,'4 bis 6 Monate'),(20000037,'7 bis 12 Monate'),(20000039,'Hausschuhe'),(20000041,'Gemälde'),(20000042,'Fotografien'),(20000043,'Skulpturen'),(20000044,'Verschiedenes'),(20000047,'Betten'),(20000048,'Sofas'),(20000049,'Schränke'),(20000050,'TV-Möbel'),(20000051,'Sessel'),(20000052,'Limousine'),(20000053,'Coupé'),(20000054,'4x4'),(20000055,'Sportwagen'),(20000056,'Hybrid'),(20000057,'Elektro'),(20000058,'50 ccm'),(20000059,'125 ccm'),(20000060,'250 ccm'),(20000061,'500 ccm'),(20000062,'750 ccm'),(20000063,'1000 ccm'),(20000064,'Helme'),(20000065,'Handschuhe'),(20000066,'Zubehör'),(20000067,'Jacken'),(20000068,'Hosen'),(20000070,'DVD-Player'),(20000071,'Hi-Fi-Anlagen'),(20000072,'Flachbildfernseher'),(20000073,'MP3-Player'),(20000074,'Kopfhörer'),(20000077,'PCs'),(20000078,'Laptops'),(20000079,'Macs'),(20000080,'Tablets'),(20000081,'Mäuse'),(20000082,'Fernbedienungen'),(20000083,'Software'),(20000084,'Mobiltelefone'),(20000085,'Webcams'),(20000086,'Verkabelung'),(20000087,'Mainboards'),(20000088,'Scanner'),(20000089,'Fotokopierer'),(20000090,'Router'),(20000091,'Satellitenantennen'),(20000092,'Biometrische Lesegeräte'),(20000093,'Chipkartenleser'),(20000094,'Registrierkassen'),(20000097,'Ringe'),(20000098,'Feuerzeuge'),(20000099,'Ketten'),(20000100,'Schmucksets'),(20000118,'Snowboards Mädchen'),(20000119,'Snowboards Jungen'),(20000120,'Snowboardbindungen Mädchen'),(20000121,'Snowboardbindungen Jungen'),(20000122,'Skateboards'),(20000129,'Skateboard-Achsen'),(20000131,'Kugellager'),(20000132,'Eishockeytrikots'),(20000133,'Pucks'),(20000135,'Laufschuhe Mädchen'),(20000136,'Laufschuhe Jungen'),(20000138,'Trekkingschuhe Jungen'),(20000140,'Tanzschuhe Jungen'),(20000141,'Basketballschuhe Mädchen'),(20000142,'Basketballschuhe Jungen'),(20000143,'Fussballschuhe Jungen'),(20000144,'Fussballschuhe Mädchen'),(20000145,'Rugbyschuhe Mädchen'),(20000146,'Rugbyschuhe Jungen'),(20000147,'Surfschuhe Mädchen'),(20000148,'Surfschuhe Jungen'),(20000149,'Snowboardschuhe Mädchen'),(20000150,'Snowboardschuhe Jungen'),(20000151,'Trainingsschuhe Mädchen'),(20000152,'Trainingsschuhe Jungen'),(20000153,'Snowboardbekleidung Mädchen'),(20000154,'Snowboardbekleidung Jungen'),(20000155,'Skibekleidung Mädchen'),(20000156,'Skibekleidung Jungen'),(20000157,'Skihelme Mädchen'),(20000158,'Skihelme Jungen'),(20000159,'Skibrillen Mädchen'),(20000160,'Skibrillen Jungen'),(20000161,'Actionkameras'),(20000162,'Basketbälle'),(20000163,'Fussbälle'),(20000164,'Rugbybälle'),(20000165,'Tennisschläger Mädchen'),(20000166,'Tennisschläger Jungen'),(20000167,'Tennistaschen Mädchen'),(20000168,'Tennistaschen Jungen'),(20000169,'Reitsättel Mädchen'),(20000170,'Reitsättel Jungen'),(20000171,'Reitstiefel Mädchen'),(20000172,'Reitstiefel Jungen'),(20000173,'Reitbekleidung Mädchen'),(20000174,'Reitbekleidung Jungen'),(20000175,'Action'),(20000176,'Spannung'),(20000177,'Krimi'),(20000178,'Komödie'),(20000179,'Tragödie'),(20000180,'Thriller'),(20000181,'Horror'),(20000182,'Animation'),(20000183,'Serien'),(20000185,'Dokumentarfilme'),(20000186,'Zeichentrickfilme'),(20000187,'Erotik'),(20000188,'Informatik'),(20000189,'Medizin'),(20000190,'Recht'),(20000191,'Mathematik'),(20000192,'Biologie'),(20000193,'Physik'),(20000194,'Sprachen'),(20000195,'Biologie'),(20000196,'Psychologie'),(20000197,'Psychiatrie'),(20000198,'Kognitionswissenschaft'),(20000199,'Architektur'),(20000201,'Kunst'),(20000202,'Gesundheit'),(20000203,'Ernährung'),(20000204,'Kriminologie'),(20000205,'Politikwissenschaft'),(20000206,'Städtebau'),(20000207,'Kinder'),(20000208,'Comics'),(20000209,'Mechanik'),(20000211,'Essays'),(20000212,'Literatur (Sprachen)'),(20000213,'Romane'),(20000214,'Genetik'),(20000215,'Krieg'),(20000216,'Casual'),(20000217,'Shoot \'em up'),(20000218,'Heilpflanzen'),(20000219,'Damenparfüms'),(20000220,'Herrenparfüms'),(20000222,'Klavier'),(20000223,'Trompete'),(20000224,'Bariton'),(20000225,'Violoncello'),(20000226,'Schlagzeug'),(20000227,'Klarinette'),(20000228,'Akkordeon'),(20000229,'Synthesizer'),(20000230,'Saxophon'),(20000231,'Bassgitarre'),(20000232,'Cembalo'),(20000233,'Dudelsack'),(20000234,'Flöte'),(20000235,'Harfe'),(20000236,'Kontrabass'),(20000237,'Tuba'),(20000238,'Bratsche'),(20000239,'Fagott'),(20000240,'Waldhorn'),(20000241,'Oboe'),(20000242,'Violine'),(20000243,'Orgeln'),(20000244,'Mischpulte'),(20000245,'Mixer'),(20000246,'DJ-CD-/MP3-Player'),(20000247,'DJ-Controller'),(20000248,'Tonabnehmersysteme'),(20000249,'DJ-Effektgeräte'),(20000250,'DJ-Software'),(20000251,'DJ-Effektgeräte'),(20000252,'DJ-Kopfhörer'),(20000253,'Flightcases'),(20000254,'DJ-Zubehör'),(20000255,'DJ-Effektgeräte'),(20000290,'Luftfahrt'),(20000291,'Eisenbahn'),(20000292,'Bistro'),(20000293,'Feuerzeuge / Streichhölzer'),(20000294,'Damenkalender'),(20000295,'Kapseln'),(20000296,'Korken'),(20000314,'Rennbahnen'),(20000315,'Figuren, Statuen'),(20000316,'Bausätze, Lego'),(20000317,'Outdoor-Spiele'),(20000318,'Alte Spielsachen und Spiele'),(20000319,'Musikspielzeug, Instrumente'),(20000320,'Zauberei'),(20000321,'Modelle'),(20000322,'Marionetten'),(20000323,'Miniaturwelten'),(20000324,'Plüschtiere, Kuscheltiere'),(20000325,'Plüschtiere, Kuscheltiere'),(20000326,'Spielzeugsoldaten'),(20000327,'Poker, Casino'),(20000328,'Puzzles'),(20000329,'Roboter, Automaten'),(20000330,'Star Wars'),(20000331,'Spielkarten'),(20000332,'Spielkarten'),(20000333,'Verkleidungen, Masken'),(20000334,'Rollenspiele, Figurenspiele'),(20000335,'Gesellschaftsspiele'),(20000336,'Lernspiele, Knobelspiele'),(20000337,'Lernspiele, Knobelspiele'),(20000338,'Elektronische Spiele'),(20000339,'Modelleisenbahnen'),(20000340,'Puppen'),(20000341,'Ferngesteuert, Modellbau'),(20000342,'Miniaturfahrzeuge'),(20000343,'Wirtshausspiele'),(20000345,'Handwerkzeuge'),(20000346,'Gartenwerkzeuge'),(20000347,'Elektrowerkzeuge'),(20000348,'Elektronik, Bauteile'),(20000349,'Elektroinstallation'),(20000350,'Materialien'),(20000351,'Farben, Zubehör'),(20000361,'Küche: Tischkultur, Zubehör'),(20000362,'Küche: Kochgeschirr, Platten'),(20000363,'Küche: Küchenmöbel'),(20000364,'Küche: Utensilien'),(20000365,'Pflege, Reinigung'),(20000418,'Sets'),(20000419,'BHs'),(20000420,'Strumpfhosen, Strümpfe'),(20000421,'Bikinis'),(20000422,'Badeanzüge'),(20000423,'Strings'),(20000426,'Sexy-Kalender'),(20000427,'Wohltätigkeitskalender'),(20000429,'Babytragen'),(20000430,'Kinderwagen, Kombisysteme'),(20000431,'Wickeltaschen'),(20000432,'Autositze, Velositze'),(20000433,'Spielzeug 0 bis 6 Monate, Mädchen und Jungen'),(20000434,'Spielzeug 6 bis 12 Monate, Mädchen und Jungen'),(20000435,'Spielzeug 12 bis 18 Monate, Mädchen und Jungen'),(20000436,'Spielzeug 18 bis 24 Monate, Mädchen und Jungen'),(20000437,'Komplette Kinderzimmer'),(20000438,'Dekoration, Nachtlichter'),(20000439,'Schlafsäcke, Babynester'),(20000440,'Bettwaren'),(20000441,'Möbel'),(20000442,'Wickelkommoden'),(20000443,'Laufgitter'),(20000444,'Babywippen, Schaukelwippen'),(20000445,'Babyflaschen'),(20000446,'Lätzchen'),(20000447,'Schnuller'),(20000448,'Sterilisatoren'),(20000449,'Sterilisatoren'),(20000450,'Babyphones'),(20000451,'Schutzgitter'),(20000452,'Babyphones mit Kamera'),(20000453,'Temperaturmesser'),(20000454,'Windeln'),(20000455,'Pflegeprodukte'),(20000456,'Badecapes'),(20000458,'Babykleidung'),(20000459,'Geschirrspüler'),(20000460,'Waschmaschinen'),(20000461,'Kühlschränke'),(20000462,'Staubsauger'),(20000463,'Backöfen'),(20000464,'Einbaubacköfen'),(20000465,'Thermomix'),(20000466,'Waschmaschinen'),(20000467,'Mikrowellen'),(20000468,'Wäschetrockner'),(20000469,'Wohnungen'),(20000470,'Villen'),(20000471,'Lofts'),(20000473,'Grundstücke'),(20000475,'Chalets'),(20000554,'Eishockeytrikots'),(20000555,'Pucks'),(20000600,'Ski Mädchen'),(20000601,'Ski Jungen'),(20000602,'Skibindungen Mädchen'),(20000603,'Skibindungen Jungen'),(20000604,'Skischuhe Mädchen'),(20000605,'Skischuhe Jungen'),(20000771,'Haut verschönern'),(20000772,'Körperformung'),(20000773,'Schwangerschaft'),(20000774,'Pflegewasser'),(20000775,'Schlankheit und Straffung'),(20000776,'Sonnenschutz'),(20000777,'After-Sun'),(20000778,'Selbstbräuner'),(20000779,'Neo Pastels'),(20000780,'Teint'),(20000781,'Augen'),(20000782,'Lippen und Nägel'),(20000783,'Sofort-Glanz: praktische Produkte'),(20000784,'Körper'),(20000785,'Reinigung'),(20000786,'Rasur'),(20000787,'Feuchtigkeitspflege'),(20000788,'Anti-Aging'),(20000789,'S.O.S. Express'),(20006352,'Bodenbeläge'),(20006353,'Wandbeläge'),(20006354,'Dach, Dämmung'),(20006355,'Bauarbeiten'),(20006356,'Arbeitskleidung'),(20006357,'Tierzubehör'),(20006358,'Heimwerken'),(20006359,'Heimwerken: Werkzeuge'),(20006360,'Eisenwaren, Beschläge'),(20006380,'Heizung, Klimatisierung'),(20008424,'Boxershorts'),(20008425,'Slips'),(20009000,'Xbox'),(20009001,'PlayStation'),(20009002,'DES'),(20009003,'Wii'),(20009005,'Action'),(20009006,'Sport'),(20010000,'Weine'),(20010001,'Rotwein'),(20010002,'Weisswein'),(20010003,'Roséwein'),(20010004,'Champagner'),(20010020,'Herrenwäsche'),(20010021,'Astrologie'),(20010022,'Karten / Tarot'),(20010023,'Bücher'),(20010024,'Tiere'),(20010025,'Hunde'),(20010026,'Katzen'),(20010027,'Pferde'),(20010028,'Schlangen'),(20010029,'Damenuhren und Damenschmuck'),(20010030,'Herrenuhren und Herrenschmuck'),(20010031,'Haus und Garten'),(20010040,'DVD'),(20010049,'Musikinstrumente'),(20010050,'Jachten'),(20010051,'Segelboote'),(20010053,'Offshore-Boote'),(20010054,'Boote'),(20010055,'Videospiele'),(20010056,'Eishockey'),(20010057,'Snowboard'),(20010058,'Ski'),(20010059,'Reitsport'),(20022222,'Kunst und Design'),(21000124,'Professionelle Eishockeyschläger'),(21000125,'Uhren'),(21000126,'Ohrringe'),(30009004,'Action'),(30009005,'Action'),(30009006,'Sports'),(30010001,'Red wine'),(30010002,'White wine'),(30010003,'Ros wine'),(30010004,'Champagne'),(30010033,'Rent'),(30010034,'Buy'),(30010035,'Used'),(30010036,'New'),(30010041,'Petrol'),(30010042,'Diesel'),(30010043,'Electric'),(30010046,'Manual'),(30010047,'Automatic'),(30010060,'LCD'),(30010061,'Plasma'),(30010100,'Cash payment'),(30010101,'PayPal'),(30010102,'Bank transfer'),(30010103,'Credit card'),(30010200,'Letters A-Mail'),(30010201,'Parcels B-Mail'),(30010202,'Pick up from the seller'),(30010203,'Home delivery'),(31000123,'Twint'),(40009004,'Action'),(40009005,'Action'),(40009006,'Sport'),(40010001,'Rotwein'),(40010002,'Weisswein'),(40010003,'Roswein'),(40010004,'Champagner'),(40010033,'Miete'),(40010034,'Kauf'),(40010035,'Gebraucht'),(40010036,'Neu'),(40010041,'Benzin'),(40010042,'Diesel'),(40010043,'Elektrisch'),(40010046,'Manuell'),(40010047,'Automatik'),(40010060,'LCD'),(40010061,'Plasma'),(40010100,'Barzahlung'),(40010101,'PayPal'),(40010102,'Banküberweiung'),(40010103,'Kreditkarte'),(40010200,'Briefe A-Post'),(40010201,'Pakete B-Post'),(40010202,'Abholung beim Verkufer'),(40010203,'Lieferung nach Hause'),(41000123,'Twint'),(90000001,'Sports'),(90000002,'Sport');
/*!40000 ALTER TABLE `libelle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_ou_achat_libelle_langue`
--

DROP TABLE IF EXISTS `location_ou_achat_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_ou_achat_libelle_langue` (
  `ref_location_ou_achat` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_location_ou_achat`,`ref_libelle`,`ref_langue`),
  KEY `fk_loall_libelle` (`ref_libelle`),
  KEY `fk_loall_langue` (`ref_langue`),
  CONSTRAINT `fk_loall_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_loall_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_ou_achat_libelle_langue`
--

LOCK TABLES `location_ou_achat_libelle_langue` WRITE;
/*!40000 ALTER TABLE `location_ou_achat_libelle_langue` DISABLE KEYS */;
INSERT INTO `location_ou_achat_libelle_langue` VALUES (1,10033,1),(2,10034,1),(1,30010033,3),(2,30010034,3),(1,40010033,2),(2,40010034,2);
/*!40000 ALTER TABLE `location_ou_achat_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `main_categorie_libelle_langue`
--

DROP TABLE IF EXISTS `main_categorie_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `main_categorie_libelle_langue` (
  `ref_main_categorie` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_main_categorie`,`ref_libelle`,`ref_langue`),
  KEY `fk_mcat_ll_langue` (`ref_langue`),
  KEY `fk_mcat_ll_libelle` (`ref_libelle`),
  CONSTRAINT `fk_mcat_ll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_mcat_ll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `main_categorie_libelle_langue`
--

LOCK TABLES `main_categorie_libelle_langue` WRITE;
/*!40000 ALTER TABLE `main_categorie_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `main_categorie_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `met_en_vente`
--

DROP TABLE IF EXISTS `met_en_vente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `met_en_vente` (
  `ref_vendeur` int NOT NULL DEFAULT '0',
  `ref_article` int NOT NULL DEFAULT '0',
  `date_stock` date NOT NULL DEFAULT '0000-00-00',
  `page_principale` varchar(40) DEFAULT NULL,
  `page_categorie` varchar(40) DEFAULT NULL,
  `pack_photo` varchar(20) DEFAULT NULL,
  `notre_selection` int DEFAULT '0',
  `statut` int DEFAULT NULL,
  PRIMARY KEY (`ref_vendeur`,`ref_article`),
  KEY `fk_mev_article` (`ref_article`),
  CONSTRAINT `fk_mev_article` FOREIGN KEY (`ref_article`) REFERENCES `article` (`id_article`),
  CONSTRAINT `fk_mev_vendeur` FOREIGN KEY (`ref_vendeur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `met_en_vente`
--

LOCK TABLES `met_en_vente` WRITE;
/*!40000 ALTER TABLE `met_en_vente` DISABLE KEYS */;
INSERT INTO `met_en_vente` VALUES (1,1,'2026-10-13',NULL,NULL,NULL,0,NULL),(1,2,'2026-10-13',NULL,NULL,NULL,0,NULL),(1,3,'2026-10-14',NULL,NULL,NULL,0,NULL),(1,4,'2026-10-15',NULL,NULL,NULL,0,NULL),(1,5,'2026-10-15',NULL,NULL,NULL,0,NULL),(1,6,'2026-10-16',NULL,NULL,NULL,0,NULL),(1,7,'2026-10-16',NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `met_en_vente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mode_de_payement`
--

DROP TABLE IF EXISTS `mode_de_payement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mode_de_payement` (
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mode_de_payement`
--

LOCK TABLES `mode_de_payement` WRITE;
/*!40000 ALTER TABLE `mode_de_payement` DISABLE KEYS */;
/*!40000 ALTER TABLE `mode_de_payement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mode_de_payement_libelle_langue`
--

DROP TABLE IF EXISTS `mode_de_payement_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mode_de_payement_libelle_langue` (
  `ref_mode_de_payement` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_mode_de_payement`,`ref_libelle`,`ref_langue`),
  KEY `fk_mdpll_libelle` (`ref_libelle`),
  KEY `fk_mdpll_langue` (`ref_langue`),
  CONSTRAINT `fk_mdpll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_mdpll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_mdpll_type` FOREIGN KEY (`ref_mode_de_payement`) REFERENCES `mode_de_payement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mode_de_payement_libelle_langue`
--

LOCK TABLES `mode_de_payement_libelle_langue` WRITE;
/*!40000 ALTER TABLE `mode_de_payement_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `mode_de_payement_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mois`
--

DROP TABLE IF EXISTS `mois`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mois` (
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mois`
--

LOCK TABLES `mois` WRITE;
/*!40000 ALTER TABLE `mois` DISABLE KEYS */;
INSERT INTO `mois` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12);
/*!40000 ALTER TABLE `mois` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mois_libelle_langue`
--

DROP TABLE IF EXISTS `mois_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mois_libelle_langue` (
  `ref_mois` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_mois`,`ref_libelle`,`ref_langue`),
  KEY `fk_mll_libelle` (`ref_libelle`),
  KEY `fk_mll_langue` (`ref_langue`),
  CONSTRAINT `fk_mll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_mll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_mll_mois` FOREIGN KEY (`ref_mois`) REFERENCES `mois` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mois_libelle_langue`
--

LOCK TABLES `mois_libelle_langue` WRITE;
/*!40000 ALTER TABLE `mois_libelle_langue` DISABLE KEYS */;
INSERT INTO `mois_libelle_langue` VALUES (1,100000,1),(2,100001,1),(3,100002,1),(4,100003,1),(5,100004,1),(6,100005,1),(7,100006,1),(8,100007,1),(9,100008,1),(10,100009,1),(11,1000010,1),(12,1000122,1);
/*!40000 ALTER TABLE `mois_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pays`
--

DROP TABLE IF EXISTS `pays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pays` (
  `id_pays` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(80) NOT NULL,
  `pays_code` char(2) NOT NULL,
  PRIMARY KEY (`id_pays`),
  UNIQUE KEY `uq_id_pays` (`id_pays`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pays`
--

LOCK TABLES `pays` WRITE;
/*!40000 ALTER TABLE `pays` DISABLE KEYS */;
/*!40000 ALTER TABLE `pays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pays_present`
--

DROP TABLE IF EXISTS `pays_present`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pays_present` (
  `id_pays_present` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(40) NOT NULL,
  PRIMARY KEY (`id_pays_present`),
  UNIQUE KEY `uq_id_pays_present` (`id_pays_present`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pays_present`
--

LOCK TABLES `pays_present` WRITE;
/*!40000 ALTER TABLE `pays_present` DISABLE KEYS */;
INSERT INTO `pays_present` VALUES (1,'Suisse'),(2,'France');
/*!40000 ALTER TABLE `pays_present` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pays_region_vin`
--

DROP TABLE IF EXISTS `pays_region_vin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pays_region_vin` (
  `id_pays_region_vin` int NOT NULL AUTO_INCREMENT,
  `ref_pays` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `nom` varchar(40) NOT NULL,
  PRIMARY KEY (`id_pays_region_vin`),
  KEY `fk_pays_region_vin_pays` (`ref_pays`),
  KEY `fk_pays_region_vin_parent` (`parent_id`),
  CONSTRAINT `fk_pays_region_vin_parent` FOREIGN KEY (`parent_id`) REFERENCES `pays_region_vin` (`id_pays_region_vin`),
  CONSTRAINT `fk_pays_region_vin_pays` FOREIGN KEY (`ref_pays`) REFERENCES `pays` (`id_pays`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pays_region_vin`
--

LOCK TABLES `pays_region_vin` WRITE;
/*!40000 ALTER TABLE `pays_region_vin` DISABLE KEYS */;
INSERT INTO `pays_region_vin` VALUES (1,1,1,'Suisse'),(2,2,2,'France');
/*!40000 ALTER TABLE `pays_region_vin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personne`
--

DROP TABLE IF EXISTS `personne`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personne` (
  `id_personne` int NOT NULL AUTO_INCREMENT,
  `nom_utilisateur` varchar(40) NOT NULL,
  `mot_de_passe` varchar(41) NOT NULL DEFAULT '',
  `nom` varchar(40) NOT NULL DEFAULT '',
  `prenom` varchar(40) NOT NULL DEFAULT '',
  `adresse` varchar(40) NOT NULL DEFAULT '',
  `npa` int NOT NULL DEFAULT '0',
  `ville` varchar(40) NOT NULL DEFAULT '',
  `pays` varchar(40) NOT NULL DEFAULT '',
  `email` varchar(100) NOT NULL DEFAULT '',
  `no_telephone` varchar(20) NOT NULL DEFAULT '',
  `active` int NOT NULL DEFAULT '0',
  `level` int NOT NULL,
  `ref_host` varchar(50) NOT NULL DEFAULT '',
  `lang` varchar(10) DEFAULT NULL,
  `banque` varchar(40) DEFAULT NULL,
  `no_compte` varchar(40) DEFAULT NULL,
  `iban` varchar(20) DEFAULT NULL,
  `msn_messenger` varchar(20) DEFAULT NULL,
  `skype_name` varchar(20) DEFAULT NULL,
  `date_start` varchar(20) DEFAULT NULL,
  `date_expiration` date DEFAULT NULL,
  `ref_canton` int DEFAULT NULL,
  `paypal_me` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_personne`),
  UNIQUE KEY `uq_personne_nom_utilisateur` (`nom_utilisateur`),
  UNIQUE KEY `uq_personne_email` (`email`),
  KEY `fk_personne_canton` (`ref_canton`),
  CONSTRAINT `fk_personne_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personne`
--

LOCK TABLES `personne` WRITE;
/*!40000 ALTER TABLE `personne` DISABLE KEYS */;
INSERT INTO `personne` VALUES (1,'alexjaquet','','Alexandre Jaquet','','test2',1212,'Belfaux','Suisse','alexjaquet@gmail.com','+41 78 000 00 00',1,1,'',NULL,'CHF 0000-000',NULL,'CHF 0000 0000 000',NULL,NULL,NULL,NULL,7,''),(2,'testtest','','Test','Test','',0,'','','alexandre.jaquet1980@outlook.com','',1,0,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'dev.jaquet.alexandre','','Alexandre Jaquet 23','','test',1111,'test','Suisse','dev.jaquet.alexandre@gmail.com','+41 78 000 00 00',1,1,'',NULL,'CHF 0000-000',NULL,'CHF 0000 0000 000',NULL,NULL,NULL,NULL,NULL,''),(4,'laetitia.plancherel98','','Laetitia Plancherel','','',0,'','','laetitia.plancherel98@gmail.com','',1,1,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `personne` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pointure`
--

DROP TABLE IF EXISTS `pointure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pointure` (
  `id_pointure` int NOT NULL AUTO_INCREMENT,
  `valeur` varchar(4) NOT NULL,
  PRIMARY KEY (`id_pointure`),
  UNIQUE KEY `uq_pointure_valeur` (`valeur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pointure`
--

LOCK TABLES `pointure` WRITE;
/*!40000 ALTER TABLE `pointure` DISABLE KEYS */;
/*!40000 ALTER TABLE `pointure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publication_option`
--

DROP TABLE IF EXISTS `publication_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publication_option` (
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication_option`
--

LOCK TABLES `publication_option` WRITE;
/*!40000 ALTER TABLE `publication_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `publication_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publication_option_libelle_langue`
--

DROP TABLE IF EXISTS `publication_option_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publication_option_libelle_langue` (
  `ref_publication_option` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  `prix` decimal(5,0) NOT NULL,
  PRIMARY KEY (`ref_publication_option`,`ref_libelle`,`ref_langue`),
  KEY `fk_poll_libelle` (`ref_libelle`),
  KEY `fk_poll_langue` (`ref_langue`),
  CONSTRAINT `fk_poll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_poll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_poll_option` FOREIGN KEY (`ref_publication_option`) REFERENCES `publication_option` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication_option_libelle_langue`
--

LOCK TABLES `publication_option_libelle_langue` WRITE;
/*!40000 ALTER TABLE `publication_option_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `publication_option_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recherche`
--

DROP TABLE IF EXISTS `recherche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recherche` (
  `id_recherche` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(40) NOT NULL,
  `nbr` int NOT NULL,
  `ref_categorie` int NOT NULL,
  PRIMARY KEY (`id_recherche`),
  KEY `fk_recherche_categorie` (`ref_categorie`),
  CONSTRAINT `fk_recherche_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie` (`id_categorie`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recherche`
--

LOCK TABLES `recherche` WRITE;
/*!40000 ALTER TABLE `recherche` DISABLE KEYS */;
/*!40000 ALTER TABLE `recherche` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` char(32) NOT NULL,
  `a_session` text NOT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `states`
--

DROP TABLE IF EXISTS `states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `states` (
  `id` int NOT NULL AUTO_INCREMENT,
  `state` varchar(50) NOT NULL,
  `abbrev` char(2) NOT NULL,
  PRIMARY KEY (`id`,`state`,`abbrev`),
  UNIQUE KEY `uq_states_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `states`
--

LOCK TABLES `states` WRITE;
/*!40000 ALTER TABLE `states` DISABLE KEYS */;
/*!40000 ALTER TABLE `states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statut_libelle_langue`
--

DROP TABLE IF EXISTS `statut_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statut_libelle_langue` (
  `ref_statut` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_statut`,`ref_libelle`,`ref_langue`),
  KEY `fk_sll_libelle` (`ref_libelle`),
  KEY `fk_sll_langue` (`ref_langue`),
  CONSTRAINT `fk_sll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_sll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statut_libelle_langue`
--

LOCK TABLES `statut_libelle_langue` WRITE;
/*!40000 ALTER TABLE `statut_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `statut_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategorie_libelle_langue`
--

DROP TABLE IF EXISTS `subcategorie_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategorie_libelle_langue` (
  `ref_subcategorie` int NOT NULL,
  `ref_categorie` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_subcategorie`,`ref_categorie`,`ref_langue`),
  KEY `fk_subcat_ll_langue` (`ref_langue`),
  KEY `fk_subcat_ll_libelle` (`ref_libelle`),
  KEY `fk_subcat_ll_categorie` (`ref_categorie`),
  CONSTRAINT `fk_subcat_ll_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie` (`id_categorie`),
  CONSTRAINT `fk_subcat_ll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_subcat_ll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategorie_libelle_langue`
--

LOCK TABLES `subcategorie_libelle_langue` WRITE;
/*!40000 ALTER TABLE `subcategorie_libelle_langue` DISABLE KEYS */;
INSERT INTO `subcategorie_libelle_langue` VALUES (130,12,9,1),(1,1,26,1),(2,2,26,1),(26,3,26,1),(23,2,27,1),(24,1,27,1),(25,3,27,1),(7,1,28,1),(21,3,28,1),(20,3,29,1),(31,1,29,1),(10,3,30,1),(11,2,30,1),(30,1,30,1),(12,3,31,1),(13,2,31,1),(32,1,31,1),(14,2,32,1),(15,3,32,1),(33,1,32,1),(16,2,33,1),(17,3,33,1),(34,1,33,1),(18,3,34,1),(19,2,34,1),(35,1,34,1),(27,3,35,1),(28,3,36,1),(29,3,37,1),(3,3,39,1),(41,22222,41,1),(42,22222,42,1),(43,22222,43,1),(44,22222,44,1),(47,22222,47,1),(48,22222,48,1),(49,22222,49,1),(50,22222,50,1),(51,22222,51,1),(51,5,52,1),(52,5,53,1),(53,5,54,1),(54,5,55,1),(55,5,56,1),(56,5,57,1),(57,6,58,1),(58,6,59,1),(59,6,60,1),(60,6,61,1),(61,6,62,1),(62,6,63,1),(63,6,64,1),(64,6,65,1),(65,6,66,1),(66,6,67,1),(67,6,68,1),(68,7,70,1),(69,7,71,1),(70,7,72,1),(71,7,73,1),(72,7,74,1),(77,188,77,1),(78,188,78,1),(79,188,79,1),(80,188,80,1),(81,188,81,1),(82,188,82,1),(83,188,83,1),(84,188,84,1),(85,188,85,1),(86,188,86,1),(87,188,87,1),(88,188,88,1),(89,188,89,1),(90,188,90,1),(91,188,91,1),(92,188,92,1),(93,188,93,1),(94,188,94,1),(377,35,97,1),(371,34,98,1),(378,35,98,1),(372,34,99,1),(379,35,99,1),(373,34,100,1),(310,26,118,1),(553,98,118,1),(311,26,119,1),(552,98,119,1),(312,26,120,1),(555,98,120,1),(313,26,121,1),(554,98,121,1),(314,26,122,1),(315,26,129,1),(316,26,131,1),(317,26,132,1),(318,26,133,1),(319,26,133,1),(320,26,135,1),(321,26,136,1),(322,26,138,1),(324,26,140,1),(325,26,141,1),(326,26,142,1),(327,26,143,1),(328,26,144,1),(329,26,145,1),(330,26,146,1),(331,26,147,1),(332,26,148,1),(333,26,149,1),(557,98,149,1),(334,26,150,1),(556,98,150,1),(335,26,151,1),(336,26,152,1),(337,26,153,1),(559,98,153,1),(338,26,154,1),(558,98,154,1),(339,26,155,1),(572,97,155,1),(340,26,156,1),(571,97,156,1),(341,26,157,1),(570,97,157,1),(342,26,158,1),(569,97,158,1),(343,26,159,1),(568,97,159,1),(344,26,160,1),(566,97,160,1),(345,26,161,1),(567,97,161,1),(346,26,162,1),(347,26,163,1),(348,26,164,1),(349,26,165,1),(350,26,166,1),(351,26,167,1),(352,26,168,1),(353,26,169,1),(578,96,169,1),(354,26,170,1),(577,96,170,1),(355,26,171,1),(576,96,171,1),(356,26,172,1),(575,96,172,1),(357,26,173,1),(574,96,173,1),(358,26,174,1),(573,96,174,1),(537,38,175,1),(538,38,176,1),(539,38,177,1),(540,38,178,1),(541,38,179,1),(542,38,180,1),(543,38,181,1),(544,38,182,1),(545,38,183,1),(547,38,185,1),(548,38,186,1),(549,38,187,1),(93,9,188,1),(94,9,189,1),(95,9,190,1),(96,9,191,1),(97,9,192,1),(98,9,193,1),(99,9,194,1),(100,9,195,1),(101,9,196,1),(102,9,197,1),(103,9,198,1),(104,9,199,1),(105,9,201,1),(106,9,202,1),(107,9,203,1),(108,9,204,1),(109,9,205,1),(110,9,206,1),(111,9,207,1),(112,9,208,1),(113,9,208,1),(114,9,209,1),(116,9,211,1),(117,9,212,1),(118,9,213,1),(119,9,214,1),(126,95,215,1),(127,95,216,1),(128,95,217,1),(129,12,218,1),(131,13,219,1),(132,13,220,1),(138,15,222,1),(443,92,222,1),(139,15,223,1),(444,92,223,1),(140,15,224,1),(445,92,224,1),(141,15,225,1),(143,15,225,1),(446,92,225,1),(142,15,226,1),(144,15,226,1),(145,15,226,1),(447,92,226,1),(146,15,227,1),(147,15,227,1),(151,15,227,1),(448,92,227,1),(148,15,228,1),(152,15,228,1),(449,92,228,1),(149,15,229,1),(153,15,229,1),(450,92,229,1),(150,15,230,1),(154,15,230,1),(451,92,230,1),(155,15,231,1),(452,92,231,1),(156,15,232,1),(453,92,232,1),(157,15,233,1),(454,92,233,1),(158,15,234,1),(455,92,234,1),(159,15,235,1),(456,92,235,1),(160,15,236,1),(457,92,236,1),(161,15,237,1),(458,92,237,1),(162,15,238,1),(459,92,238,1),(163,15,239,1),(164,15,239,1),(460,92,239,1),(165,15,240,1),(461,92,240,1),(166,15,241,1),(462,92,241,1),(167,15,242,1),(463,92,242,1),(168,15,243,1),(464,92,243,1),(169,15,244,1),(465,93,244,1),(170,15,245,1),(466,93,245,1),(171,15,246,1),(467,93,246,1),(172,15,247,1),(468,93,247,1),(173,15,248,1),(469,93,248,1),(174,15,249,1),(470,93,249,1),(175,15,250,1),(471,93,250,1),(176,15,251,1),(472,93,251,1),(177,15,252,1),(473,93,252,1),(178,15,253,1),(474,93,253,1),(179,15,254,1),(475,93,254,1),(180,15,255,1),(476,93,255,1),(200,18,290,1),(477,18,290,1),(201,18,291,1),(478,18,291,1),(202,18,292,1),(479,18,292,1),(203,18,293,1),(480,18,293,1),(204,18,294,1),(481,18,294,1),(205,18,295,1),(482,18,295,1),(206,18,296,1),(483,18,296,1),(207,18,314,1),(501,18,314,1),(208,18,315,1),(502,18,315,1),(209,18,316,1),(503,18,316,1),(210,18,317,1),(504,18,317,1),(211,18,318,1),(213,18,318,1),(505,18,318,1),(212,18,319,1),(214,18,319,1),(506,18,319,1),(215,18,320,1),(507,18,320,1),(216,18,321,1),(508,18,321,1),(217,18,322,1),(509,18,322,1),(218,18,323,1),(510,18,323,1),(511,18,324,1),(220,18,325,1),(512,18,325,1),(221,18,326,1),(513,18,326,1),(222,18,327,1),(514,18,327,1),(223,18,328,1),(515,18,328,1),(224,18,329,1),(516,18,329,1),(225,18,330,1),(517,18,330,1),(226,19,331,1),(518,18,331,1),(227,19,332,1),(519,18,332,1),(228,19,333,1),(520,18,333,1),(229,19,334,1),(521,18,334,1),(230,19,335,1),(522,18,335,1),(231,19,336,1),(523,18,336,1),(232,19,337,1),(233,19,338,1),(524,18,338,1),(234,19,339,1),(525,18,339,1),(235,19,340,1),(526,18,340,1),(236,19,341,1),(527,18,341,1),(237,19,342,1),(528,18,342,1),(238,19,343,1),(529,18,343,1),(239,20,345,1),(380,36,345,1),(240,20,346,1),(381,36,346,1),(241,20,347,1),(382,36,347,1),(242,20,348,1),(383,36,348,1),(243,20,349,1),(384,36,349,1),(244,20,350,1),(385,36,350,1),(245,20,351,1),(386,36,351,1),(133,14,361,1),(387,36,361,1),(134,14,362,1),(388,36,362,1),(135,14,363,1),(136,14,364,1),(137,14,365,1),(256,21,418,1),(257,21,419,1),(258,21,420,1),(259,21,421,1),(260,21,422,1),(261,21,423,1),(262,22,426,1),(263,22,427,1),(262,23,429,1),(263,23,430,1),(264,23,431,1),(265,23,432,1),(266,23,433,1),(267,23,434,1),(268,23,435,1),(269,23,436,1),(270,23,437,1),(271,23,438,1),(272,23,439,1),(273,23,440,1),(274,23,441,1),(275,23,442,1),(276,23,443,1),(277,23,444,1),(278,23,445,1),(279,23,446,1),(280,23,447,1),(281,23,448,1),(282,23,449,1),(283,23,450,1),(284,23,451,1),(285,23,452,1),(286,23,453,1),(287,23,454,1),(289,23,454,1),(288,23,455,1),(290,23,455,1),(291,23,456,1),(292,23,458,1),(293,24,459,1),(294,24,460,1),(295,24,461,1),(296,24,462,1),(297,24,463,1),(298,24,464,1),(299,24,465,1),(300,24,466,1),(301,24,467,1),(302,24,468,1),(303,25,469,1),(304,25,470,1),(305,25,471,1),(307,25,473,1),(309,25,475,1),(550,99,554,1),(551,99,555,1),(565,97,600,1),(564,97,601,1),(563,97,602,1),(562,97,603,1),(561,97,604,1),(560,97,605,1),(181,16,771,1),(182,16,772,1),(183,16,773,1),(184,16,774,1),(185,16,775,1),(186,16,776,1),(187,16,777,1),(188,16,778,1),(189,16,779,1),(190,16,780,1),(191,16,781,1),(192,16,782,1),(193,16,783,1),(194,16,784,1),(195,16,785,1),(196,16,786,1),(197,16,787,1),(198,16,788,1),(199,16,789,1),(246,20,6352,1),(247,20,6353,1),(248,20,6354,1),(249,20,6355,1),(250,20,6356,1),(251,20,6357,1),(252,20,6358,1),(253,20,6359,1),(254,20,6360,1),(255,20,6380,1),(361,31,8424,1),(362,31,8425,1),(120,11,9000,1),(121,11,9001,1),(122,11,9002,1),(123,11,9003,1),(124,11,9005,1),(125,95,9006,1),(1,27,10001,1),(2,27,10002,1),(3,27,10003,1),(4,27,10004,1),(363,32,10022,1),(364,32,10023,1),(365,33,10025,1),(366,33,10026,1),(367,33,10027,1),(368,33,10028,1),(999999,35,10030,1),(442,41,10048,1),(531,94,10050,1),(532,94,10051,1),(536,94,10053,1),(99999,99,1000124,1),(0,34,1000125,1),(10000,34,1000125,1),(10001,34,1000126,1),(1000000,35,1000126,1),(130,12,10000009,3),(1,1,10000026,3),(2,2,10000026,3),(26,3,10000026,3),(23,2,10000027,3),(24,1,10000027,3),(25,3,10000027,3),(7,1,10000028,3),(21,3,10000028,3),(20,3,10000029,3),(31,1,10000029,3),(10,3,10000030,3),(11,2,10000030,3),(30,1,10000030,3),(12,3,10000031,3),(13,2,10000031,3),(32,1,10000031,3),(14,2,10000032,3),(15,3,10000032,3),(33,1,10000032,3),(16,2,10000033,3),(17,3,10000033,3),(34,1,10000033,3),(18,3,10000034,3),(19,2,10000034,3),(35,1,10000034,3),(27,3,10000035,3),(28,3,10000036,3),(29,3,10000037,3),(3,3,10000039,3),(41,22222,10000041,3),(42,22222,10000042,3),(43,22222,10000043,3),(44,22222,10000044,3),(47,22222,10000047,3),(48,22222,10000048,3),(49,22222,10000049,3),(50,22222,10000050,3),(51,22222,10000051,3),(51,5,10000052,3),(52,5,10000053,3),(53,5,10000054,3),(54,5,10000055,3),(55,5,10000056,3),(56,5,10000057,3),(57,6,10000058,3),(58,6,10000059,3),(59,6,10000060,3),(60,6,10000061,3),(61,6,10000062,3),(62,6,10000063,3),(63,6,10000064,3),(64,6,10000065,3),(65,6,10000066,3),(66,6,10000067,3),(67,6,10000068,3),(68,7,10000070,3),(69,7,10000071,3),(70,7,10000072,3),(71,7,10000073,3),(72,7,10000074,3),(77,188,10000077,3),(78,188,10000078,3),(79,188,10000079,3),(80,188,10000080,3),(81,188,10000081,3),(82,188,10000082,3),(83,188,10000083,3),(84,188,10000084,3),(85,188,10000085,3),(86,188,10000086,3),(87,188,10000087,3),(88,188,10000088,3),(89,188,10000089,3),(90,188,10000090,3),(91,188,10000091,3),(92,188,10000092,3),(93,188,10000093,3),(94,188,10000094,3),(377,35,10000097,3),(371,34,10000098,3),(378,35,10000098,3),(372,34,10000099,3),(379,35,10000099,3),(373,34,10000100,3),(310,26,10000118,3),(553,98,10000118,3),(311,26,10000119,3),(552,98,10000119,3),(312,26,10000120,3),(555,98,10000120,3),(313,26,10000121,3),(554,98,10000121,3),(314,26,10000122,3),(315,26,10000129,3),(316,26,10000131,3),(317,26,10000132,3),(318,26,10000133,3),(319,26,10000133,3),(320,26,10000135,3),(321,26,10000136,3),(322,26,10000138,3),(324,26,10000140,3),(325,26,10000141,3),(326,26,10000142,3),(327,26,10000143,3),(328,26,10000144,3),(329,26,10000145,3),(330,26,10000146,3),(331,26,10000147,3),(332,26,10000148,3),(333,26,10000149,3),(557,98,10000149,3),(334,26,10000150,3),(556,98,10000150,3),(335,26,10000151,3),(336,26,10000152,3),(337,26,10000153,3),(559,98,10000153,3),(338,26,10000154,3),(558,98,10000154,3),(339,26,10000155,3),(572,97,10000155,3),(340,26,10000156,3),(571,97,10000156,3),(341,26,10000157,3),(570,97,10000157,3),(342,26,10000158,3),(569,97,10000158,3),(343,26,10000159,3),(568,97,10000159,3),(344,26,10000160,3),(566,97,10000160,3),(345,26,10000161,3),(567,97,10000161,3),(346,26,10000162,3),(347,26,10000163,3),(348,26,10000164,3),(349,26,10000165,3),(350,26,10000166,3),(351,26,10000167,3),(352,26,10000168,3),(353,26,10000169,3),(578,96,10000169,3),(354,26,10000170,3),(577,96,10000170,3),(355,26,10000171,3),(576,96,10000171,3),(356,26,10000172,3),(575,96,10000172,3),(357,26,10000173,3),(574,96,10000173,3),(358,26,10000174,3),(573,96,10000174,3),(537,38,10000175,3),(538,38,10000176,3),(539,38,10000177,3),(540,38,10000178,3),(541,38,10000179,3),(542,38,10000180,3),(543,38,10000181,3),(544,38,10000182,3),(545,38,10000183,3),(547,38,10000185,3),(548,38,10000186,3),(549,38,10000187,3),(93,9,10000188,3),(94,9,10000189,3),(95,9,10000190,3),(96,9,10000191,3),(97,9,10000192,3),(98,9,10000193,3),(99,9,10000194,3),(100,9,10000195,3),(101,9,10000196,3),(102,9,10000197,3),(103,9,10000198,3),(104,9,10000199,3),(105,9,10000201,3),(106,9,10000202,3),(107,9,10000203,3),(108,9,10000204,3),(109,9,10000205,3),(110,9,10000206,3),(111,9,10000207,3),(112,9,10000208,3),(113,9,10000208,3),(114,9,10000209,3),(116,9,10000211,3),(117,9,10000212,3),(118,9,10000213,3),(119,9,10000214,3),(126,95,10000215,3),(127,95,10000216,3),(128,95,10000217,3),(129,12,10000218,3),(131,13,10000219,3),(132,13,10000220,3),(138,15,10000222,3),(443,92,10000222,3),(139,15,10000223,3),(444,92,10000223,3),(140,15,10000224,3),(445,92,10000224,3),(141,15,10000225,3),(143,15,10000225,3),(446,92,10000225,3),(142,15,10000226,3),(144,15,10000226,3),(145,15,10000226,3),(447,92,10000226,3),(146,15,10000227,3),(147,15,10000227,3),(151,15,10000227,3),(448,92,10000227,3),(148,15,10000228,3),(152,15,10000228,3),(449,92,10000228,3),(149,15,10000229,3),(153,15,10000229,3),(450,92,10000229,3),(150,15,10000230,3),(154,15,10000230,3),(451,92,10000230,3),(155,15,10000231,3),(452,92,10000231,3),(156,15,10000232,3),(453,92,10000232,3),(157,15,10000233,3),(454,92,10000233,3),(158,15,10000234,3),(455,92,10000234,3),(159,15,10000235,3),(456,92,10000235,3),(160,15,10000236,3),(457,92,10000236,3),(161,15,10000237,3),(458,92,10000237,3),(162,15,10000238,3),(459,92,10000238,3),(163,15,10000239,3),(164,15,10000239,3),(460,92,10000239,3),(165,15,10000240,3),(461,92,10000240,3),(166,15,10000241,3),(462,92,10000241,3),(167,15,10000242,3),(463,92,10000242,3),(168,15,10000243,3),(464,92,10000243,3),(169,15,10000244,3),(465,93,10000244,3),(170,15,10000245,3),(466,93,10000245,3),(171,15,10000246,3),(467,93,10000246,3),(172,15,10000247,3),(468,93,10000247,3),(173,15,10000248,3),(469,93,10000248,3),(174,15,10000249,3),(470,93,10000249,3),(175,15,10000250,3),(471,93,10000250,3),(176,15,10000251,3),(472,93,10000251,3),(177,15,10000252,3),(473,93,10000252,3),(178,15,10000253,3),(474,93,10000253,3),(179,15,10000254,3),(475,93,10000254,3),(180,15,10000255,3),(476,93,10000255,3),(200,18,10000290,3),(477,18,10000290,3),(201,18,10000291,3),(478,18,10000291,3),(202,18,10000292,3),(479,18,10000292,3),(203,18,10000293,3),(480,18,10000293,3),(204,18,10000294,3),(481,18,10000294,3),(205,18,10000295,3),(482,18,10000295,3),(206,18,10000296,3),(483,18,10000296,3),(207,18,10000314,3),(501,18,10000314,3),(208,18,10000315,3),(502,18,10000315,3),(209,18,10000316,3),(503,18,10000316,3),(210,18,10000317,3),(504,18,10000317,3),(211,18,10000318,3),(213,18,10000318,3),(505,18,10000318,3),(212,18,10000319,3),(214,18,10000319,3),(506,18,10000319,3),(215,18,10000320,3),(507,18,10000320,3),(216,18,10000321,3),(508,18,10000321,3),(217,18,10000322,3),(509,18,10000322,3),(218,18,10000323,3),(510,18,10000323,3),(511,18,10000324,3),(220,18,10000325,3),(512,18,10000325,3),(221,18,10000326,3),(513,18,10000326,3),(222,18,10000327,3),(514,18,10000327,3),(223,18,10000328,3),(515,18,10000328,3),(224,18,10000329,3),(516,18,10000329,3),(225,18,10000330,3),(517,18,10000330,3),(226,19,10000331,3),(518,18,10000331,3),(227,19,10000332,3),(519,18,10000332,3),(228,19,10000333,3),(520,18,10000333,3),(229,19,10000334,3),(521,18,10000334,3),(230,19,10000335,3),(522,18,10000335,3),(231,19,10000336,3),(523,18,10000336,3),(232,19,10000337,3),(233,19,10000338,3),(524,18,10000338,3),(234,19,10000339,3),(525,18,10000339,3),(235,19,10000340,3),(526,18,10000340,3),(236,19,10000341,3),(527,18,10000341,3),(237,19,10000342,3),(528,18,10000342,3),(238,19,10000343,3),(529,18,10000343,3),(239,20,10000345,3),(380,36,10000345,3),(240,20,10000346,3),(381,36,10000346,3),(241,20,10000347,3),(382,36,10000347,3),(242,20,10000348,3),(383,36,10000348,3),(243,20,10000349,3),(384,36,10000349,3),(244,20,10000350,3),(385,36,10000350,3),(245,20,10000351,3),(386,36,10000351,3),(133,14,10000361,3),(387,36,10000361,3),(134,14,10000362,3),(388,36,10000362,3),(135,14,10000363,3),(136,14,10000364,3),(137,14,10000365,3),(256,21,10000418,3),(257,21,10000419,3),(258,21,10000420,3),(259,21,10000421,3),(260,21,10000422,3),(261,21,10000423,3),(262,22,10000426,3),(263,22,10000427,3),(262,23,10000429,3),(263,23,10000430,3),(264,23,10000431,3),(265,23,10000432,3),(266,23,10000433,3),(267,23,10000434,3),(268,23,10000435,3),(269,23,10000436,3),(270,23,10000437,3),(271,23,10000438,3),(272,23,10000439,3),(273,23,10000440,3),(274,23,10000441,3),(275,23,10000442,3),(276,23,10000443,3),(277,23,10000444,3),(278,23,10000445,3),(279,23,10000446,3),(280,23,10000447,3),(281,23,10000448,3),(282,23,10000449,3),(283,23,10000450,3),(284,23,10000451,3),(285,23,10000452,3),(286,23,10000453,3),(287,23,10000454,3),(289,23,10000454,3),(288,23,10000455,3),(290,23,10000455,3),(291,23,10000456,3),(292,23,10000458,3),(293,24,10000459,3),(294,24,10000460,3),(295,24,10000461,3),(296,24,10000462,3),(297,24,10000463,3),(298,24,10000464,3),(299,24,10000465,3),(300,24,10000466,3),(301,24,10000467,3),(302,24,10000468,3),(303,25,10000469,3),(304,25,10000470,3),(305,25,10000471,3),(307,25,10000473,3),(309,25,10000475,3),(550,99,10000554,3),(551,99,10000555,3),(565,97,10000600,3),(564,97,10000601,3),(563,97,10000602,3),(562,97,10000603,3),(561,97,10000604,3),(560,97,10000605,3),(181,16,10000771,3),(182,16,10000772,3),(183,16,10000773,3),(184,16,10000774,3),(185,16,10000775,3),(186,16,10000776,3),(187,16,10000777,3),(188,16,10000778,3),(189,16,10000779,3),(190,16,10000780,3),(191,16,10000781,3),(192,16,10000782,3),(193,16,10000783,3),(194,16,10000784,3),(195,16,10000785,3),(196,16,10000786,3),(197,16,10000787,3),(198,16,10000788,3),(199,16,10000789,3),(246,20,10006352,3),(247,20,10006353,3),(248,20,10006354,3),(249,20,10006355,3),(250,20,10006356,3),(251,20,10006357,3),(252,20,10006358,3),(253,20,10006359,3),(254,20,10006360,3),(255,20,10006380,3),(361,31,10008424,3),(362,31,10008425,3),(120,11,10009000,3),(121,11,10009001,3),(122,11,10009002,3),(123,11,10009003,3),(124,11,10009005,3),(125,95,10009006,3),(1,27,10010001,3),(2,27,10010002,3),(3,27,10010003,3),(4,27,10010004,3),(363,32,10010022,3),(364,32,10010023,3),(365,33,10010025,3),(366,33,10010026,3),(367,33,10010027,3),(368,33,10010028,3),(999999,35,10010030,3),(531,94,10010050,3),(532,94,10010051,3),(536,94,10010053,3),(99999,99,11000124,3),(0,34,11000125,3),(10000,34,11000125,3),(10001,34,11000126,3),(1000000,35,11000126,3),(130,12,20000009,2),(1,1,20000026,2),(2,2,20000026,2),(26,3,20000026,2),(23,2,20000027,2),(24,1,20000027,2),(25,3,20000027,2),(7,1,20000028,2),(21,3,20000028,2),(20,3,20000029,2),(31,1,20000029,2),(10,3,20000030,2),(11,2,20000030,2),(30,1,20000030,2),(12,3,20000031,2),(13,2,20000031,2),(32,1,20000031,2),(14,2,20000032,2),(15,3,20000032,2),(33,1,20000032,2),(16,2,20000033,2),(17,3,20000033,2),(34,1,20000033,2),(18,3,20000034,2),(19,2,20000034,2),(35,1,20000034,2),(27,3,20000035,2),(28,3,20000036,2),(29,3,20000037,2),(3,3,20000039,2),(41,22222,20000041,2),(42,22222,20000042,2),(43,22222,20000043,2),(44,22222,20000044,2),(47,22222,20000047,2),(48,22222,20000048,2),(49,22222,20000049,2),(50,22222,20000050,2),(51,22222,20000051,2),(51,5,20000052,2),(52,5,20000053,2),(53,5,20000054,2),(54,5,20000055,2),(55,5,20000056,2),(56,5,20000057,2),(57,6,20000058,2),(58,6,20000059,2),(59,6,20000060,2),(60,6,20000061,2),(61,6,20000062,2),(62,6,20000063,2),(63,6,20000064,2),(64,6,20000065,2),(65,6,20000066,2),(66,6,20000067,2),(67,6,20000068,2),(68,7,20000070,2),(69,7,20000071,2),(70,7,20000072,2),(71,7,20000073,2),(72,7,20000074,2),(77,188,20000077,2),(78,188,20000078,2),(79,188,20000079,2),(80,188,20000080,2),(81,188,20000081,2),(82,188,20000082,2),(83,188,20000083,2),(84,188,20000084,2),(85,188,20000085,2),(86,188,20000086,2),(87,188,20000087,2),(88,188,20000088,2),(89,188,20000089,2),(90,188,20000090,2),(91,188,20000091,2),(92,188,20000092,2),(93,188,20000093,2),(94,188,20000094,2),(377,35,20000097,2),(371,34,20000098,2),(378,35,20000098,2),(372,34,20000099,2),(379,35,20000099,2),(373,34,20000100,2),(310,26,20000118,2),(553,98,20000118,2),(311,26,20000119,2),(552,98,20000119,2),(312,26,20000120,2),(555,98,20000120,2),(313,26,20000121,2),(554,98,20000121,2),(314,26,20000122,2),(315,26,20000129,2),(316,26,20000131,2),(317,26,20000132,2),(318,26,20000133,2),(319,26,20000133,2),(320,26,20000135,2),(321,26,20000136,2),(322,26,20000138,2),(324,26,20000140,2),(325,26,20000141,2),(326,26,20000142,2),(327,26,20000143,2),(328,26,20000144,2),(329,26,20000145,2),(330,26,20000146,2),(331,26,20000147,2),(332,26,20000148,2),(333,26,20000149,2),(557,98,20000149,2),(334,26,20000150,2),(556,98,20000150,2),(335,26,20000151,2),(336,26,20000152,2),(337,26,20000153,2),(559,98,20000153,2),(338,26,20000154,2),(558,98,20000154,2),(339,26,20000155,2),(572,97,20000155,2),(340,26,20000156,2),(571,97,20000156,2),(341,26,20000157,2),(570,97,20000157,2),(342,26,20000158,2),(569,97,20000158,2),(343,26,20000159,2),(568,97,20000159,2),(344,26,20000160,2),(566,97,20000160,2),(345,26,20000161,2),(567,97,20000161,2),(346,26,20000162,2),(347,26,20000163,2),(348,26,20000164,2),(349,26,20000165,2),(350,26,20000166,2),(351,26,20000167,2),(352,26,20000168,2),(353,26,20000169,2),(578,96,20000169,2),(354,26,20000170,2),(577,96,20000170,2),(355,26,20000171,2),(576,96,20000171,2),(356,26,20000172,2),(575,96,20000172,2),(357,26,20000173,2),(574,96,20000173,2),(358,26,20000174,2),(573,96,20000174,2),(537,38,20000175,2),(538,38,20000176,2),(539,38,20000177,2),(540,38,20000178,2),(541,38,20000179,2),(542,38,20000180,2),(543,38,20000181,2),(544,38,20000182,2),(545,38,20000183,2),(547,38,20000185,2),(548,38,20000186,2),(549,38,20000187,2),(93,9,20000188,2),(94,9,20000189,2),(95,9,20000190,2),(96,9,20000191,2),(97,9,20000192,2),(98,9,20000193,2),(99,9,20000194,2),(100,9,20000195,2),(101,9,20000196,2),(102,9,20000197,2),(103,9,20000198,2),(104,9,20000199,2),(105,9,20000201,2),(106,9,20000202,2),(107,9,20000203,2),(108,9,20000204,2),(109,9,20000205,2),(110,9,20000206,2),(111,9,20000207,2),(112,9,20000208,2),(113,9,20000208,2),(114,9,20000209,2),(116,9,20000211,2),(117,9,20000212,2),(118,9,20000213,2),(119,9,20000214,2),(126,95,20000215,2),(127,95,20000216,2),(128,95,20000217,2),(129,12,20000218,2),(131,13,20000219,2),(132,13,20000220,2),(138,15,20000222,2),(443,92,20000222,2),(139,15,20000223,2),(444,92,20000223,2),(140,15,20000224,2),(445,92,20000224,2),(141,15,20000225,2),(143,15,20000225,2),(446,92,20000225,2),(142,15,20000226,2),(144,15,20000226,2),(145,15,20000226,2),(447,92,20000226,2),(146,15,20000227,2),(147,15,20000227,2),(151,15,20000227,2),(448,92,20000227,2),(148,15,20000228,2),(152,15,20000228,2),(449,92,20000228,2),(149,15,20000229,2),(153,15,20000229,2),(450,92,20000229,2),(150,15,20000230,2),(154,15,20000230,2),(451,92,20000230,2),(155,15,20000231,2),(452,92,20000231,2),(156,15,20000232,2),(453,92,20000232,2),(157,15,20000233,2),(454,92,20000233,2),(158,15,20000234,2),(455,92,20000234,2),(159,15,20000235,2),(456,92,20000235,2),(160,15,20000236,2),(457,92,20000236,2),(161,15,20000237,2),(458,92,20000237,2),(162,15,20000238,2),(459,92,20000238,2),(163,15,20000239,2),(164,15,20000239,2),(460,92,20000239,2),(165,15,20000240,2),(461,92,20000240,2),(166,15,20000241,2),(462,92,20000241,2),(167,15,20000242,2),(463,92,20000242,2),(168,15,20000243,2),(464,92,20000243,2),(169,15,20000244,2),(465,93,20000244,2),(170,15,20000245,2),(466,93,20000245,2),(171,15,20000246,2),(467,93,20000246,2),(172,15,20000247,2),(468,93,20000247,2),(173,15,20000248,2),(469,93,20000248,2),(174,15,20000249,2),(470,93,20000249,2),(175,15,20000250,2),(471,93,20000250,2),(176,15,20000251,2),(472,93,20000251,2),(177,15,20000252,2),(473,93,20000252,2),(178,15,20000253,2),(474,93,20000253,2),(179,15,20000254,2),(475,93,20000254,2),(180,15,20000255,2),(476,93,20000255,2),(200,18,20000290,2),(477,18,20000290,2),(201,18,20000291,2),(478,18,20000291,2),(202,18,20000292,2),(479,18,20000292,2),(203,18,20000293,2),(480,18,20000293,2),(204,18,20000294,2),(481,18,20000294,2),(205,18,20000295,2),(482,18,20000295,2),(206,18,20000296,2),(483,18,20000296,2),(207,18,20000314,2),(501,18,20000314,2),(208,18,20000315,2),(502,18,20000315,2),(209,18,20000316,2),(503,18,20000316,2),(210,18,20000317,2),(504,18,20000317,2),(211,18,20000318,2),(213,18,20000318,2),(505,18,20000318,2),(212,18,20000319,2),(214,18,20000319,2),(506,18,20000319,2),(215,18,20000320,2),(507,18,20000320,2),(216,18,20000321,2),(508,18,20000321,2),(217,18,20000322,2),(509,18,20000322,2),(218,18,20000323,2),(510,18,20000323,2),(511,18,20000324,2),(220,18,20000325,2),(512,18,20000325,2),(221,18,20000326,2),(513,18,20000326,2),(222,18,20000327,2),(514,18,20000327,2),(223,18,20000328,2),(515,18,20000328,2),(224,18,20000329,2),(516,18,20000329,2),(225,18,20000330,2),(517,18,20000330,2),(226,19,20000331,2),(518,18,20000331,2),(227,19,20000332,2),(519,18,20000332,2),(228,19,20000333,2),(520,18,20000333,2),(229,19,20000334,2),(521,18,20000334,2),(230,19,20000335,2),(522,18,20000335,2),(231,19,20000336,2),(523,18,20000336,2),(232,19,20000337,2),(233,19,20000338,2),(524,18,20000338,2),(234,19,20000339,2),(525,18,20000339,2),(235,19,20000340,2),(526,18,20000340,2),(236,19,20000341,2),(527,18,20000341,2),(237,19,20000342,2),(528,18,20000342,2),(238,19,20000343,2),(529,18,20000343,2),(239,20,20000345,2),(380,36,20000345,2),(240,20,20000346,2),(381,36,20000346,2),(241,20,20000347,2),(382,36,20000347,2),(242,20,20000348,2),(383,36,20000348,2),(243,20,20000349,2),(384,36,20000349,2),(244,20,20000350,2),(385,36,20000350,2),(245,20,20000351,2),(386,36,20000351,2),(133,14,20000361,2),(387,36,20000361,2),(134,14,20000362,2),(388,36,20000362,2),(135,14,20000363,2),(136,14,20000364,2),(137,14,20000365,2),(256,21,20000418,2),(257,21,20000419,2),(258,21,20000420,2),(259,21,20000421,2),(260,21,20000422,2),(261,21,20000423,2),(262,22,20000426,2),(263,22,20000427,2),(262,23,20000429,2),(263,23,20000430,2),(264,23,20000431,2),(265,23,20000432,2),(266,23,20000433,2),(267,23,20000434,2),(268,23,20000435,2),(269,23,20000436,2),(270,23,20000437,2),(271,23,20000438,2),(272,23,20000439,2),(273,23,20000440,2),(274,23,20000441,2),(275,23,20000442,2),(276,23,20000443,2),(277,23,20000444,2),(278,23,20000445,2),(279,23,20000446,2),(280,23,20000447,2),(281,23,20000448,2),(282,23,20000449,2),(283,23,20000450,2),(284,23,20000451,2),(285,23,20000452,2),(286,23,20000453,2),(287,23,20000454,2),(289,23,20000454,2),(288,23,20000455,2),(290,23,20000455,2),(291,23,20000456,2),(292,23,20000458,2),(293,24,20000459,2),(294,24,20000460,2),(295,24,20000461,2),(296,24,20000462,2),(297,24,20000463,2),(298,24,20000464,2),(299,24,20000465,2),(300,24,20000466,2),(301,24,20000467,2),(302,24,20000468,2),(303,25,20000469,2),(304,25,20000470,2),(305,25,20000471,2),(307,25,20000473,2),(309,25,20000475,2),(550,99,20000554,2),(551,99,20000555,2),(565,97,20000600,2),(564,97,20000601,2),(563,97,20000602,2),(562,97,20000603,2),(561,97,20000604,2),(560,97,20000605,2),(181,16,20000771,2),(182,16,20000772,2),(183,16,20000773,2),(184,16,20000774,2),(185,16,20000775,2),(186,16,20000776,2),(187,16,20000777,2),(188,16,20000778,2),(189,16,20000779,2),(190,16,20000780,2),(191,16,20000781,2),(192,16,20000782,2),(193,16,20000783,2),(194,16,20000784,2),(195,16,20000785,2),(196,16,20000786,2),(197,16,20000787,2),(198,16,20000788,2),(199,16,20000789,2),(246,20,20006352,2),(247,20,20006353,2),(248,20,20006354,2),(249,20,20006355,2),(250,20,20006356,2),(251,20,20006357,2),(252,20,20006358,2),(253,20,20006359,2),(254,20,20006360,2),(255,20,20006380,2),(361,31,20008424,2),(362,31,20008425,2),(120,11,20009000,2),(121,11,20009001,2),(122,11,20009002,2),(123,11,20009003,2),(124,11,20009005,2),(125,95,20009006,2),(1,27,20010001,2),(2,27,20010002,2),(3,27,20010003,2),(4,27,20010004,2),(363,32,20010022,2),(364,32,20010023,2),(365,33,20010025,2),(366,33,20010026,2),(367,33,20010027,2),(368,33,20010028,2),(999999,35,20010030,2),(531,94,20010050,2),(532,94,20010051,2),(536,94,20010053,2),(99999,99,21000124,2),(0,34,21000125,2),(10000,34,21000125,2),(10001,34,21000126,2),(1000000,35,21000126,2);
/*!40000 ALTER TABLE `subcategorie_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taille_libelle_langue`
--

DROP TABLE IF EXISTS `taille_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taille_libelle_langue` (
  `ref_taille` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  `valeur` varchar(40) NOT NULL,
  PRIMARY KEY (`ref_taille`,`ref_libelle`,`ref_langue`),
  KEY `fk_tall_libelle` (`ref_libelle`),
  KEY `fk_tall_langue` (`ref_langue`),
  CONSTRAINT `fk_tall_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tall_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taille_libelle_langue`
--

LOCK TABLES `taille_libelle_langue` WRITE;
/*!40000 ALTER TABLE `taille_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `taille_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temps`
--

DROP TABLE IF EXISTS `temps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temps` (
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temps`
--

LOCK TABLES `temps` WRITE;
/*!40000 ALTER TABLE `temps` DISABLE KEYS */;
/*!40000 ALTER TABLE `temps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temps_libelle_langue`
--

DROP TABLE IF EXISTS `temps_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temps_libelle_langue` (
  `ref_temps` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_temps`,`ref_libelle`,`ref_langue`),
  KEY `fk_tll_libelle` (`ref_libelle`),
  KEY `fk_tll_langue` (`ref_langue`),
  CONSTRAINT `fk_tll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_tll_temps` FOREIGN KEY (`ref_temps`) REFERENCES `temps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temps_libelle_langue`
--

LOCK TABLES `temps_libelle_langue` WRITE;
/*!40000 ALTER TABLE `temps_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `temps_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id_transaction` int NOT NULL AUTO_INCREMENT,
  `ref_acheteur` int NOT NULL,
  `ref_vendeur` int NOT NULL,
  `date` datetime NOT NULL,
  `prix` decimal(10,0) NOT NULL,
  `validate` int NOT NULL,
  `ref_article` int NOT NULL,
  PRIMARY KEY (`id_transaction`),
  KEY `fk_tx_acheteur` (`ref_acheteur`),
  KEY `fk_tx_vendeur` (`ref_vendeur`),
  KEY `fk_tx_article` (`ref_article`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (4,3,0,'2026-03-09 17:41:55',9087532,0,15),(5,1,1,'2026-03-15 11:24:55',1000,0,32),(6,1,1,'2026-03-17 08:19:23',2990,0,36),(7,1,1,'2026-03-17 10:20:46',12,0,33),(8,1,0,'2026-03-19 11:23:32',150,0,27),(9,1,0,'2026-03-19 11:38:07',150,0,27),(10,1,0,'2026-03-19 11:40:58',150,0,27),(11,1,0,'2026-03-19 11:43:47',150,0,27),(12,1,0,'2026-03-19 11:46:36',150,0,27),(13,1,0,'2026-03-19 11:48:56',150,0,27),(14,1,0,'2026-03-19 11:51:29',150,0,27),(15,1,0,'2026-03-19 11:55:58',150,0,27),(16,1,0,'2026-03-19 12:03:22',150,0,27),(17,1,1,'2026-03-19 12:06:47',2990,0,36),(18,1,1,'2026-03-19 16:07:15',2990,0,36),(19,1,1,'2026-03-20 10:17:14',2990,0,36),(20,1,1,'2026-03-20 14:03:15',2990,0,36);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_de_compte`
--

DROP TABLE IF EXISTS `type_de_compte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_de_compte` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_de_compte`
--

LOCK TABLES `type_de_compte` WRITE;
/*!40000 ALTER TABLE `type_de_compte` DISABLE KEYS */;
/*!40000 ALTER TABLE `type_de_compte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_de_compte_libelle_langue`
--

DROP TABLE IF EXISTS `type_de_compte_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_de_compte_libelle_langue` (
  `ref_type_de_compte` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  `prix` decimal(4,2) NOT NULL,
  PRIMARY KEY (`ref_type_de_compte`,`ref_libelle`,`ref_langue`),
  KEY `fk_tdcll_libelle` (`ref_libelle`),
  KEY `fk_tdcll_langue` (`ref_langue`),
  CONSTRAINT `fk_tdcll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tdcll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_tdcll_type` FOREIGN KEY (`ref_type_de_compte`) REFERENCES `type_de_compte` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_de_compte_libelle_langue`
--

LOCK TABLES `type_de_compte_libelle_langue` WRITE;
/*!40000 ALTER TABLE `type_de_compte_libelle_langue` DISABLE KEYS */;
/*!40000 ALTER TABLE `type_de_compte_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_de_jeux`
--

DROP TABLE IF EXISTS `type_de_jeux`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_de_jeux` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_de_jeux`
--

LOCK TABLES `type_de_jeux` WRITE;
/*!40000 ALTER TABLE `type_de_jeux` DISABLE KEYS */;
INSERT INTO `type_de_jeux` VALUES (1),(2),(3);
/*!40000 ALTER TABLE `type_de_jeux` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_de_jeux_libelle_langue`
--

DROP TABLE IF EXISTS `type_de_jeux_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_de_jeux_libelle_langue` (
  `ref_type_de_jeux` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_type_de_jeux`,`ref_libelle`,`ref_langue`),
  KEY `fk_tdjll_libelle` (`ref_libelle`),
  KEY `fk_tdjll_langue` (`ref_langue`),
  CONSTRAINT `fk_tdjll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tdjll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_tdjll_type` FOREIGN KEY (`ref_type_de_jeux`) REFERENCES `type_de_jeux` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_de_jeux_libelle_langue`
--

LOCK TABLES `type_de_jeux_libelle_langue` WRITE;
/*!40000 ALTER TABLE `type_de_jeux_libelle_langue` DISABLE KEYS */;
INSERT INTO `type_de_jeux_libelle_langue` VALUES (1,9004,1),(2,9005,1),(3,9006,1),(1,30009004,3),(2,30009005,3),(3,30009006,3),(1,40009004,2),(2,40009005,2),(3,40009006,2);
/*!40000 ALTER TABLE `type_de_jeux_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_de_vin_libelle_langue`
--

DROP TABLE IF EXISTS `type_de_vin_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_de_vin_libelle_langue` (
  `ref_type_de_vin` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_type_de_vin`,`ref_libelle`,`ref_langue`),
  KEY `fk_tdvll_libelle` (`ref_libelle`),
  KEY `fk_tdvll_langue` (`ref_langue`),
  CONSTRAINT `fk_tdvll_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tdvll_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_de_vin_libelle_langue`
--

LOCK TABLES `type_de_vin_libelle_langue` WRITE;
/*!40000 ALTER TABLE `type_de_vin_libelle_langue` DISABLE KEYS */;
INSERT INTO `type_de_vin_libelle_langue` VALUES (1,10001,1),(2,10002,1),(3,10003,1),(4,10004,1),(1,30010001,3),(2,30010002,3),(3,30010003,3),(4,30010004,3),(1,40010001,2),(2,40010002,2),(3,40010003,2),(4,40010004,2);
/*!40000 ALTER TABLE `type_de_vin_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_ecran`
--

DROP TABLE IF EXISTS `type_ecran`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_ecran` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_ecran`
--

LOCK TABLES `type_ecran` WRITE;
/*!40000 ALTER TABLE `type_ecran` DISABLE KEYS */;
INSERT INTO `type_ecran` VALUES (1),(2);
/*!40000 ALTER TABLE `type_ecran` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_ecran_libelle_langue`
--

DROP TABLE IF EXISTS `type_ecran_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_ecran_libelle_langue` (
  `ref_type_ecran` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_type_ecran`,`ref_libelle`,`ref_langue`),
  KEY `fk_tell_libelle` (`ref_libelle`),
  KEY `fk_tell_langue` (`ref_langue`),
  CONSTRAINT `fk_tell_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tell_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_tell_type` FOREIGN KEY (`ref_type_ecran`) REFERENCES `type_ecran` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_ecran_libelle_langue`
--

LOCK TABLES `type_ecran_libelle_langue` WRITE;
/*!40000 ALTER TABLE `type_ecran_libelle_langue` DISABLE KEYS */;
INSERT INTO `type_ecran_libelle_langue` VALUES (1,10060,1),(2,10061,1),(1,30010060,3),(2,30010061,3),(1,40010060,2),(2,40010061,2);
/*!40000 ALTER TABLE `type_ecran_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_essence`
--

DROP TABLE IF EXISTS `type_essence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_essence` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_essence`
--

LOCK TABLES `type_essence` WRITE;
/*!40000 ALTER TABLE `type_essence` DISABLE KEYS */;
INSERT INTO `type_essence` VALUES (1),(2),(3);
/*!40000 ALTER TABLE `type_essence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_essence_libelle_langue`
--

DROP TABLE IF EXISTS `type_essence_libelle_langue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_essence_libelle_langue` (
  `ref_type_essence` int NOT NULL,
  `ref_libelle` int NOT NULL,
  `ref_langue` int NOT NULL,
  PRIMARY KEY (`ref_type_essence`,`ref_libelle`,`ref_langue`),
  KEY `fk_tess_libelle` (`ref_libelle`),
  KEY `fk_tess_langue` (`ref_langue`),
  CONSTRAINT `fk_tess_langue` FOREIGN KEY (`ref_langue`) REFERENCES `langue` (`id_langue`),
  CONSTRAINT `fk_tess_libelle` FOREIGN KEY (`ref_libelle`) REFERENCES `libelle` (`id_libelle`),
  CONSTRAINT `fk_tess_type` FOREIGN KEY (`ref_type_essence`) REFERENCES `type_essence` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_essence_libelle_langue`
--

LOCK TABLES `type_essence_libelle_langue` WRITE;
/*!40000 ALTER TABLE `type_essence_libelle_langue` DISABLE KEYS */;
INSERT INTO `type_essence_libelle_langue` VALUES (1,10041,1),(2,10042,1),(3,10043,1),(1,30010041,3),(2,30010042,3),(3,30010043,3),(1,40010041,2),(2,40010042,2),(3,40010043,2);
/*!40000 ALTER TABLE `type_essence_libelle_langue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visiteur`
--

DROP TABLE IF EXISTS `visiteur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visiteur` (
  `ref_personne` int NOT NULL,
  `ref_visiteur` int NOT NULL,
  PRIMARY KEY (`ref_personne`,`ref_visiteur`),
  KEY `fk_visiteur_visiteur` (`ref_visiteur`),
  CONSTRAINT `fk_visiteur_personne` FOREIGN KEY (`ref_personne`) REFERENCES `personne` (`id_personne`),
  CONSTRAINT `fk_visiteur_visiteur` FOREIGN KEY (`ref_visiteur`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visiteur`
--

LOCK TABLES `visiteur` WRITE;
/*!40000 ALTER TABLE `visiteur` DISABLE KEYS */;
/*!40000 ALTER TABLE `visiteur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wish`
--

DROP TABLE IF EXISTS `wish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wish` (
  `id_wish` int NOT NULL AUTO_INCREMENT,
  `ref_wish_list` int NOT NULL,
  `ref_user` int NOT NULL,
  `auteur` varchar(40) DEFAULT NULL,
  `marque` varchar(40) NOT NULL,
  `nom` varchar(40) NOT NULL DEFAULT '',
  `label` text NOT NULL,
  `prix` int NOT NULL,
  `pochette` varchar(100) NOT NULL DEFAULT '',
  `presound` varchar(100) NOT NULL DEFAULT '',
  `ref_genre` int NOT NULL DEFAULT '0',
  `etat` varchar(50) NOT NULL,
  `ref_categorie` int NOT NULL,
  `ref_subcategorie` int DEFAULT NULL,
  `owned` int NOT NULL,
  `ref_statut` int NOT NULL,
  `date` datetime NOT NULL,
  `ref_depot` int NOT NULL,
  `enchere` int NOT NULL,
  `ref_condition_payement` int NOT NULL,
  `ref_mode_de_livraison` int NOT NULL,
  `enchere_date_debut` datetime NOT NULL,
  `enchere_date_fin` datetime NOT NULL,
  `vendu` int NOT NULL,
  `lang` varchar(2) NOT NULL,
  `ref_canton` int DEFAULT NULL,
  `ref_pays` int DEFAULT NULL,
  PRIMARY KEY (`id_wish`),
  KEY `fk_wish_list` (`ref_wish_list`),
  KEY `fk_wish_user` (`ref_user`),
  KEY `fk_wish_categorie` (`ref_categorie`),
  KEY `fk_wish_payement` (`ref_condition_payement`),
  KEY `fk_wish_livraison` (`ref_mode_de_livraison`),
  KEY `fk_wish_canton` (`ref_canton`),
  CONSTRAINT `fk_wish_canton` FOREIGN KEY (`ref_canton`) REFERENCES `canton_fr` (`id_canton`),
  CONSTRAINT `fk_wish_categorie` FOREIGN KEY (`ref_categorie`) REFERENCES `categorie` (`id_categorie`),
  CONSTRAINT `fk_wish_list` FOREIGN KEY (`ref_wish_list`) REFERENCES `wish_list` (`id_wish_list`),
  CONSTRAINT `fk_wish_livraison` FOREIGN KEY (`ref_mode_de_livraison`) REFERENCES `condition_livraison` (`id`),
  CONSTRAINT `fk_wish_payement` FOREIGN KEY (`ref_condition_payement`) REFERENCES `condition_payement_ref` (`ref_condition_payement`),
  CONSTRAINT `fk_wish_user` FOREIGN KEY (`ref_user`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wish`
--

LOCK TABLES `wish` WRITE;
/*!40000 ALTER TABLE `wish` DISABLE KEYS */;
/*!40000 ALTER TABLE `wish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wish_list`
--

DROP TABLE IF EXISTS `wish_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wish_list` (
  `id_wish_list` int NOT NULL AUTO_INCREMENT,
  `ref_user` int NOT NULL,
  `date_creation` date NOT NULL,
  PRIMARY KEY (`id_wish_list`),
  KEY `fk_wl_user` (`ref_user`),
  CONSTRAINT `fk_wl_user` FOREIGN KEY (`ref_user`) REFERENCES `personne` (`id_personne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wish_list`
--

LOCK TABLES `wish_list` WRITE;
/*!40000 ALTER TABLE `wish_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `wish_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'new_db_recordzv3'
--

--
-- Dumping routines for database 'new_db_recordzv3'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-17 19:31:48
