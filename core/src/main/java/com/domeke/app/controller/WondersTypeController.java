package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.OfWonders;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.model.WondersType;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "wondersType")
public class WondersTypeController extends Controller {

	/**
	 * 版块入口
	 * 请求 ./wondersType
	 */
	public void index() {
		setWondersTypePic();
		setWondersTypeFatList();
		setWondersTypeSonListCount();
		setVentWall();
		setPublishNumber();
		
		render("/wondersType/wondersType.html");
	}

	/**
	 * 跳转到选择主题页面
	 * 请求 ./wondersType/skipWondersType?wondersTypeId = ${wondersTypeId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipWondersType(){
		setWondersTypeFatList();
		render("/wondersType/wondersTypeSelect.html");
	}
	
	/**
	 * 下拉选择发布的主题
	 * 请求 ./wonders/selectSon?pId=${pId!}
	 */
	@Before(LoginInterceptor.class)
	public void selectSon(){
		String pId = getPara("pId");
		if(StrKit.notBlank(pId)){
			setWondersTypeSonListByPid(pId);
		}
		render("/wondersType/wondersTypeSelDetail.html");
	}
	
	/**
	 * 跳转到指定主题
	 * 请求 ./wondersType/goToWondersType
	 */
	public void goToWondersType(){
		String wondersTypeId = getPara("wondersTypeId");
		if(StrKit.isBlank(wondersTypeId)){
			renderNull();
			return;
		}
		String actionKey = "/ofWonders/skipCreate";
		forwardAction(actionKey);
	}
	
	/**
	 * 显示更多的版块
	 * 请求 ./wondersType/showMoreWondersType
	 */
	public void showMoreWondersType(){
		setWondersTypeSonList();
		setAttr("wondersTypeSize", 81);
		render("/wondersType/wondersType_showMore.html");
	}
	
	/**
	 * 设置热门主题
	 */
	private void setWondersTypePic() {
		List<OfWonders> ofWondersPicList = OfWonders.dao.findPic();
		setAttr("ofWondersPicList", ofWondersPicList);
	}
	
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(){
		// 当前登录人发帖数
		User user = getUser();
		if (user != null) {
			Long userId = user.get("userid");
			Long userCount = OfWonders.dao.getCountByUserId(userId);
			setAttr("userCount", userCount);
		}
		
		//今日发帖数
		Long todayCount = OfWonders.dao.getTodayCount();
		setAttr("todayCount", todayCount);
		
		//昨日发帖数
		Long yesCount = OfWonders.dao.getYesterdayCount();
		setAttr("yesCount", yesCount);
		
		//总发帖数
		Long count = OfWonders.dao.getCount();
		setAttr("count", count);
	}

	/**
	 * admin管理中对应的社区管理入口
	 */
	public void goToManager() {
		setWondersTypeFatList();
		setWondersTypeSonList();
		render("/admin/admin_wondersType.html");
	}
	
	/**
	 * 跳转到指定版块明细内容
	 */
	public void goToDetailContent(){
		String wondersTypeId = getPara("wondersTypeId");
		if(wondersTypeId == null || wondersTypeId.length()<=0){
			renderNull();
			return;
		}
		WondersType wondersType = WondersType.dao.findById(wondersTypeId);
		String actionKey = wondersType.getStr("actionkey");
		actionKey = actionKey + "/findById";
		forwardAction(actionKey);
	}
	
	/**
	 * 
	 */
	@Before(LoginInterceptor.class)
	public void saveSon(){
		Object wondersTypeId = getPara("wondersTypeId");
		WondersType wondersType = getModel(WondersType.class);
		if(wondersTypeId != null){
			updateWondersType(wondersType);
		}else{
			insertWondersType(wondersType);
		}
		
		Object pId = getPara("pId");
		WondersType wondersTypeFat = wondersType.dao.findById(pId);
		setAttr("wondersTypeFat", wondersTypeFat);
		setWondersTypeSonListByPid(pId);
		render("/admin/admin_wondersTypeFat.html");
	}
	
	/**
	 * 
	 */
	@Before(LoginInterceptor.class)
	public void saveFat(){
		Object wondersTypeId = getPara("wondersTypeId");
		WondersType wondersType = getModel(WondersType.class);
		if(wondersTypeId != null){
			updateWondersType(wondersType);
			setwondersTypeFat(wondersTypeId);
			setWondersTypeSonListByPid(wondersTypeId);
		}else{
			insertWondersType(wondersType);
			setAttr("wondersTypeFat", wondersType);
		}
		render("/admin/admin_wondersTypeFat.html");
	}
	
	private void setwondersTypeFat(Object pId){
		WondersType wondersTypeFat = WondersType.dao.findById(pId);
		setAttr("wondersTypeFat", wondersTypeFat);
	}
	
	/**
	 * 
	 * @param wondersType
	 */
	@Before(LoginInterceptor.class)
	private void updateWondersType(WondersType wondersType){
		setWondersType(wondersType);
		wondersType.update();
	}
	
	private void insertWondersType(WondersType wondersType){
		setWondersType(wondersType);
		wondersType.save();
	}
	
	/**
	 * admin管理--跳转修改社区版块页面
	 */
	@Before(LoginInterceptor.class)
	public void skipModify(){
		WondersType wondersType = null;
		String wondersTypeId = getPara("wondersTypeId");
		if(StrKit.notBlank(wondersTypeId)){
			wondersType = wondersType.dao.findById(wondersTypeId);
		}else {
			wondersType = addWondersType();
		}
		setAttr("wondersType", wondersType);
		
		setWondersTypeFatList();
		render("/admin/admin_updatewondersType.html");
	}
	
	/**
	 * admin--设置更新版块信息
	 */
	@Before(LoginInterceptor.class)
	private WondersType addWondersType() {
		WondersType wondersType = getModel(WondersType.class);
		wondersType.set("level", 1);
		Long pId = getParaToLong("pId");
		if (pId != null) {
			WondersType wondersTypeFat = wondersType.dao.findById(pId);
			Object actionKey = wondersTypeFat.get("actionkey");
			wondersType.set("actionkey", actionKey);
			wondersType.set("pid", pId);
			wondersType.set("level", 2);
		}
		return wondersType;
	}
	
	/**
	 * admin管理--更新社区版块
	 */
	@Before(LoginInterceptor.class)
	public void updatewondersType() {
		WondersType wondersType = getModel(WondersType.class);
		Object wondersTypeId = wondersType.get("wondersTypeid");
		if (wondersTypeId == null) {
			wondersType.save();
		} else {
			wondersType.update();
		}

		goToManager();
	}
	
	/**
	 * admin管理--删除版块
	 */
	@Before(LoginInterceptor.class)
	public void deleteSon(){
		String wondersTypeId = getPara("wondersTypeId");
		if(wondersTypeId != null && wondersTypeId.length() > 0){
			WondersType.dao.deleteById(wondersTypeId);
		}
		
		Object pId = getPara("pId");
		WondersType wondersType = WondersType.dao.findById(pId);
		setAttr("wondersTypeFat", wondersType);
		setWondersTypeSonListByPid(pId);
		render("/admin/admin_wondersTypeFat.html");
	}
	
	/**
	 * admin管理--删除区域版块
	 */
	@Before(LoginInterceptor.class)
	public void deleteFat(){
		String wondersTypeId = getPara("wondersTypeId");
		
		setWondersTypeFatList();
		setWondersTypeSonList();
		render("/admin/admin_detailwondersType.html");
	}
	
	/**
	 * 设置版块
	 * @param wondersTypeId
	 */
	private void setWondersType(WondersType wondersType){
		String wondersTypeId = getPara("wondersTypeId");
		if(wondersTypeId != null && wondersTypeId.length()>0){
			wondersType.set("wondersTypeid", wondersTypeId);
		}
		
		String title = getPara("title");
		if(title != null && title.length()>0){
			wondersType.set("title", title);
		}
		
		String content = getPara("content");
		if(content != null && content.length()>0){
			wondersType.set("content", content);
		}
		
		String actionkey = getPara("actionkey");
		if(actionkey != null && actionkey.length()>0){
			wondersType.set("actionkey", actionkey);
		}
		
		String level = getPara("level");
		if(level != null && level.length()>0){
			wondersType.set("level", level);
		}
		
		String position = getPara("position");
		if(position != null && position.length()>0){
			wondersType.set("position", position);
		}
		
		String pId = getPara("pId");
		if(pId != null && pId.length()>0){
			wondersType.set("pid", pId);
		}
	}
	
	/**
	 * 设置父版块
	 */
	private void setWondersTypeFatList(){
		List<WondersType> wondersTypeFatList = WondersType.dao.findFatList();
		setAttr("wondersTypeFatList", wondersTypeFatList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setWondersTypeSonList(){
		List<WondersType> wondersTypeSonList = WondersType.dao.findSonList();
		setAttr("wondersTypeSonList", wondersTypeSonList);
	}
	
	/**
	 * 设置子版块及主题数
	 */
	private void setWondersTypeSonListCount(){
		List<WondersType> wondersTypeSonList = WondersType.dao.findSonListCount();
		setAttr("wondersTypeSonList", wondersTypeSonList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setWondersTypeSonListByPid(Object pId){
		List<WondersType> wondersTypeSonList = WondersType.dao.findSonListByPid(pId);
		setAttr("wondersTypeSonList", wondersTypeSonList);
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
