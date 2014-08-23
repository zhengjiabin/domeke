package com.domeke.app.controller;

import com.domeke.app.model.Post;
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
		Page<Post> page = Post.dao.paginate(pageNumber, pageSize,
				"select *", "from post order by status,createtime");
		setAttr("page", page);
		render("/demo/post.html");
	}

	/**
	 * 查询指定帖子信息
	 * 
	 * @return 指定帖子信息
	 */
	public void findById() {
		int postid = getParaToInt();
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
		find();
	}

	/**
	 * 删除指定帖子
	 */
	public void deleteById() {
		int postid = getParaToInt();
		Post.dao.deleteById(postid);
		find();
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
		post.save();
		find();
	}

	public void index() {
		render("/demo/post.html");
	}
}
