/**
 * 
 */
package com.domeke.app.controller;

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
	 * 显示动漫的明细
	 */
	public void showDetail() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		this.setAttr("works", works);
		render("/CartoonDtl.html");
	}

	/**
	 * <a href="cartoon/playVideo?id=${works.worksid!}"><br>
	 * 明细页面点击动漫，转播放
	 */
	public void playVideo() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		this.setAttr("works", works);
		render("/CartoonPlay.html");
	}

}
