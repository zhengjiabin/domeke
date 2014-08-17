package com.domeke.app.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 商品model对象类<br>
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
	public void saveGoodsInfo(){
		this.set("creater", "chenhailin");
		this.set("modifier", "chenhailin");
		this.save();
	}
}
