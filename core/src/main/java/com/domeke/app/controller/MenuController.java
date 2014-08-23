package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Menu;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
public class MenuController extends Controller {
	
	/**
	 * 新增菜单入口
	 */
	public void goMenu(){
		render("/demo/addMenu.html");
	}
	
	/**
	 * 新增菜单
	 */
	public void saveMenu(){
		Menu menu = getModel(Menu.class);
		menu.saveMenu();
		selectMenu();
	}
	
	/**
	 * 查询菜单列表
	 */
	public void selectMenu(){
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/demo/selectMenu.html");
	}
	
	/**
	 * 根据menuid查询菜单
	 */
	public void selectMenuById(){
		int menuid = getParaToInt();
		Menu menu = Menu.menuDao.selectMenuById(menuid);
		setAttr("menu", menu);
		render("/demo/updateMenu.html");
	}
	
	/**
	 * 更新菜单
	 */
	public void updateMenu(){
		Menu menu = getModel(Menu.class);
		menu.updateMenu();
		selectMenu();
	}
	
	/**
	 * 删除菜单
	 */
	public void deleteMenu(){
		Menu menu = getModel(Menu.class);
		int menuid = getParaToInt();
		menu.deleteMenu(menuid);
		selectMenu();
	}
}
