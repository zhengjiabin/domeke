package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(pkName="useractionid",tableName="user_action")
public class UserAction extends Model<UserAction>{

	/**
DROP TABLE IF EXISTS `user_action`;
CREATE TABLE `user_action` (
  `useractionid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `username` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actionid` bigint(20) NOT NULL,
  `actionname` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actiondes` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `times` tinyint(3) NOT NULL DEFAULT '0',
  `create` mediumint(8) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` mediumint(8) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`useractionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	 */
	private static final long serialVersionUID = 1L;
	
	public static UserAction dao = new UserAction();
	
	public void saveUserAction(){
		this.save();
	}
	public UserAction getUserAction(Integer userActionId){
		return dao.getUserAction(userActionId);
	}

	public UserAction getUserAction(Object userId,Object actionId){
		String sql = "select * from user_action where userid = ? and actionid = ?";
		return dao.findFirst(sql,userId,actionId);
	}
}
