package com.domeke.app.base.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.wall.WallFilter;
import com.domeke.app.controller.ActivityController;
import com.domeke.app.controller.ActivityapplyController;
import com.domeke.app.controller.HomeController;
import com.domeke.app.controller.LoginController;
import com.domeke.app.controller.PostController;
import com.domeke.app.controller.UserController;
import com.domeke.app.model.Activity;
import com.domeke.app.model.Activityapply;
import com.domeke.app.model.Post;
import com.domeke.app.model.Role;
import com.domeke.app.model.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
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
		interceptors.add(new TxByActionMethods("save", "update", "delete",
				"regist"));
	}

	@Override
	public void configPlugin(Plugins plugins) {
		plugins.add(new EhCachePlugin());
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		plugins.add(druidPlugin);

		WallFilter wallFilter = new WallFilter();
		wallFilter.setDbType(DomeKeConstants.DB_TYPE);
		druidPlugin.addFilter(wallFilter);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		plugins.add(arp);
		arp.addMapping("user", "userid", User.class);
		arp.addMapping("role", "roleid", Role.class);
		arp.addMapping("activity", "activityid", Activity.class);
		arp.addMapping("activityapply", "activityapplyid", Activityapply.class);
		arp.addMapping("post", "postid", Post.class);
	}

	@Override
	public void configRoute(Routes routes) {
		this.routes = routes;
		routes.add("/login", LoginController.class);
		routes.add("/user", UserController.class);
		routes.add("/home", HomeController.class);
		routes.add("/activity", ActivityController.class);
		routes.add("/activityapply", ActivityapplyController.class);
		routes.add("/post", PostController.class);
	}

}
