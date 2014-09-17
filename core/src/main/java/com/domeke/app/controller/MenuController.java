package com.domeke.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
		render("/admin/admin_menu.html");
	}
	
	/**
	 * 新增菜单入口
	 */
	public void addMenu() {		
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/admin/admin_addMenu.html");
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
		render("/admin/admin_updateMenu.html");
	}
	
	/**
	 * 更新菜单
	 */
	public void updateMenu() {
		Menu menu = getModel(Menu.class);
		menu.updateMenu();
		goToManager();
	}
	
	/**
	 * 动漫
	 */
	public void cartoon() {
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);		
		render("/cartoon.html");
	}
	
	
	public void menuTypeForName(){
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = new ArrayList<Menu>();
		try {
			this.getRequest().setCharacterEncoding("utf-8");
			String menuSearch = this.getRequest().getParameter("menuType");
			menuSearch =  new String(menuSearch.getBytes("ISO-8859-1"),"UTF-8");
			menuList = menu.getMenuList("menutype", menuSearch);
			this.setAttr("menuList", menuList);
			render("/admin/admin_menu.html");			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
	}
}
