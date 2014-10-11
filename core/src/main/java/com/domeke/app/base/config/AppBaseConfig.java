package com.domeke.app.base.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.wall.WallFilter;
import com.domeke.app.beetl.DomekeBeetlRenderFactory;
import com.domeke.app.interceptor.ForumInterceptor;
import com.domeke.app.interceptor.GlobalInterceptor;
import com.domeke.app.interceptor.GoodsTypeInterceptor;
import com.domeke.app.interceptor.MyProductionInterceptor;
import com.domeke.app.interceptor.ShopInterceptor;
import com.domeke.app.route.AutoBindRoutes;
import com.domeke.app.shiro.ShiroPlugin;
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
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.spring.SpringPlugin;

public class AppBaseConfig extends JFinalConfig {

	private Routes routes;

	@Override
	public void configConstant(Constants constants) {
		// 设置编码格式统一为utf-8 解决乱码问题
		constants.setEncoding(DomekeConstants.ENCODE);
		constants.setMainRenderFactory(new DomekeBeetlRenderFactory());
		constants.setError404View("/common/404.html");
		constants.setError500View("/common/500.html");
	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(DruidDatasouceUtil.getHandlerProperty());
		handlers.add(new ContextPathHandler("core"));
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		interceptors.add(new MyProductionInterceptor());
		interceptors.add(new ForumInterceptor());
		interceptors.add(new ShopInterceptor());
		interceptors.add(new GlobalInterceptor());
		interceptors.add(new GoodsTypeInterceptor());
		interceptors.add(new SessionInViewInterceptor());
		interceptors.add(new TxByActionMethods("save", "update", "delete", "regist"));
		interceptors.add(new TxByRegex(".*add*."));
		interceptors.add(new TxByRegex(".*save*."));
		interceptors.add(new TxByRegex(".*updte*."));
		interceptors.add(new TxByRegex(".*delete*."));
	}

	@Override
	public void configPlugin(Plugins plugins) {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		plugins.add(druidPlugin);

		WallFilter wallFilter = new WallFilter();
		wallFilter.setDbType(DomekeConstants.DB_TYPE);
		druidPlugin.addFilter(wallFilter);

		AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin, SimpleNameStyles.DEFAULT);
		plugins.add(atbp);

		ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext-mail.xml");
		plugins.add(new SpringPlugin(app));
		plugins.add(new ShiroPlugin(routes));
		plugins.add(new EhCachePlugin());
	}

	@Override
	public void configRoute(Routes routes) {
		this.routes = routes;
		AutoBindRoutes abs = new AutoBindRoutes();
		routes.add(abs);
	}

}
