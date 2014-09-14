package com.domeke.app.controller;

import javax.servlet.http.HttpSession;

import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activityApply")
public class ActivityApplyController extends Controller {

	/**
	 * 跳转活动报名申请
	 */
	public void skipCreate() {
		keepPara();
		render("/demo/createActivityApply.html");
	}

	/**
	 * 创建活动报名申请
	 */
	public void create() {
		ActivityApply activityApply = getModel(ActivityApply.class);

		User user = getUser();
		Object userId = user.get("userid");
		activityApply.set("userid", userId);

		Object activtiyId = getPara("activityId");
		activityApply.set("activityid", activtiyId);
		activityApply.save();

		Activity activity = Activity.dao.findById(activtiyId);
		int applyNumber = activity.getInt("applynumber");
		activity.set("applynumber", ++applyNumber);
		activity.update();

		renderHtml("<html><body onload=\"alert('提交成功!');window.opener.location.reload();window.close();\"></body></html>");
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
		HttpSession session = getSession();
		Object user = session.getAttribute("user");
		if (user instanceof User) {
			return (User) user;
		}
		User test = new User();
		test.set("userid", 1);
		test.set("username", "zheng");
		session.setAttribute("user", test);
		return test;
	}

}
