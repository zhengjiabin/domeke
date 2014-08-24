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
	 * 查询所有活动申请信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("activityapplyPage_pageNumber", 1);
		int pageSize = getParaToInt("activityapplyPage_pageSize", 2);
		Page<ActivityApply> activityapplyPage = ActivityApply.dao.paginate(
				pageNumber, pageSize, "select *",
				"from activityapply order by create_time");
		setAttr("activityapplyPage", activityapplyPage);
		render("/demo/activityapply.html");
	}

	/**
	 * 跳转活动申请
	 */
	public void skipCreate() {
		keepPara();
		render("/demo/createActivityApply.html");
	}

	/**
	 * 创建活动申请
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
		
		renderHtml("<html><body onload=\"alert('提交成功!');window.opener.location.reload();window.close()\"></body></html>");
	}

	public void index() {
		find();
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
		return null;
	}
}
