/**
 * 
 */
package com.domeke.app.controller;

import com.jfinal.core.Controller;

/**
 * @author lijiasen@domeke.com
 *
 */
public class CartoonController extends Controller {

	public void index() {
		setAttr("menuid", "2");

		render("/CartoonSubModule.html");
	}

}
