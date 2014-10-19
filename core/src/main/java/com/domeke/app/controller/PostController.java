package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
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
		setPublishNumber();
		setCommunity();
		
		render("/community/post.html");
	}
	
	/**
	 * admin管理中对应的社区管理入口
	 */
	public void goToManager() {
		findPageAll();
		render("/admin/admin_post.html");
	}
	

	/**
	 * 查询指定社区的分页帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void find() {
		String communityId = getPara("communityId");
		setPostPage(communityId);
		
		render("/community/postPage.html");
	}
	
	/**
	 * 分页查询帖子
	 */
	public void findPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Post> postPage = Post.dao.findPage(pageNumber, pageSize);
		setAttr("postPage", postPage);
		render("/admin/admin_detailPost.html");
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

		Post post = getModel(Post.class);
		Page<Post> page = post.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/community/myPost.html");
	}
	
	/**
	 * 置顶功能
	 */
	@Before(LoginInterceptor.class)
	public void setTop(){
		String postId = getPara("targetId");
		if(postId == null || postId.length()<=0){
			renderJson("false");
			return;
		}
		Post post = getModel(Post.class);
		post.set("postid", postId);
		post.set("top", 1);
		post.update();
		renderJson("true");
	}
	
	/**
	 * 精华功能
	 */
	@Before(LoginInterceptor.class)
	public void setEssence(){
		String postId = getPara("targetId");
		if(postId == null || postId.length()<=0){
			renderJson("false");
			return;
		}
		Post post = getModel(Post.class);
		post.set("postid", postId);
		post.set("essence", 1);
		post.update();
		renderJson("true");
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
		keepPara("communityId");
		
		forwardComment(postId);
	}
	
	/**
	 * 跳转回复控制器，设置回复信息
	 */
	private void forwardComment(Object targetId){
		String action = "/comment/setPage";
		setAttr("render", "/community/detailPost.html");
		setAttr("targetId", targetId);
		setAttr("idtype", IDTYPE);
		forwardAction(action);
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunitys(){
		String communityId = getPara("communityId");
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
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
	 * 设置帖子信息
	 * @param postId
	 */
	private void setPost(Object postId){
		Post post = Post.dao.findById(postId);
		setAttr("post", post);
	}

	/**
	 * 查询修改的帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void modifyById() {
		String postId = getPara("postId");
		Post post = Post.dao.findById(postId);
		setAttr("post", post);
		render("/community/modifyPost.html");
	}

	/**
	 * 修改帖子信息
	 */
	@Before(LoginInterceptor.class)
	public void modify() {
		Post post = getModel(Post.class);
		post.update();

		findByUserId();
	}

	/**
	 * admin管理--删除指定帖子
	 */
	@Before(LoginInterceptor.class)
	public void deleteById() {
		String postId = getPara("postId");
		Post.dao.deleteById(postId);
		
		Comment.dao.deleteByTheme(postId, IDTYPE);
		findPageAll();
		render("/admin/admin_detailPost.html");
	}

	/**
	 * 跳转帖子申请
	 */
	@Before(LoginInterceptor.class)
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object post = Post.dao.findHasPublish(communityId, userId);
		if(post != null){
			renderJson(false);
			return;
		}
		
		keepPara("communityId");
		render("/community/createPost.html");
	}
	
	/**
	 * admin管理--跳转发表/修改主题
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
		render("/admin/admin_updatePost.html");
	}
	
	/**
	 * admin管理--发表/修改主题
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
		
		goToManager();
	}
	
	/**
	 * admin管理--跳转指定页面
	 */
	public void findByAdminPage(){
		findPageAll();
		render("/admin/admin_detailPost.html");
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
	 * 创建帖子申请
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
		
		render("/community/post.html");
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
		Long userPostCount = Post.dao.getCountByUserId(userId);
		setAttr("userCount", userPostCount);
		
		//今日发帖数
		Long postTodayCount = Post.dao.getTodayCount();
		setAttr("todayCount", postTodayCount);
		//昨日发帖数
		Long postYesCount = Post.dao.getYesterdayCount();
		setAttr("yesCount", postYesCount);
		//总发帖数
		Long postCount = Post.dao.getCount();
		setAttr("count", postCount);
	}
	
	public void home(){
		setPostPage(null);
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		setPublishNumber();
		render("/community/postAll.html");
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
