package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * CREATE TABLE `attachment` (
 * `aattachmentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
 * `postid` int(10) unsigned NOT NULL DEFAULT '0',
 * `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `dateline` int(14) unsigned NOT NULL DEFAULT '0',
 * `filename` varchar(255) NOT NULL,
 * `filesize` int(10) unsigned NOT NULL DEFAULT '0',
 * `attachment` varchar(255) NOT NULL,
 * `remote` tinyint(1) unsigned NOT NULL DEFAULT '0',
 * `isimage` tinyint(1) NOT NULL DEFAULT '0',
 * `width` smallint(6) unsigned NOT NULL DEFAULT '0',
 * `downloads` mediumint(8) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`aattachmentid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "attachment", pkName = "attachmentid")
public class Attachment extends Model<Attachment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Attachment dao = new Attachment();
}
