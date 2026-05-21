CREATE DATABASE  IF NOT EXISTS `603355` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `603355`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: 603355
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `equipment_items`
--

DROP TABLE IF EXISTS `equipment_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_items` (
  `equipment_items_index` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `price` varchar(50) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `weight` varchar(50) DEFAULT NULL,
  `description` json DEFAULT NULL,
  `homebrew` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`equipment_items_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `magic_items`
--

DROP TABLE IF EXISTS `magic_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magic_items` (
  `magic_items_index` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` json DEFAULT NULL,
  `image` mediumblob,
  `rarity` varchar(50) DEFAULT NULL,
  `homebrew` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`magic_items_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `spells`
--

DROP TABLE IF EXISTS `spells`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spells` (
  `spell_index` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `level` int DEFAULT NULL,
  `spell_range` varchar(50) DEFAULT NULL,
  `components` json DEFAULT NULL,
  `material` text,
  `ritual` tinyint(1) DEFAULT NULL,
  `concentration` tinyint(1) DEFAULT NULL,
  `duration` varchar(50) DEFAULT NULL,
  `casting_time` varchar(50) DEFAULT NULL,
  `attack_type` varchar(50) DEFAULT NULL,
  `description` json DEFAULT NULL,
  `higher_level` json DEFAULT NULL,
  `school` varchar(50) DEFAULT NULL,
  `area_of_effect` varchar(50) DEFAULT NULL,
  `homebrew` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`spell_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-16 10:02:32
