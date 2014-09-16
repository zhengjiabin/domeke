/*
Navicat MySQL Data Transfer

Source Server         : domeke
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-09-16 21:27:16
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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '10', '首页', 'index', '1', '100000000', null, '2014-09-13 19:13:02', null, '2014-09-13 19:13:02', null, '1');
INSERT INTO `menu` VALUES ('2', '20', '动漫', '/core/menu/cartoon', '1', '200000000', null, '2014-09-13 22:43:09', null, '2014-09-13 22:43:09', null, '1');
INSERT INTO `menu` VALUES ('3', '30', '无奇不有', '#', '1', '300000000', null, '2014-09-16 20:44:30', null, '2014-09-16 20:44:30', null, '1');
INSERT INTO `menu` VALUES ('4', '40', '商城', 'shop', '1', '400000000', null, '2014-09-13 22:20:08', null, '2014-09-13 22:20:08', null, '1');
INSERT INTO `menu` VALUES ('5', '50', 'HIGH豆社区', 'forum', '1', '500000000', null, '2014-09-13 22:20:13', null, '2014-09-13 22:20:13', null, '1');
INSERT INTO `menu` VALUES ('6', '60', '签到发泄墙', 'ventwall', '1', '600000000', null, '2014-09-13 19:13:10', null, '2014-09-13 19:13:10', null, '1');
INSERT INTO `menu` VALUES ('8', '20020', '个人中心', '#', '1', '200010000', null, '2014-09-13 20:51:46', null, '2014-09-13 20:51:46', null, '2');
INSERT INTO `menu` VALUES ('15', null, '我的作品', 'personal/forMyProduction?menuId=1', '2', '200010010', '8', '2014-09-16 21:23:35', null, '2014-09-16 21:23:35', null, '2');
INSERT INTO `menu` VALUES ('16', null, '播放记录', 'personal/forMyProduction?menuId=2', '2', '200010020', '8', '2014-09-16 21:23:39', null, '2014-09-16 21:23:39', null, '2');
INSERT INTO `menu` VALUES ('17', null, '我的下载', '#', '2', '200010030', '8', '2014-09-13 20:52:05', null, '2014-09-13 20:52:05', null, '2');
INSERT INTO `menu` VALUES ('18', null, '参与的活动', '#', '2', '200010040', '8', '2014-09-13 20:52:06', null, '2014-09-13 20:52:06', null, '2');
INSERT INTO `menu` VALUES ('19', null, '会员等级', 'personal/forMyProduction?menuId=3', '2', '200010050', '8', '2014-09-16 21:23:58', null, '2014-09-16 21:23:58', null, '2');
INSERT INTO `menu` VALUES ('20', null, '会员积分', 'personal/forMyProduction?menuId=3', '2', '200010060', '8', '2014-09-16 21:24:02', null, '2014-09-16 21:24:02', null, '2');
INSERT INTO `menu` VALUES ('21', null, '动画收藏', '#', '2', '200010070', '8', '2014-09-13 20:52:09', null, '2014-09-13 20:52:09', null, '2');
INSERT INTO `menu` VALUES ('35', null, '修改密码', 'personal/forMyProduction?menuId=4', '2', '200040020', '8', '2014-09-16 21:24:06', null, '2014-09-16 21:24:06', null, '2');
INSERT INTO `menu` VALUES ('37', null, '首页', '#', '1', '100000000', null, '2014-09-13 21:11:11', null, '2014-09-13 21:11:11', null, '3');
INSERT INTO `menu` VALUES ('38', null, '论坛', '#', '1', '200000000', null, '2014-09-13 21:11:15', null, '2014-09-13 21:11:15', null, '3');
INSERT INTO `menu` VALUES ('39', null, '同城会', '#', '1', '300000000', null, '2014-09-13 21:11:19', null, '2014-09-13 21:11:19', null, '3');
INSERT INTO `menu` VALUES ('40', null, '客服', '#', '1', '400000000', null, '2014-09-13 21:11:24', null, '2014-09-13 21:11:24', null, '3');
INSERT INTO `menu` VALUES ('41', null, '更多', '#', '1', '500000000', null, '2014-09-13 21:11:30', null, '2014-09-13 21:11:30', null, '3');
INSERT INTO `menu` VALUES ('42', null, '全部商品分类', '#', '1', '100000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('43', null, '创意生活', '#', '1', '200000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('44', null, '无土栽培', '#', '1', '300000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('45', null, ' 进口食品', '#', '1', '400000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('46', null, '母婴用品', '#', '1', '500000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('47', null, '动漫周边', '#', '1', '600000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('48', null, '图书音像', '#', '1', '700000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('49', null, '跳蚤市场', '#', '1', '800000000', null, null, null, null, null, '4');
