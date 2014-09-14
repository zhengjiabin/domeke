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


INSERT INTO `menu` VALUES ('1', '10', '首页', 'index', '1', '100000000', null, '2014-09-13 19:13:02', null, '2014-09-13 19:13:02', null, '1');
INSERT INTO `menu` VALUES ('2', '20', '动漫', '/core/menu/cartoon', '1', '200000000', null, '2014-09-13 22:43:09', null, '2014-09-13 22:43:09', null, '1');
INSERT INTO `menu` VALUES ('3', '30', '无奇不有', '#', '1', '300000000', null, '2014-09-13 19:13:06', null, '2014-09-13 19:13:06', null, '1');
INSERT INTO `menu` VALUES ('4', '40', '商城', 'shop', '1', '400000000', null, '2014-09-13 22:20:08', null, '2014-09-13 22:20:08', null, '1');
INSERT INTO `menu` VALUES ('5', '50', 'HIGH豆社区', 'forum', '1', '500000000', null, '2014-09-13 22:20:13', null, '2014-09-13 22:20:13', null, '1');
INSERT INTO `menu` VALUES ('6', '60', '签到发泄墙', 'ventwall', '1', '600000000', null, '2014-09-13 19:13:10', null, '2014-09-13 19:13:10', null, '1');
INSERT INTO `menu` VALUES ('7', '70', '菜单管理', '/core/menu/selectMenu', '1', '700000000', null, '2014-09-13 19:13:12', null, '2014-09-13 19:13:12', null, '1');
INSERT INTO `menu` VALUES ('8', '20020', '个人中心', '#', '1', '200010000', null, '2014-09-13 20:51:46', null, '2014-09-13 20:51:46', null, '2');
INSERT INTO `menu` VALUES ('9', '20030', '个人账户', '#', '1', '200020000', null, '2014-09-13 20:51:47', null, '2014-09-13 20:51:47', null, '2');
INSERT INTO `menu` VALUES ('10', null, '售后服务', '#', '1', '200030000', null, '2014-09-13 20:51:49', null, '2014-09-13 20:51:49', null, '2');
INSERT INTO `menu` VALUES ('11', null, '账户管理', '#', '1', '200040000', null, '2014-09-13 20:51:51', null, '2014-09-13 20:51:51', null, '2');
INSERT INTO `menu` VALUES ('15', null, '我的作品', '#', '2', '200010010', '8', '2014-09-13 20:52:03', null, '2014-09-13 20:52:03', null, '2');
INSERT INTO `menu` VALUES ('16', null, '播放记录', '#', '2', '200010020', '8', '2014-09-13 20:52:04', null, '2014-09-13 20:52:04', null, '2');
INSERT INTO `menu` VALUES ('17', null, '我的下载', '#', '2', '200010030', '8', '2014-09-13 20:52:05', null, '2014-09-13 20:52:05', null, '2');
INSERT INTO `menu` VALUES ('18', null, '参与的活动', '#', '2', '200010040', '8', '2014-09-13 20:52:06', null, '2014-09-13 20:52:06', null, '2');
INSERT INTO `menu` VALUES ('19', null, '会员等级', '#', '2', '200010050', '8', '2014-09-13 20:52:07', null, '2014-09-13 20:52:07', null, '2');
INSERT INTO `menu` VALUES ('20', null, '会员积分', '#', '2', '200010060', '8', '2014-09-13 20:52:08', null, '2014-09-13 20:52:08', null, '2');
INSERT INTO `menu` VALUES ('21', null, '动画收藏', '#', '2', '200010070', '8', '2014-09-13 20:52:09', null, '2014-09-13 20:52:09', null, '2');
INSERT INTO `menu` VALUES ('22', null, '我的订单', '#', '2', '200020010', '9', '2014-09-13 20:52:10', null, '2014-09-13 20:52:10', null, '2');
INSERT INTO `menu` VALUES ('24', null, '我的付费服务', '#', '2', '200020020', '9', '2014-09-13 20:52:11', null, '2014-09-13 20:52:11', null, '2');
INSERT INTO `menu` VALUES ('25', null, '我的现金账户', '#', '2', '200020030', '9', '2014-09-13 20:52:11', null, '2014-09-13 20:52:11', null, '2');
INSERT INTO `menu` VALUES ('26', null, '商品评价', '#', '2', '200020040', '9', '2014-09-13 20:52:12', null, '2014-09-13 20:52:12', null, '2');
INSERT INTO `menu` VALUES ('27', null, '商品收藏', '#', '2', '200020050', '9', '2014-09-13 20:52:13', null, '2014-09-13 20:52:13', null, '2');
INSERT INTO `menu` VALUES ('28', null, '我的优惠券', '#', '2', '200020060', '9', '2014-09-13 20:52:14', null, '2014-09-13 20:52:14', null, '2');
INSERT INTO `menu` VALUES ('29', null, '换货单', '#', '2', '200030010', '10', '2014-09-13 20:52:14', null, '2014-09-13 20:52:14', null, '2');
INSERT INTO `menu` VALUES ('30', null, '退货单', '#', '2', '200030020', '10', '2014-09-13 20:52:15', null, '2014-09-13 20:52:15', null, '2');
INSERT INTO `menu` VALUES ('31', null, '维修单', '#', '2', '200030030', '10', '2014-09-13 20:52:17', null, '2014-09-13 20:52:17', null, '2');
INSERT INTO `menu` VALUES ('32', null, '领取快递补偿', '#', '2', '200030040', '10', '2014-09-13 20:52:18', null, '2014-09-13 20:52:18', null, '2');
INSERT INTO `menu` VALUES ('33', null, '预约亲临门店服务', '#', '2', '200030050', '10', '2014-09-13 20:52:20', null, '2014-09-13 20:52:20', null, '2');
INSERT INTO `menu` VALUES ('34', null, '收货地址管理', '#', '2', '200040010', '11', '2014-09-13 21:11:01', null, '2014-09-13 21:11:01', null, '2');
INSERT INTO `menu` VALUES ('35', null, '修改密码', '#', '2', '200040020', '11', '2014-09-13 21:11:02', null, '2014-09-13 21:11:02', null, '2');
INSERT INTO `menu` VALUES ('36', null, 'VIP认证', '#', '2', '200040030', '11', '2014-09-13 21:11:04', null, '2014-09-13 21:11:04', null, '2');
INSERT INTO `menu` VALUES ('37', null, '首页', '#', '1', '100000000', null, '2014-09-13 21:11:11', null, '2014-09-13 21:11:11', null, '3');
INSERT INTO `menu` VALUES ('38', null, '论坛', '#', '1', '200000000', null, '2014-09-13 21:11:15', null, '2014-09-13 21:11:15', null, '3');
INSERT INTO `menu` VALUES ('39', null, '同城会', '#', '1', '300000000', null, '2014-09-13 21:11:19', null, '2014-09-13 21:11:19', null, '3');
INSERT INTO `menu` VALUES ('40', null, '客服', '#', '1', '400000000', null, '2014-09-13 21:11:24', null, '2014-09-13 21:11:24', null, '3');
INSERT INTO `menu` VALUES ('41', null, '更多', '#', '1', '500000000', null, '2014-09-13 21:11:30', null, '2014-09-13 21:11:30', null, '3');
INSERT INTO `menu` VALUES ('42', null, '全部商品分类', '#', '1', '100000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('43', null, '创意生活', '#', '1', '200000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('44', null, '无土栽培', '#', '1', '300000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('45', null, '进口食品', '#', '1', '400000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('46', null, '母婴用品', '#', '1', '500000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('47', null, '动漫周边', '#', '1', '600000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('48', null, '图书音像', '#', '1', '700000000', null, null, null, null, null, '4');
INSERT INTO `menu` VALUES ('49', null, '跳蚤市场', '#', '1', '800000000', null, null, null, null, null, '4');
