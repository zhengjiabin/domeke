package com.domeke.app.controller;

import com.domeke.app.model.Activity;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class ActivityController extends Controller {

	/**
	 * 查询所有活动信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("activityPage_pageNumber", 1);
		int pageSize = getParaToInt("activityPage_pageSize", 2);
		Page<Activity> activityPage = Activity.dao.paginate(pageNumber,
				pageSize, "select *", "from activity order by create_time");
		setAttr("activityPage", activityPage);
		render("/demo/activity.html");
	}

	/**
	 * 查询指定活动信息
	 * 
	 * @return 指定活动信息
	 */
	public void findById() {
		int activityid = getParaToInt();
		Activity activity = Activity.dao.findById(activityid);
		setAttr("activity", activity);
		render("/demo/modifyActivity.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Activity activityid = getModel(Activity.class);
		activityid.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int activityid = getParaToInt();
		Activity.dao.deleteById(activityid);
		find();
	}

	public void index() {
		render("/demo/activity.html");
	}
}
