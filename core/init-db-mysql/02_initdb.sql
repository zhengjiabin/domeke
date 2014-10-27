
INSERT INTO domeke.user
(userid,username, password, email, mobile, create_time, creater, modifier, modify_time,activation)
VALUES('1','admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP,'Y');

INSERT INTO domeke.role(roleid,rolename) VALUES('2','user');
INSERT INTO domeke.role(roleid,rolename) VALUES('1','admin');
INSERT INTO domeke.role(roleid,rolename) VALUES('3','guest');

INSERT INTO domeke.user_role(userroleid,roleid,userid) VALUES('1','2','1');

INSERT INTO `code_type` VALUES ('1', 'workstype', '作品类型', null, '2014-09-15 22:47:48', '1', '2014-09-15 22:47:52', '1');
INSERT INTO `code_type` VALUES ('2', 'idtype', '回复类型', null, '2014-09-16 21:40:13', '1', '2014-09-16 21:40:17', '1');
INSERT INTO `code_type` VALUES ('3', 'goodstype', '商品类型', null, '2014-09-18 20:06:49', '1', '2014-09-18 20:06:49', '1');
INSERT INTO `code_type` VALUES ('4', 'grade', '等级', null, '2014-09-23 21:38:44', null, '2014-09-23 21:38:48', '1');
INSERT INTO `code_type` VALUES ('5', 'gender', '性别', null, '2014-9-25 19:42:24', '1', '2014-9-25 19:42:30', '1');
INSERT INTO `code_type` VALUES ('6', 'papers', '证件类型', null, '2014-9-27 10:43:26', '1', '2014-9-27 10:43:33', '1');
INSERT INTO `code_type` VALUES ('7', 'level', '级别', null, '2014-10-18 17:37:57', '1', '2014-10-18 17:37:57', '1');

INSERT INTO `code_table` VALUES ('1', '00', 'workstype', '首页推荐', '首页推荐', '1', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('2', '10', 'workstype', '原创动漫', '原创动漫', '2', '2014-09-18 19:28:31', null, '2014-09-18 19:28:31', null, '0');
INSERT INTO `code_table` VALUES ('3', '20', 'workstype', '亲子动漫', '亲子动漫', '3', '2014-09-18 19:28:36', null, '2014-09-18 19:28:36', null, '0');
INSERT INTO `code_table` VALUES ('5', '30', 'workstype', 'HIGH动漫', 'HIGH动漫', '5', '2014-09-18 19:28:45', null, '2014-09-18 19:28:45', null, '0');
INSERT INTO `code_table` VALUES ('6', '40', 'workstype', 'HI豆推荐', 'HI豆推荐', '6', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('7', '50', 'workstype', '原创精选', '原创精选', '7', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('8', '60', 'workstype', '人气作品', '人气作品', '8', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('9', '70', 'workstype', '新作预告', '新作预告', '9', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('10', '10001', 'idtype', '作品回复', 'works', null, '2014-09-18 19:28:49', null, '2014-09-18 19:28:49', null, '0');
INSERT INTO `code_table` VALUES ('11', '100102', 'goodstype', '小米路由', '小米路由', null, '2014-09-18 19:26:56', null, '2014-09-18 19:26:59', null, '0');
INSERT INTO `code_table` VALUES ('12', '100103', 'goodstype', '小米手机', '小米手机', null, '2014-09-18 19:29:41', null, '2014-09-18 19:29:43', null, '0');
INSERT INTO `code_table` VALUES ('13', '100104', 'goodstype', '路由器', '路由器', null, '2014-09-18 19:30:37', null, '2014-09-18 19:30:40', null, '0');
INSERT INTO `code_table` VALUES ('14', '100105', 'goodstype', '自行车', '自行车', null, '2014-09-18 19:31:27', null, '2014-09-18 19:31:27', null, '0');
INSERT INTO `code_table` VALUES ('15', '0', 'grade', '豆米粒', '豆米粒', null, '2014-09-23 21:56:31', null, '2014-09-23 21:56:31', null, '0');
INSERT INTO `code_table` VALUES ('16', '100', 'grade', '豆芽棒', '豆芽棒', null, '2014-09-23 21:56:56', null, '2014-09-23 21:56:56', null, '0');
INSERT INTO `code_table` VALUES ('17', '500', 'grade', '豆瓣花', '豆瓣花', null, '2014-09-23 21:57:13', null, '2014-09-23 21:57:13', null, '0');
INSERT INTO `code_table` VALUES ('18', '1500', 'grade', '小弯豆', '小弯豆', null, '2014-09-23 21:57:49', null, '2014-09-23 21:57:51', null, '0');
INSERT INTO `code_table` VALUES ('19', '4000', 'grade', '豆青', '豆青', null, '2014-09-23 21:58:28', null, '2014-09-23 21:58:30', null, '0');
INSERT INTO `code_table` VALUES ('20', '20000', 'grade', '豆爷', '豆爷', null, '2014-09-23 21:59:47', null, '2014-09-23 21:59:47', null, '0');
INSERT INTO `code_table` VALUES ('21', '50000', 'grade', '豆皇', '豆皇', null, '2014-09-23 21:59:48', null, '2014-09-23 21:59:48', null, '0');
INSERT INTO `code_table` VALUES ('22', '0', 'gender', '男', '男', '1', '2014-9-25 19:45:20', '1', '2014-9-25 19:45:27', '1', '0');
INSERT INTO `code_table` VALUES ('23', '1', 'gender', '女', '女', '2', '2014-9-25 19:45:24', '1', '2014-9-25 19:45:31', '1', '0');
INSERT INTO `code_table` VALUES ('24', '0', 'papers', '身份证', '身份证', '1', '2014-9-27 10:45:08', '1', '2014-9-27 10:45:13', '1', '0');
INSERT INTO `code_table` VALUES ('25', '1', 'papers', '军官证', '身份证', '2', '2014-9-27 10:45:08', '1', '2014-9-27 10:45:08', '1', '0');
INSERT INTO `code_table` VALUES ('26', '2', 'papers', '士兵证', '士兵证', '3', '2014-9-27 10:45:08', '1', '2014-9-27 10:45:08', '1', '0');
INSERT INTO `code_table` VALUES ('27', '3', 'papers', '学生证', '学生证', '4', '2014-9-27 10:45:08', '1', '2014-9-27 10:45:08', '1', '0');
INSERT INTO `code_table` VALUES ('28', '4', 'papers', '驾驶证', '驾驶证', '5', '2014-9-27 10:45:08', '1', '2014-9-27 10:45:08', '1', '0');
INSERT INTO `code_table` VALUES ('29', '1', 'level', '一级', '一级', '10', '2014-10-20 19:49:32', null, '2014-10-20 19:49:32', null, '0');
INSERT INTO `code_table` VALUES ('30', '2', 'level', '二级', '二级', '20', '2014-10-20 19:49:33', null, '2014-10-20 19:49:33', null, '0');
INSERT INTO `code_table` VALUES ('31', '3', 'level', '三级', '三级', '30', '2014-10-20 19:49:34', null, '2014-10-20 19:49:34', null, '0');


INSERT INTO `menu` VALUES (1, 10, '首页', 'index', '1', 100000000, NULL, '2014-9-13 11:13:02', NULL, '2014-9-13 11:13:02', NULL, '1');
INSERT INTO `menu` VALUES (2, 20, '动漫', 'cartoon', '1', 200000000, NULL, '2014-9-13 14:43:09', NULL, '2014-9-13 14:43:09', NULL, '1');
INSERT INTO `menu` VALUES (3, 30, '无奇不有', 'wondersType', '1', 300000000, NULL, '2014-10-18 11:56:12', NULL, '2014-10-18 11:56:12', NULL, '1');
INSERT INTO `menu` VALUES (4, 40, '商城', 'goods/shop', '1', 400000000, NULL, '2014-9-13 14:20:08', NULL, '2014-9-13 14:20:08', NULL, '1');
INSERT INTO `menu` VALUES (5, 50, 'HIGH豆社区', 'forum', '1', 500000000, NULL, '2014-9-13 14:20:13', NULL, '2014-9-13 14:20:13', NULL, '1');
INSERT INTO `menu` VALUES (6, 60, '签到发泄墙', 'ventwall', '1', 600000000, NULL, '2014-9-13 11:13:10', NULL, '2014-9-13 11:13:10', NULL, '1');
INSERT INTO `menu` VALUES (8, 20020, '个人中心', '#', '1', 200010000, NULL, '2014-9-13 12:51:46', NULL, '2014-9-13 12:51:46', NULL, '2');
INSERT INTO `menu` VALUES (15, NULL, '我的作品', 'personal/forMyProduction?menuId=1', '2', 200010010, 8, '2014-9-16 13:23:35', NULL, '2014-9-16 13:23:35', NULL, '2');
INSERT INTO `menu` VALUES (16, NULL, '播放记录', 'personal/forMyProduction?menuId=2', '2', 200010020, 8, '2014-9-16 13:23:39', NULL, '2014-9-16 13:23:39', NULL, '2');
INSERT INTO `menu` VALUES (17, NULL, '我的下载', 'personal/forMyProduction?menuId=13', '2', 200010030, 8, '2014-9-13 12:52:05', NULL, '2014-9-13 12:52:05', NULL, '2');
INSERT INTO `menu` VALUES (18, NULL, '参与的活动', 'personal/forMyProduction?menuId=14', '2', 200010040, 8, '2014-9-13 12:52:06', NULL, '2014-9-13 12:52:06', NULL, '2');
INSERT INTO `menu` VALUES (19, NULL, '会员等级', 'personal/forMyProduction?menuId=3', '2', 200010050, 8, '2014-9-16 13:23:58', NULL, '2014-9-16 13:23:58', NULL, '2');
INSERT INTO `menu` VALUES (20, NULL, '会员积分', 'personal/forMyProduction?menuId=3', '2', 200010060, 8, '2014-9-16 13:24:02', NULL, '2014-9-16 13:24:02', NULL, '2');
INSERT INTO `menu` VALUES (21, NULL, '动画收藏', '#', '2', 200010070, 8, '2014-9-13 12:52:09', NULL, '2014-9-13 12:52:09', NULL, '2');
INSERT INTO `menu` VALUES (37, NULL, '社区', 'community', '1', 200000000, NULL, '2014-9-21 09:38:36', NULL, '2014-9-21 09:38:36', NULL, '3');
INSERT INTO `menu` VALUES (38, NULL, '论坛', 'post/home', '1', 300000000, NULL, '2014-9-21 09:38:14', NULL, '2014-9-21 09:38:14', NULL, '3');
INSERT INTO `menu` VALUES (39, NULL, '活动', 'activity/home', '1', 400000000, NULL, '2014-9-21 09:38:02', NULL, '2014-9-21 09:38:02', NULL, '3');
INSERT INTO `menu` VALUES (40, NULL, '宝贝', 'treasure/home', '1', 500000000, NULL, '2014-9-21 09:37:50', NULL, '2014-9-21 09:37:50', NULL, '3');
INSERT INTO `menu` VALUES (42, NULL, '全部商品分类', '#', '1', 100000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (43, NULL, '创意生活', '#', '1', 200000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (44, NULL, '无土栽培', '#', '1', 300000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (45, NULL, ' 进口食品', '#', '1', 400000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (46, NULL, '母婴用品', '#', '1', 500000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (47, NULL, '动漫周边', '#', '1', 600000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (48, NULL, '图书音像', '#', '1', 700000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (49, NULL, '跳蚤市场', '#', '1', 800000000, NULL, NULL, NULL, NULL, NULL, '4');
INSERT INTO `menu` VALUES (50, NULL, '修改密码', 'personal/forMyProduction?menuId=4', '2', 200010080, 8, NULL, NULL, NULL, NULL, '2');
INSERT INTO `menu` VALUES (51, NULL, '站内留言', 'um/forLeaveMsg?menuId=5', '2', 200010005, 8, NULL, NULL, NULL, NULL, '2');
INSERT INTO `menu` VALUES (52, NULL, '首页', '#', '1', 100000000, NULL, '2014-9-21 09:37:21', NULL, '2014-9-21 09:37:21', NULL, '3');
INSERT INTO `menu` VALUES (53,NULL,'资料修改','personal/forMyProduction?menuId=12','2',200010075,8,NULL,NULL,NULL,NULL,'2');
INSERT INTO `menu` VALUES (54,NULL,'发布的活动','personal/forMyProduction?menuId=15','2',200010041,8,NULL,NULL,NULL,NULL,'2');
INSERT INTO `menu` VALUES (55,NULL,'发布的帖子','personal/forMyProduction?menuId=16','2',200010042,8,NULL,NULL,NULL,NULL,'2');
INSERT INTO `menu` VALUES (56,NULL,'发布的宝贝','personal/forMyProduction?menuId=17','2',200010043,8,NULL,NULL,NULL,NULL,'2');
INSERT INTO `menu` VALUES (57,NULL,'我的无奇不有','personal/forMyProduction?menuId=18','2',200010070,8,NULL,NULL,NULL,NULL,'2');

INSERT INTO `goods_type` VALUES ('1', '家用电器', '#', '1', '10000000', null, '1');
INSERT INTO `goods_type` VALUES ('2', '大家电', '#', '2', '10100000', '1', '1');
INSERT INTO `goods_type` VALUES ('3', '生活电器', '#', '2', '10200000', '1', '1');
INSERT INTO `goods_type` VALUES ('4', '厨房电器', '#', '2', '10300000', '1', '1');
INSERT INTO `goods_type` VALUES ('5', '五金家装', '#', '2', '10400000', '1', '1');
INSERT INTO `goods_type` VALUES ('6', '平板电脑', '#', '3', '10101000', '2', '1');
INSERT INTO `goods_type` VALUES ('7', '空调', '#', '3', '10102000', '2', '1');
INSERT INTO `goods_type` VALUES ('8', '冰箱', '#', '3', '10103000', '2', '1');
INSERT INTO `goods_type` VALUES ('9', '电风扇', '#', '3', '10201000', '3', '1');
INSERT INTO `goods_type` VALUES ('10', '插座', '#', '3', '10202000', '3', '1');
INSERT INTO `goods_type` VALUES ('11', '电压力锅', '#', '3', '10301000', '4', '1');
INSERT INTO `goods_type` VALUES ('12', '电磁炉', '#', '3', '10302000', '4', '1');
INSERT INTO `goods_type` VALUES ('13', '电动工具', '#', '3', '10401000', '5', '1');
INSERT INTO `goods_type` VALUES ('14', '手动工具', '#', '3', '10402000', '5', '1');

INSERT INTO `works_type` VALUES ('10', '原创动漫', '1', '99', '原创动漫');
INSERT INTO `works_type` VALUES ('20', '亲子动漫', '1', '98', '亲子动漫');
INSERT INTO `works_type` VALUES ('30', 'HIGH动漫', '1', '97', 'HIGH动漫');
INSERT INTO `works_type` VALUES ('40', 'HI豆推荐', '1', '96', 'HI豆推荐');
INSERT INTO `works_type` VALUES ('50', '原创精选', '1', '95', '原创精选');
INSERT INTO `works_type` VALUES ('60', '人气作品', '1', '94', '人气作品');
INSERT INTO `works_type` VALUES ('61', '新作预告', '1', '93', '新作预告');
INSERT INTO `works_type` VALUES ('70', '搞笑视频', '0', '92', '搞笑视频');