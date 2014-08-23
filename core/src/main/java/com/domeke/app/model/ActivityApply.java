package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * CREATE TABLE `activityapply` (
 * `activityapplyid` int(10) unsigned NOT NULL AUTO_INCREMENT,
 * `activityid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `username` varchar(255) NOT NULL,
 * `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `message` varchar(255) NOT NULL,
 * `verified` tinyint(1) NOT NULL DEFAULT '0',
 * `dateline` timestamp NOT NULL ,
 * `payment` mediumint(8) NOT NULL DEFAULT '0',
 * `ufielddata` text NOT NULL,
 * `gender` tinyint(1) NOT NULL DEFAULT '0',
 * `papers` tinyint(1) DEFAULT '0',
 * `papersid` varchar(30) DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`activityapplyid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "activity_apply", pkName = "activityapplyid")
public class ActivityApply extends Model<ActivityApply> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ActivityApply dao = new ActivityApply();

}
