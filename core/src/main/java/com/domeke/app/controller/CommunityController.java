package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.List;

import com.domeke.app.model.Community;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {

	public void index() {
		List<Community> communityFatList = Community.dao.findFatList();
		setAttr("communityFatList", communityFatList);

		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		
		setPublishNumber();
		render("/community/community.html");
	}
	
	/**
	 * 获取发帖数
	 */
	private void setPublishNumber(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 跳转指定板块
	 */
	public void goToOrderCommunity() {
		String actionKey = getPara("actionKey");
		redirect(actionKey, true);
	}

	@ClearInterceptor
	public void goToManager() {
		render("/admin/community.html");
	}
}
