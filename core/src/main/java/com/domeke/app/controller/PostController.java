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
		int pageSize = getParaToInt("pageSize", 2);

		Page<Post> postPage = Post.dao.findPage(pageNumber, pageSize);
		setAttr("postPage", postPage);
		render("/demo/postPage.html");
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
		render("/demo/myPost.html");
	}

	/**
	 * 查询指定帖子信息
	 * 
	 * @return 指定帖子信息
	 */
	public void findById() {
		setDataById();
		render("/demo/detailPost.html");
	}

	/**
	 * 查询帖子回复信息
	 */
	public void findCommentByPostId() {
		setDataById();
		keepPara("pageAction", "fatherNode");
		render("/demo/comment.html");
	}

	/**
	 * 设置值
	 */
	private void setDataById() {
		Object postId = getPara("postId");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Post post = Post.dao.findById(postId);
		setAttr("post", post);

		Page<Comment> commentPage = Comment.dao.findPageByTargetId(postId,
				pageNumber, pageSize);
		setAttr("commentPage", commentPage);

		List<Comment> followPage = Comment.dao.findFollowByTargetId(postId);
		setAttr("followPage", followPage);

		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			Comment comment = Comment.dao.findById(pId);
			setAttr("comment", comment);
		}
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
		render("/demo/modifyPost.html");
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

		setDataById();
		setReplyCommentData();

		String renderAction = getPara("renderAction");
		render(renderAction);
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

		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			comment.set("pid", pId);
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
		keepPara("pageAction", "fatherNode", "commentFatherNode");
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
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);

		Page<Post> postPage = Post.dao.findPage(pageNumber, pageSize);
		setAttr("postPage", postPage);
		render("/demo/post.html");
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
