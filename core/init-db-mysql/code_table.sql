/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-08-23 12:47:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for code_table
-- ----------------------------
DROP TABLE IF EXISTS `code_table`;
CREATE TABLE `code_table` (
  `codetableid` bigint(20) NOT NULL AUTO_INCREMENT,
  `codekey` varchar(32) DEFAULT NULL,
  `codetype` varchar(32) DEFAULT NULL,
  `codename` varchar(32) DEFAULT NULL,
  `ccodevalue` varchar(256) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`codetableid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
