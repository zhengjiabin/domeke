/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author lijiasen@domeke.com
 *
 */
public class CartoonController extends Controller {

	public void index() {
		setAttr("menuid", "2");
		Works works = getModel(Works.class);
		Page<Works> list = works.getWorksInfoPage("10", 1, 14);
		setAttr("pageList", list);
		render("/CartoonSubModule.html");
	}

	/**
	 * 显示动漫的明细CartoonDetail<br>
	 * <a href="cartoon/showDetail?id=${works.worksid!}">
	 */
	public void showDetail() {
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
		// 取某一部动漫
		Works works = worksModel.findById(getParaToInt("id"));
		// 取该动漫的集数
		List<Work> workList = workModel.getWorkByWorksID(getParaToInt("id"));
		// 取精彩推荐的动漫作品
		List<Works> wonderfulWorksList = worksModel.getWorksByStatistics(10);
		this.setAttr("works", works);
		this.setAttr("workList", workList);
		this.setAttr("wonderfulWorksList", wonderfulWorksList);
		render("/CartoonDtl.html");
	}

	/**
	 * 明细页面点击动漫，转播放<br>
	 * cartoon/playVideo?id=${works.worksid!}
	 */
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
		this.setAttr("work", workData);
		render("/CartoonPlay.html");
	}

}
