package com.domeke.app.controller;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Goods;
import com.domeke.app.model.GoodsType;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

/**
 * 商品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/goods")
@Before(LoginInterceptor.class)
public class GoodsController extends FilesLoadController {
	
	/**
	 * to管理界面
	 */
	public void goToManager() {
		setGoodsPage(null);
		render("/admin/admin_goods.html");
	}
	
	/**
	 * 分页查询
	 */
	public void setGoodsPage(String goods){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Goods> goodsList = null;
		//if ("".equals(menuType) || "0".equals(menuType) || menuType == null){
			goodsList = Goods.dao.findPage(pageNumber, pageSize);
			goods = "0";
		//}else {
			//goodsList = Goods.dao.findPage(pageNumber, pageSize, menuType);
		//}
		setAttr("goods", goods);
		setAttr("goodsList", goodsList);
	}
	
	public void find(){
		goToManager();
	}
	
	/**
	 * 新增商品
	 */
	public void saveGoods(){
		try {
			long timeMillis = System.currentTimeMillis();
			String fileNmae = Long.toString(timeMillis);
			String picturePath = upLoadFile("pic", fileNmae + "\\", 200 * 1024 * 1024, "utf-8");
			String[] strs = picturePath.split("\\\\");
			int leng = strs.length;
			String headimg = upLoadFile("headimg", fileNmae + "\\", 200 * 1024 * 1024, "utf-8");
			Goods goods = getModel(Goods.class);
			goods.set("pic", strs[leng - 1]);
			goods.set("headimg", headimg);
			// 可改为获取当前用户的名字或者ID
			goods.set("creater", 111111);
			goods.set("modifier", 111111);
			goods.saveGoodsInfo();
			goToManager();
		} catch (Exception e) {
			e.printStackTrace();
			render("/admin/admin_addGoods.html");
		}
	}
	
	/**
	 * 遍历文件夹下的图片
	 */
	final static String[] showAllFiles(File dir) {
		File[] fs = dir.listFiles();
		String[] files = null;
		for(int i=0; i<fs.length; i++){
			files[i] = fs[i].getAbsolutePath();
			if(fs[i].isDirectory()){
				try{
					showAllFiles(fs[i]);
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
		return files;
	}
	
	public void addGoods(){
		render("/admin/admin_addGoods.html");
	}
	
	/**
	 * 商品详情
	 */
	public void goodsMessage(){
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsId"));
		setAttr("goods", goods);
		render("/admin/admin_goodsMsg.html");
	}

	/**
	 * 删除商品
	 */
	public void deleteGoods() {
		Goods goods = getModel(Goods.class);
		goods.deleteById(getParaToInt("goodsId"));
		goToManager();
	}
	
	/**
	 * 编辑商品
	 */
	public void updateGoods(){
		Goods goods = getModel(Goods.class);
		goods.updateGoodsInfo();
		goToManager();
	}
	
	/**
	 * 获取类型为types的叶子商品
	 * @param params key=字段名，value=参赛值
	 * @return
	 */
	public List<Goods> getGoodsType(Map<String, Object> params){
		Set<String> key = params.keySet();
		Map<String, Object> gtMap = new HashMap<String, Object>();
		for (Iterator it = key.iterator(); it.hasNext();) {	
			String k = (String) it.next();
			gtMap.put(k, GoodsType.gtDao.getGoodsType(params.get(k).toString()));
		}
		Goods goods = getModel(Goods.class);
		List<Goods> goodsList = goods.goodsType(gtMap);
		return goodsList;		
	}
	
	
	
	public void shop() {
		String goodstype = getPara("goodstype");
		Integer pageNumber = getParaToInt("pageNumber");
		Integer pageSize = getParaToInt("pageSize");
		
		if(pageNumber == null){
			pageNumber = 1;
		}
		if(pageSize == null){
			pageSize = 25;
		}
		
		//获取类型集合
		Map<String,Object> typeMap = new HashMap<String, Object>();
		List<CodeTable> codeTables = CodeKit.getList("goodstype");
		//按类型获取商品分类
		Page<Goods> goodss = getModel(Goods.class).getGoodsPageByType(goodstype, pageNumber, pageSize);
		setAttr("goodstype",goodstype);
		setAttr("goodss",goodss);
		setAttr("codeTables",codeTables);
		render("/ShopCategory.html");
	}
	
	/**
	 * 跳转商品管理界面
	 */
	public void goGoodsMan() {
		String goodsType = getPara("type");
		if (goodsType == null || goodsType.trim().length() == 0) {
			queryAllGoodsInfo();
		} else {
			getGoodsInfoByType(goodsType);
		}
		render("/demo/goodsmanage.html");
	}

	/**
	 * 跳转添加商品界面
	 */
	public void goAddGoods() {
		render("/demo/addgoods.html");
	}

	/**
	 * 保存商品信息<br>
	 * 注：// 图片上传后，以会员的名字作为文件名，路径\core\ upload\goodspic\XXXX\XXXXX.jpg
	 */
	public void save() {
		try {
			String picturePath = upLoadFile("pictrue", "", 200 * 1024 * 1024,
					"utf-8");
			Goods goodsModel = getModel(Goods.class);
			goodsModel.set("pic", picturePath);
			// 可改为获取当前用户的名字或者ID
			goodsModel.set("creater", 111111);
			goodsModel.set("modifier", 111111);
			goodsModel.saveGoodsInfo();
			redirect("/goods/goGoodsMan");
		} catch (Exception e) {
			e.printStackTrace();
			render("/demo/addgoods.html");
		}
	}

	/**
	 * 更新已修的商品
	 */
	public void update() {
		String picturePath = upLoadFile("pictrue", "", 200 * 1024 * 1024,
				"utf-8");
		Goods goodsModel = getModel(Goods.class);
		if (picturePath != null) {
			goodsModel.set("pic", picturePath);
		} else {
			goodsModel.remove("pic");
		}
		// 可改为获取当前用户的名字或者ID
		goodsModel.set("modifier", 111111);
		goodsModel.update();
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 删除商品信息
	 */
	public void delete() {
		Goods goodsModel = getModel(Goods.class);
		goodsModel.deleteById(getParaToInt("id"));
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 查询出所有商品
	 */
	public void queryAllGoodsInfo() {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.queryAllGoodsInfo();
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 根据商品ID获取某商品
	 */
	public void getGoodsById() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("id"));
		setAttr("goods", goods);
		render("/demo/modifygoods.html");
	}

	/**
	 * 根据商品类型获取商品信息
	 */
	public void getGoodsInfoByType(String goodsType) {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.getGoodsInfoByType(goodsType);
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 通过商品名字模糊查询商品信息
	 * 
	 * @param goodsName
	 * @return
	 */
	public void getGoodsInfoByName(String goodsName) {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.getGoodsInfoByName(goodsName);
		this.setAttr("goodslist", goodsList);
	}
	/**
	 * 商品明细
	 */
	public void getGoodsDetail(){
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsid"));
		String goodsattr = String.valueOf(getParaToInt("goodsattr"));
		List<GoodsType> goodsTypes= GoodsType.gtDao.getTypeUrl(goodsattr);
		setAttr("goodsTypes", goodsTypes);
		setAttr("goods", goods);
		Map<String,Object> changeMap = getPeas();
		String isChange = (String)changeMap.get("isChange");	
		setAttr("isChange", isChange);
		setAttr("userId", changeMap.get("userId"));
		render("/ShopDtl.html");
	}
	/**
	 * 
	 */
	public void backGoodsType(){
		int goodstypeid = getParaToInt("goodstypeid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsattr1", goodstypeid);
		List<Goods> goodsList = getGoodsType(map);
		
	}
	/**
	 * 豆豆兑换
	 */
	public void peasChange(){
		Map<String,Object> changeMap = getPeas();
		String isChange = (String)changeMap.get("isChange");
		int dougprice = (int)changeMap.get("dougprice");
		User user = getSessionAttr("user");
		Long userId = user.get("userid");
		user = user.findById(userId);
		//豆子
		Long peas = (Long)changeMap.get("peas");
		if (dougprice <= peas){
			isChange = "1";
		}
		if (isChange == "1" || "1".equals(isChange)){
			peas = peas - dougprice;
			User.dao.updatePeas(userId, peas);
		}
	}
	/**
	 * 获得豆豆
	 */
	public Map<String,Object> getPeas(){
		int dougprice = getParaToInt("dougprice");
		User user = getSessionAttr("user");
		Long userId = user.get("userid");
		user = user.findById(userId);
		String isChange = "0";
		//豆子
		Long peas = user.get("peas");
		if (dougprice <= peas){
			isChange = "1";
		}
		Map<String,Object> changeMap = new HashMap<String, Object>();
		changeMap.put("isChange", isChange);
		changeMap.put("dougprice", dougprice);
		changeMap.put("peas", peas);
		changeMap.put("userId", userId);
		return changeMap;
	}
}
