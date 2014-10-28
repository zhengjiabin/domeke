/*
Navicat MySQL Data Transfer

Source Server         : zs
Source Server Version : 50620
Source Host           : 218.85.136.199:13306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2014-10-28 21:59:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `login_pic`
-- ----------------------------
DROP TABLE IF EXISTS `login_pic`;
CREATE TABLE `login_pic` (
  `picid` int(11) NOT NULL AUTO_INCREMENT,
  `picurl` varchar(128) NOT NULL,
  `picname` varchar(32) NOT NULL,
  `status` int(4) NOT NULL,
  PRIMARY KEY (`picid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_pic
-- ----------------------------
INSERT INTO `login_pic` VALUES ('1', 'http://www.dongmark.com/images/LoginPic.png', '登录', '70');
