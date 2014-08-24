/**
 * 
 */
package com.domeke.app.controller;

import com.domeke.app.interceptor.MailAuthInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.validator.RegistValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author lijiasen@domeke.com
 *
 */
@ControllerBind(controllerKey = "user")
public class UserController extends Controller {

	public void save() {
		User user = getModel(User.class);
		user.saveUser();
	}

	public void index() {
		render("/demo/regist.html");
	}

	@Before({RegistValidator.class,MailAuthInterceptor.class})
	public void regist() {
		User user = getModel(User.class);
		user.saveUser();
		render("/demo/login.html");
	}


}
