/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-08-14 15:18:26
*/

SET FOREIGN_KEY_CHECKS=0;

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
