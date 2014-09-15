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
	
	public void goToManager(){
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/admin/menu.html");
	}
	
	/**
	 * 新增菜单入口
	 */
	public void addMenu() {		
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/admin/addMenu.html");
	}
	
	/**
	 * 新增菜单
	 */
	public void saveMenu() {
		Menu menu = getModel(Menu.class);
		menu.saveMenu();
		goToManager();
	}
	
	/**
	 * 删除菜单
	 */
	public void deleteMenu() {
		Menu menu = getModel(Menu.class);
		int menuid = getParaToInt();
		menu.deleteMenu(menuid);
		goToManager();
	}
	
	/**
	 * 根据menuid查询菜单
	 */
	public void selectMenuById() {
		int menuid = getParaToInt();
		Menu menu = Menu.menuDao.selectMenuById(menuid);
		setAttr("menu", menu);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/admin/updateMenu.html");
	}
	
	/**
	 * 更新菜单
	 */
	public void updateMenu() {
		Menu menu = getModel(Menu.class);
		menu.updateMenu();
		goToManager();
	}

	public Menu getMenuById(int menuid){
		Menu menu = Menu.menuDao.selectMenuById(menuid);
		return menu;
	}

	/**
	 * 动漫
	 */
	public void cartoon() {
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);		
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

}
