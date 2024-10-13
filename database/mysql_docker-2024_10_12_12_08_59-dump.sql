-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: account-service
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `faceboo_account_id` varchar(255) DEFAULT NULL,
  `google_account_id` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(10) NOT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk8h1bgqoplx0rkngj01pm1rgp` (`username`),
  KEY `FKt3wava8ssfdspnh3hg4col3m1` (`role_id`),
  CONSTRAINT `FKt3wava8ssfdspnh3hg4col3m1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES ('0fc8ed86-ace3-42b5-ac83-818de8e7806b','2024-10-09 02:30:00.784961','2024-10-09 02:30:00.784961',NULL,NULL,_binary '','$2a$10$llKXUgnF8yXEhsbnM//a9u6mRFII7dU9K0RGHIV56DOwyAB1SLKU.','0869885512',1),('fa8aaf91-c312-4352-b956-9ea74fe096c1','2024-10-10 10:38:24.389935','2024-10-10 10:38:24.389935',NULL,NULL,_binary '','$2a$10$FVXVWWinB0TJbzqtmToaq.rRIrpam4nJaNaGVJnpFoqIrpZb0rl2W','0869885513',1);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `data_of_birth` date DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `gender` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlnk8iosvsrn5614xw3lgnybgk` (`email`),
  CONSTRAINT `FKph94xatq3eb421xema3y7p7b8` FOREIGN KEY (`id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES ('0fc8ed86-ace3-42b5-ac83-818de8e7806b','Nam Định','f2d59bf4-b4f5-4280-8755-365126e2856a_Tư duy.png','2024-10-09','Nguyễn Văn Hưng',_binary '',NULL),('fa8aaf91-c312-4352-b956-9ea74fe096c1','Thái Bình',NULL,'2024-10-10','Nguyễn Hương Giang',_binary '\0',NULL);
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKch1113horj4qr56f91omojv8` (`code`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'CUSTOMER','Khách hàng'),(2,'EMPLOYEE','Nhân viên'),(3,'MANAGER','Quản lý'),(4,'IT_ADMIN','IT ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `id` varchar(255) NOT NULL,
  `expired` bit(1) NOT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `refresh_token_expires_at` datetime(6) DEFAULT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` varchar(255) DEFAULT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkxd4xsavefdtlpkvnxpgojn6` (`account_id`),
  CONSTRAINT `FKkxd4xsavefdtlpkvnxpgojn6` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES ('55d61270-a7fe-44c0-944a-58b99d54e8ea',_binary '\0','2024-10-18 01:02:49.707607','16d8449c-0da5-42b5-bef7-4bf2493862fc','2024-11-01 01:02:49.707607',_binary '\0','eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIwZmM4ZWQ4Ni1hY2UzLTQyYjUtYWM4My04MThkZThlNzgwNmIiLCJpYXQiOjE3Mjg1ODMzNjksImV4cCI6MTcyOTE4ODE2OX0.VsoRmahjFHqBTxEVqj4U0acFO5Cdrr0vtESYE-JcB92TjbzktWXDyhAjnKWMU2eK','Bearer','0fc8ed86-ace3-42b5-ac83-818de8e7806b'),('65c75ee9-2b0c-44a4-b149-e9964d9a395d',_binary '\0','2024-10-18 01:38:08.785663','fe557446-4e66-4b71-8f35-6050f6faa735','2024-11-01 01:38:08.786184',_binary '\0','eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIwZmM4ZWQ4Ni1hY2UzLTQyYjUtYWM4My04MThkZThlNzgwNmIiLCJpYXQiOjE3Mjg1ODU0ODgsImV4cCI6MTcyOTE5MDI4OH0.5c_hqFYSZga9O8s60JqK0KWpYVeAr89eysnGXsCATh58yEnhxJnqvLiw50kKi2CZ','Bearer','0fc8ed86-ace3-42b5-ac83-818de8e7806b'),('778ff715-c6cb-4271-9375-dd6dc31850ad',_binary '\0','2024-10-18 09:17:59.573851','435a3fd5-6aa1-4315-991f-15e3eb5a228e','2024-11-01 09:17:59.573851',_binary '\0','eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIwZmM4ZWQ4Ni1hY2UzLTQyYjUtYWM4My04MThkZThlNzgwNmIiLCJpYXQiOjE3Mjg2MTMwNzksImV4cCI6MTcyOTIxNzg3OX0.eQ1kD3KhxsSmR4dR5PnQxIMbbFUylCSBuFcAJOEG1Pq2yjRlpiBYDOG0xYlkX_O-','Bearer','0fc8ed86-ace3-42b5-ac83-818de8e7806b'),('adbf9080-5919-423c-8ab4-144c4fb573e3',_binary '\0','2024-10-17 10:51:21.509618','f2535077-ea25-451e-92b1-7885c41393aa','2024-10-31 10:51:21.509943',_binary '','eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJmYThhYWY5MS1jMzEyLTQzNTItYjk1Ni05ZWE3NGZlMDk2YzEiLCJpYXQiOjE3Mjg1MzIyODEsImV4cCI6MTcyOTEzNzA4MX0.zPGPeBhJ-e81l3GxxNZux0j_DCMhAzdBpyJyLNM7bJcpLbnaZri5VwEm7JkcuBWu','Bearer','fa8aaf91-c312-4352-b956-9ea74fe096c1');
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-12 12:09:00
