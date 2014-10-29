package com.domeke.app.controller;

import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "worksType")
public class WorksTypeController extends Controller{
	
	public void goToManager(){
		render("/admin/admin_workstype.html");
	}
	
	
}
