package com.domeke.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.domeke.app.interceptor.GlobalInterceptor;
import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Menu;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author 陈智聪
 *
 */
@Before(GlobalInterceptor.class)
public class MenuController extends Controller {
	@Before(LoginInterceptor.class)
	public void renderMenu(){
		setMenuPage(null);
		render("/admin/admin_menu.html");
	}
	
	/**
	 * 新增菜单入口
	 */
	@Before(LoginInterceptor.class)
	public void addMenu() {		
		Menu menu = getModel(Menu.class);
		List<Menu> menuList = menu.selectMenu();
		setAttr("menuList", menuList);
		render("/admin/admin_addMenu.html");
	}
	
	/**
	 * 新增菜单
	 */
	@Before(LoginInterceptor.class)
	public void saveMenu() {
		Menu menu = getModel(Menu.class);
		menu.saveMenu();
		redirect("/menu/renderMenu");
	}
	
	/**
	 * 删除菜单
	 */
	@Before(LoginInterceptor.class)
	public void deleteMenu() {
		Menu menu = getModel(Menu.class);
		int menuid = getParaToInt();
		menu.deleteMenu(menuid);
		redirect("/menu/renderMenu");
	}
	
	/**
	 * 根据menuid查询菜单
	 */
	@Before(LoginInterceptor.class)
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
	@Before(LoginInterceptor.class)
	public void updateMenu() {
		Menu menu = getModel(Menu.class);
		menu.updateMenu();
		redirect("/menu/renderMenu");
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
	 * 按菜单模块查询
	 */
	@Before(LoginInterceptor.class)
	public void menuTypeForName(){
		//Menu menu = getModel(Menu.class);
		//List<Menu> menuList = new ArrayList<Menu>();
		try {
			this.getRequest().setCharacterEncoding("utf-8");
			String menuSearch = this.getRequest().getParameter("menuType");
			menuSearch =  new String(menuSearch.getBytes("ISO-8859-1"),"UTF-8");
			//menuList = menu.getMenuList("menutype", menuSearch);
			setMenuPage(menuSearch);
			//this.setAttr("menuList", menuList);
			render("/admin/admin_menu.html");			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询
	 */
	@Before(LoginInterceptor.class)
	public void setMenuPage(String menuType){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Menu> menuList = null;
		if ("".equals(menuType) || "0".equals(menuType) || menuType == null){
			menuList = Menu.menuDao.findPage(pageNumber,
					pageSize);
			menuType = "0";
		}else {
			menuList = Menu.menuDao.findPage(pageNumber,
					pageSize, menuType);
		}
		setAttr("menuType", menuType);
		setAttr("menuList", menuList);
	}
	
	/**
	 * 分页查询
	 */
	@Before(LoginInterceptor.class)
	public void find() {
		String menuType = getPara("menuType");
		setMenuPage(menuType);
		render("/admin/admin_page.html");
	}	
}
