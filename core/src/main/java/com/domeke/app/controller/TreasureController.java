package com.domeke.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.domeke.app.model.Treasure;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "treasure")
public class TreasureController extends Controller {

	/**
	 * 查询所有宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void find() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 20);

		Treasure treasure = getModel(Treasure.class);
		Page<Treasure> page = treasure.findPage(pageNumber, pageSize);
		setAttr("page", page);
		render("/demo/treasure.html");
	}

	/**
	 * 查询用户所有宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void findByUserId() {
		User user = getUser();
		Object userId = user.get("userid");
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Treasure treasure = getModel(Treasure.class);
		Page<Treasure> page = treasure.findByUserId(userId, pageNumber,
				pageSize);
		setAttr("page", page);
		render("/demo/myTreasure.html");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		Object treasureId = getPara("treasureId");
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
		render("/demo/detailTreasure.html");
	}

	/**
	 * 查询修改的宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void modifyById() {
		Object treasureId = getPara("treasureId");
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
		render("/demo/modifyTreasure.html");
	}

	/**
	 * 修改活动信息
	 */
	public void modify() {
		Treasure treasure = getModel(Treasure.class);
		treasure.update();
		findByUserId();
	}

	/**
	 * 删除指定活动
	 */
	public void deleteById() {
		int treasureid = getParaToInt();
		Treasure.dao.deleteById(treasureid);
		findByUserId();
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
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		User user = getUser();
		treasure.set("userid", user.get("userid"));
		treasure.set("userip", remoteIp);
		treasure.save();
		findByUserId();
	}

	public void index() {
		find();
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		HttpSession session = getSession();
		Object user = session.getAttribute("user");
		if (user instanceof User) {
			return (User) user;
		}
		User test = new User();
		test.set("userid", 1);
		test.set("username", 2);
		setAttr("user", test);
		return test;
	}
}
