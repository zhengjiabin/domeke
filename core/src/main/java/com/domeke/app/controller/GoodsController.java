package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Goods;
import com.domeke.app.route.ControllerBind;

/**
 * 商品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/goods")
public class GoodsController extends FilesLoadController {

	/**
	 * to管理界面
	 */
	public void goToManager() {
		render("/admin/goods.html");
	}

	/**
	 * 跳转商品管理界面
	 */
	public void goGoodsMan() {
		String goodsType = getPara("type");
		if (goodsType == null || goodsType.trim().length() == 0) {
			queryAllGoodsInfo();
		} else {
			getGoodsInfoByType(goodsType);
		}
		render("/demo/goodsmanage.html");
	}

	/**
	 * 跳转添加商品界面
	 */
	public void goAddGoods() {
		render("/demo/addgoods.html");
	}

	/**
	 * 保存商品信息<br>
	 * 注：// 图片上传后，以会员的名字作为文件名，路径\core\ upload\goodspic\XXXX\XXXXX.jpg
	 */
	public void save() {
		try {
			String picturePath = upLoadFile("pictrue", 200 * 1024 * 1024, "utf-8");
			Goods goodsModel = getModel(Goods.class);
			goodsModel.set("pic", picturePath);
			// 可改为获取当前用户的名字或者ID
			goodsModel.set("creater", 111111);
			goodsModel.set("modifier", 111111);
			goodsModel.saveGoodsInfo();
			redirect("/goods/goGoodsMan");
		} catch (Exception e) {
			e.printStackTrace();
			render("/demo/addgoods.html");
		}
	}

	/**
	 * 更新已修的商品
	 */
	public void update() {
		String picturePath = upLoadFile("pictrue", 200 * 1024 * 1024, "utf-8");
		Goods goodsModel = getModel(Goods.class);
		if (picturePath != null) {
			goodsModel.set("pic", picturePath);
		} else {
			goodsModel.remove("pic");
		}
		// 可改为获取当前用户的名字或者ID
		goodsModel.set("modifier", 111111);
		goodsModel.update();
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 删除商品信息
	 */
	public void delete() {
		Goods goodsModel = getModel(Goods.class);
		goodsModel.deleteById(getParaToInt("id"));
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 查询出所有商品
	 */
	public void queryAllGoodsInfo() {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.queryAllGoodsInfo();
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 根据商品ID获取某商品
	 */
	public void getGoodsById() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("id"));
		setAttr("goods", goods);
		render("/demo/modifygoods.html");
	}

	/**
	 * 根据商品类型获取商品信息
	 */
	public void getGoodsInfoByType(String goodsType) {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.getGoodsInfoByType(goodsType);
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 通过商品名字模糊查询商品信息
	 * 
	 * @param goodsName
	 * @return
	 */
	public void getGoodsInfoByName(String goodsName) {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.getGoodsInfoByName(goodsName);
		this.setAttr("goodslist", goodsList);
	}

}
