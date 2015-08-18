package com.domeke.app.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Community;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.FileKit;
import com.domeke.app.utils.MapKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {
	
	/** 统计各版块总帖子数方法名 */
	private static final String FORUMPIDMETHODNAME = "getCountByCommunityPid";
	
	/** 统计版块用户总帖子数方法名 */
	private static final String FORUMUSERMETHODNAME = "getCountOfUser";
	
	/** 统计版块今日总帖子数方法名 */
	private static final String FORUMTODAYMETHODNAME = "getCountOfToday";
	
	/** 统计版块昨日总帖子数方法名 */
	private static final String FORUMYESTERDAYMETHODNAME = "getCountOfYes";
	
	/** 统计版块总帖子数方法名 */
	private static final String FORUMCOUNTMETHODNAME = "getCount";
	
	/** 更新主题状态方法名 */
	private static final String UPDATESTATUSBYFORUMCLASSIFY = "updateStatusByForumClassify";
	
	/** 更新主题状态方法名 */
	private static final String UPDATESTATUSBYFORUM = "updateStatusByForum";
	
	/** 删除主题状态方法名 */
	private static final String DELETEBYFORUMCLASSIFY = "deleteByForumClassify";
	
	/** 删除主题状态方法名 */
	private static final String DELETEBYFORUM = "deleteByForum";
	
	/** 跳转版块首页指令 */
	private static final String HOME = "/home";
	
	/** 主题申请入口方法指令 */
	private static final String SKIPCREATE = "/skipCreate";
	
	/** 跳转主题指令 */
	private static final String SKIPTHEME = "/skipTheme";
	
	/** 主题保存指令 */
	private static final String THEMESAVE = "/save";
	
	/** 主题设置置顶指令 */
	private static final String SETTOP = "/setTop";
	
	/** 主题设置精华指令 */
	private static final String SETESSENCE = "/setEssence";
	
	/** 社区id（主键） */
	private static final String COMMUNITYID = "communityid";
	
	/** 各版块入口指令的包名 */
	private static final String PACKAGENAME = "com.domeke.app.model";
	
	/** 各版块入口指令  */
	private static final String ACTIONKEY = "actionkey";
	
	/** 版块父级id  */
	private static final String PID = "pid";
	
	/** 版块等级  */
	private static final String LEVEL = "level";
	
	/** 显示隐藏状态 */
	private static final String STATUS = "status";
	
	/** 用户id  */
	private static final String USERID = "userid";

	/**
	 * 版块入口
	 * 请求 ./community
	 */
	public void index() {
		setBodyInfo();
		setVentWall();
		render("/community/community.html");
	}
	
	/**
	 * 显示更多的版块
	 * 请求 ./community/showMoreForum
	 */
	public void showMoreForum(){
		List<Community> forumList = handleForumList("10");
		setAttr("limitSize", forumList.size());
		render("/community/community_aside.html");
	}
	
	/**
	 * 跳转到发布主题页面
	 * 请求 点击发布主题按钮 
	 * ./community/skipPublishForum?communityid={communityid!}
	 */
	@Before(LoginInterceptor.class)
	public void skipPublishTheme(){
		handleForumClassifyList("10");
		handleForumList("10");
		setVentWall();
		
		String communityid = getPara(COMMUNITYID);
		if(StrKit.notBlank(communityid)){
			handleCommunityForum(communityid);
		}
		render("/community/community_publishTheme.html");
	}
	
	/**
	 * 跳转到主题申请页面
	 * 请求 选择版块信息
	 * ./community/showTheme?communityid=${communityid!}
	 */
	public void showTheme(){
		String communityid = getPara(COMMUNITYID);
		if(StrKit.isBlank(communityid)){
			renderNull();
			return;
		}
		Community community = handleCommunityForum(communityid);
		String actionKey = community.getStr(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, SKIPCREATE);
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转指定版块
	 * <p>
	 * 社区首页点击指定版块
	 * <p> 
	 * 请求 ./community/skipForum?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 * 
	 */
	public void skipForum() {
		String communityId = getPara(COMMUNITYID);
		Community.dao.updateTimes(communityId);
		
		setAsideInfo();
		Community communityForum = handleCommunityInfo(communityId);
		String actionKey = communityForum.get(ACTIONKEY);
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转指定版块首页
	 * <p>
	 * 菜单栏点击，跳转版块首页
	 * <p> 
	 * 请求 ./community/skipForumClassify?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 * 
	 */
	public void skipForumClassify() {
		String communityId = getPara(COMMUNITYID);
		Community.dao.updateTimes(communityId);
		
		setAsideInfo();
		Community communityForum = handleCommunityInfo(communityId);
		String actionKey = communityForum.get(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, HOME);
		forwardAction(actionKey);
	}
	
	/**
	 * 提交发布的主题
	 * <p>
	 * 点击发布主题按钮
	 * <p>
	 * 请求 ./community/submitTheme?communityid=${communityid!}
	 */
	@Before(LoginInterceptor.class)
	public void submitTheme() {
		String communityId = getPara(COMMUNITYID);
		if(StrKit.isBlank(communityId)){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, THEMESAVE);
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转指定主题
	 * <p>
	 * 点击主题列表中的主题信息
	 * <p>
	 * 请求 ./community/skipTheme?communityid=${communityid!}&targetid={targetid!}$pageNumber=${pageNumber!}$pageSize=${pageSize!}
	 */
	public void skipTheme() {
		String communityId = getPara(COMMUNITYID);
		setAsideInfo();
		Community communityForum = handleCommunityInfo(communityId);
		String actionKey = communityForum.getStr(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, SKIPTHEME);
		forwardAction(actionKey);
	}
	
	/**
	 * 主题页面设置置顶
	 * <p>
	 * 请求 ./community/setTop?communityid=${communityid!}&targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String communityId = getPara(COMMUNITYID);
		if(StrKit.isBlank(communityId)){
			renderJson(2);
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, SETTOP);
		forwardAction(actionKey);
	}
	
	/**
	 * 主题页面设置精华
	 * 请求 ./community/setEssence?communityid=${communityid!}&targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		if(!isAdmin()){
			renderJson(1);
			return;
		}
		String communityId = getPara(COMMUNITYID);
		if(StrKit.isBlank(communityId)){
			renderJson(2);
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr(ACTIONKEY);
		actionKey = FileKit.getDirectory(actionKey, SETESSENCE);
		forwardAction(actionKey);
	}
	
	/**
	 * admin管理入口
	 * 请求 ./community/goToManger
	 */
	@Before(LoginInterceptor.class)
	public void goToManager() {
		setBodyInfoOfAdmin();
		render("/admin/admin_community.html");
	}
	
	/**
	 * admin管理--跳转添加社区版块页面
	 * 请求 ./community/skipModify?communityId={communityId!}&pId={pId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipAddOfAdmin(){
		Community community = getModel(Community.class);
		setAttr("community", community);
		handleForumClassifyList();
		render("/admin/admin_communityUpdate.html");
	}
	
	/**
	 * admin管理--跳转修改社区版块页面
	 * 请求 ./community/skipModifyOfAdmin?communityid={communityid!}
	 */
	@Before(LoginInterceptor.class)
	public void skipModifyOfAdmin(){
		String communityid = getPara(COMMUNITYID);
		handleCommunity(communityid);
		handleForumClassifyList();
		render("/admin/admin_communityUpdate.html");
	}
	
	/**
	 * admin管理--新建社区分类或版块
	 * 请求 ./community/updateCommunity
	 */
	@Before(LoginInterceptor.class)
	public void save() {
		Community community = getModel(Community.class);
		try{
			community.save();
			renderJson(true);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * admin管理--更新社区分类或版块
	 * 请求 ./community/updateCommunity
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		Community community = getModel(Community.class);
		try{
			community.update();
			int level = community.getInt(LEVEL);
			if(level == 1){
				updateByForumClassify(community);
				updateCommentByForumClassify(community);
			}else{
				updateByForum(community);
			}
			renderJson(true);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * admin管理--删除社区分类或版块
	 * 请求 ./community/delete?communityid=${communityid!}
	 */
	@Before(LoginInterceptor.class)
	public void delete() {
		try{
			Object communityid = getPara(COMMUNITYID);
			Community community = Community.dao.findById(communityid);
			int level = community.getInt(LEVEL);
			if(level == 1){
				deleteByForumClassify(community);
			}else{
				deleteByForum(community);
			}
			renderJson(true);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * 设置各版块总发帖数
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	private <T> void setEveryForumCount(List<Community> forumClassifyList){
		Map<String,T> map = null;
		String className = null,communityId = null;
		
		Map<String,Map<String,T>> forumCountMap = new HashMap<String,Map<String,T>>(); 
		for(Community community : forumClassifyList){
			className = getClassAllName(community);
			if(StrKit.isBlank(className)){
				continue;
			}
			communityId = community.get(COMMUNITYID).toString();
			
			map = (Map<String,T>)refrectMethod(className,FORUMPIDMETHODNAME, communityId);
			if(MapKit.isBlank(map)){
				continue;
			}
			forumCountMap.put(communityId, map);
		}
		setAttr("forumCountMap", forumCountMap);
	}
	
	/**
	 * 获取Action 对应的className
	 * @param community
	 * @return
	 */
	private String getClassAllName(Community community){
		String actionKey = community.get(ACTIONKEY);
		if(StrKit.isBlank(actionKey)){
			return null;
		}
		actionKey = getClassName(actionKey);
		actionKey = actionKey.replaceFirst(actionKey.substring(0, 1), actionKey.substring(0, 1).toUpperCase());
		String className = PACKAGENAME + "." + actionKey;
		return className;
	}
	
	/**
	 * 获取actionKey 对应的className
	 * @return
	 */
	private String getClassName(String actionKey){
		int sep = actionKey.indexOf("/");
		if(sep >= 0){
			actionKey = actionKey.split("/")[1];
		}
		return actionKey;
	}
	
	/**
	 * 反射加载指定方法
	 * @param 
	 */
	private Object refrectMethod(String className,String methodName,Object...param){
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
            	Class<?>[] classes = new Class[param.length];
            	for(int i=0;i<param.length;i++){
            		classes[i] = Object.class;
            	}
            	method=object.getClass().getMethod(methodName, classes);
            	return method.invoke(object, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * 设置版块指定情况下的总发帖数
	 * @param forumClassifyList 版块分类信息
	 */
	private void setAllForumCount(List<Community> forumClassifyList) {
		// 当前登录人发帖数
		setForumCountOfUser(forumClassifyList);
		// 今日发帖数
		setForumCountOfToday(forumClassifyList);
		// 昨日发帖数
		setForumCountOfYes(forumClassifyList);
		// 总发帖数
		setForumCount(forumClassifyList);
	}
	
	/**
	 * 设置当前登录人发帖总数
	 * @param forumClassifyList 版块分类信息
	 */
	private void setForumCountOfUser(List<Community> forumClassifyList){
		Object userId = getUserId();
		if (userId == null) {
			return;
		}
		String className = null;
		Long userCount = new Long(0),count = null;
		for(Community community : forumClassifyList){
			className = getClassAllName(community);
			if(StrKit.isBlank(className)){
				continue;
			}
			count = (Long)refrectMethod(className, FORUMUSERMETHODNAME,userId);
			if(count == null){
				continue;
			}
			userCount = userCount + count;
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 设置今日发帖总数
	 * @param forumClassifyList 版块分类信息
	 */
	private void setForumCountOfToday(List<Community> forumClassifyList) {
		String className = null;
		Long todayCount = new Long(0);
		Object count = null;
		for (Community community : forumClassifyList) {
			className = getClassAllName(community);
			if(StrKit.isBlank(className)){
				continue;
			}
			count = refrectMethod(className, FORUMTODAYMETHODNAME);
			if(count == null){
				continue;
			}
			todayCount = todayCount + (Long)count;
		}
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 设置昨日发帖总数
	 * @param forumClassifyList 版块分类信息
	 */
	private void setForumCountOfYes(List<Community> forumClassifyList) {
		String className = null;
		Long yesCount = new Long(0);
		Object count = null;
		for (Community community : forumClassifyList) {
			className = getClassAllName(community);
			if(StrKit.isBlank(className)){
				continue;
			}
			count = refrectMethod(className, FORUMYESTERDAYMETHODNAME);
			if(count == null){
				continue;
			}
			yesCount = yesCount + (Long)count;
		}
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 设置总发帖数
	 * @param forumClassifyList 版块分类信息
	 */
	private void setForumCount(List<Community> forumClassifyList) {
		String className = null;
		Long counts = new Long(0);
		Object count = null;
		for (Community community : forumClassifyList) {
			className = getClassAllName(community);
			if(StrKit.isBlank(className)){
				continue;
			}
			count = refrectMethod(className, FORUMCOUNTMETHODNAME);
			if(count == null){
				continue;
			}
			counts = counts + (Long)count;
		}
		setAttr("count", counts);
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
	 * 
	 * @param community
	 */
	@Before(LoginInterceptor.class)
	private void updateCommuntiy(Community community){
		setCommunityData(community);
		community.update();
	}
	
	private void insertCommunity(Community community){
		setCommunityData(community);
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
	private void setCommunityData(Community community){
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
	 * 设置子版块
	 */
	private void setCommunitySonListByPid(Object pId){
		List<Community> communitySonList = Community.dao.findSonListByPid(pId);
		setAttr("communitySonList", communitySonList);
	}
	
	
	/**
	 * 根据版块分类删除版块以及主题信息
	 * @param community
	 */
	private void deleteByForumClassify(Community community) {
		Object communityid = community.get(COMMUNITYID);
		Community.dao.deleteById(communityid);
		
		String className = getClassAllName(community);
		if(StrKit.isBlank(className)){
			return;
		}
		refrectMethod(className, DELETEBYFORUMCLASSIFY,communityid);
	}
	
	
	/**
	 * 根据版块删除主题信息
	 * @param community
	 */
	private void deleteByForum(Community community) {
		Object communityid = community.get(COMMUNITYID);
		Community.dao.deleteById(communityid);
		
		String className = getClassAllName(community);
		if(StrKit.isBlank(className)){
			return;
		}
		refrectMethod(className, DELETEBYFORUM,communityid);
	}
	
	/**
	 * 根据版块分类更新版块以及主题信息
	 * @param community
	 * @param communityid
	 */
	private void updateByForumClassify(Community community) {
		Object communityid = community.get(COMMUNITYID);
		Object actionkey = community.get(ACTIONKEY);
		Object status = community.get(STATUS);
		Community.dao.updateForum(communityid, actionkey, status);
		
		String className = getClassAllName(community);
		if(StrKit.isBlank(className)){
			return;
		}
		refrectMethod(className, UPDATESTATUSBYFORUMCLASSIFY,communityid,status);
	}
	
	/**
	 * 根据版块分类更新回复信息
	 * @param community
	 * @param communityid
	 */
	private void updateCommentByForumClassify(Community community) {
		Object communityid = community.get(COMMUNITYID);
		Object actionkey = community.get(ACTIONKEY);
		Object status = community.get(STATUS);
		Community.dao.updateForum(communityid, actionkey, status);
		
		String className = getClassAllName(community);
		if(StrKit.isBlank(className)){
			return;
		}
		refrectMethod(className, UPDATESTATUSBYFORUMCLASSIFY,communityid,status);
	}
	
	/**
	 * 根据版块更新主题信息
	 * @param community
	 * @param communityid
	 */
	private void updateByForum(Community community) {
		Object communityid = community.get(COMMUNITYID);
		Object status = community.get(STATUS);
		String className = getClassAllName(community);
		if(StrKit.isBlank(className)){
			return;
		}
		refrectMethod(className, UPDATESTATUSBYFORUM,communityid,status);
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}
	
	/**
	 * 设置admin管理的主体信息
	 */
	private void setBodyInfoOfAdmin(){
		handleForumClassifyList();
		handleForumList();
	}
	
	/**
	 * 设置版块及版块分类集合信息
	 */
	private void setBodyInfo(){
		List<Community> forumClassifyList = handleForumClassifyList("10");
		handleForumList("10");
		setEveryForumCount(forumClassifyList);
		setAllForumCount(forumClassifyList);
	}
	
	/**
	 * 设置旁边信息
	 * 
	 */
	private void setAsideInfo(){
		handleForumList("10");
		setVentWall();
	}
	
	/**
	 * 设置社区信息
	 * @param communityid 社区id
	 * @return
	 */
	private Community handleCommunity(Object communityid){
		Community community = Community.dao.findCommunityInfo(communityid);
		setAttr("community", community);
		return community;
	}
	
	/**
	 * 设置版块分类集合信息
	 * @param status 显示状态
	 * @return 版块分类信息
	 */
	private List<Community> handleForumClassifyList(Object status){
		List<Community> forumClassifyList = Community.dao.findForumClassifyList(status);
		setAttr("forumClassifyList", forumClassifyList);
		return forumClassifyList;
	}
	
	/**
	 * 设置版块分类集合信息
	 * @return 版块分类信息
	 */
	private List<Community> handleForumClassifyList(){
		List<Community> forumClassifyList = Community.dao.findForumClassifyList();
		setAttr("forumClassifyList", forumClassifyList);
		return forumClassifyList;
	}
	
	/**
	 * 设置版块集合信息
	 * @param status 显示状态
	 * @return 版块信息
	 */
	private List<Community> handleForumList(Object status){
		List<Community> forumList = Community.dao.findForumList(status);
		setAttr("forumList", forumList);
		return forumList;
	}
	
	/**
	 * 设置版块集合信息
	 * @return 版块信息
	 */
	private List<Community> handleForumList(){
		List<Community> forumList = Community.dao.findForumList();
		setAttr("forumList", forumList);
		return forumList;
	}
	
	/**
	 * 设置版块及版块分类信息
	 * @param communityid 版块id
	 */
	private Community handleCommunityInfo(Object communityid){
		Community communityForum = handleCommunityForum(communityid);
		Object pid = communityForum.get(PID);
		handleCommunityForumClassify(pid);
		return communityForum;
	}
	
	/**
	 * 设置版块信息
	 * @param communityid 版块id
	 * @return 版块信息
	 */
	private Community handleCommunityForum(Object communityid){
		Community community = Community.dao.findById(communityid);
		setAttr("communityForum", community);
		return community;
	}
	
	/**
	 * 设置版块分类信息
	 * @param communityid 版块分类id
	 * @return 版块分类信息
	 */
	private Community handleCommunityForumClassify(Object communityid){
		Community community = Community.dao.findById(communityid);
		setAttr("communityForumClassify", community);
		return community;
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
		User user = getUser();
		if(user == null){
			return null;
		}
		return user.get(USERID);
	}
	
	/**
	 * 判断当前用户是否管理员
	 * 
	 * @return 是否管理员
	 */
	private boolean isAdmin() {
		User user = getUser();
		Long userId = user.getLong(USERID);
		return userId.equals(new Long(1));
	}
}
