package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activity")
@Before(LoginInterceptor.class)
public class ActivityController extends Controller {

	/**
	 * 查询所有活动信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 20);

		Page<Activity> activityPage = Activity.dao.findPage(pageNumber,
				pageSize);
		setAttr("activityPage", activityPage);
		render("/community/activityPage.html");
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
		render("/community/myActivity.html");
	}

	/**
	 * 查询指定活动信息
	 * 
	 * @return 指定活动信息
	 */
	public void findById() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);
		String activityId = getPara("activityId");
		Activity activity = Activity.dao.findById(activityId);
		setAttr("activity", activity);

		Page<ActivityApply> activityApplyPage = ActivityApply.dao
				.findByActivityId(activityId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);

		 Page<Comment> commentPage =
		 Comment.dao.findPageByTargetId(activityId,"20",
		 pageNumber, pageSize);
		 setAttr("commentPage", commentPage);
		
		 List<Comment> followPage =
		 Comment.dao.findFollowByTargetId(activityId,"20");
		 setAttr("followPage", followPage);
		render("/community/detailActivity.html");
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
		render("/community/modifyActivity.html");
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
		keepPara("communityId");
		render("/community/createActivity.html");
	}

	/**
	 * 创建活动申请
	 */
	public void create() {
		Activity activity = getModel(Activity.class);

		User user = getUser();
		activity.set("userid", user.get("userid"));
		
		String communityId = getPara("communityId");
		activity.set("communityid", communityId);
		activity.save();

		findByUserId();
	}

	public void index() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 20);
		
		String communityId = getPara("communityId");
		setAttr("communityId", communityId);

		Page<Activity> activityPage = Activity.dao.findPage(pageNumber,
				pageSize,communityId);
		setAttr("activityPage", activityPage);
		render("/community/activity.html");
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		setDataById();
		setReplyCommentData();

		String renderAction = getPara("renderAction");
		render(renderAction);
	}
	
	/**
	 * 保存回复信息
	 */
	private void addComment() {
		Comment comment = getModel(Comment.class);

		Object targetId = getPara("targetId");
		comment.set("targetid", targetId);

		User user = getUser();
		comment.set("userid", user.get("userid"));

		comment.set("idtype", "20");

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set("userip", remoteIp);

		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			comment.set("pid", pId);
		}

		String toUserId = getPara("toUserID");
		if (toUserId != null && toUserId.length() > 0) {
			comment.set("touserid", toUserId);
		}

		Object message = getPara("message");
		comment.set("message", message);
		comment.save();
	}
	
	/**
	 * 添加额外的回复信息
	 */
	private void setReplyCommentData() {
		Object targetId = getPara("targetId");
		Object activityId = getPara("activityId");
		Object publishFaceAction = "./activity/replyComment?activityId=" + activityId;

		setAttr("targetId", targetId);
		setAttr("publishFaceAction", publishFaceAction);
		keepPara("pageAction", "fatherNode", "commentFatherNode");
	}
	
	/**
	 * 设置值
	 */
	private void setDataById() {
		Object activityId = getPara("activityId");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Activity activity = Activity.dao.findById(activityId);
		setAttr("activity", activity);

		Page<Comment> commentPage = Comment.dao.findPageByTargetId(activityId,"20",
				pageNumber, pageSize);
		setAttr("commentPage", commentPage);

		List<Comment> followPage = Comment.dao.findFollowByTargetId(activityId,"20");
		setAttr("followPage", followPage);

		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			Comment comment = Comment.dao.findById(pId);
			setAttr("comment", comment);
		}
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
