package com.domeke.app.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.Schedule.WorkSchedule;
import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Community;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CollectionKit;
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
//		setCommunityFatList();
//		setCommunitySonList();
//		setForumCount();
//		setVentWall();
//		setPublishNumber();
//		
//		render("/community/community.html");
		WorkSchedule schedule = new WorkSchedule();
		schedule.handleProcess();
	}
	
	/**
	 * 跳转到指定主题
	 * 请求 ./community/goToCommunity?communityId=${communityId!}
	 */
	public void goToCommunity(){
		String communityId = getPara("communityId");
		if(StrKit.isBlank(communityId)){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/skipCreate";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转置顶功能
	 * 请求 ./community/setTop?communityId={communityId!}&targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderJson(2);
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/setTop";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转精华功能
	 * 请求 ./community/setEssence?communityId={communityId!}&targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderJson(2);
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/setEssence";
		forwardAction(actionKey);
	}
	
	/**
	 * 显示更多的版块
	 * 请求 ./community/showMoreCommunity
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
	 * admin管理入口
	 * 请求 ./community/goToManger
	 */
	public void goToManager() {
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_community.html");
	}
	
	/**
	 * admin管理--更新社区版块
	 * 请求 ./community/updateCommunity
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
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_communityForum.html");
	}
	
	/**
	 * admin管理--删除版块
	 * 请求 ./community/deleteSon?communityId={communityId!}&pId={pId!}
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
		render("/admin/admin_communityDetail.html");
	}
	
	/**
	 * admin管理--删除区域版块
	 * 请求 ./community/deleteFat?communityId={communityId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteFat(){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length() > 0){
			Community.dao.deleteFatAndSonByCommunityId(communityId);
		}
		
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_communityForum.html");
	}
	
	/**
	 * admin管理--跳转修改社区版块页面
	 * 请求 ./community/skipModify?communityId={communityId!}&pId={pId!}
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
		render("/admin/admin_communityUpdate.html");
	}
	
	/**
	 * 设置各子版块帖子数
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> void setForumCount(){
		List<Community> communityFatList = getAttr("communityFatList");

		Map<String,T> map = null;
		String className = null,communityId = null;
		String methodName = "getCountByCommunityPid";
		Map<String,Map<String,T>> forumCountMap = new HashMap<String,Map<String,T>>(); 
		for(Community community : communityFatList){
			className = getClassName(community);
			communityId = community.get("communityid").toString();
			
			map = (Map<String,T>)refrectMethod(className,methodName, communityId);
			forumCountMap.put(communityId, map);
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
	 */
	private Object refrectMethod(String className,String methodName,Object param ){
		if(StrKit.isBlank(className) || StrKit.isBlank(methodName)){
			return null;
		}
		try {
            Object object=Class.forName(className).newInstance();
            Method method= null;
            if(param == null){
            	method=object.getClass().getMethod(methodName);
            	return method.invoke(object);
            }else{
            	method=object.getClass().getMethod(methodName,Object.class);
            	return method.invoke(object, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 创建主题
	 */
	@Before(LoginInterceptor.class)
	public void create() {
		String communityId = getPara("communityId");
		if(StrKit.isBlank(communityId)){
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
		render("/community/community_select.html");
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
		render("/community/community_selectDetail.html");
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber() {
		// 当前登录人发帖数
		setUserCount();
		// 今日发帖数
		setTodayCount();
		// 昨日发帖数
		setYseCount();
		// 总发帖数
		setCount();
	}
	
	/**
	 * 当前登录人发帖数
	 */
	private void setUserCount(){
		Object userId = getUserId();
		if (userId == null) {
			return;
		}
		List<Community> communityFatList = getAttr("communityFatList");
		String className = null;
		String methodName = "getCountByUserId";
		Long userCount = new Long(0),count = null;
		for(Community community : communityFatList){
			className = getClassName(community);
			
			count = (Long)refrectMethod(className,methodName, userId);
			userCount = userCount + count;
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 今日发帖数
	 */
	private void setTodayCount() {
		List<Community> communityFatList = getAttr("communityFatList");
		String className = null;
		String methodName = "getTodayCount";
		Long todayCount = new Long(0);
		Object count = null;
		for (Community community : communityFatList) {
			className = getClassName(community);

			count = refrectMethod(className, methodName, null);
			if(count == null){
				continue;
			}
			todayCount = todayCount + (Long)count;
		}
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 昨日发帖数
	 */
	private void setYseCount() {
		List<Community> communityFatList = getAttr("communityFatList");
		String className = null;
		String methodName = "getYesterdayCount";
		Long yesCount = new Long(0);
		Object count = null;
		for (Community community : communityFatList) {
			className = getClassName(community);
			
			count = refrectMethod(className, methodName, null);
			if(count == null){
				continue;
			}
			yesCount = yesCount + (Long)count;
		}
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 总发帖数
	 */
	private void setCount() {
		List<Community> communityFatList = getAttr("communityFatList");
		String className = null;
		String methodName = "getCount";
		Long counts = new Long(0);
		Object count = null;
		for (Community community : communityFatList) {
			className = getClassName(community);
			
			count = refrectMethod(className, methodName, null);
			if(count == null){
				continue;
			}
			counts = counts + (Long)count;
		}
		setAttr("count", counts);
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
		render("/admin/admin_communityDetail.html");
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
		render("/admin/admin_communityForum.html");
	}
	
	private void setCommunityFat(Object pId){
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 设置子版块
	 */
	private void setCommunitySonList(){
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
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
	 * 设置更新版块信息
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
	 * 设置版块明细
	 */
	private void setCommunitySonMap(){
		List<Community> communityFatList = getAttr("communityFatList");
		List<Object> list = CollectionKit.getFieldValueList(communityFatList, "communityid", Object.class);
		Map<String,List<Community>> communitySonMapList = Community.dao.findSonMapList(list);
		setAttr("communitySonMapList", communitySonMapList);
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
	
	/**
	 * 获取登录User对象的id
	 * @return
	 */
	private Object getUserId(){
		User user = getSessionAttr("user");
		if(user == null){
			return null;
		}
		return user.get("userid");
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
