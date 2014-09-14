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
public class ShopInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		Menu.menuDao.removeCache();
		List<Menu> topMenuShop = Menu.menuDao.getMenuShop();
		Controller controller = ai.getController();
		controller.setAttr("topMenuShop", topMenuShop);
		List<Menu> leftMenuList = null;
		controller.setAttr("leftMenuList", leftMenuList);
		ai.invoke();
	}

}
