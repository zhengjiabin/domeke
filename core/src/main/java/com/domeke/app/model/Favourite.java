package com.domeke.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author 陈智聪
 *DROP TABLE IF EXISTS `favourite`;
CREATE TABLE `favourite` (
  `favouriteid` bigint(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `cartoon_name` varchar(50) DEFAULT NULL COMMENT '卡通名称',
  `cartoon_id` varchar(50) DEFAULT NULL,
  `section_name` varchar(50) DEFAULT NULL COMMENT '章节名称',
  `section_id` varchar(50) DEFAULT NULL COMMENT '章节id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`favouriteid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
 */
public class Favourite extends Model<Favourite> {
	
	public static Favourite dao = new Favourite();
	
	/**
	 * 新增
	 */
	public void saveFavourite(){
		this.save();
	}
	
	/**
	 * 查询
	 * @return
	 */
	public List<Favourite> getFavouriteList(){
		List<Favourite> favouritelist = dao.findByCache("favouritelist", "key", "select * from favourite");					
		return favouritelist;	
	}
	
	/**
	 * 删除
	 * @param favouriteid
	 */
	public void deleteFavourite(int favouriteid){
		dao.deleteById(favouriteid);
	}
	
	/**
	 * 更新
	 */
	public void updateFavourite(){
		dao.update();
	}
}
