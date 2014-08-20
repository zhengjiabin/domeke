package com.domeke.app.controller;

import com.domeke.app.model.Attachment;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class AttachmentController extends Controller {

	/**
	 * 查询所有宝贝信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("attachmentPage_pageNumber", 1);
		int pageSize = getParaToInt("attachmentPage_pageSize", 2);
		Page<Attachment> attachmentPage = Attachment.dao.paginate(
				pageNumber, pageSize, "select *",
				"from attachment order by create_time");
		setAttr("attachmentPage", attachmentPage);
		render("/demo/attachment.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		int attachmentid = getParaToInt();
		Attachment attachment = Attachment.dao
				.findById(attachmentid);
		setAttr("attachment", attachment);
		render("/demo/modifyAttachment.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Attachment attachment = getModel(Attachment.class);
		attachment.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int attachmentid = getParaToInt();
		Attachment.dao.deleteById(attachmentid);
		find();
	}

	/**
	 * 跳转宝贝
	 */
	public void skipCreate() {
		render("/demo/createAttachment.html");
	}

	/**
	 * 创建宝贝
	 */
	public void create() {
		Attachment attachment = getModel(Attachment.class);
		attachment.save();
		find();
	}

	public void index() {
		render("/demo/attachment.html");
	}
}
