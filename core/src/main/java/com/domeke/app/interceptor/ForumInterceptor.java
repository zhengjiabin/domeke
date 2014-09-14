package com.domeke.app.interceptor;

import java.util.List;

import com.domeke.app.model.Menu;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * @author chenzhicong
 *
 */
public class ForumInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		Menu.menuDao.removeCache();		
		List<Menu> topMenuForum = Menu.menuDao.getMenuForum();
		Controller controller = ai.getController();
		controller.setAttr("topMenuForum", topMenuForum);
		controller.setAttr("menuid", "37");
		List<Menu> leftMenuList = null;
		controller.setAttr("leftMenuList", leftMenuList);
		ai.invoke();
	}
}
