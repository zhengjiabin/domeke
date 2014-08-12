package com.domeke.app.validator.login;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class PasswordValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequired("password", "password-msg", "请输入密码！");
		validateRequired("repassword", "password-msg", "请输入确认密码！");
		validateEqualField("password", "repassword", "password-msg", "两次密码不一致，请重新输入！");
	}

	@Override
	protected void handleError(Controller c) {
	}

}
