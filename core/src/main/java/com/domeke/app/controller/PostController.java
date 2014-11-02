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
	
	/** 回复类型 */
	private static String IDTYPE = "10";

	/**
	 * 论坛列表
	 * 请求 ./post?communityId=${communityId!}
	 */
	public void index() {
		String communityId = getPara("communityId");
		setPostPage(communityId);
		setPublishNumber(communityId);
		setCommunity();
		
		render("/community/community_post.html");
	}

	/**
	 * 创建帖子申请
	 * 请求 ./post/create
	 */
	@Before(LoginInterceptor.class)
	public void create() {
		Post post = getModel(Post.class);
		Object communityId = post.get("communityid");
		Object userId = getUserId();
		Object postOld = Post.dao.findHasPublish(communityId, userId);
		if(postOld != null){
			renderJson(false);
			return;
		}
		
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		post.set("userid", userId);
		post.set("userip", remoteIp);
		post.save();

		setPostPage(communityId);
		setPublishNumber(communityId);
		
		render("/community/community_post.html");
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
	 * 版块入口
	 * 请求 ./post/home
	 */
	public void home(){
		setPostPage(null);
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		setPublishNumber(null);
		render("/community/community_postAll.html");
	}
	
	/**
	 * 置顶功能
	 * 请求 ./post/setTop?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String postId = getPara("targetId");
		if(postId == null || postId.length()<=0){
			renderJson(2);
			return;
		}
		Post post = getModel(Post.class);
		post.set("postid", postId);
		post.set("top", 1);
		post.update();
		renderJson(3);
	}
	
	/**
	 * 精华功能
	 * 请求 ./post/setEssence?targetId={targetId!}
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String postId = getPara("targetId");
		if(postId == null || postId.length()<=0){
			renderJson(2);
			return;
		}
		Post post = getModel(Post.class);
		post.set("postid", postId);
		post.set("essence", 1);
		post.update();
		renderJson(3);
	}
	
	/**
	 * admin管理--入口
	 * 请求 ./post/goToManager
	 */
	public void goToManager() {
		findPageAll();
		render("/admin/admin_post.html");
	}
	
	/**
	 * admin管理--分页跳转
	 * 请求 ./post/findByAdminPage
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_postPage.html");
	}
	
	/**
	 * admin管理--删除指定主题
	 * 请求 ./post/deleteById?postId=${postId!}
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		String postId = getPara("postId");
		Post.dao.deleteById(postId);
		
		Comment.dao.deleteByTheme(postId, IDTYPE);
		findPageAll();
		render("/admin/admin_postPage.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
	 * 请求 ./post/skipUpdate?postId=${postId!}
	 */
	@Before(LoginInterceptor.class)
	public void skipUpdate() {
		Post post = null;
		String postId = getPara("postId");
		if(StrKit.notBlank(postId)){
			post = Post.dao.findById(postId);
		}else{
			post = getModel(Post.class);
		}
		setAttr("post", post);
		
		List<Community> communityList = Community.dao.findSonList();
		setAttr("communityList", communityList);
		render("/admin/admin_postUpdate.html");
	}
	
	/**
	 * admin管理--发表/修改主题
	 * 请求 ./post/update
	 */
	@Before(LoginInterceptor.class)
	public void update() {
		Post post = getModel(Post.class);
		Object postId = post.get("postid");
		if (postId == null) {
			Object userId = getUserId();
			HttpServletRequest request = getRequest();
			String remoteIp = request.getRemoteAddr();

			post.set("userid", userId);
			post.set("userip", remoteIp);
			post.save();
		} else {
			post.update();
		}
		findPageAll();
		render("/admin/admin_postPage.html");
	}
	
	/**
	 * 个人会用中心（我发布的帖子）--入口
	 */
	@Before(LoginInterceptor.class)
	public void personalHome(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Object userId = getUserId();
		Page<Post> postPage = Post.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("postPage", postPage);
		render("/personal/personal_post.html");
	}
	
	/**
	 * 个人会员中心--查询用户发布的帖子
	 * 请求 post/findByUserId
	 */
	@Before(LoginInterceptor.class)
	public void findByUserId() {
		Object userId = getUserId();
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Post> postPage = Post.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("postPage", postPage);
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
	@Before(LoginInterceptor.class)
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
	 * 查询指定帖子信息
	 * 请求  post/findById?communityId=${communityId!}&targetId=${targetId!}&idtype=${idtype!}
	 */
	public void findById() {
		String postId = getPara("targetId");
		Post.dao.updateTimes(postId);
		
		setPost(postId);
		setCommunitys();
		
		String render = "/community/community_postDetail.html";
		forwardComment(postId,render);
	}

	/**
	 * 查询指定社区的分页帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void find() {
		String communityId = getPara("communityId");
		setPostPage(communityId);
		
		render("/community/community_postPage.html");
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
	 * 设置版块信息
	 */
	private void setCommunity(){
		String communityId = getPara("communityId");
		Community community = Community.dao.findById(communityId);
		setAttr("community", community);
	}
	
	/**
	 * 设置签到人数
	 */
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
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
	private void setPublishNumber(Object communityId){
		Long userPostCount = null,postTodayCount = null,postYesCount = null,postCount = null;
		Object userId = getUserId();
		if(StrKit.notNull(communityId)){
			userPostCount = Post.dao.getCountByCommunityAndUsr(communityId, userId);
			postTodayCount = Post.dao.getTodayCountByCommunityId(communityId);
			postYesCount = Post.dao.getYesterdayCountByCommunityId(communityId);
			postCount = Post.dao.getCountByCommunityId(communityId);
		}else{
			userPostCount = Post.dao.getCountByUserId(userId);
			postTodayCount = Post.dao.getTodayCount();
			postYesCount = Post.dao.getYesterdayCount();
			postCount = Post.dao.getCount();
		}
		// 当前登录人发帖数
		setAttr("userCount", userPostCount);
		//今日发帖数
		setAttr("todayCount", postTodayCount);
		//昨日发帖数
		setAttr("yesCount", postYesCount);
		//总发帖数
		setAttr("count", postCount);
	}
	
	/**
	 * admin管理--分页查询论坛(不区分显示隐藏状态)
	 */
	private void findPageAll() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Post> postPage = Post.dao.findPageAll(pageNumber, pageSize);
		setAttr("postPage", postPage);
	}

	/**
	 * 设置分页帖子
	 */
	private void setPostPage(Object communityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Post> postPage = null;
		if(communityId == null){
			postPage =Post.dao.findPage(pageNumber, pageSize);
		}else{
			postPage = Post.dao.findPageByCommunityId(pageNumber,pageSize, communityId);
		}
		setAttr("postPage", postPage);
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
