package com.domeke.app.controller;

import com.domeke.app.model.Treasureapprove;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class TreasureApproveController extends Controller {

	/**
	 * 查询所有宝贝信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("treasureApprovePage_pageNumber", 1);
		int pageSize = getParaToInt("treasureApprovePage_pageSize", 2);
		Page<Treasureapprove> treasureApprovePage = Treasureapprove.dao.paginate(
				pageNumber, pageSize, "select *",
				"from treasureApprove order by create_time");
		setAttr("treasureApprovePage", treasureApprovePage);
		render("/demo/treasureApprove.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		int treasureApproveid = getParaToInt();
		Treasureapprove treasureApprove = Treasureapprove.dao
				.findById(treasureApproveid);
		setAttr("treasureApprove", treasureApprove);
		render("/demo/modifytreasureApprove.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Treasureapprove treasureApprove = getModel(Treasureapprove.class);
		treasureApprove.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int treasureApproveid = getParaToInt();
		Treasureapprove.dao.deleteById(treasureApproveid);
		find();
	}

	/**
	 * 跳转宝贝
	 */
	public void skipCreate() {
		render("/demo/createtreasureApprove.html");
	}

	/**
	 * 创建宝贝
	 */
	public void create() {
		Treasureapprove treasureApprove = getModel(Treasureapprove.class);
		treasureApprove.save();
		find();
	}

	public void index() {
		render("/demo/treasureApprove.html");
	}
}
