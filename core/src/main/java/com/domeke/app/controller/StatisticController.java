package com.domeke.app.controller;

import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey="count")
public class StatisticController extends Controller{
	public void goUserCount(){
		User user = getModel(User.class);
		Long countUser = user.countUser(null, null);
		Long isActivation = user.countUser("activation", "Y");
		Long noActivation = user.countUser("activation", "N");
		countPeas();
		countPoint();
		setAttr("countUser", countUser);
		setAttr("isActivation", isActivation);
		setAttr("noActivation", noActivation);
		render("/admin/admin_userCount.html");
	}
	public void countPeas(){
		User user = getModel(User.class);
		//豆米粒 0~99
		Long peas1 = user.countPeas("0", "99");
		//豆芽棒 100~499
		Long peas2 = user.countPeas("100", "499");
		//豆瓣花 500~1499
		Long peas3 = user.countPeas("500", "1499");
		//小弯豆 1500~3999
		Long peas4 = user.countPeas("1500", "3999");
		//豆青 4000~19999
		Long peas5 = user.countPeas("4000", "19999");
		//豆爷 20000~49999
		Long peas6 = user.countPeas("20000", "49999");
		//豆 皇 50000~10000000000
		Long peas7 = user.countPeas("50000", "10000000000");
		setAttr("peas1", peas1);
		setAttr("peas2", peas2);
		setAttr("peas3", peas3);
		setAttr("peas4", peas4);
		setAttr("peas5", peas5);
		setAttr("peas6", peas6);
		setAttr("peas7", peas7);
		
	}
	
	public void countPoint(){
		User user = getModel(User.class);
		//积分 0~99
		Long point1 = user.countPeas("0", "99");
		//积分 100~499
		Long point2 = user.countPeas("100", "499");
		//积分 500~1499
		Long point3 = user.countPeas("500", "1499");
		//积分 1500~3999
		Long point4 = user.countPeas("1500", "3999");
		//积分 4000~19999
		Long point5 = user.countPeas("4000", "19999");
		//积分 20000~49999
		Long point6 = user.countPeas("20000", "49999");
		//积分 50000~10000000000
		Long point7 = user.countPeas("50000", "10000000000");
		setAttr("point1", point1);
		setAttr("point2", point2);
		setAttr("point3", point3);
		setAttr("point4", point4);
		setAttr("point5", point5);
		setAttr("point6", point6);
		setAttr("point7", point7);
		
	}
}
