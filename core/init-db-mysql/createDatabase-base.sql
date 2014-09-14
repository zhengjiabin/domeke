
  
  
INSERT INTO domeke.user
(username, password, email, moblie, create_time, creater, modifier, modify_time)
VALUES('admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP);




INSERT INTO domeke.role(rolename) VALUES('user');
INSERT INTO domeke.role(rolename) VALUES('admin');
INSERT INTO domeke.role(rolename) VALUES('guest');


INSERT INTO domeke.Permission(roleid,Permission,remark) VALUES(2,'all','超级管理员');




