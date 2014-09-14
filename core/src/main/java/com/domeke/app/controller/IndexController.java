package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Menu;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
		setAttr("menuid", "1");
		render("index.html");
	}

	public void cartoon() {
		render("cartoon.html");
	}

	// TODO 测试用 记得删除，播放的入口应为具体的controller
	public void play() {
		String menuid = getPara("menuid");
		setAttr("menuid", menuid);
		render("play.html");
	}
	
	public void shop() {
		setAttr("menuid", "42");
		render("shop.html");
	}
	
	public void forum() {
		setAttr("menuid", "37");
		render("forum.html");
	}

}
