package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "worksType")
@Before(LoginInterceptor.class)
public class WorksTypeController extends Controller{
	
	public void goToManager(){
		render("/admin/admin_workstype.html");
	}
	public void showPage(){
		try {
			String flag = getPara("flag");
			String pageNumberStr = getPara("pageNumber");
			Integer pageNumber = 1;
			if (!StrKit.isBlank(pageNumberStr)) {
				pageNumber = Integer.parseInt(pageNumberStr);
			}
			String pageSizeStr = getPara("pageSize");
			Integer pageSize = 10;
			if (!StrKit.isBlank(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			WorksType worksTypeModel = getModel(WorksType.class);
			if ("0".equals(flag)) {
				// 0跳转 作品类型管理 按首页顺序排序
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypesNoRank("", pageNumber, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("flag", flag);
				render("/admin/admin_workstypeShow.html");
				return;
			}else if("1".equals(flag)){
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypesNoRank("1", pageNumber, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("flag", flag);
				render("/admin/admin_workstypeShow.html");
				return;
			}else if("2".equals(flag)){
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypesNoRank("2", pageNumber, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("flag", flag);
				render("/admin/admin_workstypeShow.html");
				return;
			}else if("3".equals(flag)){
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypes("", pageNumber, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("flag", flag);
				render("/admin/admin_workstypeShow.html");
				return;
			}else if("4".equals(flag)){
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypesByCartoonDesc("", pageNumber, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("flag", flag);
				render("/admin/admin_workstypeShow.html");
				return;
			}else if("5".equals(flag)){
				//跳转添加页面
				setAttr("pageNumber", pageNumber);
				setAttr("flag", flag);
				render("/admin/admin_workstypeAdd.html");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addWorksType() {
		try {
			String name = getPara("name");
			String des = getPara("des");
			String type = getPara("type");
			WorksType worksTypeModel = getModel(WorksType.class);
			worksTypeModel.set("name", name);
			worksTypeModel.set("type", type);
			worksTypeModel.set("des", des);
			worksTypeModel.set("indextop", 0);
			worksTypeModel.set("cartoontop", 0);
			boolean bool = worksTypeModel.save();
			if (bool) {
				// 成功
				renderJson("success",1);
				return;
			} else {
				renderJson("success",0);
				renderJson("message","服务器错误！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("success",0);
			renderJson("message","服务器错误！");
			return;
		}
	}

	
	public void upranking(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String state = getPara("state");
			String flag = getPara("flag");
			String id = getPara("id");
			WorksType worksTypeModel = getModel(WorksType.class).findById(id);
			if (!worksTypeModel.isNotEmpty()) {
				map.put("success", 0);
				renderJson(map);
				return;
			}
			if ("3".equals(flag)) {
				// 首页置顶
				if("0".equals(state)){
					worksTypeModel.set("indextop", 0);
				}else if("1".equals(state)){
					Integer topValue = worksTypeModel.getMaxIndexTopValue("");
					worksTypeModel.set("indextop", topValue + 1);
				}
			}
			if ("4".equals(flag)) {
				// 动漫置顶
				if("0".equals(state)){
					worksTypeModel.set("cartoontop", 0);
				}else if("1".equals(state)){
					Integer topValue = worksTypeModel.getMaxCartoonTopValue("");
					worksTypeModel.set("cartoontop", topValue + 1);
				}
			}
			worksTypeModel.update();
			map.put("success", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
		}
		renderJson(map);
		return;
	}
	
	public void delete() {
		try {
			String id = getPara("id");
			WorksType worksTypeModel = getModel(WorksType.class).findById(id);
			if (!worksTypeModel.isNotEmpty()) {
				renderJson("success",0);
				return;
			}
			Works worksModel = getModel(Works.class);
			if(worksModel.getWorksInfoByType(id).size() > 0){
				renderJson("success",0);
				renderJson("message","请删除该类型下的作品");
				return;
			}
			boolean bool = worksTypeModel.delete();
			if (bool) {
				renderJson("success","1");
				return;
			} else {
				renderJson("success","0");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("success","0");
			renderJson("message","服务器错误");
			return;
		}
	}
}
