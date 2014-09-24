package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author chenzhicong
 *
 */
@TableBind(tableName="goods_type", pkName="goodstypeid")
public class GoodsType extends Model<GoodsType> {

	private static final long serialVersionUID = -4324755844654141029L;
	
	public static GoodsType gtDao = new GoodsType();
	
	/**
	 * 更新商品类型
	 */
	public void updateGoodsType() {
		this.update();
	}
	
	/**
	 * 根据商品类型id删除
	 * @param goodsTypeId
	 */
	public void deleteGoodsTypeById(int goodsTypeId) {
		this.deleteById(goodsTypeId);
	}
	
	/**
	 * 新增商品分类
	 */
	public void saveGoodsType(){
		this.save();
	}
}
