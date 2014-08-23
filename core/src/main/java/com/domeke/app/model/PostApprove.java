package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * CREATE TABLE `postapprove` (
 * `postapproveid` int(10) unsigned NOT NULL AUTO_INCREMENT ,
 * `postid` int(10) unsigned NOT NULL DEFAULT '0',
 * `status` tinyint(3) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`postapproveid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "post_approve", pkName = "postapproveid")
public class PostApprove extends Model<PostApprove> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static PostApprove dao = new PostApprove();
}
