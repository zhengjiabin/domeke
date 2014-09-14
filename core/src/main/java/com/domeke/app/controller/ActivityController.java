package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
//@Before(LoginInterceptor.class)
public class ActivityController extends Controller {

	/**
	 * 分页查询指定社区的活动
	 * 
	 * @return 活动信息
	 */
	public void find() {
		Object communityId = getPara("communityId");
		setActivityPage(communityId);
		
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
		String activityId = getPara("activityId");
		
		setActivity(activityId);
		setActivityApplyPage(activityId);
		setCommentPage(activityId);
		setFollowList(activityId);
		
		keepPara("communityId");
		render("/community/detailActivity.html");
	}
	
	/**
	 * 设置活动信息
	 * @param activityId
	 */
	private void setActivity(Object activityId){
		Activity activity = Activity.dao.findById(activityId);
		setAttr("activity", activity);
	}
	
	/**
	 * 分页设置活动申请信息
	 * 
	 * @param activityId
	 */
	private void setActivityApplyPage(Object activityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);
		Page<ActivityApply> activityApplyPage = ActivityApply.dao
				.findByActivityId(activityId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);
	}
	
	/**
	 * 分页设置父回复信息
	 */
	public void setCommentPage(Object activityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);
		Page<Comment> commentPage = Comment.dao.findPageByTargetId(activityId,
				"20", pageNumber, pageSize);
		setAttr("commentPage", commentPage);
	}

	/**
	 * 设置子回复信息
	 */
	public void setFollowList(Object activityId) {
		List<Comment> followPage = Comment.dao.findFollowByTargetId(activityId,
				"20");
		setAttr("followPage", followPage);
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
		
		activity.save();

		Object communityId = activity.get("communityid");
		setActivityPage(communityId);
		
		render("/community/activity.html");
	}

	public void index() {
		String communityId = getPara("communityId");
		setActivityPage(communityId);
		
		render("/community/activity.html");
	}
	
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId
	 */
	public void setActivityPage(Object communityId){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Page<Activity> activityPage = Activity.dao.findPage(pageNumber,
				pageSize,communityId);
		setAttr("activityPage", activityPage);
		setAttr("communityId", communityId);
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		Object activityId = getPara("activityId");
		setActivity(activityId);
		setCommentPage(activityId);
		setFollowList(activityId);

		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			setComment(pId);
		}
		setReplyCommentData();

		String renderAction = getPara("renderAction");
		render(renderAction);
	}
	
	/**
	 * 异步分页查询回复信息
	 */
	public void findCommentByActivityId(){
		Object activityId = getPara("activityId");
		setCommentPage(activityId);
		setFollowList(activityId);
		setReplyCommentData();
		render("/comment/comment.html");
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

		//默认回复楼层
		comment.set("level", "1");
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			comment.set("pid", pId);
			comment.set("level", "2");
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
		Object publishFaceAction = "./activity/replyComment?activityId="
				+ activityId;

		setAttr("targetId", targetId);
		setAttr("publishFaceAction", publishFaceAction);
		keepPara("pageAction", "fatherNode");
	}
	
	/**
	 * 设置回复信息
	 * 
	 * @param commentId
	 */
	private void setComment(Object commentId) {
		Comment comment = Comment.dao.findById(commentId);
		setAttr("comment", comment);
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
