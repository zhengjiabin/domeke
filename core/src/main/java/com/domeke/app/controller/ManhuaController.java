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

@ControllerBind(controllerKey = "/manhua")
@Before(LoginInterceptor.class)
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
		Works worksData = worksModel.findById(worksid);
		this.setAttr("works", worksData);
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
}
