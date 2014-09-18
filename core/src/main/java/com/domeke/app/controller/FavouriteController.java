package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Favourite;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
/**
 * @author 陈智聪
 *
 */
@ControllerBind(controllerKey="favourite")
public class FavouriteController extends Controller {
	
	public void goFavourite(){
		render("/demo/addFavourite.html");
	}
	
	/**
	 * 新增
	 */
	public void saveFavourite(){
		Favourite favourite = getModel(Favourite.class);
		favourite.saveFavourite();
		selectFavourite();
	}
	
	/**
	 * 查询
	 */
	public void selectFavourite(){
		Favourite.favouriteDao.removeCache();
		Favourite favourite = getModel(Favourite.class);
		List<Favourite> favouriteList = favourite.selectFavourite();
		setAttr("favouriteList", favouriteList);
		render("/demo/selectFavourite.html");
	}
	
	/**
	 * 根据id查询
	 */
	public void selectFavouriteById(){
		int favouriteId = getParaToInt();
		Favourite favourite = Favourite.favouriteDao.selectFavouriteById(favouriteId);
		setAttr("favourite", favourite);
		render("/demo/updateFavourite.html");
	}
	
	/**
	 * 更新
	 */
	public void updateFavourite(){
		Favourite favourite = getModel(Favourite.class);
		favourite.updateFavourite();
		selectFavourite();
	}
	
	/**
	 * 删除
	 */
	public void deleteFavourite(){
		Favourite favourite = getModel(Favourite.class);
		int favouriteId = getParaToInt();
		favourite.deleteFavourite(favouriteId);
		selectFavourite();
	}
}
