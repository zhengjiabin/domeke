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
		render("../index.html");
		
	}
	
	/**
	 * 获取一级菜单
	 * @return 
	 */
	public List<Menu> menuOneMenu(){
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getOneMenu();
		return menuOneMenu;
	}
	
	/**
	 * 新增菜单入口
	 */
	public void goMenu(){
		List<Menu> menuOneMenu = menuOneMenu();
		setAttr("menuid", "1");
		setAttr("menuOneMenu", menuOneMenu);
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
		
		List<Menu> menuOneMenu = menuOneMenu();
		setAttr("menuOneMenu", menuOneMenu);
		setAttr("menuid", "1");
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
		List<Menu> menuOneMenu = menu.getOneMenu();
		List<Menu> menuMenuList = menu.getTwoMenu(menuid);
		if(menuid == "" || menuid == null || "14".equals(menuid)){
			menuMenuList = menu.selectMenu();
		}
		setAttr("menuOneMenu", menuOneMenu);
		setAttr("menuMenuList", menuMenuList);
		setAttr("menuid", "1");
		render("/demo/selectMainMenu.html");
	}
	
	/**
	 * 获取子菜单
	 */
	public void getTwoMenu(){
		String menuid = getPara();
		Menu menu = getModel(Menu.class);
		List<Menu> menuTwoMenu = menu.getTwoMenu(menuid);
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
		Menu.menuDao.removeCache();
		List<Menu> menuOneMenu = menu.getOneMenu();
		setAttr("menuOneMenu", menuOneMenu);
		setAttr("menuid", "1");
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
