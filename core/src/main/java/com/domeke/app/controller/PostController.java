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
	 * 查询所有帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void find() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Post post = getModel(Post.class);
		Page<Post> page = post.findPage(pageNumber, pageSize);
		setAttr("page", page);
		render("/demo/post.html");
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
		int pageSize = getParaToInt("pageSize", 10);

		Post post = getModel(Post.class);
		Page<Post> page = post.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/demo/myPost.html");
	}

	/**
	 * 查询指定帖子信息
	 * 
	 * @return 指定帖子信息
	 */
	public void findByID() {
		setDataByID();
		render("/demo/detailPost.html");
	}

	/**
	 * 设置值
	 */
	private void setDataByID() {
		Object postID = getPara("postID");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Post post = Post.dao.findById(postID);
		setAttr("post", post);

		Page<Comment> page = Comment.dao.findPageByTargetId(postID, pageNumber,
				pageSize);
		setAttr("page", page);

		List<Comment> followPage = Comment.dao.findFollowByTargetId(postID);
		setAttr("followPage", followPage);
	}

	/**
	 * 查询修改的帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void modifyById() {
		String postid = getPara("postId");
		Post post = Post.dao.findById(postid);
		setAttr("post", post);
		render("/demo/modifyPost.html");
	}

	/**
	 * 修改帖子信息
	 */
	public void modify() {
		Post postid = getModel(Post.class);
		postid.update();

		findByUserId();
	}

	/**
	 * 删除指定帖子
	 */
	public void deleteById() {
		String postid = getPara("postId");
		Post.dao.deleteById(postid);

		findByUserId();
	}

	/**
	 * 跳转帖子申请
	 */
	public void skipCreate() {
		render("/demo/createPost.html");
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

		findByUserId();
	}

	/**
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		setDataByID();
		setReplyCommentData();

		render("/demo/comment.html");
	}

	/**
	 * 保存回复信息
	 */
	private void addComment() {
		Comment comment = getModel(Comment.class);

		Object targetID = getPara("targetID");
		comment.set("targetid", targetID);

		User user = getUser();
		comment.set("userid", user.get("userid"));

		comment.set("idtype", "10");

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set("userip", remoteIp);

		Object pID = getPara("pID");
		if (pID != null) {
			comment.set("pid", pID);
		}

		Object toUserID = getPara("toUserID");
		if (toUserID != null) {
			comment.set("touserid", toUserID);
		}

		Object message = getPara("message");
		comment.set("message", message);
		comment.save();
	}

	/**
	 * 添加额外的回复信息
	 */
	private void setReplyCommentData() {
		Object targetID = getPara("targetID");
		Object postID = getPara("postID");
		Object action = "./post/replyComment?postID=" + postID;

		setAttr("targetID", targetID);
		setAttr("action", action);
	}

	/**
	 * 删除回复信息
	 */
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		findByID();
	}

	public void index() {
		find();
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
