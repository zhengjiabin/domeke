package com.domeke.app.controller;

import com.domeke.app.model.Homepage;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/homepage")
public class HomepageController extends Controller{
	public void goToManager(){
		render("/admin/admin_homepageManager.html");
	}
	
	public void showPage(){
		User user = getSessionAttr("user");
		String flag = getPara("flag");
		String status = getPara("status");
		
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if (!StrKit.isBlank(pageIndexStr)) {
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if (!StrKit.isBlank(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		Homepage homepageModel = getModel(Homepage.class);
		if("0".equals(flag)){
			//显示所有
			Page<Homepage> pageHomepage = homepageModel.findHomepagesByStatusRank(status, pageIndex, pageSize);
			setAttr("pageHomepage", pageHomepage);
			render("/admin/admin_homepageShow.html");
			return;
		}
		if("1".equals(flag)){
			//添加页面
			setAttr("pageIndex", pageIndex);
			setAttr("status", status);
			render("/admin/admin_homepageAdd.html");
			return;
		}
		if("2".equals(flag)){
			//修改页面
		}
	}
}
