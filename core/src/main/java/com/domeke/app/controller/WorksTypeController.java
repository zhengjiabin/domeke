package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
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
			User user = getSessionAttr("user");
			String flag = getPara("flag");
			String type = getPara("type");
			String workstype = getPara("workstype");
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
			if (StrKit.isBlank(flag)) {
				flag = type;
			}
			WorksType worksTypeModel = getModel(WorksType.class);
			if ("0".equals(flag)) {
				// 0跳转 作品类型管理 按首页顺序排序
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypes(Integer.parseInt(type), pageIndex, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("type", type);
				render("/admin_workstypeShow.html");
				return;
			}else if("1".equals(flag)){
				Page<WorksType> pageWorksType = worksTypeModel.getWorksTypesByCartoonDesc(Integer.parseInt(type), pageIndex, pageSize);
				setAttr("pageWorksType", pageWorksType);
				setAttr("type", type);
				render("/admin_workstypeShow.html");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
