package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "post")
public class PostController extends Controller {
	
	/** 社区id（主键） */
	private static final String COMMUNITYID = "communityid";
	
	/** 社区id（主键） */
	private static final String PID = "pid";
	
	/** 目标id(主键) */
	private static final String TARGETID = "targetid";
	
	/** 帖子id(主键) */
	private static final String POSTID = "postid";
	
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
	private static String IDTYPEVALUE = "10";

	/**
	 * 根据版块信息分页查询帖子信息
	 * <p>
	 * 1、入口
	 * <p>
	 * 2、帖子列表中点击分页相关按钮
	 * <p>
	 * 请求 ./community/skipForum?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void index() {
		String communityId = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForum(communityId,"10", pageNumber, pageSize);
		render("/community/community_post.html");
	}
	
	/**
	 * 根据版块分类信息分页查询帖子信息
	 * <p>
	 * 版块首页
	 * <P>
	 * 请求 ./community/skipForumClassify?communityid=${communityid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void home(){
		String communityId = getPara(COMMUNITYID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfoByForumClassify(communityId,"10", pageNumber, pageSize);
		render("/community/community_post.html");
	}
	
	/**
	 * 创建帖子
	 * 请求 ./post/save
	 */
	@Before(LoginInterceptor.class)
	public void save() {
		try {
			Post post = getModel(Post.class);
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();

			post.set("userid", userId);
			post.set("userip", remoteIp);
			post.save();
			renderJson(true);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * 跳转帖子申请
	 * 请求 ./post/skipCreate?communityId={communityId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object post = Post.dao.findHasPublish(communityId, userId);
		if(post != null){
			renderJson(1);
			return;
		}
		
		keepPara("communityId");
		render("/community/community_postCreate.html");
	}
	
	/**
	 * 跳转主题
	 * <p>
	 * 主题列表点击主题信息事件
	 * 请求 community/skipTheme?communityid=${communityid!}&targetid=${targetid!}&pageNumber=${pageNumber!}&pageSize=${pageSize!}
	 */
	public void skipTheme() {
		String postId = getPara(TARGETID);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		
		Post.dao.updateTimes(postId);
		setBodyInfoOfTheme(postId, pageNumber, pageSize);
		
		String render = "/community/community_postDetail.html";
		forwardComment(postId,render);
	}
	
	/**
	 * 置顶功能
	 * 请求 ./community/setTop?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String postId = getPara(TARGETID);
		if(StrKit.isBlank(postId)){
			renderJson(2);
			return;
		}
		Post post = getModel(Post.class);
		post.set(POSTID, postId);
		post.set(TOP, 1);
		post.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./community/setEssence?targetid=${targetid!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String postId = getPara(TARGETID);
		if(StrKit.isBlank(postId)){
			renderJson(2);
			return;
		}
		Post post = getModel(Post.class);
		post.set(POSTID, postId);
		post.set(ESSENCE, 1);
		post.update();
		renderJson(3);
	}
	
	/**
	 * admin管理--入口
	 * 请求 ./post/goToManager?pageNumber=${pageNumber!}&pageSize={pageSize!}
	 */
	@Before(LoginInterceptor.class)
	public void goToManager() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_post.html");
	}
	
	/**
	 * admin管理--跳转修改主题
	 * 请求 ./post/skipModifyOfAdmin?postid=${postid!}
	 */
	@Before(LoginInterceptor.class)
	public void skipModifyOfAdmin() {
		String postid = getPara(POSTID);
		Post post = Post.dao.findById(postid);
		setAttr("post", post);
		
		Object communityid = post.get(COMMUNITYID);
		handleCommunity(communityid);
		render("/admin/admin_postUpdate.html");
	}
	
	/**
	 * admin管理--修改主题
	 * 请求 ./post/update
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		try{
			Post post = getModel(Post.class);
			post.update();
			renderJson(true);
		}catch(Exception e){
			e.printStackTrace();
			renderJson(false);
		}
	}
	
	/**
	 * admin管理--删除指定主题
	 * 请求 ./post/deleteById?postId=${postId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		String postId = getPara("postId");
		Post.dao.deleteById(postId);
		
		Comment.dao.deleteByTheme(postId, IDTYPEVALUE);
		setBodyInfo(pageNumber, pageSize);
		render("/admin/admin_postPage.html");
	}
	
	/**
	 * 个人会用中心（我发布的帖子）--入口
	 * 请求  ./post/personalHome
	 */
	@Before(LoginInterceptor.class)
	public void personalHome(){
		setPageByUser();
		render("/personal/personal_post.html");
	}

	/**
	 * 个人会员中心--查询用户发布的帖子
	 * 请求  ./post/findByUserId
	 */
	@Before(LoginInterceptor.class)
	public void findByUserId() {
		setPageByUser();
		render("/personal/personal_postPage.html");
	}
	
	/**
	 * 个人会员中心--跳转修改主题
	 * 请求 ./post/skipUpdateForPersonal?postId=${postId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdateForPersonal() {
		Post post = null;
		String postId = getPara("postId");
		if(StrKit.notBlank(postId)){
			post = Post.dao.findInfoById(postId);
		}
		setAttr("post", post);
		render("/personal/personal_postUpdate.html");
	}
	
	/**
	 * 个人会员中心--修改主题
	 * 请求 ./post/updateForPersonal
	 */
	@Before(LoginInterceptor.class)
	public void updateForPersonal() {
		try{
			Post post = getModel(Post.class);
			post.update();
		}catch(Exception e){
			e.printStackTrace();
		}
		personalHome();
	}
	
	/**
	 * 个人会员中心--跳转主题明细
	 * 请求 ./post/findByIdForPersonal?postId={postId!}
	 */
	public void skipContain() {
		String postId = getPara("postId");
		Post.dao.updateTimes(postId);
		
		setPost(postId);
		setCommunitys();
		setCommunityList();
		setVentWall();
		
		String render = "/community/community_postContain.html";
		forwardComment(postId,render);
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
	 * 获取版块Id
	 */
	private Object getCommunityId(){
		Object communityId = getPara("communityId");
		if(communityId == null || "".equals(communityId.toString().trim())){
			Post post = getAttr("post");
			if(post != null){
				communityId = post.get("communityid");
			}
		}
		return communityId;
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}

	/**
	 * 删除回复信息
	 */
	@Before(LoginInterceptor.class)
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		skipTheme();
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 * @param targetid 帖子主键
	 * @param render 跳转页面
	 */
	private void forwardComment(Object targetid,Object render){
		setAttr(RENDER, render);
		setAttr(TARGETID, targetid);
		setAttr(IDTYPE, IDTYPEVALUE);
		forwardAction(COMMENTACTION);
	}
	
	/**
	 * 设置帖子主题信息
	 */
	private void setBodyInfoOfTheme(String postid,int pageNumber,int pageSize){
		setPost(postid);
	}
	
	/**
	 * 设置帖子信息
	 * @param postId
	 */
	private void setPost(Object postId){
		Post post = Post.dao.findInfoById(postId);
		setAttr("post", post);
	}
	
	/**
	 * 根据用户设置分页
	 */
	private void setPageByUser() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Object userId = getUserId();
		Page<Post> postPage = Post.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("postPage", postPage);
	}
	
	/**
	 * 设置社区分类及版块信息
	 * @param communityid 版块id
	 */
	private void handleCommunity(Object communityid){
		Community community = Community.dao.findCommunityInfo(communityid);
		setAttr("community", community);
		
		Object pid = community.get(PID);
		List<Community> forumList = Community.dao.findForumList(pid, "10");
		setAttr("forumList", forumList);
	}
	
	/**
	 * admin管理--设置帖子信息
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfo(int pageNumber,int pageSize) {
		Page<Post> postPage = Post.dao.findPageAll(pageNumber, pageSize);
		setAttr("postPage", postPage);
	}
	
	/**
	 * 根据版块信息设置帖子信息
	 * @param communityId 版块信息
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForum(Object communityId,Object status,int pageNumber,int pageSize) {
		setPageByForum(communityId, status, pageNumber, pageSize);
		setPublishInfoByForum(communityId);
	}
	
	/**
	 * 根据版块分类信息设置帖子信息
	 * @param communityId 版块分类id
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setBodyInfoByForumClassify(Object communityId,Object status,int pageNumber,int pageSize) {
		setPageByForumClassify(communityId, status, pageNumber, pageSize);
		setPublishInfoByForumClassify(communityId);
	}
	
	/**
	 * 根据版块信息设置发帖数
	 * @param communityid 版块id
	 */
	private void setPublishInfoByForum(Object communityId){
		setUserPublishInfoByForum(communityId);
		setTodayPublishInfoByForum(communityId);
		setYesPublicInfoByForum(communityId);
		setCountPublisInfoByForum(communityId);
	}
	
	/**
	 * 根据版块分类信息设置发帖数
	 * @param communityid 版块分类id
	 */
	private void setPublishInfoByForumClassify(Object communityId){
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
		long count = Post.dao.getCountByForum(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块分类信息设置总发帖数
	 * @param communityid 版块分类id
	 */
	private void setCountPublisInfoByForumClassify(Object communityid){
		long count = Post.dao.getCountByForumClassify(communityid);
		setAttr("count", count);
	}
	
	/**
	 * 根据版块信息设置昨日发帖数
	 * @param communityid 版块信息
	 */
	private void setYesPublicInfoByForum(Object communityid){
		Long yesCount = Post.dao.getCountOfYesByForum(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块分类信息设置昨日发帖数
	 * @param communityid 版块分类id
	 */
	private void setYesPublicInfoByForumClassify(Object communityid){
		Long yesCount = Post.dao.getCountOfYesByForumClassify(communityid);
		setAttr("yesCount", yesCount);
	}
	
	/**
	 * 根据版块信息设置今日发帖数
	 * @param communityid 版块信息
	 */
	private void setTodayPublishInfoByForum(Object communityid){
		Long todayCount = Post.dao.getCountOfTodayByForum(communityid);
		setAttr("todayCount", todayCount);
	}
	
	/**
	 * 根据版块分类信息设置今日发帖数
	 * @param communityid 版块分类id
	 */
	private void setTodayPublishInfoByForumClassify(Object communityid){
		Long todayCount = Post.dao.getCountOfTodayByForumClassify(communityid);
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
			userCount = Post.dao.getCountOfUserByForum(communityid, userId);
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
			userCount = Post.dao.getCountOfUserByForumClassify(communityid, userId);
		}
		setAttr("userCount", userCount);
	}
	
	/**
	 * 根据版块信息设置分页帖子
	 * @param communityid 版块信息
	 * @param status 显示隐藏状态
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setPageByForum(Object communityId,Object status,int pageNumber,int pageSize) {
		Page<Post> postPage = Post.dao.findPageByForum(pageNumber,pageSize, communityId, status);
		setAttr("postPage", postPage);
	}
	
	/**
	 * 根据版块分类信息设置分页帖子
	 * @param communityid 版块信息
	 * @param status 显示隐藏状态
	 * @param pageNumber 当前页号
	 * @param pageSize 每页记录数
	 */
	private void setPageByForumClassify(Object communityId,Object status,int pageNumber,int pageSize) {
		Page<Post> postPage = Post.dao.findPageByForumClassify(pageNumber,pageSize, communityId, status);
		setAttr("postPage", postPage);
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
