package com.domeke.app.controller;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {

	/**
	 * 版块入口
	 * 请求 ./community
	 */
	public void index() {
		setCommunityFatList();
		setCommunitySonList();
		setForumCount();
		setVentWall();
		setPublishNumber();
		
		render("/community/community.html");
	}
	
	
	/**
	 * 跳转到指定主题
	 * 请求 ./community/goToCommunity?communityId=${communityId!}
	 */
	public void goToCommunity(){
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/skipCreate";
		forwardAction(actionKey);
	}
	
	/**
	 * 设置各子版块帖子数
	 * @param <T>
	 */
	private <T> void setForumCount(){
		List<Community> communityFatList = getAttr("communityFatList");

		List<T> list = null;
		BigInteger communityId = null;
		String className = null;
		String methodName = "getCountByCommunityPid";
		Map<Object,List<T>> forumCountMap = new HashMap<Object,List<T>>(); 
		for(Community community : communityFatList){
			className = getClassName(community);
			communityId = community.getBigInteger("communityid");
			
			list = refrectMethod(className,methodName, communityId);
			forumCountMap.put(communityId, list);
		}
		setAttr("forumCountMap", forumCountMap);
	}
	
	/**
	 * 获取Action 对应的className
	 * @param community
	 * @return
	 */
	private String getClassName(Community community){
		String actionKey = community.get("actionkey");
		if(StrKit.isBlank(actionKey)){
			return null;
		}
		int sep = actionKey.indexOf("/");
		if(sep >= 0){
			actionKey = actionKey.split("/")[1];
		}
		actionKey = actionKey.replaceFirst(actionKey.substring(0, 1), actionKey.substring(0, 1).toUpperCase());
		String className = "com.domeke.app.model." + actionKey;
		return className;
	}
	
	/**
	 * 反射加载指定方法
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> refrectMethod(String className,String methodName,Object param ){
		List<T> list = Lists.newArrayList();
		if(StrKit.isBlank(className) || StrKit.isBlank(methodName)){
			return list;
		}
		try {
            Object object=Class.forName(className).newInstance();
            Method method=object.getClass().getMethod(methodName, Object.class);
            list = (List<T>)method.invoke(object, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return list; 
	}
	
	/**
	 * 创建主题
	 */
	@Before(LoginInterceptor.class)
	public void create() {
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/create";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转到选择主题页面
	 */
	@Before(LoginInterceptor.class)
	public void skipCommunity(){
		setCommunityFatList();
		render("/community/selectCommunity.html");
	}
	
	/**
	 * 下拉选择发布的主题
	 */
	@Before(LoginInterceptor.class)
	public void selectSon(){
		String pId = getPara("pId");
		if(pId != null && pId.length()>0){
			setCommunitySonListByPid(pId);
		}
		render("/community/selectCommunitySon.html");
	}
	
	/**
	 * 显示更多的版块
	 */
	public void showMoreCommunity(){
		setCommunitySonList();
		setAttr("communitySize", 81);
		render("/community/community_showMore.html");
	}
	
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(){
		//当前登录人发帖数
		User user = getUser();
		if (user != null){
			Long userId = user.get("userid");
			Long userPostCount = Post.dao.getCountByUserId(userId);
			Long userActivityCount = Activity.dao.getCountByUserId(userId);
			Long userCount = userPostCount + userActivityCount;
			setAttr("userCount", userCount);
		}
		
		//今日发帖数
		Long postTodayCount = Post.dao.getTodayCount();
		Long activityTodayCount = Activity.dao.getTodayCount();
		Long todayCount = postTodayCount + activityTodayCount;
		setAttr("todayCount", todayCount);
		
		//昨日发帖数
		Long postYesCount = Post.dao.getYesterdayCount();
		Long activityYesCount = Activity.dao.getYesterdayCount();
		Long yesCount = postYesCount + activityYesCount;
		setAttr("yesCount", yesCount);
		
		//总发帖数
		Long postCount = Post.dao.getCount();
		Long activityCount = Activity.dao.getCount();
		Long count = postCount + activityCount;
		
		setAttr("count", count);
	}

	/**
	 * 跳转指定版块
	 */
	public void goToOrderForum() {
		String communityId = getPara("communityId");
		Community.dao.updateTimes(communityId);
		
		Community community = Community.dao.findById(communityId);
		String actionKey = community.get("actionkey");
		forwardAction(actionKey);
	}

	/**
	 * admin管理中对应的社区管理入口
	 */
	public void goToManager() {
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_community.html");
	}
	
	/**
	 * 跳转到指定版块明细内容
	 */
	public void goToDetailContent(){
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/findById";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转置顶功能
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderJson("false");
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/setTop";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转精华功能
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderJson("false");
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/setEssence";
		forwardAction(actionKey);
	}
	
	/**
	 * 
	 */
	@Before(LoginInterceptor.class)
	public void saveSon(){
		Object communityId = getPara("communityId");
		Community community = getModel(Community.class);
		if(communityId != null){
			updateCommuntiy(community);
		}else{
			insertCommunity(community);
		}
		
		Object pId = getPara("pId");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
	}
	
	/**
	 * 
	 */
	@Before(LoginInterceptor.class)
	public void saveFat(){
		Object communityId = getPara("communityId");
		Community community = getModel(Community.class);
		if(communityId != null){
			updateCommuntiy(community);
			setCommunityFat(communityId);
			setCommunitySonListByPid(communityId);
		}else{
			insertCommunity(community);
			setAttr("communityFat", community);
		}
		render("/admin/admin_communityFat.html");
	}
	
	private void setCommunityFat(Object pId){
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 
	 * @param community
	 */
	@Before(LoginInterceptor.class)
	private void updateCommuntiy(Community community){
		setCommunity(community);
		community.update();
	}
	
	private void insertCommunity(Community community){
		setCommunity(community);
		community.save();
	}
	
	/**
	 * admin管理--跳转修改社区版块页面
	 */
	@Before(LoginInterceptor.class)
	public void skipModify(){
		Community community = null;
		String communityId = getPara("communityId");
		if(StrKit.notBlank(communityId)){
			community = Community.dao.findById(communityId);
		}else {
			community = addCommunity();
		}
		setAttr("community", community);
		
		setCommunityFatList();
		render("/admin/admin_updateCommunity.html");
	}
	
	/**
	 * admin--设置更新版块信息
	 */
	@Before(LoginInterceptor.class)
	private Community addCommunity() {
		Community community = getModel(Community.class);
		community.set("level", 1);
		Long pId = getParaToLong("pId");
		if (pId != null) {
			Community communityFat = Community.dao.findById(pId);
			Object actionKey = communityFat.get("actionkey");
			community.set("actionkey", actionKey);
			community.set("pid", pId);
			community.set("level", 2);
		}
		return community;
	}
	
	/**
	 * admin管理--更新社区版块
	 */
	@Before(LoginInterceptor.class)
	public void updateCommunity() {
		Community community = getModel(Community.class);
		Object communityId = community.get("communityid");
		if (communityId == null) {
			community.save();
		} else {
			community.update();
		}

		goToManager();
	}
	
	/**
	 * admin管理--删除版块
	 */
	@Before(LoginInterceptor.class)
	public void deleteSon(){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length() > 0){
			Community.dao.deleteById(communityId);
		}
		
		Object pId = getPara("pId");
		Community community = Community.dao.findById(pId);
		setAttr("communityFat", community);
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
	}
	
	/**
	 * admin管理--删除区域版块
	 */
	@Before(LoginInterceptor.class)
	public void deleteFat(){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length() > 0){
			Community.dao.deleteFatAndSonByCommunityId(communityId);
		}
		
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_detailCommunity.html");
	}
	
	/**
	 * 设置版块
	 * @param communityId
	 */
	private void setCommunity(Community community){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length()>0){
			community.set("communityid", communityId);
		}
		
		String title = getPara("title");
		if(title != null && title.length()>0){
			community.set("title", title);
		}
		
		String content = getPara("content");
		if(content != null && content.length()>0){
			community.set("content", content);
		}
		
		String actionkey = getPara("actionkey");
		if(actionkey != null && actionkey.length()>0){
			community.set("actionkey", actionkey);
		}
		
		String level = getPara("level");
		if(level != null && level.length()>0){
			community.set("level", level);
		}
		
		String position = getPara("position");
		if(position != null && position.length()>0){
			community.set("position", position);
		}
		
		String pId = getPara("pId");
		if(pId != null && pId.length()>0){
			community.set("pid", pId);
		}
	}
	
	/**
	 * 设置父版块
	 */
	private void setCommunityFatList(){
		List<Community> communityFatList = Community.dao.findFatList();
		setAttr("communityFatList", communityFatList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setCommunitySonList(){
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setCommunitySonListByPid(Object pId){
		List<Community> communitySonList = Community.dao.findSonListByPid(pId);
		setAttr("communitySonList", communitySonList);
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
