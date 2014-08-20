package com.domeke.app.model;

import com.jfinal.plugin.activerecord.Model;

/**
CREATE TABLE `treasureapprove` (
  `treasureapproveid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `treasureid` int(10) unsigned NOT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`treasureapproveid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/
public class TreasureApprove extends Model<TreasureApprove>{
	public static TreasureApprove dao = new TreasureApprove();
}
