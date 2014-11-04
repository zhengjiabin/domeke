package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Treasure;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
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
		setEntryData(communityId);
		render("/community/community_treasure.html");
	}
	
	/**
	 * 查询指定社区的分页宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void find() {
		String communityId = getPara("communityId");
		setEntryData(communityId);
		render("/community/community_treasurePage.html");
	}
	
	/**
	 * 版块入口
	 * 请求 ./treasure/home
	 */
	public void home(){
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		setEntryData(null);
		render("/community/community_treasureAll.html");
	}
	
	/**
	 * 跳转宝贝申请
	 * 请求 ./treasure/skipCreate?communityId={communityId!}
	 */
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object treasure = Treasure.dao.findHasPublish(communityId, userId);
		if(treasure != null){
			renderJson(1);
			return;
		}
		
		keepPara("communityId");
		render("/community/community_treasureCreate.html");
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

		setPage(communityId);
		setPublishNumber(communityId);
		
		render("/community/community_treasure.html");
	}
	
	/**
	 * 查询指定宝贝信息
	 * 请求 ./treasure/findById?targetId={targetId!}&communityId={communityId!}
	 */
	public void findById() {
		String treasureId = getPara("targetId");
		Treasure.dao.updateTimes(treasureId);
		
		setTreasure(treasureId);
		setCommunitys();
		
		String render = "/community/community_treasureDetail.html";
		forwardComment(treasureId,render);
	}
	
	/**
	 * 置顶功能
	 * 请求 ./treasure/setTop?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson(2);
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("top", 1);
		treasure.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./treasure/setEssence?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson(2);
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("essence", 1);
		treasure.update();
		renderJson(3);
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
		findPageAll();
		render("/admin/admin_treasurePage.html");
	}
	
	/**
	 * admin管理--分页查询论坛(不区分显示隐藏状态)
	 * 请求 ./treasure/findPageAll?pageNumber={pageNumber!}&pageSize={pageSize!}
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = Treasure.dao.findPageAll(pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
	}
	
	/**
	 * 个人会用中心（我发布的宝贝）--入口
	 */
	@Before(LoginInterceptor.class)
	public void personalHome(){
		setPageByUser();
		render("/personal/personal_treasure.html");
	}
	
	/**
	 * 个人会员中心--查询用户发布的宝贝
	 * 请求 treasure/findByUserId
	 */
	@Before(LoginInterceptor.class)
	public void findByUserId() {
		setPageByUser();
		render("/personal/personal_treasurePage.html");
	}
	
	/**
	 * 个人会员中心--跳转修改主题
	 * 请求 ./treasure/skipUpdateForPersonal?treasureId=${treasureId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdateForPersonal() {
		Treasure treasure = null;
		String treasureId = getPara("treasureId");
		if(StrKit.notBlank(treasureId)){
			treasure = Treasure.dao.findInfoById(treasureId);
		}
		setAttr("treasure", treasure);
		render("/personal/personal_treasureUpdate.html");
	}
	
	/**
	 * 个人会员中心--修改主题
	 * 请求 ./treasure/updateForPersonal
	 */
	@Before(LoginInterceptor.class)
	public void updateForPersonal() {
		try{
			Treasure treasure = getModel(Treasure.class);
			treasure.update();
		}catch(Exception e){
			e.printStackTrace();
		}
		personalHome();
	}
	
	/**
	 * 个人会员中心--跳转主题明细
	 * 请求 ./treasure/findByIdForPersonal?treasureId={treasureId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipContain() {
		String treasureId = getPara("treasureId");
		Treasure.dao.updateTimes(treasureId);
		
		setTreasure(treasureId);
		setCommunitys();
		setCommunityList();
		setVentWall();
		
		String render = "/community/community_treasureContain.html";
		forwardComment(treasureId,render);
	}
	
	/**
	 * 设置入口所需数据
	 * @param communityId
	 */
	private void setEntryData(Object communityId) {
		setPage(communityId);
		setPublishNumber(communityId);
		setCommunity();
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
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
	 * 获取版块Id
	 */
	private Object getCommunityId(){
		Object communityId = getPara("communityId");
		if(communityId == null || "".equals(communityId.toString().trim())){
			Treasure treasure = getAttr("treasure");
			if(treasure != null){
				communityId = treasure.get("communityid");
			}
		}
		return communityId;
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
		Treasure treasure = Treasure.dao.findInfoById(treasureId);
		setAttr("treasure", treasure);
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
	private void setPage(Object communityId) {
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
	 * 根据用户设置分页
	 */
	private void setPageByUser() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Object userId = getUserId();
		Page<Treasure> treasurePage = Treasure.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
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
