package com.domeke.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Goods;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "/")
public class IndexController extends Controller {

	public void index() {
		WorksType worksTypeModel = getModel(WorksType.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(0);
		
		Works worksDao = getModel(Works.class);
		Map<String, Object> typeMap = Maps.newHashMap();
		for (WorksType worksType : worksTypes) {
			typeMap.put(worksType.get("id").toString(), worksType.get("name"));
		}
		//加载首页5个循环显示
		List<Works> workss0Temp = worksDao.getHomePage(5);
		List<Map<String, Object>> workss0 = Lists.newArrayList();
		workss0 = this.worksParse(workss0Temp);
		
		//加载中间数据
		List<Works> workss1temp = worksDao.getWorksInfoByType(worksTypes.get(0).get("id").toString(),7);
		List<Map<String, Object>> workss1 = Lists.newArrayList();
		workss1 = this.worksParse(workss1temp);
		
		List<Works> workss2temp = worksDao.getWorksInfoByType(worksTypes.get(1).get("id").toString(),5);
		List<Map<String, Object>> workss2 = Lists.newArrayList();
		workss2 = this.worksParse(workss2temp);
		
		List<Works> workss3temp = worksDao.getWorksInfoByType(worksTypes.get(2).get("id").toString(),5);
		List<Map<String, Object>> workss3 = Lists.newArrayList();
		workss3 = this.worksParse(workss3temp);
		
		List<Works> workss4temp = worksDao.getWorksInfoByType(worksTypes.get(3).get("id").toString(),10);
		List<Map<String, Object>> workss4 = Lists.newArrayList();
		workss4 = this.worksParse(workss4temp);
		
		
		//加载右边列表
		List<Works> worksClickListTemp = worksDao.getWorksInfoByPageViewsLimit(9);
		List<Map<String, Object>> worksClickList = Lists.newArrayList();
		worksClickList = this.worksParse(worksClickListTemp);
		
		List<Works> worksUpdateListTemp = worksDao.getWorksInfoByUpdateLimit(10);
		List<Map<String, Object>> worksUpdateList = Lists.newArrayList();
		worksUpdateList = this.worksParse(worksUpdateListTemp);
		
		//加载底部商品信息
		Goods goods = getModel(Goods.class);
		List<Goods> goodsstemp = goods.getGoodsByNewLimit(4);
		List<Map<String, Object>> goodss = Lists.newArrayList();
		goodss = this.goodsParse(goodsstemp);
		
		//返回数据
		setAttr("workss0", workss0);
		setAttr("workstype", typeMap);
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
	
	public void forum() {
		setAttr("menuid", "37");
//		keepPara();
		redirect("/community");
//		render("forum.html");
	}
	private List<Map<String, Object>> worksParse(List<Works> workss){
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Works works : workss) {
				if(works == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				Object worksid = works.get("worksid");
				map.put("worksid", worksid);
				String worksname = works.get("worksname");
				if(worksname.length() > 12){
					worksname = worksname.substring(0,12);
				}
				map.put("worksname", worksname);
				map.put("workstype", works.get("workstype"));
				map.put("pageviews", works.get("pageviews"));
				map.put("comment", works.get("comment"));
				map.put("pageviews", works.get("pageviews"));
				String cover = works.get("cover");
				map.put("cover", cover);
				String playUrl = "";
				String detailUrl = "";
				Integer type = works.getInt("type");
				if(type == 0){
					//0专辑 
					playUrl = "cartoon/playVideo?id="+worksid;
					detailUrl = "cartoon/showDetail?id="+worksid;
				}else if(type == 1){
					 //视频
					playUrl = "cartoon/playVideo?id="+worksid;
					detailUrl = playUrl;
				}
				map.put("playUrl",playUrl);
				map.put("detailUrl",detailUrl);
				String typeUrl = "cartoon/showWorksList?wtype="+works.get("workstype");
				map.put("typeUrl",typeUrl);
				String describle = works.get("describle");
				if(describle.length() > 12){
					describle = describle.substring(0, 12) + "...";
				}
				map.put("describle",describle);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	private List<Map<String, Object>> goodsParse(List<Goods> goodss){
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Goods goods : goodss) {
				if(goods == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsid", goods.get("goodsid"));
				String goodsname = goods.get("goodsname");
				if(goodsname.length() >= 12){
					goodsname = goodsname.substring(0,12);
				}
				map.put("goodsname", goodsname);
				map.put("price", goods.get("goodsid"));
				map.put("oldprice", goods.get("oldprice"));
				map.put("amount", goods.get("amount"));
				map.put("pic", goods.get("pic"));
				String message = goods.get("message");
				if(message.length() > 50){
					message = message.substring(0, 50) + "...";
				}
				map.put("message", message);
				map.put("submitdate", goods.get("submitdate"));
				map.put("username", goods.get("username"));
				map.put("headimg", goods.get("headimg"));
				String detailUrl = "goods/getGoodsDetail?id=5";
				map.put("detailUrl", detailUrl);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
