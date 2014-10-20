
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





