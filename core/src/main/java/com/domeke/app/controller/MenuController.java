package com.domeke.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.Menu;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
public class MenuController extends Controller {
	public void goTest(){
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.selectMenu();
		setAttr("menuOneMenu", menuOneMenu);
		render("/demo/test.html");
		
	}
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
	 * 动态获取菜单
	 */
	public void goMMenu(){
		String menuid = "";
		menuid = getPara();		
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getOneMenu("1");
		List<Menu> menuMenuList = menu.getTwoMenu("0", menuid);
		if(menuid == "" || menuid == null || "14".equals(menuid)){
			menuMenuList = menu.selectMenu();
		}
		setAttr("menuOneMenu", menuOneMenu);
		setAttr("menuMenuList", menuMenuList);
		render("/demo/selectMainMenu.html");
	}
	
	/**
	 * 获取子菜单
	 */
	public void getTwoMenu(){
		String menuid = getPara();
		Menu menu = getModel(Menu.class);
		List<Menu> menuTwoMenu = menu.getTwoMenu("0",menuid);
		setAttr("menuTwoMenu", menuTwoMenu);
		selectMenu();
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
