/**
 * 
 */
package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 * 角色
CREATE TABLE IF NOT EXISTS `domeke`.`role` (
  `roleid` bigint(20) NULL AUTO_INCREMENT,
  `rolename` VARCHAR(45) NULL,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
 */
@TableBind(tableName = "role", pkName = "roleid")
public class Role extends Model<Role> {
	public static Role dao = new Role();

}
