package com.domeke.app.interceptor;

import java.util.List;

import com.domeke.app.model.GoodsType;
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
		ai.invoke();
	}

}
