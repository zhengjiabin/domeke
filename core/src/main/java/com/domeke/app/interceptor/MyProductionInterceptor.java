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
public class MyProductionInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {
		Menu.menuDao.removeCache();		
		List<Menu> topMenuMP = Menu.menuDao.getMenuMP();
		Controller controller = ai.getController();
		controller.setAttr("topMenuMP", topMenuMP);
		List<Menu> leftMenuList = null;
		controller.setAttr("leftMenuList", leftMenuList);
		ai.invoke();
	}
}
