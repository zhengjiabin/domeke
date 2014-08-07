package com.domeke.app.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * 
 * @author lijiasen
 * 全局拦截区
 */
public class GlobalInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();

		ai.invoke();
	}

}
