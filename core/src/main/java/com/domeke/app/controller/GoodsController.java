package com.domeke.app.controller;

import com.domeke.app.model.Goods;
import com.jfinal.core.Controller;

/**
 * 商品model控制器
 * @author chenhailin
 *
 */
public class GoodsController extends Controller {
	
	/**
	 * 访问路劲跳转
	 */
	public void go(){
		render("/demo/addgoods.html");
	}
	
	/**
	 * 保存商品信息
	 */
	public void save(){
		Goods goods = getModel(Goods.class);
		goods.saveGoodsInfo();
	}
}
