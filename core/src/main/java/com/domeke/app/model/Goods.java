package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
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
	 * @return 返回所有商品信息
	 */
	public List<Goods> queryAllGoodsInfo() {
		String querySql = "select * from goods";
		List<Goods> goodsList = this.find(querySql);
		return goodsList == null ? Lists.newArrayList() : goodsList;
	}

	/**
	 * 根据商品类型返回商品信息
	 * 
	 * @param goodsType
	 *            商品类型
	 * @return 返回对应类型的商品信息
	 */
	public List<Goods> getGoodsInfoByType(String goodsType) {
		String querySql = "select * from goods where goods=?";
		List<Goods> goodsList = this.find(querySql, goodsType);
		return goodsList == null ? Lists.newArrayList() : goodsList;
	}
	
	public List<Goods> getGoodsByNewLimit(Integer limit) {
		String querySql = "select * from goods where goods=? limit "+limit;
		List<Goods> goodsList = this.find(querySql);
		return goodsList == null ? Lists.newArrayList() : goodsList;
	}
	

	/**
	 * 通过商品名字模糊查询商品信息
	 * 
	 * @param goodsName
	 *            商品名字
	 * @return 符合商品名字的相关商品信息
	 */
	public List<Goods> getGoodsInfoByName(String goodsName) {
		String querySql = "select * from goods where goodsname like '"
				+ goodsName + "'";
		List<Goods> goodsList = this.find(querySql);
		return goodsList == null ? Lists.newArrayList() : goodsList;
	}
}
