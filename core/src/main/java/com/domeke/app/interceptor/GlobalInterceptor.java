package com.domeke.app.interceptor;

import java.util.List;

import com.domeke.app.model.Menu;
import com.domeke.app.model.SearchKey;
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

		Menu.menuDao.removeCache();
		List<Menu> topMenuList = Menu.menuDao.getTopMenu();
		List<SearchKey> searchKeyList = SearchKey.searchdao.getSearchKey();
		Controller controller = ai.getController();
		controller.setAttr("topMenuList", topMenuList);
		List<Menu> leftMenuList = null;
		controller.setAttr("leftMenuList", leftMenuList);
		controller.setAttr("searchKeyList", searchKeyList);
		ai.invoke();
	}

}
