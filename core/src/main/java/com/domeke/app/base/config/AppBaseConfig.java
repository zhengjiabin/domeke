package com.domeke.app.base.config;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.domeke.app.controller.LoginController;
import com.domeke.app.model.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

public class AppBaseConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {
		// 设置编码格式统一为utf-8 解决乱码问题
		constants.setEncoding("utf-8");
		constants.setDevMode(true);
		constants.setMainRenderFactory(new BeetlRenderFactory());
		GroupTemplate gt = BeetlRenderFactory.groupTemplate;
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
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		plugins.add(druidPlugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		plugins.add(arp);
		arp.addMapping("user", User.class);
	}

	@Override
	public void configRoute(Routes routes) {
		routes.add("/user", LoginController.class);
	}

}
