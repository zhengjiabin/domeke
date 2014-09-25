package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activity")
@Before(LoginInterceptor.class)
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
		String activityId = getPara("targetId");
		Activity.dao.updateTimes(activityId);
		
		setActivity(activityId);
		setActivityApplyPage(activityId);
		setCommentPage(activityId);
		setFollowList(activityId);
		
		keepPara("communityId");
		render("/community/detailActivity.html");
	}
	
	/**
	 * 置顶功能
	 */
	public void setTop(){
		String activityId = getPara("targetId");
		if(activityId == null || activityId.length()<=0){
			renderJson("false");
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set("activityid", activityId);
		activity.set("top", 1);
		activity.update();
		renderJson("true");
	}
	
	/**
	 * 精华功能
	 */
	public void setEssence(){
		String activityId = getPara("targetId");
		if(activityId == null || activityId.length()<=0){
			renderJson("false");
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set("activityid", activityId);
		activity.set("essence", 1);
		activity.update();
		renderJson("true");
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
		int pageSize = getParaToInt("pageSize", 10);
		Page<ActivityApply> activityApplyPage = ActivityApply.dao
				.findByActivityId(activityId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);
	}
	
	/**
	 * 分页设置父回复信息
	 */
	public void setCommentPage(Object activityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
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
		setCodeTableList("gender", "genderList");
		render("/community/createActivity.html");
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
		setPublishNumber();
		
		render("/community/activity.html");
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(){
		//今日发帖数
		Long activityTodayCount = Activity.dao.getTodayCount();
		setAttr("todayCount", activityTodayCount);
		//昨日发帖数
		Long activityYesCount = Activity.dao.getYesterdayCount();
		setAttr("yesCount", activityYesCount);
		//总发帖数
		Long activityCount = Activity.dao.getCount();
		setAttr("count", activityCount);
	}
	
	public void home(){
		setActivityPage(null);
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		render("/community/activityAll.html");
	}
	
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId
	 */
	public void setActivityPage(Object communityId){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		
		Page<Activity> activityPage = null;
		if(communityId == null){
			activityPage = Activity.dao.findPage(pageNumber, pageSize);
		}else{
			activityPage = Activity.dao.findPage(pageNumber,pageSize,communityId);
		}
		setAttr("activityPage", activityPage);
		setAttr("communityId", communityId);
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		String activityId = getPara("activityId");
		setActivity(activityId);
		setCommentPage(activityId);
		setFollowList(activityId);
		
		setReplyCommentData();

		render("/comment/comment.html");
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyFollow() {
		addComment();

		String activityId = getPara("activityId");
		setActivity(activityId);
		setFollowList(activityId);
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			setComment(pId);
		}
		setReplyCommentData();

		render("/comment/followPage.html");
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
		setAttr("targetId", targetId);
		keepPara("pageAction", "fatherNode","commentAction","followAction");
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
		User user = getSessionAttr("user");
		return user;
	}
}
