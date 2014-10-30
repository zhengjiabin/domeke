package com.domeke.app.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

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
		String querySql = "select * from goods order by istop limit "+limit;
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
	public Page<Goods> getGoodsPageByType(String goodstype,Integer pageNumber,Integer pageSize){
		String querySql = "";
		Page<Goods> goodss;
		if(StrKit.isBlank(goodstype)){
			querySql = "from goods order by istop desc";
			goodss = this.paginate(pageNumber, pageSize, "select *", querySql);
		}else {
			querySql = "from goods where goods=? order by istop desc";
			goodss = this.paginate(pageNumber, pageSize, "select *", querySql,goodstype);
		}
		return goodss;
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
	public Page<Goods> findPage(int pageNumber, int pageSize) {
		Page<Goods> goodsPage = this.paginate(pageNumber, pageSize, "select *",
				"from goods group by goodsid");
		return goodsPage;
	}
	
	/**
	 * 根据商品类型获取商品
	 * @param params
	 * @return
	 */
	public List<Goods> goodsType(Map<String, Object> params) {
		String sql = "select * from goods ";
		Set<String> key = params.keySet();
		int row = 0;
		for (Iterator it = key.iterator(); it.hasNext();) {			
			String k = (String) it.next();
			if (row == 0) {
				row++;
				sql = sql + "where " + k + " in (" + params.get(k).toString() + ") ";
			} else {
				sql = sql + "and " + k + " in (" + params.get(k).toString() + ") ";
			}			
		}
		List<Goods> goodsType = this.find(sql);
		return goodsType;
	}
	
	public String getHeadImg(String goodsId) {
		return Db.queryStr("select headimg from goods where goodsid = " + goodsId);
	}
	/**
	 * 更新商品喜欢次数
	 * @param goodsId
	 */
	public void updateLoveCount(int goodsId){
		String sql="update goods set sumlove=sumlove+1 where goodsid='"+goodsId+"'";
		Db.update(sql);
	}
	/**
	 * 获得商品喜欢次数
	 * @param goodsId
	 * @return
	 */
	public int getSumLove(int goodsId) {
		return Db.queryInt("select sumlove from goods where goodsid = '" + goodsId+"'");
	}
	public Goods getUserForId(Long goodsid){
		String sql = "select * from goods where goodsid = '"+goodsid+"'";
		Goods goods = dao.findFirst(sql);
		return goods;
	}
}
