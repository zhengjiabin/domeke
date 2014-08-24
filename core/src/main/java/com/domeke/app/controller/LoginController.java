package com.domeke.app.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.domeke.app.base.config.DomeKeConstants;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.validator.login.LoginValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


@ControllerBind(controllerKey = "login")
public class LoginController extends Controller {

	@Before(LoginValidator.class)
	public void login() {
		String username = getPara("username");
		String password = getPara("password");
		password = EncryptKit.encryptMd5(password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch (AuthenticationException e) {
			setAttr("msg", "username or password is invalid!");
			render("/login.html");
		}
		setCache(username, password, token, currentUser);
		setAttr("username", username);
		render("/index.html");
	}

	private void setCache(String username, String password, UsernamePasswordToken token, Subject currentUser) {
		setCookie("username", username, 3600 * 24 * 30);
		User user = getModel(User.class).findUserIdByUsername(username);
		user.set("username", username);
		setSessionAttr("user",user );
		if (DomeKeConstants.IS_ADMIN.equals(username)) {
			setSessionAttr("isAdmin", "true");
		}
	}

	public void logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		render("/demo/login.html");
	}

}
