package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.History;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/manhua")
public class ManhuaController extends Controller {

	/** 回复类型 */
	private static String WORKIDTYPE = "80";

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

	public void playVideo() {
		Works worksModel = getModel(Works.class);

		Object worksid = null;
		Object workid = null;

		worksid = getAttr("id");
		if (worksid == null) {
			worksid = getParaToInt("id");
		}
		workid = getAttr("gid");
		if (workid == null) {
			workid = getParaToInt("gid");
		}

		worksModel = worksModel.findById(worksid);
		this.setAttr("works", worksModel);
		Work workModel = getModel(Work.class);

		Work workData = null;

		// 如果没有传动漫作品集数的ID则取第一集播放
		if (workid == null) {
			workData = workModel.getFirstWork(worksid);
		} else {
			workData = workModel.findById(workid);
		}
		// 播放数加1
		History historyModel = getModel(History.class);
		historyModel.saveOrUpdateHitory(workData);
		this.setAttr("work", workData);

		String action = "/comment/setPage";
		setAttr("render", "/ManhuaPlay.html");
		setAttr("targetId", workid);
		setAttr("idtype", WORKIDTYPE);
		forwardAction(action);
	}

	public void updown() {
		try {
			Work workModel = getModel(Work.class);
			String worksid = getPara("worksid");
			String workid = getPara("workid");
			String worknum = getPara("num");
			String state = getPara("state");
			if (StrKit.isBlank(worksid) || StrKit.isBlank(worknum) || StrKit.isBlank(state)) {
				workModel = workModel.findById(workid);
				setAttr("id", String.valueOf(workModel.get("worksid")));
				setAttr("gid", String.valueOf(workModel.get("workid")));
				playVideo();
				return;
			}
			if ("0".equals(state)) {
				workModel = workModel.getUpWork(worksid, worknum);
			}
			if ("1".equals(state)) {
				workModel = workModel.getDownWork(worksid, worknum);
			}
			if (workModel == null || !workModel.isNotEmpty()) {
				workModel = getModel(Work.class).findById(workid);
				setAttr("id", String.valueOf(workModel.get("worksid")));
				setAttr("gid", String.valueOf(workModel.get("workid")));
				playVideo();
				return;
			}
			setAttr("id", String.valueOf(workModel.get("worksid")));
			setAttr("gid", String.valueOf(workModel.get("workid")));
			playVideo();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
