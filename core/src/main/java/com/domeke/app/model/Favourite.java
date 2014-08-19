package com.domeke.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * 功能：收藏记录表Model
 * 创建时间：2014-08-17
 * 类全名：com.domeke.app.model.Favourite.new
 * @author 陈智聪
 *
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
