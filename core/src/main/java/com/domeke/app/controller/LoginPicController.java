package com.domeke.app.controller;

import com.domeke.app.model.LoginPic;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey="loginpic")
public class LoginPicController extends Controller {
	
	public void goToManager(){
		setGoodsPage();
		render("/admin/admin_loginPic.html");
	}
	
	public void find() {
		setGoodsPage();
		render("/admin/admin_loginPicPage.html");
	}
	
	/**
	 * 分页查询
	 */
	public void setGoodsPage() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<LoginPic> lpList = null;
		lpList = LoginPic.lpDao.findPage(pageNumber, pageSize);
		setAttr("lpList", lpList);
	}
}
