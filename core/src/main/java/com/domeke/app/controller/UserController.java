/**
 * 
 */
package com.domeke.app.controller;

import com.domeke.app.model.User;
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

}
