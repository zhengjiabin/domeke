/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2014-08-23 11:33:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `vent_wall`
-- ----------------------------
DROP TABLE IF EXISTS `vent_wall`;
CREATE TABLE `vent_wall` (
  `ventwallid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) DEFAULT NULL,
  `moodid`  varchar(100) DEFAULT NULL COMMENT '心情id',
  `message` varchar(255) DEFAULT NULL COMMENT '留言',
  `createtime`  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifier` bigint(20) NULL,
  PRIMARY KEY (`ventwallid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ventwall
-- ----------------------------
