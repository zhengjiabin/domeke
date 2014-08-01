package com.domeke.app.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class LoginInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		String username = controller.getCookie("username");
		String password = controller.getCookie("password");
		if(controller.getSessionAttr("user")==null && StrKit.notBlank(username,password)){
			controller.setAttr("msg", "需要登录才可以进行该操作！");
			controller.render("/login.html");
		}
		ai.invoke();
		
	}
}
