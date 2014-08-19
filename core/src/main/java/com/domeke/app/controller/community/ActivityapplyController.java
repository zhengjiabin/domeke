package com.domeke.app.controller.community;

import com.domeke.app.model.community.Activityapply;
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
		Page<Activityapply> activityapplyPage = Activityapply.dao.paginate(
				pageNumber, pageSize, "select *",
				"from activityapply order by create_time");
		setAttr("activityapplyPage", activityapplyPage);
		render("/community/activityapply.html");
	}

	/**
	 * 查询指定活动申请信息
	 * 
	 * @return 指定活动申请信息
	 */
	public void findById() {
		int activityapplyid = getParaToInt();
		Activityapply activityapply = Activityapply.dao
				.findById(activityapplyid);
		setAttr("activityapply", activityapply);
		render("/community/modifyActivityapply.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Activityapply activityapply = getModel(Activityapply.class);
		activityapply.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int activityapplyid = getParaToInt();
		Activityapply.dao.deleteById(activityapplyid);
		find();
	}

	/**
	 * 跳转活动申请
	 */
	public void skipCreate() {
		render("/community/createActivityapply.html");
	}

	/**
	 * 创建活动申请
	 */
	public void create() {
		Activityapply activityapply = getModel(Activityapply.class);
		activityapply.save();
		find();
	}

	public void index() {
		render("/community/activityapply.html");
	}
}
