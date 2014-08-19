package com.domeke.app.model.community;

import com.jfinal.plugin.activerecord.Model;

/**
CREATE TABLE `postapprove` (
  `postid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postapproveid` int(10) unsigned NOT NULL DEFAULT '0',
  `status` tinyint(3) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`postid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/
public class Postapprove extends Model<Postapprove>{
	public static Postapprove dao = new Postapprove();
}
