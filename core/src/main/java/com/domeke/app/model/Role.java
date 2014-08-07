/**
 * 
 */
package com.domeke.app.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 * 角色
CREATE TABLE IF NOT EXISTS `domeke`.`role` (
  `roleid` bigint(20) NULL AUTO_INCREMENT,
  `rolename` VARCHAR(45) NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` VARCHAR(45) NULL,
  `modifier` VARCHAR(45) NULL,
  `modify_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
 */
public class Role extends Model<Role> {
	public static Role dao = new Role();

}
