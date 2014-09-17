package com.domeke.app.controller;

import com.jfinal.core.Controller;

public class AdminController extends Controller {

	/**
	 * 
	 */
	public void index() {
		redirect("/community/goToManager");
	}

	/**
	 * 
	 */
	public void goToManager() {
		String render = getPara("render");
		redirect(render);
	}
}
