package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.ActionInterceptor;
import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.History;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/manhua")
public class ManhuaController extends Controller {
	public void showDetail() {
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
		Integer worksId = getParaToInt("id");
		// 取某一部动漫
		Works works = worksModel.findById(worksId);
		// 取该动漫的集数
		List<Work> workList = workModel.getWorkByWorksID(worksId);
		// 取精彩推荐的动漫作品
		List<Works> wonderfulWorksList = worksModel.getNewestWorks(worksId, 2, 5);
		
		this.setAttr("works", works);
		this.setAttr("workList", workList);
		this.setAttr("wonderfulWorksList", wonderfulWorksList);
		
		String action = "/comment/setPage";
		setAttr("render", "/ManhuaDtl.html");
		setAttr("targetId", worksId);
		setAttr("idtype", 40);
		forwardAction(action);
	}
	
	@Before(ActionInterceptor.class)
	public void playVideo() {
		Works worksModel = getModel(Works.class);
		Integer worksid = getParaToInt("id");
		worksModel = worksModel.findById(worksid);
		this.setAttr("works", worksModel);
		Work workModel = getModel(Work.class);
		Integer workid = getParaToInt("gid");
		Work workData = null;
		
		// 如果没有传动漫作品集数的ID则取第一集播放
		if (workid == null) {
			workData = workModel.getFirstWork(worksid);
		} else {
			workData = workModel.findById(workid);
		}
		//播放数加1
		History historyModel = getModel(History.class);
		historyModel.saveOrUpdateHitory(workData);
		this.setAttr("work", workData);
		render("/ManhuaPlay.html");
	}
	
	public void updown(){
		try {
			Work workModel = getModel(Work.class);
			String worksid = getPara("worksid");
			String workid = getPara("workid");
			String worknum = getPara("num");
			String state = getPara("state");
			if(StrKit.isBlank(worksid) || StrKit.isBlank(worknum) || StrKit.isBlank(state)){
				workModel = workModel.findById(workid);
				forwardAction("manhua/playVideo?id="+workModel.get("worksid")+"&gid="+workModel.get("workid"));
				return;
			}
			if("0".equals(state)){
				workModel = workModel.getUpWork(worksid, worknum);
			}
			if("1".equals(state)){
				workModel = workModel.getDownWork(worksid, worknum);
			}
			if(!workModel.isNotEmpty()){
				workModel = workModel.findById(workid);
				forwardAction("manhua/playVideo?id="+workModel.get("worksid")+"&gid="+workModel.get("workid"));
				return;
			}
			History historyModel = getModel(History.class);
			historyModel.saveOrUpdateHitory(workModel);
			this.setAttr("work", workModel);
			String url = "playVideo?id="+workModel.get("worksid")+"&gid="+workModel.get("workid");
			forwardAction(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
