package com.domeke.app.controller;

import java.io.File;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Homepage;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/homepage")
@Before(LoginInterceptor.class)
public class HomepageController extends FilesLoadController{
	public void goToManager(){
		render("/admin/admin_homepageManager.html");
	}
	public void uptop(){
		Map<String, Object> map = Maps.newHashMap();
		try {
			String id = getPara("id");
			boolean bool = false;
			Homepage homepageModel = getModel(Homepage.class).findById(id);
			if(homepageModel.isNotEmpty()){
				Integer maxRank = homepageModel.getMaxRank();
				homepageModel.set("rank", maxRank + 1);
				bool = homepageModel.update();
			}
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "置顶失败！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器报错！");
			renderJson(map);
			return;
		}
	}
	public void upstatus(){
		Map<String, Object> map = Maps.newHashMap();
		try {
			String id = getPara("id");
			String status = getPara("status");
			if(StrKit.isBlank(status)){
				status = "1";
			}
			boolean bool = false;
			Homepage homepageModel = getModel(Homepage.class).findById(id);
			if(homepageModel.isNotEmpty()){
				homepageModel.set("status", status);
				bool = homepageModel.update();
			}
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "修改失败！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器报错！");
			renderJson(map);
			return;
		}
	}
	public void delete(){
		Map<String, Object> map = Maps.newHashMap();
		try {
			String id = getPara("id");
			boolean bool = false;
			Homepage homepageModel = getModel(Homepage.class).findById(id);
			if(homepageModel.isNotEmpty()){
				String imgpath = homepageModel.get("img");
				File imgFile = new File(imgpath);
				if(imgFile.exists()){
					imgFile.delete();
				}
				bool = homepageModel.delete();
			}
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "删除失败！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器错误！");
			return;
		}
	}
	public void editHomepage(){
		Map<String, Object> map = Maps.newHashMap();
		try {
			boolean bool = false;
			String imgPath = upLoadFileDealPath("img", "", 2000 * 1024 * 1024, "utf-8");
			String id = getPara("id");
			String title = getPara("title");
			String subtitle = getPara("subtitle");
			String url = getPara("url");
			String des = getPara("des");
			Homepage homepageModel = getModel(Homepage.class).findById(id);
			if(homepageModel.isNotEmpty()){
				if(!StrKit.isBlank(title)){
					homepageModel.set("title", title);
				}
				if(!StrKit.isBlank(subtitle)){
					homepageModel.set("subtitle", subtitle);
				}
				if(!StrKit.isBlank(url)){
					homepageModel.set("url", url);
				}
				if(!StrKit.isBlank(imgPath)){
					String oldimg = homepageModel.get("img");
					File file = new File(oldimg);
					if(file.exists()){
						file.delete();
					}
					homepageModel.set("img", imgPath);
				}
				homepageModel.set("des", des);
				bool = homepageModel.update();
			}
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "修改失败！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}
	}
	
	public void addHomepage(){
		Map<String, Object> map = Maps.newHashMap();
		try {
			String imgPath = upLoadFileDealPath("img", "", 2000 * 1024 * 1024, "utf-8");
			String title = getPara("title");
			String subtitle = getPara("subtitle");
			String url = getPara("url");
			String des = getPara("des");
			Homepage homepageModel = getModel(Homepage.class);
			homepageModel.set("title", title);
			homepageModel.set("subtitle", subtitle);
			homepageModel.set("img", imgPath);
			homepageModel.set("url", url);
			homepageModel.set("title", title);
			homepageModel.set("des", des);
			homepageModel.set("rank", 0);
			homepageModel.set("status", 1);
			Boolean bool = homepageModel.save();
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "添加失败！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}
	}
	public void showPage(){
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
			setAttr("status", status);
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
			String id = getPara("id");
			homepageModel = homepageModel.findById(id);
			setAttr("homepage", homepageModel);
			setAttr("status", status);
			setAttr("pageIndex", pageIndex);
			render("/admin/admin_homepageEdit.html");
			return;
		}
	}
}
