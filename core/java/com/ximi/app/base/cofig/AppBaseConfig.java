package com.ximi.app.base.cofig;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;

public class AppBaseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants arg0) {

	}

	@Override
	public void configHandler(Handlers handlers) {
		DruidStatViewHandler dsvh = new DruidStatViewHandler("/druid");
	}

	@Override
	public void configInterceptor(Interceptors arg0) {

	}

	@Override
	public void configPlugin(Plugins plugins) {
		String password = null;
		String username = null;
		String url = null;
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
		plugins.add(druidPlugin);
	}

	@Override
	public void configRoute(Routes arg0) {

	}

}
