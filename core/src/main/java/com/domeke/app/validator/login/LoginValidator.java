/**
 * 
 */
package com.domeke.app.validator.login;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * @author Administrator
 *
 */
public class LoginValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequired("username", "username-msg", "用户名密码必须输入！");
		validateRequired("password", "password-msg", "用户名密码必须输入！");
	}

	@Override
	protected void handleError(Controller c) {
		c.keepPara();
		c.render("/Login.html");
	}

}
