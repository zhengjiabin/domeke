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
-- Table structure for table `activity_apply`
--

DROP TABLE IF EXISTS `activity_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_apply` (
  `activityapplyid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `activityid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `message` varchar(255) NOT NULL,
  `dateline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `realname` varchar(32) NOT NULL,
  `mobile` varchar(32) NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `papers` tinyint(1) DEFAULT '0',
  `papersid` bigint(20) DEFAULT '0',
  `status` varchar(4) DEFAULT '10',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`activityapplyid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_apply`
--

LOCK TABLES `activity_apply` WRITE;
/*!40000 ALTER TABLE `activity_apply` DISABLE KEYS */;
INSERT INTO `activity_apply` VALUES (1,1,1,'111','2014-09-21 14:26:42','测试1','11111',0,0,111,'10','2014-09-21 14:26:42',NULL,NULL,'2014-09-21 14:26:42'),(2,4,1,'123123123','2014-09-27 03:04:06','cs','12',0,0,123123123123,'10','2014-09-27 03:04:06',NULL,NULL,'2014-09-27 03:04:06'),(3,4,1,'1231231','2014-09-27 03:04:48','safsdf','11',1,0,1111,'10','2014-09-27 03:04:48',NULL,NULL,'2014-09-27 03:04:48'),(4,7,1,'123123','2014-09-27 06:20:26','1','12123123',0,0,11111,'10','2014-09-27 06:20:26',NULL,NULL,'2014-09-27 06:20:26');
/*!40000 ALTER TABLE `activity_apply` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-27 18:45:28
