package com.domeke.app.controller;

import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey="/")
public class IndexController extends Controller {
	
	public void index(){
		render("index.html");
	}
}
