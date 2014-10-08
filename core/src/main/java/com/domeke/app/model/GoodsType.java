package com.domeke.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@TableBind(tableName="goods_type", pkName="goodstypeid")
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
				"from goods_type group by goodstypeid");
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
				"from goods_type where goodstype=? group by goodstypeid", goodstype);
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
	
	/**
	 * 根据id查询类型
	 * @param goodsTypeId
	 * @return
	 */
	public GoodsType getGoodsTypeById(int goodsTypeId) {
		GoodsType goodsType = this.findById(goodsTypeId);
		return goodsType;
	}
	
	/**
	 * 根据id删除类型
	 * @param goodsTypeId
	 */
	public void deleteGoodsType(int goodsTypeId) {
		this.deleteById(goodsTypeId);
	}
		
	/**
	 * 根据商品类型id查询出返回改类型下的所有子类型
	 * @param strs
	 * @return
	 */
	public String getGoodsType(String strs){		
		//获取strs级的叶子
		List<GoodsType> gtList = this.find("select * from goods_type where parenttypeid in ("+strs+")");		
		if (gtList.size() == 0) {
			return strs;
		}		
		strs = "";
		int row = 0;
		for (GoodsType gt:gtList) {
			if (row != gtList.size() - 1){
				strs = strs + Long.toString(gt.getLong("goodstypeid")) + ",";
			} else {
				strs = strs + Long.toString(gt.getLong("goodstypeid"));
			}
			row++;			
		}
		strs = getGoodsType(strs);
		return strs;
	}

	/**
	 * 根据商品id返回倒序的商品分类顺序
	 * @param goodsTypeId
	 * @return
	 */
	public List<GoodsType> getTypeUrl(String goodsTypeId) {
		List<GoodsType> goodsTypeList = new ArrayList<GoodsType>();
		int row = 0;
		while (row < 1) {
			GoodsType gt = this.findById(goodsTypeId);
			goodsTypeList.add(gt);
			goodsTypeId = String.valueOf(gt.get("parenttypeid"));
			if ("null".equals(goodsTypeId) || "".equals(goodsTypeId) || goodsTypeId == null) {
				break;
			}
		}
		Collections.reverse(goodsTypeList);
		return goodsTypeList;
	}
}
