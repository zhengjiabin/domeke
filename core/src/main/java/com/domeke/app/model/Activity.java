package com.domeke.app.model;

import com.domeke.app.utils.EncryptUtils;
import com.domeke.app.utils.HtmlTagKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * CREATE TABLE `activity` (
 * `activityid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
 * `uid` mediumint(8) NOT NULL DEFAULT '0' ,
 * `aid` mediumint(8) NOT NULL DEFAULT '0' ,
 * `cost` mediumint(8) NOT NULL DEFAULT '0',
 * `starttimefrom` timestamp NOT NULL ,
 * `starttimeto` timestamp NOT NULL ,
 * `place` varchar(255) NOT NULL,
 * `class` varchar(255) NOT NULL,
 * `gender` tinyint(1) NOT NULL DEFAULT '0',
 * `number` smallint(5) unsigned NOT NULL DEFAULT '0',
 * `applynumber` smallint(5) unsigned NOT NULL DEFAULT '0',
 * `expiration` timestamp NOT NULL ,
 * `ufield` text NOT NULL,
 * `credit` smallint(6) unsigned NOT NULL DEFAULT '0',
 * `status` tinyint(1) DEFAULT NULL,
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`activityid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
public class Activity extends Model<Activity> {
	public static Activity dao = new Activity();
	
}
