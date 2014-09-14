package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Community;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {

	public void index() {
		String sql = "select * from community where status='10' and level=1 order by position";
		List<Community> communityFatList = Community.dao.findByCache(
				"CommunityFatList", "key", sql);
		setAttr("communityFatList", communityFatList);
		
		sql = "select * from community where status='10' and level=2 order by position";
		List<Community> communitySonList = Community.dao.findByCache(
				"CommunitySonList", "key", sql);
		setAttr("communitySonList", communitySonList);
		render("/demo/community.html");
	}
	
	/**
	 * 跳转指定板块
	 */
	public void goToOrderCommunity(){
		String actionKey = getPara("actionKey");
		redirect(actionKey, true);
	}
	
	@ClearInterceptor
	public void goToManager(){
		render("/admin/community.html");
	}
}
