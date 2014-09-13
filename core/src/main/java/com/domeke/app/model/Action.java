package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(pkName="actionid",tableName="action")
public class Action extends Model<Action> {

	/**
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `actionid` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '动作名称',
  `des` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `limits` tinyint(5) NOT NULL DEFAULT '0' COMMENT '周期范围 单位天',
  `maxnum` int(10) NOT NULL DEFAULT '0',
  `peas` tinyint(5) NOT NULL DEFAULT '0' COMMENT '豆豆',
  `point` tinyint(5) NOT NULL DEFAULT '0',
  `creater` mediumint(8) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` mediumint(8) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`actionid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
	 */
	private static final long serialVersionUID = 1L;
	public static Action dao = new Action();
	
	public Action getActionByName(String name){
		String sql = "select * from action where name = ?";
		return dao.findFirst(sql,name);
	}
	public Action getActionById(Integer actionid){
		return dao.findById(actionid);
	}
}
