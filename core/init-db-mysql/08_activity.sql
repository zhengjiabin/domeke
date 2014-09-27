CREATE DATABASE  IF NOT EXISTS `domeke` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `domeke`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: domeke
-- ------------------------------------------------------
-- Server version	5.6.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `activityid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(80) NOT NULL,
  `userid` bigint(20) NOT NULL DEFAULT '0',
  `aid` int(11) NOT NULL DEFAULT '0',
  `starttimefrom` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `starttimeto` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `place` varchar(255) NOT NULL,
  `class` varchar(255) NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `number` smallint(5) unsigned NOT NULL DEFAULT '0',
  `expiration` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ufield` text NOT NULL,
  `status` varchar(4) DEFAULT '10',
  `times` bigint(20) DEFAULT '0',
  `top` tinyint(1) NOT NULL DEFAULT '0',
  `essence` tinyint(1) NOT NULL DEFAULT '0',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `communityid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activityid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'1',1,0,'2014-09-25 11:33:11','2014-09-23 16:26:35','1','1',1,1,'2014-09-23 16:26:39','<p>sfsd<img alt=\"\" src=\"./upload/image/ceshi1-1411489606376.jpg\" style=\"height:75px; width:258px\" /></p>','10',41,1,1,'2014-09-23 16:26:49',NULL,NULL,'2014-09-25 11:33:11',3),(2,'1',1,0,'2014-09-24 16:11:20','2014-09-23 16:27:40','1','11',1,1,'2014-09-23 16:27:45','<p>sfds<img alt=\"\" src=\"./upload/image/ceshi1-1411489672984.jpg\" style=\"height:75px; width:258px\" /></p>','10',2,0,0,'2014-09-23 16:28:10',NULL,NULL,'2014-09-24 16:11:20',3),(3,'123',1,0,'2014-09-24 14:45:29','2014-09-24 14:33:58','1','1',1,1,'2014-09-24 14:34:02','<p>sfsfadgas</p>','10',2,1,0,'2014-09-23 14:34:06',NULL,NULL,'2014-09-24 14:45:29',3),(4,'11',1,0,'2014-09-25 12:19:38','2014-09-25 12:19:39','1','1',0,12,'2014-09-25 12:19:46','<p>sfsaf</p>','10',0,0,0,'2014-09-25 12:20:05',NULL,NULL,'2014-09-25 12:20:05',3),(5,'12',1,0,'2014-09-25 16:50:46','2014-09-25 16:50:47','1','1',0,1,'2014-09-25 16:50:55','<p>sfsf</p>','10',0,0,0,'2014-09-25 16:51:09',NULL,NULL,'2014-09-25 16:51:09',6),(6,'1',1,0,'2014-09-27 03:37:34','2014-09-27 03:37:40','1','1',0,12,'2014-09-27 03:37:47','<p>123123</p>','10',0,0,0,'2014-09-27 03:40:02',NULL,NULL,'2014-09-27 03:40:02',3),(7,'ceshi 截止时间',1,0,'2014-09-27 10:38:19','2014-09-29 16:00:00','1','1',0,1,'2014-09-29 16:00:00','<p>111</p>','10',11,0,0,'2014-09-27 06:15:14',NULL,NULL,'2014-09-27 10:38:19',3),(8,'1',1,0,'2014-09-27 06:53:19','2014-09-27 06:53:20','1','1',0,1,'2014-09-29 16:00:00','<p>12123</p>','10',0,0,0,'2014-09-27 06:53:32',NULL,NULL,'2014-09-27 06:53:32',3),(9,'1',1,0,'2014-09-27 07:09:57','2014-09-27 07:09:59','1','1',0,1,'2014-09-27 07:10:05','<p>123</p>','10',0,0,0,'2014-09-27 07:10:11',NULL,NULL,'2014-09-27 07:10:11',3),(10,'1',1,0,'2014-09-27 07:18:15','2014-09-27 07:18:21','1','1',0,1,'2014-09-27 07:18:26','<p>12</p>','10',0,0,0,'2014-09-27 07:18:30',NULL,NULL,'2014-09-27 07:18:30',3),(11,'1',1,0,'2014-09-27 07:44:06','2014-09-27 07:44:08','1','1',0,1,'2014-09-27 07:44:14','<p>12321</p>','10',0,0,0,'2014-09-27 07:44:19',NULL,NULL,'2014-09-27 07:44:19',3),(12,'1123',1,0,'2014-09-27 09:40:51','2014-09-27 09:40:53','1','12',0,1,'2014-09-27 09:40:59','<p>123123</p>','10',0,0,0,'2014-09-27 09:41:03',NULL,NULL,'2014-09-27 09:41:03',3);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-27 18:45:29
