package com.domeke.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public void findByAuthorid() {
		User user = getUser();
		String userId = user.getStr("userid");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Post post = getModel(Post.class);
		Page<Post> page = post.findByAuthorid(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/demo/myPost.html");
	}

	/**
	 * 查询指定帖子信息
	 * 
	 * @return 指定帖子信息
	 */
	public void findById() {
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
		
		findByAuthorid();
	}

	/**
	 * 删除指定帖子
	 */
	public void deleteById() {
		String postid = getPara("postId");
		Post.dao.deleteById(postid);

		findByAuthorid();
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
		post.set("username", user.get("username"));
		post.set("userid", user.get("userid"));
		post.set("useip", remoteIp);
		post.save();

		findByAuthorid();
	}

	public void index() {
		find();
	}

	/**
	 * 获取登录User对象
	 * @return user
	 */
	private User getUser() {
		HttpSession session = getSession();
		Object user = session.getAttribute("user");
		if (user instanceof User) {
			return (User) user;
		}
		return null;
	}
}
