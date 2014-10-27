package com.domeke.app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.domeke.app.model.Goods;
import com.domeke.app.model.GoodsType;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * 商品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/goods")
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
	public void setGoodsPage(String goods) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Goods> goodsList = null;
		// if ("".equals(menuType) || "0".equals(menuType) || menuType == null){
		goodsList = Goods.dao.findPage(pageNumber, pageSize);
		goods = "0";
		// }else {
		// goodsList = Goods.dao.findPage(pageNumber, pageSize, menuType);
		// }
		setAttr("goods", goods);
		setAttr("goodsList", goodsList);
	}

	public void find() {
		setGoodsPage(null);
		render("/admin/admin_goodsPage.html");
	}

	/**
	 * 新增商品
	 */
	public void saveGoods() {
		try {
			long timeMillis = System.currentTimeMillis();
			String fileNmae = Long.toString(timeMillis);
			String picturePath = upLoadFileNotDealPath("pic", fileNmae, 200 * 1024 * 1024, "utf-8");
			String[] strs = picturePath.split("\\\\");
			int leng = strs.length;
			String headimg = upLoadFilesNotDealPath(fileNmae, 200 * 1024 * 1024, "utf-8");
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
		if (fs != null) {
			files = new String[fs.length];
			for (int i = 0; i < fs.length; i++) {
				files[i] = fs[i].getAbsolutePath();
				if (fs[i].isDirectory()) {
					try {
						showAllFiles(fs[i]);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		return files;
	}
	
	/**
	 * 获取指定路径下的所有文件域路径
	 * @param url 指定文件夹路径
	 * @return 返回指定路径下的所有文件域路径数组
	 */
	public List<String> getFileUrls(String url){
		File dir = new File(url);
		List<String> fileUrls = new ArrayList<String>();
		String[] files = this.showAllFiles(dir);
		for (int i =0; i < files.length; i ++) {
//			fileUrls.add(getDomainNameFilePath(files[i]));
			fileUrls.add(files[i]);
		}
		return fileUrls;
	}
	
	/**
	 * 删除指定文件夹下的指定文件
	 * @param url 文件夹路径
	 * @param fileName 文件名称
	 */
	public void deleteFile(String url, String fileName){
		File folder = new File(url);
		File[] files = folder.listFiles();
		for(File file:files){
			if(file.getName().equals(fileName)){
				file.delete();
			}
		}		
	}
	
	/**
	 * 删除图片
	 */
	public void deleteImg() {
		String url = getPara("url");
		String urlFile = url.substring(0, url.lastIndexOf("\\"));
		String[] fileNames = url.split("\\\\");
		String fileName = fileNames[fileNames.length - 1];
		deleteFile(urlFile, fileName);
		Goods goods = Goods.dao.findById(getParaToInt("goodsId"));
		String headimg = goods.getStr("headimg");
		List<String> headimgs = this.getFileUrls(headimg);		
		setAttr("headimgs", headimgs);
		setAttr("goods", goods);
		render("/admin/admin_goodsMsg.html");
	}

	public void addGoods() {
		render("/admin/admin_addGoods.html");
	}

	/**
	 * 商品详情
	 */
	public void goodsMessage() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsId"));
		String headimg = goods.getStr("headimg");
		List<String> headimgs = new ArrayList<String>();
		if (headimg != null && !"".equals(headimg)) {
			headimgs = this.getFileUrls(headimg);	
		}
		setAttr("headimgs", headimgs);
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
	public void updateGoods() {
		String goodsId = getPara("goodsId");
		String headimg = Goods.dao.getHeadImg(goodsId);
		String[] headimgs2 = headimg.split("/");
		String fileName = headimgs2[headimgs2.length - 1];
		String picturePath = upLoadFileNotDealPath("pic", fileName, 200 * 1024 * 1024, "utf-8");
		String[] strs = picturePath.split("/");
		int leng = strs.length;
		String headimg2 = upLoadFilesNotDealPath(fileName, 200 * 1024 * 1024, "utf-8");
		UploadFile file = getFile();
		Goods gs = getModel(Goods.class);
		if (leng > 1) {
			gs.set("pic", strs[leng - 1]);
		}
		if (headimg2 != null) {
			gs.updateGoodsInfo();
			goToManager();
		} else {
			Goods goods = gs.findById(goodsId);
			String hg = goods.getStr("headimg");
			List<String> headimgs = this.getFileUrls(hg);		
			setAttr("headimgs", headimgs);
			setAttr("goods", goods);
			render("/admin/admin_goodsMsg.html");
		}
		/*
		String goodsId = Long.toString(gs.getLong("goodsid"));		
		String headimg = gs.getHeadImg(goodsId);
		String[] headimgs2 = headimg.split("/");
		String fileName = headimgs2[headimgs2.length - 1];
		String picturePath = upLoadFileNotDealPath("pic", fileName, 200 * 1024 * 1024, "utf-8");
		String[] strs = picturePath.split("/");
		int leng = strs.length;
		gs.set("pic", strs[leng - 1]);
		String headimg2 = upLoadFilesNotDealPath(fileName, 200 * 1024 * 1024, "utf-8");
		
//		int row = headimg.lastIndexOf("/") + 1; 
//		headimg = headimg.substring(row, headimg.length()-1);
//		String headimg2 = upLoadFilesNotDealPath(headimg, 200 * 1024 * 1024, "utf-8");
		
		if (headimg2 != null) {
			gs.updateGoodsInfo();
			goToManager();
		} else {
			Goods goods = gs.findById(goodsId);
			String hg = goods.getStr("headimg");
			List<String> headimgs = this.getFileUrls(hg);		
			setAttr("headimgs", headimgs);
			setAttr("goods", goods);
			render("/admin/admin_goodsMsg.html");
		}
		*/
	}

	/**
	 * 获取类型为types的叶子商品
	 * @param params key=字段名，value=参赛值
	 * @return
	 */
	public List<Goods> getGoodsType(Map<String, Object> params) {
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
		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 25;
		}

		// 获取类型集合
		List<GoodsType> goodsTypeStack = Lists.newArrayList();
		List<GoodsType> goodsTypeList = Lists.newArrayList();
		GoodsType goodsTypeModel = getModel(GoodsType.class);
		if (!StrKit.isBlank(goodstype)) {
			goodsTypeModel = getModel(GoodsType.class).getGoodsTypeById(Integer.parseInt(goodstype));
		}
		if (goodsTypeModel.get("goodstypeid") != null) {
			goodsTypeStack.add(goodsTypeModel);
			String goodstypeid = String.valueOf(goodsTypeModel.get("parenttypeid"));
			while (!StrKit.isBlank(goodstypeid)) {
				GoodsType goodsType = getModel(GoodsType.class).getGoodsTypeById(Integer.parseInt(goodstypeid));
				goodsTypeStack.add(goodsType);
				goodstypeid = String.valueOf(goodsType.get("parenttypeid"));
			}
			Collections.reverse(goodsTypeStack);
		}
		if (!goodsTypeStack.isEmpty()) {
			goodsTypeModel = goodsTypeStack.get(goodsTypeStack.size() - 1);
			goodsTypeList = goodsTypeModel.getGoodsTypeByParId(String.valueOf(goodsTypeModel.get("goodstypeid")));
		} else {
			goodsTypeList = goodsTypeModel.getGoodsTypeByParId("");
		}

		// 按类型获取商品分类
		Page<Goods> goodss = getModel(Goods.class).getGoodsPageByType(goodstype, pageNumber, pageSize);
		List<Map<String, Object>> datas = goodsParse(goodss.getList());
		Page<List<Map<String, Object>>> pageMap = new Page(datas, goodss.getPageNumber(), goodss.getPageSize(),
				goodss.getTotalPage(), goodss.getTotalRow());
		setAttr("goodsTypeStack", goodsTypeStack);
		setAttr("goodsTypeList", goodsTypeList);
		setAttr("goodstype", goodstype);
		setAttr("goodss", pageMap);
		render("/shop.html");
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
			String picturePath = upLoadFileNotDealPath("pictrue", "", 200 * 1024 * 1024, "utf-8");
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
		String picturePath = upLoadFileNotDealPath("pictrue", "", 200 * 1024 * 1024, "utf-8");
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
		String isChange = "";
		if (changeMap != null){
			isChange = (String)changeMap.get("isChange");	
			setAttr("userId", changeMap.get("userId"));
		}
		setAttr("isChange", isChange);
		render("/ShopCentre.html");
	}

	/**
	 * 
	 */
	public void backGoodsType() {
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
		if(user == null){
			return;
		} 
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
			if (peas<=0)
			isChange = "0";
		}
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsid"));
		String goodsattr = String.valueOf(getParaToInt("goodsattr"));
		List<GoodsType> goodsTypes= GoodsType.gtDao.getTypeUrl(goodsattr);
//		String tamllurl = getPara("tamllurl");
//		List<String> headimgs = this.getFileUrls(tamllurl);
//		setAttr("images", headimgs);
		setAttr("goodsTypes", goodsTypes);
		setAttr("goods", goods);	
		setAttr("isChange", isChange);
		setAttr("userId", changeMap.get("userId"));
		render("/ShopDtl.html");
	}

	/**
	 * 获得豆豆
	 */
	public Map<String, Object> getPeas() {
		int dougprice = getParaToInt("dougprice");
		User user = getSessionAttr("user");
		if(user == null){
			return null;
		} 
		Long userId = user.get("userid");
		user = user.findById(userId);
		String isChange = "0";
		// 豆子
		Long peas = user.get("peas");
		if (dougprice <= peas) {
			isChange = "1";
		}
		Map<String, Object> changeMap = new HashMap<String, Object>();
		changeMap.put("isChange", isChange);
		changeMap.put("dougprice", dougprice);
		changeMap.put("peas", peas);
		changeMap.put("userId", userId);
		return changeMap;
	}

	private List<Map<String, Object>> goodsParse(List<Goods> goodss) {
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Goods goods : goodss) {
				if (goods == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsid", goods.get("goodsid"));
				String goodsname = goods.get("goodsname");
				if (goodsname.length() >= 12) {
					goodsname = goodsname.substring(0, 12);
				}
				map.put("goods", goods.get("goods"));
				map.put("goodsname", goodsname);
				map.put("price", goods.get("goodsid"));
				map.put("oldprice", goods.get("oldprice"));
				map.put("amount", goods.get("amount"));
				map.put("pic", goods.get("pic"));
				map.put("goodsattr1", goods.get("goodsattr1"));
				map.put("dougprice", goods.get("dougprice"));
				String message = goods.get("message");
				if (message.length() > 50) {
					message = message.substring(0, 50) + "...";
				}
				map.put("message", message);
				map.put("submitdate", goods.get("submitdate"));
				map.put("username", goods.get("username"));
				map.put("headimg", goods.get("headimg"));
				String detailUrl = "goods/getGoodsDetail?goodsid=5&goodsattr=12&dougprice=0";
				map.put("detailUrl", detailUrl);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
