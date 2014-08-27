package com.domeke.app.model;

import java.util.ArrayList;
import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * 商品model对象类<br>
 * 
 * @author chenhailin
 *
 */
@TableBind(tableName = "goods", pkName = "goodsid")
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
		this.save();
	}

	/**
	 * 更新商品信息
	 */
	public void updateGoodsInfo() {
		this.update();
	}

	/**
	 * 获取所有商品信息
	 * 
	 * @return 返回商品所有信息
	 */
	public List<Goods> queryAllGoodsInfo() {
		String querySql = "select * from goods";
		List<Goods> goodsList = this.find(querySql);
		return goodsList == null ? new ArrayList<Goods>() : goodsList;
	}
}
