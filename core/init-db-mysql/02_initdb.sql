
INSERT INTO domeke.user
(userid,username, password, email, mobile, create_time, creater, modifier, modify_time,activation)
VALUES('1','admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP,'Y');

INSERT INTO `user` VALUES ('1', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '有才', '11111111111', 'images/touxiang.png', '2014-09-16 19:49:05', 'admin', 'admin', '2014-09-16 19:49:05', 'Y', '123', '222');



INSERT INTO domeke.role(roleid,rolename) VALUES('1','admin');
INSERT INTO domeke.role(roleid,rolename) VALUES('2','user');
INSERT INTO domeke.role(roleid,rolename) VALUES('3','guest');

insert into domeke.user_role(userid,roleid) values('1','1');

INSERT INTO `code_type` VALUES ('1', 'workstype', '作品类型', null, '2014-09-15 22:47:48', '1', '2014-09-15 22:47:52', '1');
INSERT INTO `code_type` VALUES ('2', 'idtype', '回复类型', null, '2014-09-16 21:40:13', '1', '2014-09-16 21:40:17', '1');

INSERT INTO `code_table` VALUES ('1', '原创动漫', 'workstype', '原创动漫', '10', '1', '2014-09-15 22:57:07', null, '2014-09-15 22:57:07', null, '0');
INSERT INTO `code_table` VALUES ('2', '亲子动漫', 'workstype', '亲子动漫', '20', '2', '2014-09-15 22:57:07', null, '2014-09-15 22:57:07', null, '0');
INSERT INTO `code_table` VALUES ('3', '无奇不有', 'workstype', '无奇不有', '30', '3', '2014-09-15 22:57:08', null, '2014-09-15 22:57:08', null, '0');
INSERT INTO `code_table` VALUES ('4', 'HIGH动漫', 'workstype', 'HIGH动漫', '40', '4', '2014-09-15 22:57:08', null, '2014-09-15 22:57:08', null, '0');
INSERT INTO `code_table` VALUES ('5', 'works', 'idtype', '作品回复', '10001', null, '2014-09-16 21:43:38', null, '2014-09-16 21:43:46', null, '0');
