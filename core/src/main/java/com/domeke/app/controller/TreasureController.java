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

	/** 社区id（主键） */
	private static final String COMMUNITYID = "communityid";

	/** 目标id(主键) */
	private static final String TARGETID = "targetid";
	
	/** 宝贝id(主键) */
	private static final String TREASUREID = "treasureid";
	
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
	private static String IDTYPEVALUE = "30";

	/**
	 * 根据版块信息分页查询宝贝信息
	 * <p>
	 * 1、入口
	 * <p>
	 * 2、宝贝列表中点击分页相关按钮
	 * <p>
	 * 请求 ./community/skipForum?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void index() {
		String communityid = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForum(communityid, "10",pageNumber, pageSize);
		render("/community/community_treasure.html");
	}
	
	/**
	 * 根据版块分类信息分页查询宝贝信息
	 * <p>
	 * 版块首页
	 * <p>
	 * 请求 ./community/skipForumClassify?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void home(){
		String communityid = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForumClassify(communityid, "10",pageNumber, pageSize);
		render("/community/community_treasure.html");
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
	 * 请求 ./treasure/save
	 */
	public void save() {
		try {
			Treasure treasure = getModel(Treasure.class);
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();

			treasure.set("userid", userId);
			treasure.set("userip", remoteIp);
			treasure.save();
			renderJson(true);
		} catch (Exception e) {
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
		String treasureid = getPara(TARGETID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		
		Treasure.dao.updateTimes(treasureid);
		setBodyInfoOfTheme(treasureid, pageNumber, pageSize);
		
		String render = "/community/community_treasureDetail.html";
		forwardComment(treasureid,render);
	}
	
	/**
	 * 置顶功能
	 * 请求 ./community/setTop?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String treasureId = getPara(TARGETID);
		if(StrKit.isBlank(treasureId)){
			renderJson(2);
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set(TREASUREID, treasureId);
		treasure.set(TOP, 1);
		treasure.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./community/setEssence?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String treasureId = getPara(TARGETID);
		if(StrKit.isBlank(treasureId)){
			renderJson(2);
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set(TREASUREID, treasureId);
		treasure.set(ESSENCE, 1);
		treasure.update();
		renderJson(3);
	}

	/**
	 * admin管理--入口
	 * 请求 ./treasure/goToManager
	 */
	public void goToManager() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_treasure.html");
	}
	
	/**
	 * admin管理--分页跳转
	 * 请求 ./treasure/findByAdminPage
	 */
	public void findByAdminPage(){
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_treasurePage.html");
	}
	
	/**
	 * admin管理--删除指定主题
	 * 请求 ./treasure/deleteById?treasureId=${treasureId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		String treasureId = getPara("treasureId");
		Treasure.dao.deleteById(treasureId);
		
		Comment.dao.deleteByTheme(treasureId, IDTYPEVALUE);
		setBodyInfo(pageNumber, pageSize);
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
		
		List<Community> communityList = Community.dao.findForumList();
		setAttr("communityList", communityList);
		render("/admin/admin_treasureUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./treasure/update
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
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
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_treasurePage.html");
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
	public void skipContain() {
		String treasureId = getPara("treasureId");
		Treasure.dao.updateTimes(treasureId);
		
		setTreasure(treasureId);
		setCommunitys();
		setCommunityList();
		
		String render = "/community/community_treasureContain.html";
		forwardComment(treasureId,render);
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
		List<Community> communityFatList = Community.dao.findForumClassifyList();
		setAttr("communityFatList", communityFatList);
		
		List<Community> communitySonList = Community.dao.findForumList();
		setAttr("communitySonList", communitySonList);
	}

	/**
	 * 删除回复信息
	 */
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);
	}
	
	
	/**
	 * 跳转回复控制器，设置回复信息
	 * @param targetid 宝贝主键
	 * @param render 跳转页面
	 */
	private void forwardComment(Object targetid,Object render){
		setAttr(RENDER, render);
		setAttr(TARGETID, targetid);
		setAttr(IDTYPE, IDTYPEVALUE);
		forwardAction(COMMENTACTION);
	}
	
	/**
	 * 设置宝贝主题信息
	 */
	private void setBodyInfoOfTheme(String treasureid,int pageNumber,int pageSize){
		setTreasure(treasureid);
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
	 * admin管理--设置宝贝信息
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfo(int pageNumber,int pageSize) {
		Page<Treasure> treasurePage = Treasure.dao.findPageAll(pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
	}
	
	/**
	 * 根据版块信息设置宝贝信息
	 * @param communityId 版块id 
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForum(Object communityId,Object status,int pageNumber,int pageSize) {
		setPageByForum(communityId, status, pageNumber, pageSize);
		setPublishNumber(communityId);
	}
	
	/**
	 * 根据版块分类信息设置宝贝信息
	 * @param communityId 版块分类id
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForumClassify(Object communityId,Object status,int pageNumber,int pageSize) {
		setPageByForumClassify(communityId, status,pageNumber, pageSize);
		setPublishNumberClassify(communityId);
	}
	
	/**
	 * 根据版块信息设置宝贝数
	 * @param communityid 版块id
	 */
	private void setPublishNumber(Object communityId){
		setUserPublishInfoByForum(communityId);
		setTodayPublishInfoByForum(communityId);
		setYesPublicInfoByForum(communityId);
		setCountPublisInfoByForum(communityId);
	}
	
	/**
	 * 根据版块分类信息设置宝贝数
	 * @param communityid 版块分类id
	 */
	private void setPublishNumberClassify(Object communityId){
		setUserPublishInfoByForumClassify(communityId);
		setTodayPublishInfoByForumClassify(communityId);
		setYesPublicInfoByForumClassify(communityId);
		setCountPublisInfoByForumClassify(communityId);
	}

	/**
	 * 根据版块信息设置总发帖数
	 * @param communityid
	 */
	private void setCountPublisInfoByForum(Object communityid){
		Long count = Treasure.dao.getCountByForum(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块分类信息设置总发帖数
	 * @param communityid 版块分类id
	 */
	private void setCountPublisInfoByForumClassify(Object communityid){
		Long count = Treasure.dao.getCountByForumClassify(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块信息设置昨日发帖数
	 * @param communityid 版块信息
	 */
	private void setYesPublicInfoByForum(Object communityid){
		Long yesCount = Treasure.dao.getCountOfYesByForum(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块分类信息设置昨日发帖数
	 * @param communityid 版块分类id
	 */
	private void setYesPublicInfoByForumClassify(Object communityid){
		Long yesCount = Treasure.dao.getCountOfYesByForumClassify(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块信息设置今日发帖数
	 * @param communityid 版块信息
	 */
	private void setTodayPublishInfoByForum(Object communityid){
		Long todayCount = Treasure.dao.getCountOfTodayByForum(communityid);
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 根据版块分类信息设置今日发帖数
	 * @param communityid 版块分类id
	 */
	private void setTodayPublishInfoByForumClassify(Object communityid){
		Long todayCount = Treasure.dao.getCountOfTodayByForumClassify(communityid);
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
			userCount = Treasure.dao.getCountOfUserByForum(communityid, userId);
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
			userCount = Treasure.dao.getCountOfUserByForumClassify(communityid, userId);
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 根据版块设置分页宝贝
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setPageByForum(Object communityId,Object status,int pageNumber,int pageSize) {
		Page<Treasure> treasurePage = Treasure.dao.findPageByForum(pageNumber,pageSize, communityId,status);
		setAttr("treasurePage", treasurePage);
	}
	
	/**
	 * 根据版块分类设置分页宝贝
	 * @param communityid 版块分类id
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setPageByForumClassify(Object communityId,Object status,int pageNumber,int pageSize) {
		Page<Treasure> treasurePage = Treasure.dao.findPageByForumClassify(pageNumber,pageSize, communityId,status);
		setAttr("treasurePage", treasurePage);
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
