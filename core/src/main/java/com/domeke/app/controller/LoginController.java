package com.domeke.app.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.domeke.app.base.config.DomekeConstants;
import com.domeke.app.interceptor.ActionInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.model.UserRole;
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
			
			setAttr("msg", "用户名或密码错误!");
			setAttr("username",username);
			render("/Login.html");
			return;
		}
		User user= getModel(User.class);
		user = user.findUserByUsername(username);
		//获取激活状态
		String activation = user.getStr("activation");
		if("N".equals(activation)){
			setAttr("msg", "该用户还未认证，请先到邮箱认证!");
			render("/Login.html");
			return;
		}
		UserRole userrole = getModel(UserRole.class);
		userrole = userrole.getRolid(user.getLong("userid")); 
		Long roleid = userrole.getLong("roleid");
		if(roleid == 1){
			setCache(username, password, token, currentUser);
			setAttr("username", username);
			setAttr("menuid", "1");
			
			redirect("/admin");
//			render("/admin/admin_community.html");
//			return;
		}
		setCache(username, password, token, currentUser);
		setAttr("username", username);
		setAttr("menuid", "1");
		render("/index.html");
	}

	private void setCache(String username, String password, UsernamePasswordToken token, Subject currentUser) {
		setCookie("username", username, 3600 * 24 * 30);
		User user = getModel(User.class).findUserIdByUsername(username);
		user.set("username", username);
		setSessionAttr("user",user );
		if (DomekeConstants.IS_ADMIN.equals(username)) {
			setSessionAttr("isAdmin", "true");
		}
	}

	@Before(ActionInterceptor.class)
	public void logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		render("/Login.html");
	}

}
