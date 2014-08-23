package com.domeke.app.base.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.wall.WallFilter;
import com.domeke.app.route.AutoBindRoutes;
import com.domeke.app.tablebind.AutoTableBindPlugin;
import com.domeke.app.tablebind.SimpleNameStyles;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.tx.TxByActionMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;

public class AppBaseConfig extends JFinalConfig {

	private Routes routes;

	@Override
	public void configConstant(Constants constants) {
		// 设置编码格式统一为utf-8 解决乱码问题
		constants.setEncoding(DomeKeConstants.ENCODE);
		constants.setDevMode(false);
		constants.setMainRenderFactory(new BeetlRenderFactory());
		constants.setError404View("");
		constants.setError500View("");
	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(DruidDatasouceUtil.getHandlerProperty());
		handlers.add(new ContextPathHandler("core"));
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		interceptors.add(new SessionInViewInterceptor());
		interceptors.add(new TxByActionMethods("save", "update", "delete", "regist"));
	}

	@Override
	public void configPlugin(Plugins plugins) {
		plugins.add(new EhCachePlugin());
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		plugins.add(druidPlugin);

		WallFilter wallFilter = new WallFilter();
		wallFilter.setDbType(DomeKeConstants.DB_TYPE);
		druidPlugin.addFilter(wallFilter);

		AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin, SimpleNameStyles.DEFAULT);
		plugins.add(atbp);
	}

	@Override
	public void configRoute(Routes routes) {
		this.routes = routes;
		AutoBindRoutes abs = new AutoBindRoutes();
		routes.add(abs);
	}

}
