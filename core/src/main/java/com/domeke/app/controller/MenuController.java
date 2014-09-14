package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.GlobalInterceptor;
import com.domeke.app.model.Menu;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
@Before(GlobalInterceptor.class)
public class MenuController extends Controller {

	public void redirect() {
		String actionkey = getPara("actionkey");
		int menuid = getParaToInt("menuid");
		List<Menu> menuOneMenu = menuOneMenu();
		redirect(actionkey + "?menuid=" + menuid, true);
	}

	public void cartoon() {
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);		
		Menu menu = getModel(Menu.class);
//		List<Menu> menuListById = menu.getMenuByMenuId(menuid);
//		setAttr("menuListById", menuListById);
		render("/cartoon.html");
	}

	/**
	 * 查询菜单列表
	 */
	public void selectMenu() {
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		List<Menu> menuOneMenu = menuOneMenu();
		setAttr("menuOneMenu", menuOneMenu);
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);
		render("/demo/selectMenu.html");
	}

	/**
	 * 获取一级菜单
	 * @return 
	 */
	public List<Menu> menuOneMenu() {
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getTopMenu();
		return menuOneMenu;
	}

	/**
	 * 新增菜单入口
	 */
	public void addMenu() {
		List<Menu> menuOneMenu = menuOneMenu();
		setAttr("menuid", "7");
		setAttr("menuOneMenu", menuOneMenu);
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/demo/addMenu.html");
	}

	/**
	 * 新增菜单
	 */
	public void saveMenu() {
		Menu menu = getModel(Menu.class);
		menu.saveMenu();
		selectMenu();
	}

	/**
	 * 动态获取菜单
	 */
	/*
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
		render("/demo/selectMainMenu.html");
	}
	*/
	/**
	 * 获取子菜单
	 */
	public void getTwoMenu() {
		String menuid = getPara();
		Menu menu = getModel(Menu.class);
		List<Menu> menuTwoMenu = menu.getTwoMenu(menuid);
		setAttr("menuTwoMenu", menuTwoMenu);
		selectMenu();
	}

	/**
	 * 根据menuid查询菜单
	 */
	public void selectMenuById() {
		int menuid = getParaToInt();
		Menu menu = Menu.menuDao.selectMenuById(menuid);
		setAttr("menu", menu);
		Menu.menuDao.removeCache();
		List<Menu> menuOneMenu = menu.getTopMenu();
		setAttr("menuOneMenu", menuOneMenu);
		setAttr("menuid", "7");
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/demo/updateMenu.html");
	}

	/**
	 * 更新菜单
	 */
	public void updateMenu() {
		Menu menu = getModel(Menu.class);
		menu.updateMenu();
		selectMenu();
	}

	/**
	 * 删除菜单
	 */
	public void deleteMenu() {
		Menu menu = getModel(Menu.class);
		int menuid = getParaToInt();
		menu.deleteMenu(menuid);
		// selectMenu();
		redirect("/menu/selectMenu?menuid=" + menuid, true);
	}
}
