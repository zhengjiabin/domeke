package com.domeke.app.controller;

import com.domeke.app.model.PostApprove;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "postApprove")
public class PostApproveController extends Controller {

	/**
	 * 查询所有帖子审核信息
	 * 
	 * @return 帖子审核信息
	 */
	public void find() {
		int pageNumber = getParaToInt("postapprovePage_pageNumber", 1);
		int pageSize = getParaToInt("postapprovePage_pageSize", 2);
		Page<PostApprove> postapprovePage = PostApprove.dao.paginate(
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
		PostApprove postapprove = PostApprove.dao.findById(postapproveid);
		setAttr("postapprove", postapprove);
		render("/demo/modifyPostapprove.html");
	}

	/**
	 * 修改帖子审核信息
	 */
	public void modify() {
		PostApprove postapprove = getModel(PostApprove.class);
		postapprove.update();
		find();
	}

	/**
	 * 删除指定帖子审核信息
	 */
	public void deleteById() {
		int postapproveid = getParaToInt();
		PostApprove.dao.deleteById(postapproveid);
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
		PostApprove postapprove = getModel(PostApprove.class);
		postapprove.save();
		find();
	}

	public void index() {
		render("/demo/postapprove.html");
	}
}
