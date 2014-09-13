package com.domeke.app.controller;

import com.jfinal.core.Controller;

public class PersonalController extends Controller{
	public void forMyProduction(){
		String mid = getPara("menuId");
		setAttr("menuId", mid);
		render("/personalCenter.html");
	}
}
