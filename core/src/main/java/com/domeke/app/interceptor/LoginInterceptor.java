package com.domeke.app.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class LoginInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		try {
			Controller controller = ai.getController();
			HttpServletRequest request = controller.getRequest();
			if (controller.getSessionAttr("user") == null) {
				controller.setAttr("msg", "需要登录才可以进行该操作！");
				String basePath = getbasePath(request);
				controller.renderHtml("<script> window.location.href = '" + basePath + "/user/goLogin';</script>");
				return;
			}
			ai.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取根路径
	 * @param request
	 * @return
	 */
	private String getbasePath(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String basePath = StrKit.replace(url.toString(), uri, "");
		basePath = basePath + request.getContextPath();
		return basePath;
	}
}
