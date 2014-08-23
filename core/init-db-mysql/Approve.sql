/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2014-08-23 11:33:27
*/


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `approve`
-- ----------------------------
DROP TABLE IF EXISTS `approve`;
CREATE TABLE `approve` (
  `approveid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `typeid` varchar(50) DEFAULT NULL COMMENT '审批类型id  如：“商城”“作品”。。。。。',
  `detailid` varchar(50) DEFAULT NULL COMMENT '明细项id',
  `status` varchar(50) DEFAULT NULL COMMENT '审批状态',
  `remark` varchar(200) DEFAULT NULL,
  `approvetime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`approveid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of approve
-- ----------------------------
