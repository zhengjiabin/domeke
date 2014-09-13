package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activity")
public class ActivityController extends Controller {

	/**
	 * 查询所有活动信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 20);

		Activity activity = getModel(Activity.class);
		Page<Activity> page = activity.findPage(pageNumber, pageSize);
		setAttr("page", page);
		render("/demo/activity.html");
	}

	/**
	 * 查询发起人所有活动信息
	 * 
	 * @return 活动信息
	 */
	public void findByUserId() {
		User user = getUser();
		Object userId = user.get("userid");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Activity activity = getModel(Activity.class);
		Page<Activity> page = activity.findByUserId(userId, pageNumber,
				pageSize);
		setAttr("page", page);
		render("/demo/myActivity.html");
	}

	/**
	 * 查询指定活动信息
	 * 
	 * @return 指定活动信息
	 */
	public void findById() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 20);
		String activityId = getPara("activityId");
		Activity activity = Activity.dao.findById(activityId);
		setAttr("activity", activity);

		Object userId = activity.get("userid");
		Page<ActivityApply> activityApplyPage = ActivityApply.dao.findByUserID(
				userId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);

		Page<Comment> commentPage = Comment.dao.findPageByTargetId(activityId,
				pageNumber, pageSize);
		setAttr("commentPage", commentPage);

		List<Comment> followPage = Comment.dao.findFollowByTargetId(activityId);
		setAttr("followPage", followPage);
		render("/demo/detailActivity.html");
	}

	/**
	 * 查询修改的指定活动信息
	 * 
	 * @return 指定活动信息
	 */
	public void modifyById() {
		String activityId = getPara("activityID");
		Activity activity = Activity.dao.findById(activityId);
		setAttr("activity", activity);
		render("/demo/modifyActivity.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Activity activity = getModel(Activity.class);
		activity.update();

		findByUserId();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		String activityId = getPara("activityID");
		Post.dao.deleteById(activityId);

		findByUserId();
	}

	/**
	 * 跳转活动申请
	 */
	public void skipCreate() {
		render("/demo/createActivity.html");
	}

	/**
	 * 创建活动申请
	 */
	public void create() {
		Activity activity = getModel(Activity.class);

		User user = getUser();
		activity.set("userid", user.get("userid"));
		activity.save();

		findByUserId();
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
		return test;
	}
}
