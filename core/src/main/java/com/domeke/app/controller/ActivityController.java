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
	
	/** 回复类型 */
	private static String IDTYPE = "20";
	
	/**
	 * 活动列表
	 * 请求 /activity?communityId=${communityId!}
	 */
	public void index() {
		String communityId = getPara("communityId");
		setEntryData(communityId);
		render("/community/community_activity.html");
	}
	
	/**
	 * 分页查询指定社区的活动
	 * 请求 activity/find?communityId=${communityId!}
	 */
	public void find() {
		Object communityId = getPara("communityId");
		setEntryData(communityId);
		render("/community/community_activityPage.html");
	}
	
	/**
	 * 版块入口
	 * 请求 ./activity/home
	 */
	public void home(){
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		setEntryData(null);
		render("/community/community_activityAll.html");
	}

	/**
	 * 创建活动
	 * 请求 ./activity/create
	 * 
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

		setPage(communityId);
		setPublishNumber(communityId);
		
		render("/community/community_activity.html");
	}
	
	/**
	 * 置顶功能
	 * 请求 ./activity/setTop?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String activityId = getPara("targetId");
		if(activityId == null || activityId.length()<=0){
			renderJson(2);
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set("activityid", activityId);
		activity.set("top", 1);
		activity.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./activity/setEssence?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String activityId = getPara("targetId");
		if(activityId == null || activityId.length()<=0){
			renderJson(2);
			return;
		}
		Activity activity = getModel(Activity.class);
		activity.set("activityid", activityId);
		activity.set("essence", 1);
		activity.update();
		renderJson(3);
	}
	
	/**
	 * admin管理--入口
	 * 请求 ./activity/goToManager
	 */
	public void goToManager() {
		findPageAll();
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
		
		List<Community> communityList = Community.dao.findSonList();
		setAttr("communityList", communityList);
		render("/admin/admin_activityUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./activity/update
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
		findPageAll();
		render("/admin/admin_activityPage.html");
	}
	
	/**
	 * admin管理--分页跳转
	 * 请求 ./activity/findByAdminPage
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_activityPage.html");
	}

	/**
	 * admin管理--分页查询活动(不区分显示隐藏状态)
	 * 请求 ./activity/findPageAll?pageNumber={pageNumber!}&pageSize={pageSize!}
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Activity> activityPage = Activity.dao.findPageAll(pageNumber, pageSize);
		setAttr("activityPage", activityPage);
	}

	/**
	 * 查询指定活动信息
	 * 请求 activity/findById?communityId=${communityId!}&targetId=${targetId!}&idtype=${idtype!}
	 */
	public void findById() {
		String activityId = getPara("targetId");
		Activity.dao.updateTimes(activityId);
		
		setActivity(activityId);
		setCommunitys();
		setActivityApplyPage(activityId);
		
		String render = "/community/community_activityDetail.html";
		forwardComment(activityId,render);
	}
	
	/**
	 * 删除指定活动
	 * 请求 ./activity/deleteById?activityId=${activityId!}
	 */
	public void deleteById() {
		String activityId = getPara("activityId");
		Activity.dao.deleteById(activityId);
		Comment.dao.deleteByTheme(activityId, IDTYPE);
		
		findPageAll();
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
		String activityId = getPara("activityId");
		Activity.dao.updateTimes(activityId);
		
		setActivity(activityId);
		setCommunitys();
		setCommunityList();
		setActivityApplyPage(activityId);
		setVentWall();
		
		String render = "/community/community_activityContain.html";
		forwardComment(activityId,render);
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId,Object render){
		String action = "/comment/setPage";
		setAttr("render", render);
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunity(Object communityId){
		Community community = Community.dao.findById(communityId);
		setAttr("community", community);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunitys(){
		Object communityId = getCommunityId();
		setAttr("communityId", communityId);
		
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 设置版块集合
	 */
	private void setCommunityList(){
		List<Community> communityFatList = Community.dao.findFatList();
		setAttr("communityFatList", communityFatList);
		
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
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
	private void setActivityApplyPage(Object activityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<ActivityApply> activityApplyPage = ActivityApply.dao
				.findByActivityId(activityId, pageNumber, pageSize);
		setAttr("activityApplyPage", activityApplyPage);
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
	 * 设置入口所需数据
	 * @param communityId
	 */
	private void setEntryData(Object communityId) {
		setPage(communityId);
		setPublishNumber(communityId);
		setCommunity(communityId);
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
	 * 获取版块Id
	 */
	private Object getCommunityId(){
		Object communityId = getPara("communityId");
		if(communityId == null || "".equals(communityId.toString().trim())){
			Activity activity = getAttr("activity");
			if(activity != null){
				communityId = activity.get("communityid");
			}
		}
		return communityId;
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
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}

	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(Object communityId){
		Long userActivityCount = null,activityTodayCount = null,activityYesCount = null,activityCount = null;
		Object userId = getUserId();
		if(StrKit.notNull(communityId)){
			userActivityCount = Activity.dao.getCountByCommunityAndUser(communityId, userId);
			activityTodayCount = Activity.dao.getTodayCountByCommunityId(communityId);
			activityYesCount = Activity.dao.getYesterdayCountByCommunityId(communityId);
			activityCount = Activity.dao.getCountByCommunityId(communityId);
		}else{
			userActivityCount = Activity.dao.getCountByUserId(userId);
			activityTodayCount = Activity.dao.getTodayCount();
			activityYesCount = Activity.dao.getYesterdayCount();
			activityCount = Activity.dao.getCount();
		}
		// 当前登录人发帖数
		setAttr("userCount", userActivityCount);
		//今日发帖数
		setAttr("todayCount", activityTodayCount);
		//昨日发帖数
		setAttr("yesCount", activityYesCount);
		//总发帖数
		setAttr("count", activityCount);
	}
	
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId
	 */
	public void setPage(Object communityId){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		
		Page<Activity> activityPage = null;
		if(communityId == null){
			activityPage = Activity.dao.findPage(pageNumber, pageSize);
		}else{
			activityPage = Activity.dao.findPageByCommunityId(pageNumber,pageSize,communityId);
		}
		setAttr("activityPage", activityPage);
		setAttr("communityId", communityId);
	}
	
	/**
	 * 根据用户分页查询
	 */
	private void setPageByUser() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Object userId = getUserId();
		Page<Activity> activityPage = Activity.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("activityPage", activityPage);
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
