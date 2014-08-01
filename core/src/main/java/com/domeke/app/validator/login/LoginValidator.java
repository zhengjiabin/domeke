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
		String msg ="message";
		validateRequired("username", msg, "用户名必须输入！");
		validateRequired("password", msg, "密码必须输入！");
	}

	@Override
	protected void handleError(Controller c) {
 		c.keepPara();
		c.render("/login.html");
	}

}
