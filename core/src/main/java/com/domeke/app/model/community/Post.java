package com.domeke.app.model.community;

import com.jfinal.plugin.activerecord.Model;

/**
 * CREATE TABLE `post` (
 * `postid` int(10) unsigned NOT NULL AUTO_INCREMENT,
 * `author` varchar(15) NOT NULL,
 * `authorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `subject` varchar(80) NOT NULL,
 * `dateline` timestamp NOT NULL ,
 * `message` text NOT NULL,
 * `useip` varchar(15) NOT NULL,
 * `invisible` tinyint(1) NOT NULL DEFAULT '0',
 * `anonymous` tinyint(1) NOT NULL DEFAULT '0',
 * `usesig` tinyint(1) NOT NULL DEFAULT '0',
 * `attachment` tinyint(1) NOT NULL DEFAULT '0',
 * `rate` smallint(6) NOT NULL DEFAULT '0',
 * `ratetimes` tinyint(3) unsigned NOT NULL DEFAULT '0',
 * `status` int(10) NOT NULL DEFAULT '0',
 * `comment` tinyint(1) NOT NULL DEFAULT '0',
 * `replycredit` int(10) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`postid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
public class Post extends Model<Post> {
	public static Post dao = new Post();
}
