package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Goods;
import com.jfinal.core.Controller;

/**
 * 商品model控制器
 * 
 * @author chenhailin
 *
 */
public class GoodsController extends Controller {

	/**
	 * 跳转商品管理界面
	 */
	public void goGoodsMan() {
		query();
		render("/demo/goodsmanage.html");
	}

	/**
	 * 跳转添加商品界面
	 */
	public void goAddGoods() {
		render("/demo/addgoods.html");
	}

	/**
	 * 保存商品信息
	 */
	public void save() {
		try {
			Goods goodsModel = getModel(Goods.class);
			goodsModel.saveGoodsInfo();
			goGoodsMan();
		} catch (Exception e) {
			e.printStackTrace();
			render("/demo/addgoods.html");
		}
	}

	/**
	 * 查询出所有商品
	 */
	public void query() {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.queryAllGoodsInfo();
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 更新已修的商品
	 */
	public void update() {
		Goods goodsModel = getModel(Goods.class);
		goodsModel.update();
		goGoodsMan();
	}

	/**
	 * 删除商品信息
	 */
	public void delete() {
		Goods goodsModel = getModel(Goods.class);
		goodsModel.deleteById(getParaToInt());
		goGoodsMan();
	}

	/**
	 * 根据商品ID获取某商品
	 */
	public void getGoodsById() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt());
		setAttr("goods", goods);
		render("/demo/modifygoods.html");
	}
}
