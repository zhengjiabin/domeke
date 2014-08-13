/**
 * 
 */
package com.domeke.app.validator;

import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

/**
 * @author lijiasen@domeke.com
 *
 */
public class RegistValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateEmail("user.email", "emailMsg", "错误的邮箱地址");
		validateRegex("user.nickname", "[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,50}", "usernameMsg",
				"昵称的长度介于2-50之间，只能包含中文，数字，字母，下划线");
		validateRegex("user.password", "[a-zA-Z0-9_]{6,16}", "passwordMsg", "密码的长度介于6-16之间，只能包含数字，字母，下划线");
		validateEqualField("user.password", "repassword", "repasswordMsg", "2次输入的密码不一致");
		String email = c.getPara("user.email");
		if (StrKit.notBlank(email) && User.dao.containColumn("email", email)) {
			addError("emailMsg", "该email已经被注册过了：（");
		}
		String username = c.getPara("user.username");
		if (StrKit.notBlank(username) && User.dao.containColumn("username", username)) {
			addError("usernameMsg", "该用户名已经被注册过了：（");
		}

		String nickname = c.getPara("user.nickname");
		if (StrKit.notBlank(nickname) && User.dao.containColumn("nickname", nickname)) {
			addError("nicknameMsg", "该昵称已经被注册过了：（");
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(User.class);
		c.render("/demo/regist.html");
	}

}
