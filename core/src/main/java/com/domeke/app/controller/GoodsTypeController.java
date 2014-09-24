package com.domeke.app.controller;

import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

/**
 * @author chenzhicong
 *
 */
@ControllerBind(controllerKey="goodstype")
public class GoodsTypeController extends Controller {
	
	public void goToManager() {
		render("#");
	}
}
