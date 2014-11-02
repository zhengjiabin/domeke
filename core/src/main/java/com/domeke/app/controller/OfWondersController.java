package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.OfWonders;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.model.WondersType;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "ofWonders")
public class OfWondersController extends FilesLoadController {
	
	/** 回复类型 */
	private static String IDTYPE = "50";
	
	private static String saveFolderName = "/wondersType";
	private static String parameterName = "ofWonders.themeimg";
	private static int maxPostSize = 2 * 1024 * 1024;
	private static String encoding = "UTF-8";

	/**
	 * 查询主题列表
	 * 请求 /ofWonders/
	 */
	public void index() {
		String wondersTypeId = getPara("wondersTypeId");
		setOfWondersPage(wondersTypeId);
		setPublishNumber(wondersTypeId);
		setWondersType();
		
		render("/wondersType/ofWonders.html");
	}
	
	/**
	 * 跳转主题申请
	 * 请求 ./ofWonders/skipCreate?wondersTypeId={wondersTypeId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipCreate() {
		String wondersTypeId = getPara("wondersTypeId");
		Object userId = getUserId();
		Object ofWonders = OfWonders.dao.findHasPublish(wondersTypeId, userId);
		if(ofWonders != null){
			renderJson(2);
			return;
		}
		
		keepPara("wondersTypeId");
		render("/wondersType/ofWondersCreate.html");
	}

	/**
	 * 创建主题
	 * 请求 ./ofWonders/create?wondersTypeId = ${wondersTypeId!}
	 */
	@Before(LoginInterceptor.class)
	public void create() {
		boolean isSucceed = dealOfWonders();
		if(!isSucceed){
			return;
		}
		Object wondersTypeId = getPara("wondersTypeId");
		setOfWondersPage(wondersTypeId);
		setPublishNumber(wondersTypeId);
		
		render("/wondersType/ofWonders.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 请求  ofWonders/findById?wondersTypeId=${wondersTypeId!}&targetId=${targetId!}
	 */
	public void findById() {
		String ofWondersId = getPara("targetId");
		OfWonders.dao.updateTimes(ofWondersId);
		
		setOfWonders(ofWondersId);
		setWondersTypes();
		keepPara("wondersTypeId");
		String render = "/wondersType/ofWondersDetail.html";
		forwardComment(ofWondersId, render);
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
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String ofWondersId = getPara("targetId");
		if(StrKit.isBlank(ofWondersId)){
			renderJson(2);
			return;
		}
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.set("ofwondersid", ofWondersId);
		ofWonders.set("top", 1);
		ofWonders.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 /ofWonders/setEssence
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String ofWondersId = getPara("targetId");
		if(StrKit.isBlank(ofWondersId)){
			renderJson(2);
			return;
		}
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.set("ofwondersid", ofWondersId);
		ofWonders.set("essence", 1);
		ofWonders.update();
		renderJson(3);
	}
	
	/**
	 * 首页无奇不有跳转指定明细
	 * 请求 ./ofWonders/skipContain?ofWondersId = ${ofWondersId!}
	 */
	public void skipContain(){
		String ofWondersId = getPara("ofWondersId");
		OfWonders.dao.updateTimes(ofWondersId);
		
		setOfWonders(ofWondersId);
		setWondersTypeFatList();
		setWondersTypeSonListCount();
		setWondersTypes();
		setVentWall();
		
		String render = "/wondersType/ofWondersContain.html";
		forwardComment(ofWondersId,render);
	}
	
	/**
	 * admin管理--入口
	 * 请求 ./ofWonders/goToManger
	 */
	public void goToManager() {
		findPageAll();
		render("/admin/admin_ofWonders.html");
	}
	
	/**
	 * admin管理--删除指定主题
	 * 请求 ./ofWonders/deleteById?ofWondersId=${ofWondersId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		String ofWondersId = getPara("ofWondersId");
		OfWonders.dao.deleteById(ofWondersId);
		
		Comment.dao.deleteByTheme(ofWondersId, IDTYPE);
		findPageAll();
		render("/admin/admin_ofWondersPage.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 * 请求 ./ofWonders/skipUpdate?ofWondersId=${ofWondersId!}
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
		
		List<WondersType> WondersTypeDetailList = WondersType.dao.findSonList();
		setAttr("WondersTypeDetailList", WondersTypeDetailList);
		render("/admin/admin_ofWondersUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./ofWonders/update?ofWondersId=${ofWondersId!}
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		boolean isSucceed = dealOfWonders();
		if(!isSucceed){
			return;
		}
		findPageAll();
		render("/admin/admin_ofWondersPage.html");
	}
	
	/**
	 * admin管理--跳转指定页面
	 * 请求  ./ofWonders/skipUpdate?ofWondersId=${ofWondersId!}
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_ofWondersPage.html");
	}
	
	/**
	 * 个人会用中心（我发布的宝贝）--入口
	 */
	@Before(LoginInterceptor.class)
	public void personalHome(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Object userId = getUserId();
		Page<OfWonders> ofWondersPage = OfWonders.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
		render("/personal/personal_ofWonders.html");
	}
	
	/**
	 * 个人会员中心--查询用户发布的宝贝
	 * 请求 ofWonders/findByUserId
	 */
	@Before(LoginInterceptor.class)
	public void findByUserId() {
		Object userId = getUserId();
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = OfWonders.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
		render("/personal/personal_ofWondersPage.html");
	}
	
	/**
	 * 个人会员中心--跳转修改主题
	 * 请求 ./ofWonders/skipUpdateForPersonal?ofWondersId=${ofWondersId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdateForPersonal() {
		OfWonders ofWonders = null;
		String ofWondersId = getPara("ofWondersId");
		if(StrKit.notBlank(ofWondersId)){
			ofWonders = OfWonders.dao.findInfoById(ofWondersId);
		}
		setAttr("ofWonders", ofWonders);
		render("/personal/personal_ofWondersUpdate.html");
	}
	
	/**
	 * 个人会员中心--修改主题
	 * 请求 ./ofWonders/updateForPersonal
	 */
	@Before(LoginInterceptor.class)
	public void updateForPersonal() {
		boolean isSucceed = dealOfWonders();
		if(!isSucceed){
			return;
		}
		personalHome();
	}
	
	/**
	 * 设置父版块
	 */
	private void setWondersTypeFatList(){
		List<WondersType> wondersTypeFatList = WondersType.dao.findFatList();
		setAttr("wondersTypeFatList", wondersTypeFatList);
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}
	
	/**
	 * 设置子版块及主题数
	 */
	private void setWondersTypeSonListCount(){
		List<WondersType> wondersTypeSonList = WondersType.dao.findSonListCount();
		setAttr("wondersTypeSonList", wondersTypeSonList);
	}
	
	/**
	 * 处理图片上传
	 * @return 是否上传成功
	 */
	private boolean dealUploadImg(StringBuffer imgPath){
		try {
			String path = upLoadFileDealPath(parameterName, saveFolderName, maxPostSize, encoding);
			if(StrKit.isBlank(path)){
				OfWonders ofWonders = getModel(OfWonders.class);
				Object ofWondersId = ofWonders.get("ofwondersid");
				if(ofWondersId  == null){
					renderJson(1);
					return false;
				}
				OfWonders ofWondersOld = OfWonders.dao.findById(ofWondersId);
				path = ofWondersOld.getStr("themeimg");
			}
			imgPath.append(path);
			return true;
		} catch (RuntimeException e) {
			renderJson(1);
		} catch (Exception e) {
			renderJson(2);
		} 
		return false;
	}
	
	/**
	 * 处理创建/修改无奇不有
	 * @return 是否保存成功
	 * 
	 */
	private boolean dealOfWonders(){
		StringBuffer imgPath = new StringBuffer();
		boolean isSucceed = dealUploadImg(imgPath);
		if(!isSucceed){
			return isSucceed;
		}
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.set("themeimg", imgPath.toString());
		Object ofWondersId = ofWonders.get("ofwondersid");
		if (ofWondersId == null) {
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();
			
			Object wondersTypeId = getPara("wondersTypeId");
			ofWonders.set("wonderstypeid", wondersTypeId);

			ofWonders.set("userid", userId);
			ofWonders.set("userip", remoteIp);
			ofWonders.save();
		} else {
			ofWonders.update();
		}
		return isSucceed;
	}
	
	/**
	 * 查询指定社区的分页宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void find() {
		String ofWondersId = getPara("ofWondersId");
		setOfWondersPage(ofWondersId);
		
		render("/ofWonders/ofWondersPage.html");
	}
	
	/**
	 * 分页查询宝贝
	 */
	public void findPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = OfWonders.dao.findPage(pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
		render("/admin/admin_detailofWonders.html");
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId,String render){
		String action = "/comment/setPage";
		setAttr("render", render);
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setWondersTypes(){
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
	 * 设置版块信息
	 */
	private void setWondersType(){
		Object wondersTypeId = getPara("wondersTypeId");
		WondersType wondersType = WondersType.dao.findById(wondersTypeId);
		setAttr("wondersType", wondersType);
	}
	
	/**
	 * 设置宝贝信息
	 * @param ofWondersId
	 */
	private void setOfWonders(Object ofWondersId){
		OfWonders ofWonders = OfWonders.dao.findById(ofWondersId);
		setAttr("ofWonders", ofWonders);
	}

	/**
	 * 查询修改的宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void modifyById() {
		String ofWondersId = getPara("ofWondersId");
		OfWonders ofWonders = OfWonders.dao.findById(ofWondersId);
		setAttr("ofWonders", ofWonders);
		render("/ofWonders/modifyofWonders.html");
	}

	/**
	 * 修改宝贝信息
	 */
	@Before(LoginInterceptor.class)
	public void modify() {
		OfWonders ofWonders = getModel(OfWonders.class);
		ofWonders.update();

		findByUserId();
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
	 * 设置发帖数
	 */
	private void setPublishNumber(Object wondersTypeId){
		// 当前登录人发帖数
		Object userId = getUserId();
		Long userofWondersCount = OfWonders.dao.getCountByTypeAndUser(wondersTypeId, userId);
		setAttr("userCount", userofWondersCount);
		
		//今日发帖数
		Long ofWondersTodayCount = OfWonders.dao.getTodayCountByWondersTypeId(wondersTypeId);
		setAttr("todayCount", ofWondersTodayCount);
		//昨日发帖数
		Long ofWondersYesCount = OfWonders.dao.getYesterdayCountByWondersTypeId(wondersTypeId);
		setAttr("yesCount", ofWondersYesCount);
		//总发帖数
		Long ofWondersCount = OfWonders.dao.getCountByWondersTypeId(wondersTypeId);
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
	 * 设置分页宝贝
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
	
	/**
	 * 判断当前用户是否管理员
	 * 
	 * @return 是否管理员
	 */
	private boolean isAdmin() {
		User user = getUser();
		Long userId = user.getLong("userid");
		return userId.equals(new Long(1));
	}
}
