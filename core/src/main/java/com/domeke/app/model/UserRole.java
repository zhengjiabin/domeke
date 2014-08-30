/**
 * 
 */
package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author lijiasen@domeke.com
 *CREATE TABLE IF NOT EXISTS `domeke`.`user_role` (
  `userroleid` bigint(20) NULL AUTO_INCREMENT,
  `roleid` bigint(20) NULL,
  `userid` bigint(20) null,
  `createtime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` bigint(20) NULL,
  `modifier` bigint(20) NULL,
  `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userroleid`))
ENGINE = InnoDB
 */
@TableBind(tableName = "user_role", pkName = "userroleid")
public class UserRole extends Model<UserRole> {

}