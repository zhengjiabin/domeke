package com.domeke.app.controller;

import com.domeke.app.model.Treasure;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "treasure")
public class TreasureController extends Controller {

	/**
	 * 查询所有宝贝信息
	 * 
	 * @return 活动信息
	 */
	public void find() {
		int pageNumber = getParaToInt("treasurePage_pageNumber", 1);
		int pageSize = getParaToInt("treasurePage_pageSize", 2);
		Page<Treasure> treasurePage = Treasure.dao.paginate(
				pageNumber, pageSize, "select *",
				"from treasure order by create_time");
		setAttr("treasurePage", treasurePage);
		render("/demo/treasure.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		int treasureid = getParaToInt();
		Treasure treasure = Treasure.dao
				.findById(treasureid);
		setAttr("treasure", treasure);
		render("/demo/modifyTreasure.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Treasure treasure = getModel(Treasure.class);
		treasure.update();
		find();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int treasureid = getParaToInt();
		Treasure.dao.deleteById(treasureid);
		find();
	}

	/**
	 * 跳转宝贝
	 */
	public void skipCreate() {
		render("/demo/createTreasure.html");
	}

	/**
	 * 创建宝贝
	 */
	public void create() {
		Treasure treasure = getModel(Treasure.class);
		treasure.save();
		find();
	}

	public void index() {
		render("/demo/treasure.html");
	}
}
