package com.domeke.app.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * 商品model对象类<br>
 * 
 * @author chenhailin
 *
 */
public class Goods extends Model<Goods> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4324755844654141029L;

	public static Goods dao = new Goods();

	/**
	 * 保存数据信息到数据库
	 */
	public void saveGoodsInfo() {
		// 可改为获取当前用户的名字或者ID
		this.set("creater", "chenhailin");
		this.set("modifier", "chenhailin");
		this.save();
	}

	/**
	 * 获取所有商品信息
	 * @return 返回商品所有信息
	 */
	public List<Goods> queryAllGoodsInfo() {
		String querySql = "select * from goods";
		List<Goods> goodsList = dao.find(querySql);
		return goodsList == null ? new ArrayList<Goods>() : goodsList;
	}
}
