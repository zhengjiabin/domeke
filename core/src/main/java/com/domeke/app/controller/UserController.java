/**
 * 
 */
package com.domeke.app.controller;

import com.domeke.app.model.User;
import com.domeke.app.validator.RegistValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author lijiasen@domeke.com
 *
 */
public class UserController extends Controller {

	public void save() {
		User user = getModel(User.class);
		user.saveUser();
	}

	public void goRegist() {
		render("/demo/regist.html");
	}

	@Before(RegistValidator.class)
	public void regist() {
		User user = getModel(User.class);
		user.saveUser();
		render("/demo/login.html");
	}

}
