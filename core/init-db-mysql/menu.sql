/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-09-13 19:49:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `menuid` bigint(20) NOT NULL AUTO_INCREMENT,
  `mid` int(11) DEFAULT NULL,
  `menuname` varchar(64) DEFAULT NULL,
  `actionkey` varchar(256) DEFAULT NULL,
  `top` char(2) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `parentmenuid` bigint(20) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `menutype` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '10', '首页', 'index', '1', '100000000', null, '2014-09-13 19:13:02', null, '2014-09-13 19:13:02', null, '1');
INSERT INTO `menu` VALUES ('2', '20', '动漫', '/core/menu/member', '1', '200000000', null, '2014-09-13 19:13:04', null, '2014-09-13 19:13:04', null, '1');
INSERT INTO `menu` VALUES ('3', '30', '无奇不有', '#', '1', '300000000', null, '2014-09-13 19:13:06', null, '2014-09-13 19:13:06', null, '1');
INSERT INTO `menu` VALUES ('4', '40', '商城', '#', '1', '400000000', null, '2014-09-13 19:13:07', null, '2014-09-13 19:13:07', null, '1');
INSERT INTO `menu` VALUES ('5', '50', 'HIGH豆社区', '#', '1', '500000000', null, '2014-09-13 19:13:09', null, '2014-09-13 19:13:09', null, '1');
INSERT INTO `menu` VALUES ('6', '60', '签到发泄墙', 'ventwall', '1', '600000000', null, '2014-09-13 19:13:10', null, '2014-09-13 19:13:10', null, '1');
INSERT INTO `menu` VALUES ('7', '70', '菜单管理', '/core/menu/selectMenu', '1', '700000000', null, '2014-09-13 19:13:12', null, '2014-09-13 19:13:12', null, '1');
INSERT INTO `menu` VALUES ('8', '20020', '个人中心', '#', '2', '200010000', '2', '2014-09-13 19:13:14', null, '2014-09-13 19:13:14', null, '2');
INSERT INTO `menu` VALUES ('9', '20030', '个人账户', '#', '2', '200020000', '2', '2014-09-13 19:13:15', null, '2014-09-13 19:13:15', null, '2');
INSERT INTO `menu` VALUES ('10', null, '售后服务', '#', '2', '200030000', '2', '2014-09-13 19:13:16', null, '2014-09-13 19:13:16', null, '2');
INSERT INTO `menu` VALUES ('11', null, '账户管理', '#', '2', '200040000', '2', '2014-09-13 19:13:18', null, '2014-09-13 19:13:18', null, '2');
INSERT INTO `menu` VALUES ('15', null, '我的作品', '#', '3', '200010010', '8', '2014-09-13 19:13:21', null, '2014-09-13 19:13:21', null, '2');
INSERT INTO `menu` VALUES ('16', null, '播放记录', '#', '3', '200010020', '8', '2014-09-13 19:13:22', null, '2014-09-13 19:13:22', null, '2');
INSERT INTO `menu` VALUES ('17', null, '我的下载', '#', '3', '200010030', '8', '2014-09-13 19:13:23', null, '2014-09-13 19:13:23', null, '2');
INSERT INTO `menu` VALUES ('18', null, '参与的活动', '#', '3', '200010040', '8', '2014-09-13 19:13:25', null, '2014-09-13 19:13:25', null, '2');
INSERT INTO `menu` VALUES ('19', null, '会员等级', '#', '3', '200010050', '8', '2014-09-13 19:13:26', null, '2014-09-13 19:13:26', null, '2');
INSERT INTO `menu` VALUES ('20', null, '会员积分', '#', '3', '200010060', '8', '2014-09-13 19:13:28', null, '2014-09-13 19:13:28', null, '2');
INSERT INTO `menu` VALUES ('21', null, '动画收藏', '#', '3', '200010070', '8', '2014-09-13 19:13:29', null, '2014-09-13 19:13:29', null, '2');
INSERT INTO `menu` VALUES ('22', null, '我的订单', '#', '3', '200020010', '9', '2014-09-13 19:13:32', null, '2014-09-13 19:13:32', null, '2');
