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
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `postid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subject` varchar(80) NOT NULL,
  `dateline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `message` text NOT NULL,
  `userip` varchar(15) NOT NULL,
  `times` bigint(20) DEFAULT '0',
  `top` tinyint(1) NOT NULL DEFAULT '0',
  `essence` tinyint(1) NOT NULL DEFAULT '0',
  `status` varchar(4) NOT NULL DEFAULT '10',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `communityid` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`postid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (11,1,'ce','2014-09-24 11:42:40','<p>12<img alt=\"\" src=\"./upload/image/ceshi1-1411489067198.jpg\" style=\"height:75px; width:258px\" /></p>','0:0:0:0:0:0:0:1',3,0,0,'10','2014-09-23 16:18:40',NULL,NULL,'2014-09-24 11:42:40',4),(12,1,'zxc','2014-09-24 12:07:05','<p>xzcvvzx<img alt=\"\" src=\"./upload/image/ceshi1-1411560406383.jpg\" style=\"height:75px; width:258px\" /></p><p>s</p><p>sdf</p><p>sdf</p><p>s</p><p>dfs</p><p>dfs</p><p>dfs</p><p>fcs<img alt=\"\" src=\"./upload/image/ceshi1-1411560415983.jpg\" style=\"height:75px; width:258px\" /></p>','0:0:0:0:0:0:0:1',1,0,0,'10','2014-09-24 12:07:01',NULL,NULL,'2014-09-24 12:07:05',4),(13,1,'123123','2014-09-27 10:43:15','<p>12312312</p>','0:0:0:0:0:0:0:1',9,0,0,'10','2014-09-27 06:57:47',NULL,NULL,'2014-09-27 10:43:15',4);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
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
