package com.domeke.app.base.cofig;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.druid.DruidStatViewHandler;

public class AppBaseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {

	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(DruidDatasouceUtil.getHandlerProperty());
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

	}

}
