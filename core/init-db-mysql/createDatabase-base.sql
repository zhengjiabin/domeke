CREATE TABLE IF NOT EXISTS `domeke`.`user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `email` VARCHAR(255) NULL,
  `moblie` VARCHAR(32) NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` VARCHAR(64) NULL,
  `modifier` VARCHAR(64) NULL,
  `modify_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_idx` (`username` ASC),
  UNIQUE INDEX `email` (`email` ASC));
  
INSERT INTO domeke.user
(id, username, password, email, moblie, create_time, creater, modifier, modify_time)
VALUES(0, 'admin', '', '7061089@qq.com', '11111111111', CURRENT_TIMESTAMP, 'admin', 'admin', CURRENT_TIMESTAMP);

