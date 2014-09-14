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
		Page<Works> list = works.getWorksInfoPage("1", 1, 20);
		setAttr("pageList", list);
		render("/CartoonSubModule.html");
	}

}
