package com.domeke.app.interceptor;

import java.util.List;

import com.domeke.app.model.GoodsType;
import com.domeke.app.model.SearchKey;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * @author chenzhicong
 *
 */
public class GoodsTypeInterceptor implements Interceptor  {

	@Override
	public void intercept(ActionInvocation ai) {
		List<GoodsType> topGoodsTypeList = GoodsType.gtDao.getTopGoodsType();
		Controller controller = ai.getController();
		controller.setAttr("topGoodsTypeList", topGoodsTypeList);
		List<SearchKey> searchKeyList = SearchKey.searchdao.getSearchKey();
		controller.setAttr("searchKeyList", searchKeyList);
		ai.invoke();
	}

}
