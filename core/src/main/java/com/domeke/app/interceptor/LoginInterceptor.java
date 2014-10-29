package com.domeke.app.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		if (controller.getSessionAttr("user") == null) {
			controller.setAttr("msg", "需要登录才可以进行该操作！");
			controller.renderHtml("<script> window.location.href = '../user/goLogin';</script>");
			return;
		}

		ai.invoke();
	}
}
