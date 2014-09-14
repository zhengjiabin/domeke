package com.domeke.app.interceptor;

import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class CommunityInterceptor extends ActionInterceptor {
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		String communityId = controller.getPara("communityId");
		if (communityId == null) {
			controller.render("demo/community.html");
			return;
		}
		controller.setAttr("communityId", communityId);
		ai.invoke();
	}
}
