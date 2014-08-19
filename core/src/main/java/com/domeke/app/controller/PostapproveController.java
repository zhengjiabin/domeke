package com.domeke.app.controller;

import com.domeke.app.model.Postapprove;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class PostapproveController extends Controller {

	/**
	 * 查询所有帖子审核信息
	 * 
	 * @return 帖子审核信息
	 */
	public void find() {
		int pageNumber = getParaToInt("postapprovePage_pageNumber", 1);
		int pageSize = getParaToInt("postapprovePage_pageSize", 2);
		Page<Postapprove> postapprovePage = Postapprove.dao.paginate(
				pageNumber, pageSize, "select *",
				"from postapprove order by create_time");
		setAttr("postapprovePage", postapprovePage);
		render("/demo/postapprove.html");
	}

	/**
	 * 查询指定帖子审核信息
	 * 
	 * @return 指定帖子审核信息
	 */
	public void findById() {
		int postapproveid = getParaToInt();
		Postapprove postapprove = Postapprove.dao
				.findById(postapproveid);
		setAttr("postapprove", postapprove);
		render("/demo/modifyPostapprove.html");
	}

	/**
	 * 修改帖子审核信息
	 */
	public void modify() {
		Postapprove postapprove = getModel(Postapprove.class);
		postapprove.update();
		find();
	}

	/**
	 * 删除指定帖子审核信息
	 */
	public void deleteById() {
		int postapproveid = getParaToInt();
		Postapprove.dao.deleteById(postapproveid);
		find();
	}

	/**
	 * 跳转帖子审核
	 */
	public void skipCreate() {
		render("/demo/createPostapprove.html");
	}

	/**
	 * 创建帖子审核
	 */
	public void create() {
		Postapprove postapprove = getModel(Postapprove.class);
		postapprove.save();
		find();
	}

	public void index() {
		render("/demo/postapprove.html");
	}
}
