package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Goods;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
//		List<List<Works>> lists = new ArrayList<List<Works>>();
//		List<CodeTable> codetables = CodeKit.getList("workstype");
//		Works worksDao = getModel(Works.class);
//		for (CodeTable codeTable : codetables) {
//			String codeValue = codeTable.getStr("codevalue");
//			List<Works> workss = worksDao.getWorksInfoByType(codeValue);
//			lists.add(workss);
//		}
//		Goods goods = getModel(Goods.class);
//		List<Goods> goodss = goods.getGoodsByNewLimit(4);
//		setAttr("lists", lists);
//		setAttr("goodss", goodss);
		render("index.html");
	}

	public void cartoon() {
		render("cartoon.html");
	}

	// TODO 测试用 记得删除，播放的入口应为具体的controller
	public void play() {
		String menuid = getPara("menuid");
		setAttr("menuid", menuid);
		render("play.html");
	}
	
	public void shop() {
		setAttr("menuid", "42");
		render("shop.html");
	}
	
	public void forum() {
		setAttr("menuid", "37");
//		keepPara();
		redirect("/community");
//		render("forum.html");
	}

}
