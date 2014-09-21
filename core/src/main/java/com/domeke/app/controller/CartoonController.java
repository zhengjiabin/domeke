/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.ActionInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.utils.CodeKit;
import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author lijiasen@domeke.com
 *
 */
public class CartoonController extends Controller {

	public void index() {
		setAttr("menuid", "2");
		Works worksModel = getModel(Works.class);
		List<CodeTable> wtypeCTList = CodeKit.getList("workstype");
		setAttr("wtypeCTList", wtypeCTList);
		List<Works> worksList = Lists.newArrayList();
		// 用于显示“大家都爱看”列表
		worksList = worksModel.getWorksInfoByPageViewsLimit(5);
		setAttr("olikeWorksList", worksList);
		// 查出每一种类型的动漫作品
		for (CodeTable wtypeCT : wtypeCTList) {
			String workstype = wtypeCT.get("codekey");
			worksList = worksModel.getWorksInfoByType(workstype, 10);
			setAttr("worksList" + workstype, worksList);
		}
		render("/CartoonCategory.html");
	}

	/**
	 * 根据动漫作品的类型显示动漫作品的列表<br>
	 * 请求：cartoon/showWorksList?wtype=${works.workstype!}&pnum=xx <br>
	 * wtype默认“10”；pnum默认1
	 */
	public void showWorksList() {
		Works works = getModel(Works.class);
		String workstype = getPara("wtype", "10");
		Integer pageNum = getParaToInt("pnum", 1);
		Page<Works> list = works.getWorksInfoPage(workstype, pageNum, 14);
		setAttr("pageList", list);
		render("/CartoonSubModule.html");
	}

	/**
	 * 根据动漫作品的ID，显示动漫的明细<br>
	 * 请求：cartoon/showDetail?id=${works.worksid!}
	 */
	public void showDetail() {
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
		// 取某一部动漫
		Works works = worksModel.findById(getParaToInt("id"));
		// 取该动漫的集数
		List<Work> workList = workModel.getWorkByWorksID(getParaToInt("id"));
		// 取精彩推荐的动漫作品
		List<Works> wonderfulWorksList = worksModel.getWonderfulRecommend(
				getParaToInt("id"), 5);
		this.setAttr("works", works);
		this.setAttr("workList", workList);
		this.setAttr("wonderfulWorksList", wonderfulWorksList);
		render("/CartoonDtl.html");
	}

	/**
	 * 明细页面点击动漫链接，转播放<br>
	 * cartoon/playVideo?id=${works.worksid!} or
	 * cartoon/playVideo?id=${works.worksid!}&gid=${work.workid!}
	 */
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
		this.setAttr("work", workData);
		render("/CartoonPlay.html");
	}

	/**
	 * 点赞
	 */
	public void pointPraise() {
		Works worksModel = getModel(Works.class);
		Integer worksid = getParaToInt("id");
		worksModel.addPraise(worksid);
	}
}
