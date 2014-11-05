package com.domeke.app.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.Goods;
import com.domeke.app.model.History;
import com.domeke.app.model.Homepage;
import com.domeke.app.model.Menu;
import com.domeke.app.model.OfWonders;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.ParseDemoKit;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
		String menuid = getPara("menuid");
		if(StrKit.isBlank(menuid)){
			menuid = "1";
		}
		WorksType worksTypeModel = getModel(WorksType.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes("1");
		
		Works worksDao = getModel(Works.class);
		Map<String, Object> typeMap = Maps.newHashMap();
		for (WorksType worksType : worksTypes) {
			typeMap.put(worksType.get("id").toString(), worksType.get("name"));
		}
//		//加载首页 循环显示
		Homepage homepageModel = getModel(Homepage.class);
		List<Homepage> homepages = Lists.newArrayList();
		homepages =	homepageModel.findHomepagesByStatusRank("1");
		
		//加载中间数据
		List<Works> workss0temp = worksDao.getWorksInfoByType(worksTypes.get(0).get("id").toString(),7);
		List<Map<String, Object>> workss0 = Lists.newArrayList();
		workss0 = ParseDemoKit.worksParse(workss0temp);
		
		List<Works> workss1temp = worksDao.getWorksInfoByType(worksTypes.get(1).get("id").toString(),5);
		List<Map<String, Object>> workss1 = Lists.newArrayList();
		workss1 = ParseDemoKit.worksParse(workss1temp);
		
		List<Works> workss2temp = worksDao.getWorksInfoByType(worksTypes.get(2).get("id").toString(),5);
		List<Map<String, Object>> workss2 = Lists.newArrayList();
		workss2 = ParseDemoKit.worksParse(workss2temp);
		
		List<Works> workss3temp = worksDao.getWorksInfoByType(worksTypes.get(3).get("id").toString(),10);
		List<Map<String, Object>> workss3 = Lists.newArrayList();
		workss3 = ParseDemoKit.worksParse(workss3temp);
		
		List<OfWonders> ofWondersList = OfWonders.dao.findPic(10);
		Menu wonderTypeMenu = Menu.menuDao.findById(3);
		//加载右边列表
		History historyModel = getModel(History.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = dateFormat.format(historyModel.getLastMonday());
		String endTime = dateFormat.format(historyModel.getLastSunday());
		List<History> historys = historyModel.getLastWeekClickRank(9, startTime, endTime);
		List<Works> worksClickListTemp = Lists.newArrayList();
		for (History history : historys) {
			Object hid = history.get("hid");
			if(hid != null){
				worksClickListTemp.add(worksDao.findById(hid));
			}
		}
		List<Map<String, Object>> worksClickList = Lists.newArrayList();
		worksClickList = ParseDemoKit.worksParse(worksClickListTemp);
		
		List<Works> worksUpdateListTemp = worksDao.getWorksInfoByUpdateLimit(10);
		List<Map<String, Object>> worksUpdateList = Lists.newArrayList();
		worksUpdateList = ParseDemoKit.worksParse(worksUpdateListTemp);
		
		//加载底部商品信息
		Goods goods = getModel(Goods.class);
		List<Goods> goodsstemp = goods.getGoodsByNewLimit(4);
		List<Map<String, Object>> goodss = Lists.newArrayList();
		goodss = ParseDemoKit.goodsParse(goodsstemp);
		
		//返回数据
		setAttr("menuid", menuid);
		setAttr("workstype", typeMap);
		setAttr("homepages", homepages);
		setAttr("workss0", workss0);
		setAttr("workss1", workss1);
		setAttr("workss2", workss2);
		setAttr("workss3", workss3);
		setAttr("ofWondersList", ofWondersList);
		setAttr("wonderTypeMenu", wonderTypeMenu);
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
	
	public void forum() {
		setAttr("menuid", "37");
//		keepPara();
		redirect("/community");
//		render("forum.html");
	}
}
