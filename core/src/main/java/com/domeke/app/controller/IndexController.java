package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Menu;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getTopMenu();
		setAttr("menuid", "1");
		setAttr("menuOneMenu", menuOneMenu);
		render("index.html");
	}

	public void cartoon() {
		render("cartoon.html");
	}

	// TODO 测试用 记得删除，播放的入口应为具体的controller
	public void play() {
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getTopMenu();
		String menuid = getPara("menuid");
		setAttr("menuid", menuid);
		setAttr("menuOneMenu", menuOneMenu);
		render("play.html");
	}
	
	public void shop() {
		render("shop.html");
	}

}
