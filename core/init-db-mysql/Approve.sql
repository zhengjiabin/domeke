/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-08-14 15:17:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `approve`
-- ----------------------------
DROP TABLE IF EXISTS `domeke`.`approve`;
CREATE TABLE `domeke`.`approve` (
  `ApproveId` int(11) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `typeid` varchar(50) DEFAULT NULL COMMENT '审批类型id  如：“商城”“作品”。。。。。',
  `detailid` varchar(50) DEFAULT NULL COMMENT '明细项id',
  `status` varchar(50) DEFAULT NULL COMMENT '审批状态',
  PRIMARY KEY (`ApproveId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of approve
-- ----------------------------

-- ----------------------------
-- Table structure for `downloadcount`
-- ----------------------------
DROP TABLE IF EXISTS `domeke`.`downloadcount`;
CREATE TABLE `domeke`.`downloadcount` (
  `DownLoadCountId` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `cartoon_name` varchar(50) DEFAULT NULL COMMENT '卡通名称',
  `cartoon_id` varchar(50) DEFAULT NULL,
  `section_name` varchar(50) DEFAULT NULL COMMENT '章节名称',
  `download_count` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DownLoadCountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of downloadcount
-- ----------------------------

-- ----------------------------
-- Table structure for `favourite`
-- ----------------------------
DROP TABLE IF EXISTS `domeke`.`favourite`;
CREATE TABLE `domeke`.`favourite` (
  `FavouriteId` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `cartoon_name` varchar(50) DEFAULT NULL COMMENT '卡通名称',
  `cartoon_id` varchar(50) DEFAULT NULL,
  `section_name` varchar(50) DEFAULT NULL COMMENT '章节名称',
  `section_id` varchar(50) DEFAULT NULL COMMENT '章节id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`FavouriteId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of favourite
-- ----------------------------

-- ----------------------------
-- Table structure for `playcount`
-- ----------------------------
DROP TABLE IF EXISTS `domeke`.`playcount`;
CREATE TABLE `domeke`.`playcount` (
  `PlayCountId` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `cartoon_name` varchar(50) DEFAULT NULL COMMENT '卡通名称',
  `cartoon_id` varchar(50) DEFAULT NULL,
  `section_name` varchar(50) DEFAULT NULL COMMENT '章节名称',
  `section_id` varchar(50) DEFAULT NULL,
  `play_counts` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`PlayCountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of playcount
-- ----------------------------

-- ----------------------------
-- Table structure for `ventwall`
-- ----------------------------
DROP TABLE IF EXISTS `domeke`.`ventwall`;
CREATE TABLE `domeke`.`ventwall` (
  `VentWallId` int(11) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `moodid` varchar(50) DEFAULT NULL COMMENT '心情id',
  `message` varchar(255) DEFAULT NULL COMMENT '留言',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`VentWallId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ventwall
-- ----------------------------
