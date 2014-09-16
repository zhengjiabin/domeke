package com.domeke.app.controller;

import com.jfinal.core.Controller;

public class AdminController extends Controller {

	/**
	 * 
	 */
	public void index() {
		setAttr("layoutContent", "");
		render("/admin/admin_layout.html");
	}

	/**
	 * 
	 */
	public void goToManager() {
		String render = getPara("render");
		redirect(render);
	}
}
