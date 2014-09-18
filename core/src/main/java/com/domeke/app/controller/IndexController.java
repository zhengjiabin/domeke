package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Goods;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
		List<List<Works>> lists = new ArrayList<List<Works>>();
		List<CodeTable> codetables = CodeKit.getList("workstype");
		Works worksDao = getModel(Works.class);
		Map<String, Object> map = new HashMap<String, Object>();
		for (CodeTable codeTable : codetables) {
			map.put(codeTable.getStr("codekey"), codeTable.getStr("codevalue"));
		}
		//加载头部5个循环显示
		List<Works> workss0 = worksDao.getWorksInfoByType("1000",5);
		
		//加载中间数据
		List<Works> workss1 = worksDao.getWorksInfoByType("10",7);
		List<Works> workss2 = worksDao.getWorksInfoByType("20",5);
		List<Works> workss3 = worksDao.getWorksInfoByType("30",5);
		List<Works> workss4 = worksDao.getWorksInfoByType("40",10);
		
		//加载右边列表
		List<Works> worksClickList = worksDao.getWorksInfoByPageViewsLimit(9);
		List<Works> worksUpdateList = worksDao.getWorksInfoByUpdateLimit(10);
		
		
		//加载底部商品信息
		Goods goods = getModel(Goods.class);
		List<Goods> goodss = goods.getGoodsByNewLimit(4);
		
		//返回数据
		setAttr("workss0", workss0);
		setAttr("workstype", map);
		setAttr("workss1", workss1);
		setAttr("workss2", workss2);
		setAttr("workss3", workss3);
		setAttr("workss4", workss4);
		setAttr("worksClickList", worksClickList);
		setAttr("worksUpdateList", worksUpdateList);
		setAttr("goodss", goodss);
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
		String goodstype = getPara("goodstype");
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		
		if(pageNumber == null){
			pageNumber = 1;
		}
		if(pageSize == null){
			pageSize = 25;
		}
		
		//获取类型集合
		Map<String,Object> typeMap = new HashMap<String, Object>();
		List<CodeTable> codeTables = CodeKit.getList("goodstype");
//		for (CodeTable codeTable : codeTables) {
//			typeMap.put(codeTable.getStr("codekey"), codeTable.getStr("codevalue"));
//		}
		//按类型获取商品分类
		Page<Goods> goodss = getModel(Goods.class).getGoodsPageByType(goodstype, pageNumber, pageSize);
		
		setAttr("goodss",goodss);
		setAttr("codeTables",codeTables);
		render("shopCategory.html");
	}
	
	public void forum() {
		setAttr("menuid", "37");
//		keepPara();
		redirect("/community");
//		render("forum.html");
	}

}
