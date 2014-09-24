package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@TableBind(tableName="goodstype", pkName="goodstypeid")
public class GoodsType extends Model<GoodsType> {

	private static final long serialVersionUID = -4324755844654141029L;
	
	public static GoodsType gtDao = new GoodsType();
	
	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<GoodsType> findPage(int pageNumber, int pageSize) {
		Page<GoodsType> gtPage = this.paginate(pageNumber, pageSize, "select *",
				"from goodstype group by goodstypeid");
		return gtPage;
	}
	
	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<GoodsType> findPage(int pageNumber, int pageSize, String goodstype) {
		Page<GoodsType> page = this.paginate(pageNumber, pageSize, "select *",
				"from goodstype where goodstype=? group by goodstypeid", goodstype);
		return page;
	}
	
	
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
