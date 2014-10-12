package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activityApply")
public class ActivityApplyController extends Controller {

	/**
	 * 跳转活动报名申请
	 */
	public void skipCreate() {
		keepPara();
		setCodeTableList("gender", "genderList");
		setCodeTableList("papers", "papersList");
		render("/community/createActivityApply.html");
	}
	
	/**
	 * 设置码表
	 * @param key
	 * @param renderName
	 */
	private void setCodeTableList(String key,String renderName){
		List<CodeTable> list = CodeKit.getList(key);
		setAttr(renderName, list);
	}

	/**
	 * 创建活动报名申请
	 */
	public void create() {
		ActivityApply activityApply = getModel(ActivityApply.class);

		User user = getUser();
		Object userId = user.get("userid");
		activityApply.set("userid", userId);

		Object activityId = getPara("activityId");
		activityApply.set("activityid", activityId);
		activityApply.save();

		Long count = ActivityApply.dao.getCount(activityId);
		renderJson(count);
	}

	/**
	 * 异步分页查询活动申请信息
	 */
	public void findByActivityId() {
		String activityId = getPara("activityId");
		setActivityApplyPageByActivityId(activityId);

		keepPara("pageAction", "fatherNode");
		render("/community/activityApplyPage.html");
	}

	/**
	 * 根据活动ID设置活动申请信息
	 * 
	 * @param activityId
	 */
	private void setActivityApplyPageByActivityId(Object activityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Page<ActivityApply> activityApplyPage = ActivityApply.dao
				.findByActivityId(activityId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr("user");
		return user;
	}

}
