/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-08-14 15:17:59
*/

SET FOREIGN_KEY_CHECKS=0;

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
