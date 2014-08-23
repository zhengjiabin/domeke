package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * CREATE TABLE `comment` (
 * `commentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
 * `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `id` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `idtype` varchar(20) NOT NULL,
 * `authorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `author` varchar(15) NOT NULL,
 * `ip` varchar(20) NOT NULL,
 * `dateline` int(14) unsigned NOT NULL DEFAULT '0',
 * `message` text NOT NULL,
 * `status` tinyint(1) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`commentid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "comment", pkName = "commentid")
public class Comment extends Model<Comment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Comment dao = new Comment();
}
