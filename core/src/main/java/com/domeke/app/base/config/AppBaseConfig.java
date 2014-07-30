package com.domeke.app.base.config;

import com.domeke.app.controller.LoginController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;

public class AppBaseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {
		//设置编码格式统一为utf-8  解决乱码问题
		constants.setEncoding("utf-8");
	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(DruidDatasouceUtil.getHandlerProperty());
		handlers.add(new ContextPathHandler("app-core"));
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {

	}

	@Override
	public void configPlugin(Plugins plugins) {
		plugins.add(DruidDatasouceUtil.getDruidPlugin());
	}

	@Override
	public void configRoute(Routes routes) {
		routes.add("/user", LoginController.class);
	}

}
