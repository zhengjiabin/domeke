/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : domeke

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-09-09 22:07:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `work`
-- ----------------------------
DROP TABLE IF EXISTS `work`;
CREATE TABLE `work` (
  `workid` bigint(20) NOT NULL AUTO_INCREMENT,
  `worksid` bigint(20) NOT NULL COMMENT '父类ID',
  `worknum` int(10) DEFAULT '1' COMMENT '第几集',
  `workname` varchar(20) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '名称',
  `workdes` varchar(255) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '简介',
  `comic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源地址',
  `isdisable` tinyint(1) DEFAULT '0' COMMENT '是否禁用 0否1禁',
  `createtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `creater` bigint(20) NOT NULL,
  `modifytime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `modifier` bigint(20) NOT NULL,
  PRIMARY KEY (`workid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for works
-- ----------------------------
DROP TABLE IF EXISTS `works`;
CREATE TABLE `works` (
  `worksid` bigint(20) NOT NULL AUTO_INCREMENT,
  `worksname` varchar(255) NOT NULL,
  `workstype` varchar(32) DEFAULT '10',
  `creativeprocess` varchar(32) DEFAULT '10',
  `cover` varchar(255) NOT NULL,
  `describle` varchar(1024) DEFAULT NULL,
  `istop` int(11) DEFAULT '0',
  `comment` bigint(20) DEFAULT '0',
  `pageviews` bigint(20) DEFAULT '0',
  `collection` bigint(20) DEFAULT '0',
  `praise` bigint(20) DEFAULT '0',
  `maxnum` bigint(20) DEFAULT '0', 
  `updatetime` timestamp DEFAULT CURRENT_TIMESTAMP,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NOT NULL,
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` bigint(20) NOT NULL,
  PRIMARY KEY (`worksid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `actionid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '动作名称',
  `des` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `limits` tinyint(5) NOT NULL DEFAULT '0' COMMENT '周期范围 单位天',
  `maxnum` int(10) NOT NULL DEFAULT '0',
  `peas` tinyint(3) NOT NULL DEFAULT '0' COMMENT '豆豆',
  `point` tinyint(3) NOT NULL DEFAULT '0',
  `creater` mediumint(8) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` mediumint(8) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`actionid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for `user_action`
-- ----------------------------
DROP TABLE IF EXISTS `user_action`;
CREATE TABLE `user_action` (
  `useractionid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `username` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actionid` bigint(20) NOT NULL,
  `peas` bigint(20) DEFAULT '0',
  `point` bigint(20) DEFAULT '0',
  `actionname` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actiondes` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `headimg` varchar(255) DEFAULT NULL,
  `times` tinyint(3) NOT NULL DEFAULT '0',
  `create` mediumint(8) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` mediumint(8) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`useractionid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for community
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community` (
  `communityid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `title`  varchar(64) NOT NULL,
  `content`  varchar(255) DEFAULT NULL,
  `actionkey`  varchar(255) DEFAULT NULL,
  `pid` mediumint(8) DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '1',
  `position` int(11) DEFAULT '0',
  `times` bigint(20) DEFAULT '0',
  `status` tinyint(1) DEFAULT '10',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`communityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activityid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(80) NOT NULL,
  `userid` bigint(20) NOT NULL DEFAULT '0',
  `aid` int(11) NOT NULL DEFAULT '0',
  `cost` mediumint(8) NOT NULL DEFAULT '0',
  `starttimefrom` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `starttimeto` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `place` varchar(255) NOT NULL,
  `class` varchar(255) NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `number` smallint(5) unsigned NOT NULL DEFAULT '0',
  `applynumber` smallint(5) unsigned NOT NULL DEFAULT '0',
  `expiration` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ufield` text NOT NULL,
  `credit` smallint(6) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(1) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `communityid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activity_apply
-- ----------------------------
DROP TABLE IF EXISTS `activity_apply`;
CREATE TABLE `activity_apply` (
  `activityapplyid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `activityid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `message` varchar(255) NOT NULL,
  `verified` tinyint(1) NOT NULL DEFAULT '0',
  `dateline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `payment` mediumint(8) NOT NULL DEFAULT '0',
  `ufielddata` text NOT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT '0',
  `papers` tinyint(1) DEFAULT '0',
  `papersid` bigint(20) DEFAULT '0',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`activityapplyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for approve
-- ----------------------------
DROP TABLE IF EXISTS `approve`;
CREATE TABLE `approve` (
  `ApproveId` int(11) NOT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `typeid` varchar(50) DEFAULT NULL COMMENT '审批类型id  如：“商城”“作品”。。。。。',
  `detailid` varchar(50) DEFAULT NULL COMMENT '明细项id',
  `status` varchar(50) DEFAULT NULL COMMENT '审批状态',
  PRIMARY KEY (`ApproveId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
  `aattachmentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `postid` int(10) unsigned NOT NULL DEFAULT '0',
  `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `dateline` int(14) unsigned NOT NULL DEFAULT '0',
  `filename` varchar(255) NOT NULL,
  `filesize` int(10) unsigned NOT NULL DEFAULT '0',
  `attachment` varchar(255) NOT NULL,
  `remote` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `isimage` tinyint(1) NOT NULL DEFAULT '0',
  `width` smallint(6) unsigned NOT NULL DEFAULT '0',
  `downloads` mediumint(8) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`aattachmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for code_table
-- ----------------------------
DROP TABLE IF EXISTS `code_table`;
CREATE TABLE `code_table` (
  `codetableid` bigint(20) NOT NULL AUTO_INCREMENT,
  `codekey` varchar(32) DEFAULT NULL,
  `codetype` varchar(32) DEFAULT NULL,
  `codename` varchar(32) DEFAULT NULL,
  `codevalue` varchar(256) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`codetableid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for code_type
-- ----------------------------
DROP TABLE IF EXISTS `code_type`;
CREATE TABLE `code_type` (
  `codetypeid` bigint(20) NOT NULL AUTO_INCREMENT,
  `codetype` varchar(32) DEFAULT NULL,
  `typename` varchar(256) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`codetypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `commentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `userid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `pid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `targetid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `touserid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '1',
  `idtype` varchar(20) NOT NULL,
  `userip` varchar(20) NOT NULL,
  `dateline` int(14) unsigned NOT NULL DEFAULT '0',
  `message` text NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '10',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`commentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for downloadcount
-- ----------------------------
DROP TABLE IF EXISTS `downloadcount`;
CREATE TABLE `downloadcount` (
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
-- Table structure for favourite
-- ----------------------------
DROP TABLE IF EXISTS `favourite`;
CREATE TABLE `favourite` (
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
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goodsid` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods` varchar(50) NOT NULL,
  `goodsname` varchar(255) NOT NULL,
  `price` float(7,2) unsigned NOT NULL COMMENT '现价',
  `oldprice` float(7,2) COMMENT '原价',
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `pic` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `tamllurl` varchar(255) NOT NULL,
  `submitdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `istop` int(11) DEFAULT '0' COMMENT '是否置顶0否 大于1置顶并降序排列',
  `status` varchar(4) NOT NULL DEFAULT '10',
  `username` varchar(64) DEFAULT NULL,
  `headimg` varchar(64) DEFAULT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NOT NULL,
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` bigint(20) NOT NULL,
  PRIMARY KEY (`goodsid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for message_queue
-- ----------------------------
DROP TABLE IF EXISTS `message_queue`;
CREATE TABLE `message_queue` (
  `messageid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(512) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `from` varchar(32) DEFAULT NULL,
  `to` varchar(4000) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `orderid` bigint(20) NOT NULL AUTO_INCREMENT,
  `goodsid` bigint(20) NOT NULL,
  `status` varchar(4) NOT NULL DEFAULT '10',
  `buyer` varchar(50) NOT NULL,
  `userid` bigint(20) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL DEFAULT '0',
  `price` float(7,2) unsigned NOT NULL,
  `submitdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(40) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NOT NULL,
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` bigint(20) NOT NULL,
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
-- ----------------------------
-- Table structure for playcount
-- ----------------------------
DROP TABLE IF EXISTS `playcount`;
CREATE TABLE `playcount` (
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
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `postid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `subject` varchar(80) NOT NULL,
  `dateline` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `message` text NOT NULL,
  `userip` varchar(15) NOT NULL,
  `times` bigint(20) DEFAULT '0',
  `supporttimes` bigint(20) DEFAULT '0',
  `opposetimes` bigint(20) DEFAULT '0',
  `top` tinyint(1) NOT NULL DEFAULT '0',
  `essence` tinyint(1) NOT NULL DEFAULT '0',
  `status` varchar(4) NOT NULL DEFAULT '10',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `communityid` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`postid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post_approve
-- ----------------------------
DROP TABLE IF EXISTS `post_approve`;
CREATE TABLE `post_approve` (
  `postapproveid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `postid` bigint(20) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(3) NOT NULL DEFAULT '0',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`postapproveid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` bigint(20) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(45) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` varchar(45) DEFAULT NULL,
  `modifier` varchar(45) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for treasure
-- ----------------------------
DROP TABLE IF EXISTS `treasure`;
CREATE TABLE `treasure` (
  `treasureid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `author` varchar(15) NOT NULL,
  `authorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `subject` varchar(80) NOT NULL,
  `dateline` int(10) unsigned NOT NULL DEFAULT '0',
  `message` text NOT NULL,
  `useip` varchar(15) NOT NULL,
  `invisible` tinyint(1) NOT NULL DEFAULT '0',
  `anonymous` tinyint(1) NOT NULL DEFAULT '0',
  `usesig` tinyint(1) NOT NULL DEFAULT '0',
  `attachment` tinyint(1) NOT NULL DEFAULT '0',
  `rate` smallint(6) NOT NULL DEFAULT '0',
  `ratetimes` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `status` int(10) NOT NULL DEFAULT '0',
  `comment` tinyint(1) NOT NULL DEFAULT '0',
  `replycredit` int(10) NOT NULL DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modifytime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`treasureid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for treasure_approve
-- ----------------------------
DROP TABLE IF EXISTS `treasure_approve`;
CREATE TABLE `treasure_approve` (
  `treasureapproveid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `treasureid` bigint(20) unsigned NOT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '0',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`treasureapproveid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userid` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(16) DEFAULT NULL,
  `mobile` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `creater` varchar(64) DEFAULT NULL,
  `modifier` varchar(64) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  `activation` varchar(2) DEFAULT 'N',
  `peas` bigint(20) DEFAULT NULL,
  `point` bigint(20) DEFAULT NULL,
  `imgurl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username_idx` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
  `messageid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(512) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `from` varchar(32) DEFAULT NULL,
  `touser` varchar(32) DEFAULT NULL,
  `sendtype` varchar(2) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `user` VALUES ('1', 'admin', 'lueSGJZetyySpUndWjMBEg==', '634775305@qq.com', 'admin', null, null, null, null, null, 'Y', null, null, null);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `userroleid` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifier` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userroleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `user_role` VALUES ('1', '1', '11', '2014-09-20 10:52:14', null, null, '2014-09-21 10:32:54');
-- ----------------------------
-- Table structure for vent_wall
-- ----------------------------
DROP TABLE IF EXISTS `vent_wall`;
CREATE TABLE `vent_wall` (
  `ventwallid` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) DEFAULT NULL,
  `moodid` varchar(100) DEFAULT NULL COMMENT '心情id',
  `message` varchar(255) DEFAULT NULL COMMENT '留言',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifier` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ventwallid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `search_key`
-- ----------------------------
DROP TABLE IF EXISTS `search_key`;
CREATE TABLE `search_key` (
  `keyid` bigint(20) NOT NULL AUTO_INCREMENT,
  `keyname` varchar(100) NOT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userid` bigint(20) DEFAULT NULL,
  `modifytime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`keyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

