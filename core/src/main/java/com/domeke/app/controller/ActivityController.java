package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.ActivityApply;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "activity")
public class ActivityController extends Controller {

	/** 社区id（主键） */
	private static final String COMMUNITYID = "communityid";
	
	/** 目标id(主键) */
	private static final String TARGETID = "targetid";
	
	/** 活动id(主键) */
	private static final String ACTIVITYID = "activityid";
	
	/** 跳转页面指令 */
	private static final String RENDER = "render";
	
	/** 跳转回复指令 */
	private static final String COMMENTACTION = "/comment";
	
	/** 回复类型 */
	private static final String IDTYPE = "idtype";
	
	/** 置顶 */
	private static final String TOP = "top";
	
	/** 精华 */
	private static final String ESSENCE = "essence";
	
	/** 当前页号 */
	private static final String PAGENUMBER = "pageNumber";
	
	/** 每页记录数 */
	private static final String PAGESIZE = "pageSize";
	
	/** 回复类型 */
	private static String IDTYPEVALUE = "20";
	
	/**
	 * 根据版块信息分页查询活动
	 * <p>
	 * 1、入口 
	 * <p>
	 * 2、活动列表中点击分页相关按钮
	 * <p>
	 * 请求 /activity?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void index() {
		String communityid = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForum(communityid, "10", pageNumber, pageSize);
		render("/community/community_activity.html");
	}
	
	/**
	 * 根据版块分类信息分页查询活动
	 * <p>
	 * 首页
	 * <p>
	 * 请求  ./community/skipForumClassify?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void home(){
		String communityid = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForumClassify(communityid, "10", pageNumber, pageSize);
		render("/community/community_activity.html");
	}

	/**
	 * 创建活动
	 * 请求 ./activity/save
	 */
	public void save() {
		try{
			Activity activity = getModel(Activity.class);
			Object userId = getUserId();
			activity.set("userid", userId);
			activity.save();
			renderJson(true);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * 跳转主题
	 * <p>
	 * 主题列表点击主题信息事件
	 * 请求 community/skipTheme?communityid=${communityid!}&targetid=${targetid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void skipTheme() {
		String activityId = getPara(TARGETID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		
		Activity.dao.updateTimes(activityId);
		setBodyInfoOfTheme(activityId, pageNumber, pageSize);
		
		String render = "/community/community_activityDetail.html";
		forwardComment(activityId,render);
	}
	
	/**
	 * 置顶功能
	 * 请求 ./community/setTop?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String activityId = getPara(TARGETID);
		if(StrKit.isBlank(activityId)){
			renderJson(2);
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set(ACTIVITYID, activityId);
		activity.set(TOP, 1);
		activity.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./community/setEssence?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String activityId = getPara(TARGETID);
		if(StrKit.isBlank(activityId)){
			renderJson(2);
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set(ACTIVITYID, activityId);
		activity.set(ESSENCE, 1);
		activity.update();
		renderJson(3);
	}
	
	/**
	 * admin管理--入口
	 * 请求 ./activity/goToManager
	 */
	public void goToManager() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_activity.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 * 请求 ./activity/skipUpdate?activityId=${activityId!}
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
		
		List<Community> communityList = Community.dao.findForumList();
		setAttr("communityList", communityList);
		render("/admin/admin_activityUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./activity/update
	 */
	public void update() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		Activity activity = getModel(Activity.class);
		Object activityId = activity.get("activityid");
		if (activityId == null) {
			Object userId = getUserId();

			activity.set("userid", userId);
			activity.save();
		} else {
			activity.update();
		}
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_activityPage.html");
	}
	
	/**
	 * admin管理--分页跳转
	 * 请求 ./activity/findByAdminPage
	 */
	public void findByAdminPage(){
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_activityPage.html");
	}
	
	/**
	 * 删除指定活动
	 * 请求 ./activity/deleteById?activityId=${activityId!}
	 */
	public void deleteById() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		String activityId = getPara("activityId");
		Activity.dao.deleteById(activityId);
		Comment.dao.deleteByTheme(activityId, IDTYPEVALUE);
		
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_activityPage.html");
	}
	
	/**
	 * 个人会用中心（我发布的活动）--入口
	 * 请求 ./activity/personalHome
	 */
	@Before(LoginInterceptor.class)
	public void personalHome(){
		setPageByUser();
		render("/personal/personal_activity.html");
	}

	/**
	 * 个人会员中心--查询用户发布的活动
	 * 请求 activity/findByUserId
	 */
	@Before(LoginInterceptor.class)
	public void findByUserId() {
		setPageByUser();
		render("/personal/personal_activityPage.html");
	}
	
	/**
	 * 个人会员中心--跳转修改主题
	 * 请求 ./activity/skipUpdateForPersonal?activityId=${activityId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdateForPersonal() {
		Activity activity = null;
		String activityId = getPara("activityId");
		if(StrKit.notBlank(activityId)){
			activity = Activity.dao.findInfoById(activityId);
		}
		setAttr("activity", activity);
		render("/personal/personal_activityUpdate.html");
	}
	
	/**
	 * 个人会员中心--修改主题
	 * 请求 ./activity/updateForPersonal
	 */
	@Before(LoginInterceptor.class)
	public void updateForPersonal() {
		try{
			Activity activity = getModel(Activity.class);
			activity.update();
		}catch(Exception e){
			e.printStackTrace();
		}
		personalHome();
	}
	
	/**
	 * 个人会员中心--跳转主题明细
	 * 请求 ./activity/findByIdForPersonal?activityId={activityId!}
	 */
	public void skipContain() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		String activityId = getPara("activityId");
		Activity.dao.updateTimes(activityId);
		
		setActivity(activityId);
		setCommunityList();
		setActivityApplyPage(activityId, pageNumber, pageSize);
		setVentWall();
		
		String render = "/community/community_activityContain.html";
		forwardComment(activityId,render);
	}
	

	
	/**
	 * 设置版块集合
	 */
	private void setCommunityList(){
		List<Community> communityFatList = Community.dao.findForumClassifyList();
		setAttr("communityFatList", communityFatList);
		
		List<Community> communitySonList = Community.dao.findForumList();
		setAttr("communitySonList", communitySonList);
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
			renderJson(1);
			return;
		}
		
		keepPara("communityId");
		setCodeTableList("gender", "genderList");
		render("/community/community_activityCreate.html");
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
	 * 跳转回复控制器，设置回复信息
	 * @param targetid 活动主键
	 * @param render 跳转页面
	 */
	private void forwardComment(Object targetid,Object render){
		setAttr(RENDER, render);
		setAttr(TARGETID, targetid);
		setAttr(IDTYPE, IDTYPEVALUE);
		forwardAction(COMMENTACTION);
	}
	
	/**
	 * 设置活动主题信息
	 */
	private void setBodyInfoOfTheme(String activityid,int pageNumber,int pageSize){
		setActivity(activityid);
		setActivityApplyPage(activityid,pageNumber,pageSize);
	}
	
	/**
	 * 设置活动信息
	 * @param activityId
	 */
	private void setActivity(Object activityId){
		Activity activity = Activity.dao.findInfoById(activityId);
		setAttr("activity", activity);
	}
	
	/**
	 * 分页设置活动申请信息
	 * 
	 * @param activityId
	 */
	private void setActivityApplyPage(String activityid,int pageNumber,int pageSize) {
		Page<ActivityApply> activityApplyPage = ActivityApply.dao.findByActivityId(activityid, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);
	}
	
	/**
	 * 根据用户分页查询
	 */
	private void setPageByUser() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		Object userId = getUserId();
		Page<Activity> activityPage = Activity.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("activityPage", activityPage);
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}

	/**
	 * admin管理--设置活动信息
	 * @param pageNuber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfo(int pageNumber,int pageSize) {
		Page<Activity> activityPage = Activity.dao.findPageAll(pageNumber, pageSize);
		setAttr("activityPage", activityPage);
	}
	
	/**
	 * 设置活动信息
	 * @param communityId 版块id
	 * @param pageNuber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForum(Object communityId,Object status,int pageNuber,int pageSize) {
		setPageByForum(communityId,status, pageNuber, pageSize);
		setPublishInfoByForum(communityId);
	}
	
	/**
	 * 设置活动信息
	 * @param communityId 版块分类id
	 * @param pageNuber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForumClassify(Object communityId,Object status,int pageNuber,int pageSize) {
		setPageByForumClassify(communityId,status, pageNuber, pageSize);
		setPublishInfoByForumClassify(communityId);
	}

	/**
	 * 设置发帖数
	 */
	private void setPublishInfoByForum(Object communityId){
		setUserPublishInfoByForum(communityId);
		setTodayPublishInfoByForum(communityId);
		setYesPublicInfoByForum(communityId);
		setCountPublisInfoByForum(communityId);
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishInfoByForumClassify(Object communityId){
		setUserPublishInfoByForumClassify(communityId);
		setTodayPublishInfoByForumClassify(communityId);
		setYesPublicInfoByForumClassify(communityId);
		setCountPublisInfoByForumClassify(communityId);
	}
	
	/**
	 * 根据版块信息设置总发帖数
	 * @param communityid 版块id
	 */
	private void setCountPublisInfoByForum(Object communityid){
		long count = Activity.dao.getCountByForum(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块分类信息设置总发帖数
	 * @param communityid 版块分类id
	 */
	private void setCountPublisInfoByForumClassify(Object communityid){
		long count = Activity.dao.getCountByForumClassify(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块信息设置昨日发帖数
	 * @param communityid 版块信息
	 */
	private void setYesPublicInfoByForum(Object communityid){
		Long yesCount = Activity.dao.getCountOfYesByForum(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块分类信息设置昨日发帖数
	 * @param communityid 版块分类id
	 */
	private void setYesPublicInfoByForumClassify(Object communityid){
		Long yesCount = Activity.dao.getCountOfYesByForumClassify(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块信息设置今日发帖数
	 * @param communityid 版块信息
	 */
	private void setTodayPublishInfoByForum(Object communityid){
		Long todayCount = Activity.dao.getCountOfTodayByForum(communityid);
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 根据版块分类信息设置今日发帖数
	 * @param communityid 版块分类id
	 */
	private void setTodayPublishInfoByForumClassify(Object communityid){
		Long todayCount = Activity.dao.getCountOfTodayByForumClassify(communityid);
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 根据版块信息设置用户发帖数
	 * @param communityid 版块信息
	 */
	private void setUserPublishInfoByForum(Object communityid){
		Long userCount = new Long(0);
		Object userId = getUserId();
		if(userId != null){
			userCount = Activity.dao.getCountOfUserByForum(communityid, userId);
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 根据版块分类信息设置用户发帖数
	 * @param communityid 版块分类id
	 */
	private void setUserPublishInfoByForumClassify(Object communityid){
		Long userCount = new Long(0);
		Object userId = getUserId();
		if(userId != null){
			userCount = Activity.dao.getCountOfUserByForumClassify(communityid, userId);
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId 版块id
	 */
	public void setPageByForum(Object communityId,Object status,int pageNumber,int pageSize){
		Page<Activity> activityPage = Activity.dao.findPageByForum(pageNumber,pageSize,communityId, status);
		setAttr("activityPage", activityPage);
	}
	
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId 版块分类id
	 */
	public void setPageByForumClassify(Object communityId,Object status,int pageNumber,int pageSize){
		Page<Activity> activityPage = Activity.dao.findPageByForumClassify(pageNumber,pageSize,communityId, status);
		setAttr("activityPage", activityPage);
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
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr("user");
		return user;
	}
}
