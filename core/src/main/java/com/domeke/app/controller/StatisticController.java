package com.domeke.app.controller;

import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey="count")
public class StatisticController extends Controller{
	public void goUserCount(){
		User user = getModel(User.class);
		Long countUser = user.countUser(null, null);
		Long isActivation = user.countUser("activation", "Y");
		Long noActivation = user.countUser("activation", "N");
		setAttr("countUser", countUser);
		setAttr("isActivation", isActivation);
		setAttr("noActivation", noActivation);
		render("/admin/admin_userCount.html");
	}
	
}
