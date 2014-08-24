package com.domeke.app.controller;

import java.io.File;
import java.util.List;

import com.domeke.app.model.Goods;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * 商品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/goods")
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
			// 图片上传后，以会员的名字作为文件名
			UploadFile uploadFile = getFile("picture", "chenhailin",
					200 * 1024 * 1024, "utf-8");
			File picture = uploadFile.getFile();
			String fileName = picture.getName();
			String type = "jpg";
			if (fileName != null && fileName.indexOf('.') > 0) {
				String[] temp = fileName.split("\\.");
				type = temp[temp.length - 1];
			}
			File replace = new File(uploadFile.getSaveDirectory() + "\\"
					+ System.currentTimeMillis() + "." + type);
			picture.renameTo(replace);
			String picturePath = replace.getAbsolutePath();
			Goods goodsModel = getModel(Goods.class);
			goodsModel.set("pic", picturePath);
			// 可改为获取当前用户的名字或者ID
			goodsModel.set("creater", 111111);
			goodsModel.set("modifier", 111111);
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
