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
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `communityid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `actionkey` varchar(255) DEFAULT NULL,
  `pid` mediumint(8) DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '1',
  `position` int(11) DEFAULT '0',
  `times` bigint(20) DEFAULT '0',
  `status` varchar(4) DEFAULT '10',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`communityid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (1,'活动',NULL,NULL,0,1,1,0,'10','2014-09-21 13:46:42',0,'2014-09-21 13:46:42',0),(2,'帖子',NULL,NULL,0,1,2,0,'10','2014-09-21 13:46:55',0,'2014-09-21 13:46:55',0),(3,'官方活动','123123','/activity',1,2,1,73,'10','2014-09-21 13:47:04',0,'2014-09-21 13:47:04',0),(4,'官方帖子',NULL,'/post',2,2,1,3,'10','2014-09-21 13:47:15',0,'2014-09-21 13:47:15',0),(5,'礼品活动','ceshi1','/activity',1,2,2,0,'10','2014-09-23 16:38:11',0,'2014-09-23 16:38:11',0),(6,'合作伙伴活动','hah432','/activity',1,2,3,0,'10','2014-09-23 16:38:23',0,'2014-09-23 16:38:23',0),(7,'活动平台','12312','/activity',1,2,4,0,'10','2014-09-23 16:39:20',0,'2014-09-23 16:39:20',0),(8,'合作伙伴帖子',NULL,'/post',2,2,2,0,'10','2014-09-23 16:39:49',0,'2014-09-23 16:39:49',0),(9,'乡村帖子',NULL,'/post',2,2,3,0,'10','2014-09-23 16:40:07',0,'2014-09-23 16:40:07',0),(10,'亲情帖子','大家一起来玩耍','/post',2,2,4,0,'10','2014-09-23 16:40:45',0,'2014-09-23 16:40:45',0);
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-24  0:47:48
