package com.domeke.app.controller;

import com.domeke.app.model.Goods;
import com.domeke.app.model.GoodsType;
import com.domeke.app.model.Menu;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@ControllerBind(controllerKey="goodstype")
public class GoodsTypeController extends Controller {
	
	public void goToManager() {
		setMenuPage(null);
		render("/admin/admin_goodstype.html");
	}
	
	/**
	 * 分页查询
	 */
	public void setMenuPage(String goodstype){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<GoodsType> gtList = null;
		if ("".equals(goodstype) || "0".equals(goodstype) || goodstype == null){
			gtList = GoodsType.gtDao.findPage(pageNumber, pageSize);
			goodstype = "0";
		}else {
			gtList = GoodsType.gtDao.findPage(pageNumber, pageSize, goodstype);
		}
		setAttr("goodstype", goodstype);
		setAttr("gtList", gtList);
	}
	
	/**
	 * 分页查询
	 */
	public void find() {
		String goodsType = getPara("goodsType");
		setMenuPage(goodsType);
		render("/admin/admin_gtPage.html");
	}	
	
	/**
	 * 跳转新增类型界面
	 */
	public void addGoodsType(){
		render("/admin/admin_addgoodstype.html");
	}
	
	/**
	 * 新增类型
	 */
	public void saveGoodsType(){
		GoodsType goodsType = getModel(GoodsType.class);
		goodsType.saveGoodsType();
		goToManager();
	}
}
