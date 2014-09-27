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
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "post")
@Before(LoginInterceptor.class)
public class PostController extends Controller {

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
	 * 
	 * @return 指定帖子信息
	 */
	public void findById() {
		String postId = getPara("targetId");
		Post.dao.updateTimes(postId);
		
		setPost(postId);
		setCommunity();
		setCommentPage(postId);
		setFollowList(postId);
		
		keepPara("communityId");
		render("/community/detailPost.html");
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunity(){
		String communityId = getPara("communityId");
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
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
	 * 分页设置父回复信息
	 */
	public void setCommentPage(Object postId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Comment> commentPage = Comment.dao.findPageByTargetId(postId,
				"10", pageNumber, pageSize);
		setAttr("commentPage", commentPage);
	}
	
	/**
	 * 设置子回复信息
	 */
	public void setFollowList(Object postId) {
		List<Comment> followPage = Comment.dao.findFollowByTargetId(postId,
				"10");
		setAttr("followPage", followPage);
	}

	/**
	 * 异步分页查询回复信息
	 */
	public void findCommentByPostId() {
		Object postId = getPara("postId");
		setCommentPage(postId);
		setFollowList(postId);
		setReplyCommentData();
		render("/comment/comment.html");
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
	public void modify() {
		Post post = getModel(Post.class);
		post.update();

		findByUserId();
	}

	/**
	 * 删除指定帖子
	 */
	public void deleteById() {
		String post = getPara("postId");
		Post.dao.deleteById(post);

		findByUserId();
	}

	/**
	 * 跳转帖子申请
	 */
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
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		String postId = getPara("postId");
		setPost(postId);
		setCommentPage(postId);
		setFollowList(postId);
		
		setReplyCommentData();

		render("/comment/comment.html");
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyFollow() {
		addComment();

		String postId = getPara("postId");
		setPost(postId);
		setFollowList(postId);
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			setComment(pId);
		}
		setReplyCommentData();

		render("/comment/followPage.html");
	}
	
	/**
	 * 设置回复信息
	 * 
	 * @param commentId
	 */
	private void setComment(Object commentId) {
		Comment comment = Comment.dao.findById(commentId);
		setAttr("comment", comment);
	}

	/**
	 * 保存回复信息
	 */
	private void addComment() {
		Comment comment = getModel(Comment.class);

		Object targetId = getPara("targetId");
		comment.set("targetid", targetId);

		Object userId = getUserId();
		comment.set("userid", userId);

		comment.set("idtype", "10");

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set("userip", remoteIp);

		//默认回复楼层
		comment.set("level", "1");
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			comment.set("pid", pId);
			comment.set("level", "2");
		}

		String toUserId = getPara("toUserID");
		if (toUserId != null && toUserId.length() > 0) {
			comment.set("touserid", toUserId);
		}

		Object message = getPara("message");
		comment.set("message", message);
		comment.save();
	}

	/**
	 * 添加额外的回复信息
	 */
	private void setReplyCommentData() {
		Object targetId = getPara("targetId");
		setAttr("targetId", targetId);
		keepPara("pageAction", "fatherNode","commentAction","followAction");
	}

	/**
	 * 删除回复信息
	 */
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		findById();
	}

	public void index() {
		String communityId = getPara("communityId");
		setPostPage(communityId);
		setPublishNumber();
		
		render("/community/post.html");
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
		render("/community/postAll.html");
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
