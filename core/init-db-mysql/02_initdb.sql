
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

INSERT INTO `code_table` VALUES ('1', '10', 'workstype', '原创动漫', '原创动漫', '1', '2014-09-18 19:28:31', null, '2014-09-18 19:28:31', null, '0');
INSERT INTO `code_table` VALUES ('2', '20', 'workstype', '亲子动漫', '亲子动漫', '2', '2014-09-18 19:28:36', null, '2014-09-18 19:28:36', null, '0');
INSERT INTO `code_table` VALUES ('3', '30', 'workstype', '无奇不有', '无奇不有', '3', '2014-09-18 19:28:41', null, '2014-09-18 19:28:41', null, '0');
INSERT INTO `code_table` VALUES ('4', '40', 'workstype', 'HIGH动漫', 'HIGH动漫', '4', '2014-09-18 19:28:45', null, '2014-09-18 19:28:45', null, '0');
INSERT INTO `code_table` VALUES ('5', '10001', 'idtype', '作品回复', 'works', null, '2014-09-18 19:28:49', null, '2014-09-18 19:28:49', null, '0');
INSERT INTO `code_table` VALUES ('6', '1000', 'workstype', '首页推荐', '首页推荐', '5', '2014-09-18 19:28:54', null, '2014-09-18 19:28:54', null, '0');
INSERT INTO `code_table` VALUES ('7', '100102', 'goodstype', '小米路由', '小米路由', null, '2014-09-18 19:26:56', null, '2014-09-18 19:26:59', null, '0');
INSERT INTO `code_table` VALUES ('8', '100103', 'goodstype', '小米手机', '小米手机', null, '2014-09-18 19:29:41', null, '2014-09-18 19:29:43', null, '0');
INSERT INTO `code_table` VALUES ('9', '100104', 'goodstype', '路由器', '路由器', null, '2014-09-18 19:30:37', null, '2014-09-18 19:30:40', null, '0');
INSERT INTO `code_table` VALUES ('10', '100105', 'goodstype', '自行车', '自行车', null, '2014-09-18 19:31:27', null, '2014-09-18 19:31:27', null, '0');






