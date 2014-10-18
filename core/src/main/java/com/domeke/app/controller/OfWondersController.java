package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.OfWonders;
import com.domeke.app.model.User;
import com.domeke.app.model.WondersType;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "ofWonders")
public class OfWondersController extends Controller {
	
	/** 回复类型 */
	private static String IDTYPE = "50";

	/**
	 * 查询主题列表
	 * 请求 /ofWonders/
	 */
	public void index() {
		String wondersTypeId = getPara("wondersTypeId");
		setOfWondersPage(wondersTypeId);
		setPublishNumber();
		
		render("/wondersType/ofWonders.html");
	}

	/**
	 * 创建主题
	 * 请求 ./ofWonders/create?wondersTypeId = ${wondersTypeId!}
	 */
	@Before(LoginInterceptor.class)
	public void create() {
		OfWonders ofWonders = getModel(OfWonders.class);
		Object wondersTypeId = getPara("wondersTypeId");
		ofWonders.set("wonderstypeid", wondersTypeId);
		Object userId = getUserId();
		Object ofWondersOld = OfWonders.dao.findHasPublish(wondersTypeId, userId);
		if(ofWondersOld != null){
			renderJson(false);
			return;
		}
		
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		ofWonders.set("userid", userId);
		ofWonders.set("userip", remoteIp);
		ofWonders.save();

		setOfWondersPage(wondersTypeId);
		
		render("/wondersType/ofWonders.html");
	}

	/**
	 * 查询指定帖子信息
	 * 请求  ofWonders/findById?wondersTypeId=${wondersTypeId!}&targetId=${targetId!}
	 */
	public void findById() {
		String ofWondersId = getPara("targetId");
		OfWonders.dao.updateTimes(ofWondersId);
		
		setOfWonders(ofWondersId);
		setWondersType();
		keepPara("wondersTypeId");
		
		forwardComment(ofWondersId);
	}
	
	/**
	 * 版块根目录
	 * 请求 /ofWonders/home
	 */
	public void home(){
		setOfWondersPage(null);
		List<WondersType> wondersTypeSonList =  WondersType.dao.findSonList();
		setAttr("wondersTypeSonList", wondersTypeSonList);
		render("/wondersType/ofWondersForum.html");
	}
	
	/**
	 * 置顶功能
	 * 请求 /ofWonders/setTop
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String ofWondersId = getPara("targetId");
		if(StrKit.isBlank(ofWondersId)){
			renderJson("false");
			return;
		}
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.set("ofwondersid", ofWondersId);
		ofWonders.set("top", 1);
		ofWonders.update();
		renderJson("true");
	}
	
	/**
	 * 精华功能
	 * 请求 /ofWonders/setEssence
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String ofWondersId = getPara("targetId");
		if(StrKit.isBlank(ofWondersId)){
			renderJson("false");
			return;
		}
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.set("ofwondersid", ofWondersId);
		ofWonders.set("essence", 1);
		ofWonders.update();
		renderJson("true");
	}
	
	/**
	 * 查询指定社区的分页帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void find() {
		String ofWondersId = getPara("ofWondersId");
		setOfWondersPage(ofWondersId);
		
		render("/ofWonders/ofWondersPage.html");
	}
	
	/**
	 * 分页查询帖子
	 */
	public void findPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = OfWonders.dao.findPage(pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
		render("/admin/admin_detailofWonders.html");
	}

	/**
	 * 查询发帖人所有帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void findByUserId() {
		Object userId = getUserId();
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		OfWonders ofWonders = getModel(OfWonders.class);
		Page<OfWonders> page = ofWonders.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/ofWonders/myofWonders.html");
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId){
		String action = "/comment/setPage";
		setAttr("render", "/wondersType/ofWondersDetail.html");
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setWondersType(){
		Object wondersTypeId = getPara("wondersTypeId");
		if(StrKit.isBlank((String)wondersTypeId)){
			OfWonders ofWonders = getAttr("ofWonders");
			wondersTypeId = ofWonders.get("wonderstypeid");
		}
		WondersType wondersTypeSon = WondersType.dao.findById(wondersTypeId);
		setAttr("wondersTypeSon", wondersTypeSon);
		
		Object pId = wondersTypeSon.get("pid");
		WondersType wondersTypeFat = WondersType.dao.findById(pId);
		setAttr("wondersTypeFat", wondersTypeFat);
	}
	
	/**
	 * 设置帖子信息
	 * @param ofWondersId
	 */
	private void setOfWonders(Object ofWondersId){
		OfWonders ofWonders = OfWonders.dao.findById(ofWondersId);
		setAttr("ofWonders", ofWonders);
	}

	/**
	 * 查询修改的帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void modifyById() {
		String ofWondersId = getPara("ofWondersId");
		OfWonders ofWonders = OfWonders.dao.findById(ofWondersId);
		setAttr("ofWonders", ofWonders);
		render("/ofWonders/modifyofWonders.html");
	}

	/**
	 * 修改帖子信息
	 */
	@Before(LoginInterceptor.class)
	public void modify() {
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.update();

		findByUserId();
	}

	/**
	 * admin管理--删除指定帖子
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		String ofWondersId = getPara("ofWondersId");
		OfWonders.dao.deleteById(ofWondersId);
		
		Comment.dao.deleteByTheme(ofWondersId, IDTYPE);
		findPageAll();
		render("/admin/admin_detailofWonders.html");
	}

	/**
	 * 跳转主题申请
	 */
	@Before(LoginInterceptor.class)
	public void skipCreate() {
		String wondersTypeId = getPara("wondersTypeId");
		Object userId = getUserId();
		Object ofWonders = OfWonders.dao.findHasPublish(wondersTypeId, userId);
		if(ofWonders != null){
			renderJson(false);
			return;
		}
		
		keepPara("wondersTypeId");
		render("/wondersType/ofWondersCreate.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdate() {
		OfWonders ofWonders = null;
		String ofWondersId = getPara("ofWondersId");
		if(StrKit.notBlank(ofWondersId)){
			ofWonders = OfWonders.dao.findById(ofWondersId);
		}else{
			ofWonders = getModel(OfWonders.class);
		}
		setAttr("ofWonders", ofWonders);
		
		List<OfWonders> ofWondersList = null;
		setAttr("ofWondersList", ofWondersList);
		render("/admin/admin_updateofWonders.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		OfWonders ofWonders = getModel(OfWonders.class);
		Object ofWondersId = ofWonders.get("ofWondersid");
		if (ofWondersId == null) {
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();

			ofWonders.set("userid", userId);
			ofWonders.set("userip", remoteIp);
			ofWonders.save();
		} else {
			ofWonders.update();
		}
		
	}
	
	/**
	 * admin管理--跳转指定页面
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_detailofWonders.html");
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
	@Before(LoginInterceptor.class)
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		findById();
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(){
		// 当前登录人发帖数
		Object userId = getUserId();
		Long userofWondersCount = OfWonders.dao.getCountByUserId(userId);
		setAttr("userCount", userofWondersCount);
		
		//今日发帖数
		Long ofWondersTodayCount = OfWonders.dao.getTodayCount();
		setAttr("todayCount", ofWondersTodayCount);
		//昨日发帖数
		Long ofWondersYesCount = OfWonders.dao.getYesterdayCount();
		setAttr("yesCount", ofWondersYesCount);
		//总发帖数
		Long ofWondersCount = OfWonders.dao.getCount();
		setAttr("count", ofWondersCount);
	}
	
	/**
	 * admin管理--分页查询论坛(不区分显示隐藏状态)
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = OfWonders.dao.findPageAll(pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
	}

	/**
	 * 设置分页帖子
	 */
	private void setOfWondersPage(Object wondersTypeId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = null;
		if(wondersTypeId == null){
			ofWondersPage =OfWonders.dao.findPage(pageNumber, pageSize);
		}else{
			ofWondersPage = OfWonders.dao.findPageByOfWondersId(pageNumber, pageSize, wondersTypeId);
		}
		setAttr("ofWondersPage", ofWondersPage);
		setAttr("wondersTypeId", wondersTypeId);
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
