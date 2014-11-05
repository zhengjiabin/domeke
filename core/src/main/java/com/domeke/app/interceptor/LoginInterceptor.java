package com.domeke.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		try {
			Controller controller = ai.getController();
			HttpServletRequest request = controller.getRequest();
			String contentPath = request.getLocalAddr() +":"+ request.getLocalPort() + request.getContextPath();
			if (controller.getSessionAttr("user") == null) {
				controller.setAttr("msg", "需要登录才可以进行该操作！");
				controller.renderHtml("<script> window.location.href = 'http://"+contentPath+"/user/goLogin';</script>");
				return;
			}
			ai.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
