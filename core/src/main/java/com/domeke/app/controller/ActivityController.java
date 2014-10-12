package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activity")
public class ActivityController extends Controller {
	
	/** 回复类型 */
	private static String IDTYPE = "20";
	
	/**
	 * admin管理--社区管理入口
	 */
	public void goToManager() {
		findPageAll();
		render("/admin/admin_activity.html");
	}
	
	/**
	 * admin管理--分页查询活动(不区分显示隐藏状态)
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Activity> activityPage = Activity.dao.findPageAll(pageNumber, pageSize);
		setAttr("activityPage", activityPage);
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 */
	public void skipUpdate() {
		Activity activity = null;
		String activityId = getPara("activityId");
		if(StrKit.notBlank(activityId)){
			activity = Activity.dao.findById(activityId);
		}else{
			activity = getModel(Activity.class);
		}
		setAttr("activity", activity);
		
		setCodeTableList("gender", "genderList");
		
		List<Community> communityList = Community.dao.findSonList();
		setAttr("communityList", communityList);
		render("/admin/admin_updateActivity.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 */
	public void update() {
		Activity activity = getModel(Activity.class);
		Object activityId = activity.get("activityid");
		if (activityId == null) {
			Object userId = getUserId();

			activity.set("userid", userId);
			activity.save();
		} else {
			activity.update();
		}
		
		goToManager();
	}
	
	/**
	 * admin管理--跳转指定页面
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_detailActivity.html");
	}

	/**
	 * 分页查询指定社区的活动
	 * 请求 activity/find?communityId=${communityId!}
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
		Object userId = getUserId();
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
	 * 请求 activity/findById?communityId=${communityId!}&targetId=${targetId!}&idtype=${idtype!}
	 */
	public void findById() {
		String activityId = getPara("targetId");
		Activity.dao.updateTimes(activityId);
		
		setActivity(activityId);
		setCommunity();
		setActivityApplyPage(activityId);
		keepPara("communityId");
		
		forwardComment(activityId);
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId){
		String action = "/comment/setPage";
		setAttr("render", "/community/detailActivity.html");
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	
	/**
	 * 设置版块信息
	 */
	private void setCommunity(){
		String communityId = getPara("communityId");
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 置顶功能
	 */
	@Before(LoginInterceptor.class)
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
	@Before(LoginInterceptor.class)
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
	 * 判断活动是否可报名
	 * @return
	 */
	public void checkCanApply(){
		Object activityId = getPara("activityId");
		Object activity = Activity.dao.findCanApply(activityId);
		if(activity == null){
			renderText("报名已截止！");
			return;
		}
		//判断当前用户是否已报名
		Object userId = getUserId();
		Object apply = ActivityApply.dao.findByUserId(activityId, userId);
		if(apply != null){
			renderText("已申请，禁止重复申请！");
			return;
		}
		renderNull();
	}

	/**
	 * 跳转活动申请
	 */
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object activity = Activity.dao.findHasPublish(communityId, userId);
		if(activity != null){
			renderJson(false);
			return;
		}
		
		keepPara("communityId");
		setCodeTableList("gender", "genderList");
		render("/community/createActivity.html");
	}
	
	/**
	 * 获取用户Id
	 * @return
	 */
	private Object getUserId(){
		User user = getUser();
		if(user == null){
			return null;
		}
		return user.get("userid");
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
		Object communityId = activity.get("communityid");
		Object userId = getUserId();
		Object atyOld = Activity.dao.findHasPublish(communityId, userId);
		if(atyOld != null){
			renderJson(false);
			return;
		}
		
		activity.set("userid", userId);
		activity.save();

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
		// 当前登录人发帖数
		Object userId = getUserId();
		Long userActivityCount = Activity.dao.getCountByUserId(userId);
		setAttr("userCount", userActivityCount);
		
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
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr("user");
		return user;
	}
}
