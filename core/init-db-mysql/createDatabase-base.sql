CREATE TABLE IF NOT EXISTS `domeke`.`user` (
  `userid` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
   `nickname` VARCHAR(64) NULL,
  `password` VARCHAR(32) NOT NULL,
  `email` VARCHAR(255) NULL,
  `mobile` VARCHAR(32) NULL,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`),
  UNIQUE INDEX `username_idx` (`username` ASC),
  UNIQUE INDEX `email` (`email` ASC));
  
  
INSERT INTO domeke.user
(username, password, email, moblie, create_time, creater, modifier, modify_time)
VALUES('admin', 'ISMvKXpXpadDiUoOSoAfww==', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP);


CREATE TABLE IF NOT EXISTS `domeke`.`role` (
  `roleid` bigint(20) NULL AUTO_INCREMENT,
  `rolename` VARCHAR(45) NULL,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleid`))
ENGINE = InnoDB;

INSERT INTO domeke.role(rolename) VALUES('user');
INSERT INTO domeke.role(rolename) VALUES('admin');
INSERT INTO domeke.role(rolename) VALUES('guest');

CREATE TABLE IF NOT EXISTS `domeke`.`Permission` (
  `permissionId` BIGINT NOT NULL,
  `roleid` BIGINT NULL,
  `Permission` VARCHAR(45) NULL,
  `remark` VARCHAR(500) NULL,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`permissionId`),
  CONSTRAINT `fk_role_id`
    FOREIGN KEY (roleid)
    REFERENCES `domeke`.`role` 
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
INSERT INTO domeke.Permission(roleid,Permission,remark) VALUES(2,'all','超级管理员');




