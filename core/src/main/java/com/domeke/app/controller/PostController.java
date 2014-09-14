package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.domeke.app.model.Comment;
import com.domeke.app.model.Post;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "post")
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
	 * 查询发帖人所有帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void findByUserId() {
		User user = getUser();
		Object userId = user.get("userid");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Post post = getModel(Post.class);
		Page<Post> page = post.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/community/myPost.html");
	}

	/**
	 * 查询指定帖子信息
	 * 
	 * @return 指定帖子信息
	 */
	public void findById() {
		String postId = getPara("postId");
		
		setPost(postId);
		setCommentPage(postId);
		setFollowList(postId);
		
		keepPara("communityId");
		render("/community/detailPost.html");
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
		int pageSize = getParaToInt("pageSize", 2);
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
		keepPara("communityId");
		render("/community/createPost.html");
	}

	/**
	 * 创建帖子申请
	 */
	public void create() {
		Post post = getModel(Post.class);
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		User user = getUser();
		post.set("userid", user.get("userid"));
		post.set("userip", remoteIp);
		post.save();

		Object communityId = post.get("communityid");
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
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			setComment(pId);
		}
		setReplyCommentData();

		String renderAction = getPara("renderAction");
		render(renderAction);
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

		User user = getUser();
		comment.set("userid", user.get("userid"));

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
		Object postId = getPara("postId");
		Object publishFaceAction = "./post/replyComment?postId=" + postId;

		setAttr("targetId", targetId);
		setAttr("publishFaceAction", publishFaceAction);
		keepPara("pageAction", "fatherNode");
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
		
		render("/community/post.html");
	}

	/**
	 * 设置分页帖子
	 */
	private void setPostPage(Object communityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);
		Page<Post> postPage = Post.dao.findPageByCommunityId(pageNumber,
				pageSize, communityId);
		setAttr("postPage", postPage);
		setAttr("communityId", communityId);
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		HttpSession session = getSession();
		Object user = session.getAttribute("user");
		if (user instanceof User) {
			return (User) user;
		}
		User test = new User();
		test.set("userid", 1);
		test.set("username", "zheng");
		return test;
	}
}
