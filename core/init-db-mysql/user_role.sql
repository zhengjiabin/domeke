CREATE TABLE IF NOT EXISTS `domeke`.`user_role` (
  `userroleid` bigint(20) NULL AUTO_INCREMENT,
  `roleid` bigint(20) NULL,
  `userid` bigint(20) null,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userroleid`))
ENGINE = InnoDB