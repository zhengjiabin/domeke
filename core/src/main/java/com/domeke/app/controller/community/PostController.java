package com.domeke.app.controller.community;

import com.domeke.app.model.community.Post;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class PostController extends Controller {

	/**
	 * 查询所有帖子信息
	 * 
	 * @return 帖子信息
	 */
	public void find() {
		int pageNumber = getParaToInt("postPage_pageNumber", 1);
		int pageSize = getParaToInt("postPage_pageSize", 2);
		Page<Post> postPage = Post.dao.paginate(pageNumber,
				pageSize, "select *", "from post order by create_time");
		setAttr("postPage", postPage);
		render("/community/post.html");
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
		render("/community/modifyPost.html");
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

	public void index() {
		render("/community/post.html");
	}
}
