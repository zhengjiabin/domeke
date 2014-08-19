package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Favourite;
import com.jfinal.core.Controller;
/**
 * 功能：收藏记录表controller
 * 创建时间：2014-08-17
 * 类全名：com.domeke.app.controller.FavouriteController.new
 * @author 陈智聪
 *
 */
public class FavouriteController extends Controller {
	
	/**
	 * 新增
	 */
	public void save() {
		Favourite favourite = getModel(Favourite.class);
		favourite.saveFavourite();
		render("/demo/favourite.html");
	} 

	public void goFavourite() {
		render("/demo/favourite.html");
	}
	
	/**
	 * 删除
	 */
	public void getDeleteFavourite(){
		Favourite favourite = getModel(Favourite.class);
		//int favouriteid = favourite.getInt("favouriteid");
		int favouriteid = getParaToInt();
		favourite.deleteFavourite(favouriteid);
		render("/demo/selectFavourite.html");
	}
	
	/**
	 * 查询
	 */
	public void goFavouriteList() {
		Favourite favourite = getModel(Favourite.class);
		List<Favourite> favouriteList = favourite.getFavouriteList();
		this.setAttr("favouritelist", favouriteList);
		render("/demo/selectFavourite.html");
	}
	
	/**
	 * 更新
	 */
	public void goUpFavourite(){
		Favourite favourite = getModel(Favourite.class);
		int favouriteid = favourite.getInt("FavouriteId");
		favourite.dao.findById(favouriteid).setAttrs(favourite).update();
		goFavouriteList();
	}
	public void selectById(){
		int favouriteid = getParaToInt();
		Favourite favourite = Favourite.dao.findById(favouriteid);
		setAttr("favourite", favourite);
		render("/demo/updatefavourite.html");
	}
	public void goUpdate(){
		render("/demo/upfavourite.html");
	}

}
