
INSERT INTO domeke.user
(userid,username, password, email, mobile, create_time, creater, modifier, modify_time,activation)
VALUES('1','admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP,'Y');

INSERT INTO domeke.role(roleid,rolename) VALUES('1','admin');
INSERT INTO domeke.role(roleid,rolename) VALUES('2','user');
INSERT INTO domeke.role(roleid,rolename) VALUES('3','guest');

insert into domeke.user_role(userid,roleid) values('1','1');







