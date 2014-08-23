package com.domeke.app.controller;

import com.domeke.app.model.Comment;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "comment")
public class CommentController extends Controller {

	/**
	 * 查询所有宝贝信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("commentPage_pageNumber", 1);
		int pageSize = getParaToInt("commentPage_pageSize", 2);
		Page<Comment> commentPage = Comment.dao.paginate(pageNumber, pageSize,
				"select *", "from comment order by create_time");
		setAttr("commentPage", commentPage);
		render("/demo/comment.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		int commentid = getParaToInt();
		Comment comment = Comment.dao.findById(commentid);
		setAttr("comment", comment);
		render("/demo/modifycomment.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Comment comment = getModel(Comment.class);
		comment.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int commentid = getParaToInt();
		Comment.dao.deleteById(commentid);
		find();
	}

	/**
	 * 跳转宝贝
	 */
	public void skipCreate() {
		render("/demo/createcomment.html");
	}

	/**
	 * 创建宝贝
	 */
	public void create() {
		Comment comment = getModel(Comment.class);
		comment.save();
		find();
	}

	public void index() {
		render("/demo/comment.html");
	}
}
