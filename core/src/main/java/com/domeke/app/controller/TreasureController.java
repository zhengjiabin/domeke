package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Treasure;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "treasure")
public class TreasureController extends Controller {
	
	/** 回复类型 */
	private static String IDTYPE = "30";

	/**
	 * 宝贝列表
	 * 请求 ./treasure?communityId=${communityId!}
	 */
	public void index() {
		String communityId = getPara("communityId");
		setTreasurePage(communityId);
		setPublishNumber(communityId);
		setCommunity();
		
		render("/community/treasure.html");
	}
	
	/**
	 * 创建宝贝主题
	 * 请求 ./treasure/create
	 */
	public void create() {
		Treasure treasure = getModel(Treasure.class);
		Object communityId = treasure.get("communityid");
		Object userId = getUserId();
		Object treasureOld = Treasure.dao.findHasPublish(communityId, userId);
		if(treasureOld != null){
			renderJson(false);
			return;
		}
		
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		treasure.set("userid", userId);
		treasure.set("userip", remoteIp);
		treasure.save();

		setTreasurePage(communityId);
		setPublishNumber(communityId);
		
		render("/community/treasure.html");
	}
	
	/**
	 * 版块入口
	 * 请求 ./treasure/home
	 */
	public void home(){
		setTreasurePage(null);
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		setPublishNumber(null);
		render("/community/treasureAll.html");
	}

	/**
	 * admin管理--入口
	 * 请求 ./treasure/goToManager
	 */
	public void goToManager() {
		findPageAll();
		render("/admin/admin_treasure.html");
	}
	
	/**
	 * admin管理--分页跳转
	 * 请求 ./treasure/findByAdminPage
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_treasurePage.html");
	}
	
	/**
	 * admin管理--删除指定主题
	 * 请求 ./treasure/deleteById?treasureId=${treasureId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		String treasureId = getPara("treasureId");
		Treasure.dao.deleteById(treasureId);
		
		Comment.dao.deleteByTheme(treasureId, IDTYPE);
		findPageAll();
		render("/admin/admin_treasurePage.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 * 请求 ./treasure/skipUpdate?treasureId=${treasureId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdate() {
		Treasure treasure = null;
		String treasureId = getPara("treasureId");
		if(StrKit.notBlank(treasureId)){
			treasure = Treasure.dao.findById(treasureId);
		}else{
			treasure = getModel(Treasure.class);
		}
		setAttr("treasure", treasure);
		
		List<Community> communityList = Community.dao.findSonList();
		setAttr("communityList", communityList);
		render("/admin/admin_treasureUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./treasure/update
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		Treasure treasure = getModel(Treasure.class);
		Object treasureId = treasure.get("treasureid");
		if (treasureId == null) {
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();

			treasure.set("userid", userId);
			treasure.set("userip", remoteIp);
			treasure.save();
		} else {
			treasure.update();
		}
		goToManager();
	}
	
	/**
	 * admin管理--分页查询论坛(不区分显示隐藏状态)
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = Treasure.dao.findPageAll(pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
	}

	/**
	 * 查询指定社区的分页宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void find() {
		String communityId = getPara("communityId");
		setTreasurePage(communityId);
		
		render("/community/treasurePage.html");
	}
	
	/**
	 * 分页查询宝贝
	 */
	public void findPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = Treasure.dao.findPage(pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
		render("/admin/admin_detailtreasure.html");
	}

	/**
	 * 查询当前用户所有宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void findByUserId() {
		Object userId = getUserId();
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Treasure treasure = getModel(Treasure.class);
		Page<Treasure> page = treasure.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/community/myTreasure.html");
	}
	
	/**
	 * 置顶功能
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson("false");
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("top", 1);
		treasure.update();
		renderJson("true");
	}
	
	/**
	 * 精华功能
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson("false");
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("essence", 1);
		treasure.update();
		renderJson("true");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		String treasureId = getPara("targetId");
		Treasure.dao.updateTimes(treasureId);
		
		setTreasure(treasureId);
		setCommunitys();
		keepPara("communityId");
		
		forwardComment(treasureId);
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId){
		String action = "/comment/setPage";
		setAttr("render", "/community/detailTreasure.html");
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunitys(){
		String communityId = getPara("communityId");
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunity(){
		String communityId = getPara("communityId");
		Community community = Community.dao.findById(communityId);
		setAttr("community", community);
	}
	
	/**
	 * 设置宝贝信息
	 * @param treasureId
	 */
	private void setTreasure(Object treasureId){
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
	}

	/**
	 * 查询修改的宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void modifyById() {
		String treasureId = getPara("treasureId");
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
		render("/community/modifyTreasure.html");
	}

	/**
	 * 修改宝贝信息
	 */
	public void modify() {
		Treasure treasure = getModel(Treasure.class);
		treasure.update();

		findByUserId();
	}

	/**
	 * 跳转宝贝申请
	 */
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object treasure = Treasure.dao.findHasPublish(communityId, userId);
		if(treasure != null){
			renderHtml("<script> alert('5分钟内只能发布一次同类型主题！');</script>");
			return;
		}
		
		keepPara("communityId");
		render("/community/createTreasure.html");
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
	 * 删除回复信息
	 */
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		findById();
	}
	
	/**
	 * 设置宝贝数
	 */
	private void setPublishNumber(Object communityId){
		Long userTreasureCount = null,treasureTodayCount = null,treasureYesCount = null,treasureCount = null;
		Object userId = getUserId();
		if(StrKit.notNull(communityId)){
			userTreasureCount = Treasure.dao.getCountByCommunityAndUser(communityId, userId);
			treasureTodayCount = Treasure.dao.getTodayCountByCommunityId(communityId);
			treasureYesCount = Treasure.dao.getYesterdayCountByCommunityId(communityId);
			treasureCount = Treasure.dao.getCountByCommunityId(communityId);
		}else{
			userTreasureCount = Treasure.dao.getCountByUserId(userId);
			treasureTodayCount = Treasure.dao.getTodayCount();
			treasureYesCount = Treasure.dao.getYesterdayCount();
			treasureCount = Treasure.dao.getCount();
		}
		// 当前登录人宝贝数
		setAttr("userCount", userTreasureCount);
		//今日宝贝数
		setAttr("todayCount", treasureTodayCount);
		//昨日宝贝数
		setAttr("yesCount", treasureYesCount);
		//总宝贝数
		setAttr("count", treasureCount);
	}

	/**
	 * 设置分页宝贝
	 */
	private void setTreasurePage(Object communityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = null;
		if(communityId == null){
			treasurePage =Treasure.dao.findPage(pageNumber, pageSize);
		}else{
			treasurePage = Treasure.dao.findPageByCommunityId(pageNumber,pageSize, communityId);
		}
		setAttr("treasurePage", treasurePage);
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
