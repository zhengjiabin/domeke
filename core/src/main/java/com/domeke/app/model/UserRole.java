/**
 * 
 */
package com.domeke.app.model;

import java.util.List;

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

	public static UserRole dao = new UserRole();

	public void saveRoleByUserId(String rolename, String userid) {
		String roleid = getRoleIdByRoleName(rolename);
		dao.set("roleid", roleid);
		dao.set("userid", userid);
		dao.set("creater", "admin");
		dao.save();
	}

	public String getRoleIdByRoleName(String rolename) {
		List<UserRole> cache = dao
				.findByCache("role", "roleid", "select roleid from role where rolename = ?", rolename);
		UserRole userRole = cache.get(0);
		return userRole.getStr("roleid");
	}
	public UserRole getRolid(Long id){
		String sql="select * from user_role where userid="+id+"";
		UserRole userrole = dao.findFirst(sql);
		return userrole;
	}
}
