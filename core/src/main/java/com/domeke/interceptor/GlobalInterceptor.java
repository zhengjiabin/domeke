package com.domeke.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

/**
 * 
 * @author lijiasen
 * 全局拦截区，用于权限、session等控制
 */
public class GlobalInterceptor implements Interceptor {
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		String username = controller.getCookie("username");
		String password = controller.getCookie("password");
		if(controller.getSessionAttr("user")==null && StrKit.notBlank(username,password)){
			
		}
		ai.invoke();
	}

}
