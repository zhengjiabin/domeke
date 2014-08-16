/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-08-14 15:18:17
*/

SET FOREIGN_KEY_CHECKS=0;

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
