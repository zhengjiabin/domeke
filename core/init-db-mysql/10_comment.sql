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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `commentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `pid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `targetid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `touserid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '1',
  `idtype` varchar(20) NOT NULL,
  `userip` varchar(20) NOT NULL,
  `dateline` int(14) unsigned NOT NULL DEFAULT '0',
  `message` text NOT NULL,
  `status` varchar(4) NOT NULL DEFAULT '10',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`commentid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,0,1,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sdfsdf</p>\n','10','2014-09-24 12:30:15',0,'2014-09-24 12:30:15',0),(2,1,0,1,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sdfdsfsdf</p>\n','10','2014-09-24 12:30:18',0,'2014-09-24 12:30:18',0),(3,1,0,1,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sfsdf</p>\n','10','2014-09-24 12:34:56',0,'2014-09-24 12:34:56',0),(4,1,0,3,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sdf</p>\n','10','2014-09-24 14:34:44',0,'2014-09-24 14:34:44',0),(5,1,0,3,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sfd</p>\n','10','2014-09-24 14:34:46',0,'2014-09-24 14:34:46',0),(6,1,0,3,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>sdfsdf</p>\n','10','2014-09-24 14:34:49',0,'2014-09-24 14:34:49',0),(7,1,0,1,0,1,'30','0:0:0:0:0:0:0:1',0,'<p>cehishis s fsfsdfs</p>\n','10','2014-09-27 10:24:37',0,'2014-09-27 10:24:37',0),(8,1,0,7,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>dfgvdfsgd</p>\n','10','2014-09-27 10:25:06',0,'2014-09-27 10:25:06',0),(9,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>sdfsdf</p>\n','10','2014-09-27 10:25:18',0,'2014-09-27 10:25:18',0),(10,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>123123</p>\n','10','2014-09-27 10:30:05',0,'2014-09-27 10:30:05',0),(11,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>124312</p>\n','10','2014-09-27 10:33:05',0,'2014-09-27 10:33:05',0),(12,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>1111</p>\n','10','2014-09-27 10:34:01',0,'2014-09-27 10:34:01',0),(13,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>1</p>\n','10','2014-09-27 10:36:03',0,'2014-09-27 10:36:03',0),(14,1,0,7,0,1,'20','0:0:0:0:0:0:0:1',0,'<p>1231</p>\n','10','2014-09-27 10:38:34',0,'2014-09-27 10:38:34',0),(15,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>1112333</p>\n','10','2014-09-27 10:39:26',0,'2014-09-27 10:39:26',0),(16,1,0,13,0,1,'10','0:0:0:0:0:0:0:1',0,'<p>uuuu</p>\n','10','2014-09-27 10:43:24',0,'2014-09-27 10:43:24',0),(17,1,0,1,0,1,'30','0:0:0:0:0:0:0:1',0,'<p>2qq</p>\n','10','2014-09-27 10:44:06',0,'2014-09-27 10:44:06',0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-27 18:45:27
