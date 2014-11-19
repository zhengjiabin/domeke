package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

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
@TableBind(tableName="favourite", pkName="favouriteid")
public class Favourite extends Model<Favourite> {
	
	public static Favourite favouriteDao = new Favourite();
	
	/**
	 * 新增
	 */
	public void saveFavourite(){
		this.save();
	}
	
	/**
	 * 查询
	 * @return 查询列表
	 */
	public List<Favourite> selectFavourite(){
		List<Favourite> favouriteList = this.find("select * from favourite");
		return favouriteList;
	}
	
	/**
	 * 根据
	 * @param 
	 * @return 
	 */
	public Favourite selectFavouriteById(int favouriteid){
		Favourite favourite = this.findById(favouriteid);
		return favourite;
	}
	
	/**
	 * 更新
	 */
	public void updateFavourite(){
		this.update();
	}
	
	/**
	 * 删除
	 * @param 
	 */
	public void deleteFavourite(int favouriteid){
		this.deleteById(favouriteid);
	}
	
	/**
	 * 删除缓存
	 */
	public void removeCache(){
		CacheKit.removeAll("Favourite");
		CacheKit.removeAll("favouriteList");
	}
	
	public Page<Favourite> getFavouriteByUid(Long uid,int pageNumber,int pageSize){
		Page<Favourite> page = this.paginate(pageNumber, pageSize, "select * ", "from favourite where userid=?",uid);
		return page;
	}
}
