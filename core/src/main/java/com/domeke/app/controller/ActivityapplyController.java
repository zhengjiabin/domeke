package com.domeke.app.controller;

import com.domeke.app.model.ActivityApply;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class ActivityapplyController extends Controller {

	/**
	 * 查询所有活动申请信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("activityapplyPage_pageNumber", 1);
		int pageSize = getParaToInt("activityapplyPage_pageSize", 2);
		Page<ActivityApply> activityapplyPage = ActivityApply.dao.paginate(
				pageNumber, pageSize, "select *",
				"from activityapply order by create_time");
		setAttr("activityapplyPage", activityapplyPage);
		render("/demo/activityapply.html");
	}

	/**
	 * 查询指定活动申请信息
	 * 
	 * @return 指定活动申请信息
	 */
	public void findById() {
		int activityapplyid = getParaToInt();
		ActivityApply activityapply = ActivityApply.dao
				.findById(activityapplyid);
		setAttr("activityapply", activityapply);
		render("/demo/modifyActivityapply.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		ActivityApply activityapply = getModel(ActivityApply.class);
		activityapply.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int activityapplyid = getParaToInt();
		ActivityApply.dao.deleteById(activityapplyid);
		find();
	}

	/**
	 * 跳转活动申请
	 */
	public void skipCreate() {
		render("/demo/createActivityapply.html");
	}

	/**
	 * 创建活动申请
	 */
	public void create() {
		ActivityApply activityapply = getModel(ActivityApply.class);
		activityapply.save();
		find();
	}

	public void index() {
		render("/demo/activityapply.html");
	}
}
