package com.domeke.app.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.Goods;
import com.domeke.app.model.GoodsType;
import com.domeke.app.model.OrderDetail;
import com.domeke.app.model.Orders;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.domeke.app.utils.FileLoadKit;
import com.domeke.app.validator.OrderValidator;
import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * 鍟嗗搧model鎺у埗鍣�
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "goods")
@Before(LoginInterceptor.class)
public class GoodsController extends FilesLoadController {
	
	private static String saveFolderName = "/goods";
	private static int maxPostSize = 200 * 1024 * 1024;
	private static String encoding = "UTF-8";

	/**
	 * to绠＄悊鐣岄潰
	 */
	public void renderGoods() {
		setGoodsPage(null);
		render("/admin/admin_goods.html");
	}

	/**
	 * 鍒嗛〉鏌ヨ
	 */
	public void setGoodsPage(String goods) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Goods> goodsList = null;
		goodsList = Goods.dao.findPage(pageNumber, pageSize);
		goods = "0";
		setAttr("goods", goods);
		setAttr("goodsList", goodsList);
	}

	public void find() {
		setGoodsPage(null);
		render("/admin/admin_goodsPage.html");
	}

	/**
	 * 鏂板鍟嗗搧
	 */
	public void saveGoods() {
		try {
			UUID uuid = UUID.randomUUID();
			String saveDirectory = saveFolderName + "/" + uuid.toString();
			Map<String, String> uploadPath = FileLoadKit.uploadImgs(this, saveDirectory, maxPostSize, encoding);
			String pic = uploadPath.get("pic");
			String headimg = uploadPath.get("headimg");
			String str = headimg.substring(headimg.indexOf(saveDirectory) + saveDirectory.length(), headimg.length());
			String[] headimgs = headimg.split(",");
			if (str.length() > 0) {
				headimg = headimg.substring(0, headimg.lastIndexOf("/"));
			}
			Goods goods = getModel(Goods.class);
			goods.set("pic", pic);
			goods.set("headimg", headimg);
			// 鍙敼涓鸿幏鍙栧綋鍓嶇敤鎴风殑鍚嶅瓧鎴栬�匢D
			goods.set("creater", 111111);
			goods.set("modifier", 111111);
			goods.saveGoodsInfo();
			redirect("/goods/renderGoods");
		} catch (Exception e) {
			e.printStackTrace();
			List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
			setAttr("goodsType", goodsType);
			List<CodeTable> statusList = CodeKit.getList("status");
			setAttr("statusList", statusList);
			setAttr("msg", "淇濆瓨澶辫触锛�");
			render("/admin/admin_addGoods.html");
		}
	}

	/**
	 * 閬嶅巻鏂囦欢澶逛笅鐨勫浘鐗�
	 */
	final static String[] showAllFiles(File dir) {
		File[] fs = dir.listFiles();
		String[] files = null;
		if (fs != null) {
			files = new String[fs.length];
			for (int i = 0; i < fs.length; i++) {
				//files[i] = fs[i].getAbsolutePath();
				//转换域名格式
				FilesLoadController fileslc = new FilesLoadController();
				files[i] = fileslc.getDomainNameFilePath(fs[i].toString());

//				if (fs[i].isDirectory()) {
//					try {
//						showAllFiles(fs[i]);
//					} catch (Exception e) {
//						System.out.println(e);
//					}
//				}
			}
		}
		return files;
	}
	
	/**
	 * 鑾峰彇鎸囧畾璺緞涓嬬殑鎵�鏈夋枃浠跺煙璺緞
	 * @param url 鎸囧畾鏂囦欢澶硅矾寰�
	 * @return 杩斿洖鎸囧畾璺緞涓嬬殑鎵�鏈夋枃浠跺煙璺緞鏁扮粍
	 */
	public List<String> getFileUrls(String url){
//		url =getDomainNameFilePath(url);
		String sub1 = "http://www.dongmark.com/";
		String sub2 = "http://127.0.0.1:8080/core/";
		if (url.indexOf(sub1) >= 0) {
			url = url.substring(sub1.length(), url.length());
		}
		if (url.indexOf(sub2) >= 0) {
			url = url.substring(sub2.length(), url.length());
		}
		File dir = new File(url);
		List<String> fileUrls = new ArrayList<String>();
		String[] files = this.showAllFiles(dir);
		if (files != null){
			for (int i =0; i < files.length; i ++) {
//				fileUrls.add(getDomainNameFilePath(files[i]));
				fileUrls.add(files[i]);
			}
		}
		return fileUrls;
	}
	
	/**
	 * 鍒犻櫎鎸囧畾鏂囦欢澶逛笅鐨勬寚瀹氭枃浠�
	 * @param url 鏂囦欢澶硅矾寰�
	 * @param fileName 鏂囦欢鍚嶇О
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
	 * 鍒犻櫎鍥剧墖
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
		List<CodeTable> statusList = CodeKit.getList("status");
		setAttr("statusList", statusList);
		List<GoodsType> goodsType1 = GoodsType.gtDao.getLevelGoodsType("1");
		List<GoodsType> goodsType2 = GoodsType.gtDao.getLevelGoodsType("2");
		List<GoodsType> goodsType3 = GoodsType.gtDao.getLevelGoodsType("3");
		List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
		setAttr("goodsType", goodsType);
		setAttr("goodsType1", goodsType1);
		setAttr("goodsType2", goodsType2);
		setAttr("goodsType3", goodsType3);
		render("/admin/admin_addGoods.html");
	}

	/**
	 * 鍟嗗搧璇︽儏
	 */
	public void goodsMessage() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsId"));
		String headimg = goods.getStr("headimg");
		List<String> headimgs = new ArrayList<String>();
		if (headimg != null && !"".equals(headimg)) {
			headimgs = this.getFileUrls(headimg);	
		}
		List<CodeTable> statusList = CodeKit.getList("status");
		setAttr("statusList", statusList);
		List<GoodsType> goodsType1 = GoodsType.gtDao.getLevelGoodsType("1");
		List<GoodsType> goodsType2 = GoodsType.gtDao.getLevelGoodsType("2");
		List<GoodsType> goodsType3 = GoodsType.gtDao.getLevelGoodsType("3");
		List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
		setAttr("goodsType", goodsType);
		setAttr("goodsType1", goodsType1);
		setAttr("goodsType2", goodsType2);
		setAttr("goodsType3", goodsType3);
		setAttr("headimgs", headimgs);
		setAttr("goods", goods);
		render("/admin/admin_goodsMsg.html");
	}
	
	/**
	 * 鍒犻櫎鍟嗗搧
	 */
	public void deleteGoods() {
		Goods goods = getModel(Goods.class);
		goods.deleteById(getParaToInt("goodsId"));
		redirect("/goods/renderGoods");
	}

	/**
	 * 缂栬緫鍟嗗搧
	 */
	public void updateGoods() {
		String goodsId = getPara("goodsId");
		String headimg = Goods.dao.getHeadImg(goodsId);
		String saveDirectory = headimg.substring(headimg.lastIndexOf("/") + 1, headimg.length());
		Map<String, String> uploadPath = FileLoadKit.uploadImgs(this, saveDirectory, maxPostSize, encoding);
		String pic = uploadPath.get("pic");
		
		UploadFile file = getFile();
		Goods gs = getModel(Goods.class);
		gs.set("pic", pic);
		try{
			gs.updateGoodsInfo();
			redirect("/goods/renderGoods");
		} catch (Exception e) {
			e.printStackTrace();
			Goods goods = gs.findById(goodsId);
			String hg = goods.getStr("headimg");
			List<String> headimgs = this.getFileUrls(hg);		
			setAttr("headimgs", headimgs);
			setAttr("goods", goods);
			render("/admin/admin_goodsMsg.html");
		}
		
//		String goodsId = getPara("goodsId");
//		String headimg = Goods.dao.getHeadImg(goodsId);
//		String[] headimgs2 = headimg.split("/");
//		String fileName = headimgs2[headimgs2.length - 1];
//		String picturePath = upLoadFileNotDealPath("pic", fileName, 200 * 1024 * 1024, "utf-8");
//		String[] strs = picturePath.split("/");
//		int leng = strs.length;
//		String headimg2 = upLoadFilesNotDealPath(fileName, 200 * 1024 * 1024, "utf-8");
//		UploadFile file = getFile();
//		Goods gs = getModel(Goods.class);
//		if (leng > 1) {
//			gs.set("pic", strs[leng - 1]);
//		}
//		if (headimg2 != null) {
//			gs.updateGoodsInfo();
//			redirect("/goods/renderGoods");
//		} else {
//			Goods goods = gs.findById(goodsId);
//			String hg = goods.getStr("headimg");
//			List<String> headimgs = this.getFileUrls(hg);		
//			setAttr("headimgs", headimgs);
//			setAttr("goods", goods);
//			render("/admin/admin_goodsMsg.html");
//		}
	}

	/**
	 * 鑾峰彇绫诲瀷涓簍ypes鐨勫彾瀛愬晢鍝�
	 * @param params key=瀛楁鍚嶏紝value=鍙傝禌鍊�
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

		// 鑾峰彇绫诲瀷闆嗗悎
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

		// 鎸夌被鍨嬭幏鍙栧晢鍝佸垎绫�
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
	 * 璺宠浆鍟嗗搧绠＄悊鐣岄潰
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
	 * 璺宠浆娣诲姞鍟嗗搧鐣岄潰
	 */
	public void goAddGoods() {
		render("/demo/addgoods.html");
	}

	/**
	 * 淇濆瓨鍟嗗搧淇℃伅<br>
	 * 娉細// 鍥剧墖涓婁紶鍚庯紝浠ヤ細鍛樼殑鍚嶅瓧浣滀负鏂囦欢鍚嶏紝璺緞\core\ upload\goodspic\XXXX\XXXXX.jpg
	 */
	public void save() {
		try {
			String picturePath = upLoadFileNotDealPath("pictrue", "", 200 * 1024 * 1024, "utf-8");
			Goods goodsModel = getModel(Goods.class);
			goodsModel.set("pic", picturePath);
			// 鍙敼涓鸿幏鍙栧綋鍓嶇敤鎴风殑鍚嶅瓧鎴栬�匢D
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
	 * 鏇存柊宸蹭慨鐨勫晢鍝�
	 */
	public void update() {
		String picturePath = upLoadFileNotDealPath("pictrue", "", 200 * 1024 * 1024, "utf-8");
		Goods goodsModel = getModel(Goods.class);
		if (picturePath != null) {
			goodsModel.set("pic", picturePath);
		} else {
			goodsModel.remove("pic");
		}
		// 鍙敼涓鸿幏鍙栧綋鍓嶇敤鎴风殑鍚嶅瓧鎴栬�匢D
		goodsModel.set("modifier", 111111);
		goodsModel.update();
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 鍒犻櫎鍟嗗搧淇℃伅
	 */
	public void delete() {
		Goods goodsModel = getModel(Goods.class);
		goodsModel.deleteById(getParaToInt("id"));
		redirect("/goods/goGoodsMan");
	}

	/**
	 * 鏌ヨ鍑烘墍鏈夊晢鍝�
	 */
	public void queryAllGoodsInfo() {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.queryAllGoodsInfo();
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 鏍规嵁鍟嗗搧ID鑾峰彇鏌愬晢鍝�
	 */
	public void getGoodsById() {
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("id"));
		setAttr("goods", goods);
		render("/demo/modifygoods.html");
	}

	/**
	 * 鏍规嵁鍟嗗搧绫诲瀷鑾峰彇鍟嗗搧淇℃伅
	 */
	public void getGoodsInfoByType(String goodsType) {
		Goods goodsModel = getModel(Goods.class);
		List<Goods> goodsList = goodsModel.getGoodsInfoByType(goodsType);
		this.setAttr("goodslist", goodsList);
	}

	/**
	 * 閫氳繃鍟嗗搧鍚嶅瓧妯＄硦鏌ヨ鍟嗗搧淇℃伅
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
	 * 鍟嗗搧鏄庣粏
	 */
	public void getGoodsDetail(){
		goodsUtil("/ShopCentre.html");
		//render("/ShopCentre.html");
	}
	/**
	 * 鍟嗗搧淇℃伅鎷艰
	 */
	public void goodsUtil(String url){
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsid"));
		String goodsattr = String.valueOf(getParaToInt("goodsattr"));
		if (!"".equals(goodsattr) || goodsattr != null){
			List<GoodsType> goodsTypes= GoodsType.gtDao.getTypeUrl(goodsattr);
			setAttr("goodsTypes", goodsTypes);
		}
		setAttr("goods", goods);
//		String headimg = goods.getStr("headimg");
//		List<String> headimgs = this.getFileUrls(headimg);
//		setAttr("images", headimgs);
		Long dougprice = getParaToLong("dougprice");
		Map<String,Object> changeMap = getPeas(dougprice);
		String isChange = "";
		if (changeMap != null){
			isChange = (String)changeMap.get("isChange");	
			setAttr("userId", changeMap.get("userId"));
			setAttr("peas", changeMap.get("peas"));
		}
		int sumLove = Goods.dao.getSumLove(getParaToInt("goodsid"));
		setAttr("sumLove", sumLove);
		setAttr("isChange", isChange);
		int key = 0;
		if (getSessionAttr(getParaToInt("goodsid")+"") != null){
			key = getSessionAttr(getParaToInt("goodsid")+"");
		}
		int flag = 0;
		if (key == getParaToInt("goodsid")){
			flag = 1;
		}
		setAttr("flag", flag);
		String action = "/comment/setPage";
		setAttr("render", url);
		setAttr("targetId", getParaToInt("goodsid"));
		setAttr("idtype", "60");
		forwardAction(action);
	}
	public void loveCount(){
		int goodsId = getParaToInt("goodsid");
		Goods.dao.updateLoveCount(goodsId);
		int sumLove = Goods.dao.getSumLove(goodsId);
		setSessionAttr(goodsId+"", goodsId);
		setAttr("sumLove", sumLove);
		String url = "/ShopDtl.html";
		goodsUtil(url);
		//render("/ShopDtl.html");
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
	 * 璞嗚眴鍏戞崲
	 */
	public void peasChange(){
		Long dougprice = getParaToLong("dougprice");
		int goodsnum = getParaToInt("goodsnum");
		Map<String,Object> changeMap = getPeas(dougprice);
		String isChange = (String)changeMap.get("isChange");
		User user = getSessionAttr("user");
		if(user == null){
			return;
		} 
		Long userId = user.get("userid");
		user = user.findById(userId);
		//璞嗗瓙
		Long peas = (Long)changeMap.get("peas");
		Goods goodsModel = getModel(Goods.class);
		Goods goods = goodsModel.findById(getParaToInt("goodsid"));
		Long sumDougprice = goods.getLong("dougprice")*goodsnum;
		Long surplus = peas-sumDougprice;
 		String goodsattr = String.valueOf(getParaToInt("goodsattr"));
		List<GoodsType> goodsTypes= GoodsType.gtDao.getTypeUrl(goodsattr);
		setAttr("goodsTypes", goodsTypes);
		setAttr("goods", goods);	
		setAttr("isChange", isChange);
		setAttr("peas", peas);
		setAttr("userId", changeMap.get("userId"));
		setAttr("user", user);
		setAttr("goodsnum", goodsnum);
		setAttr("sumDougprice", sumDougprice);
		setAttr("surplus", surplus);
		//render("/ShopDtl.html");
		render("/ShopOrder.html");
	}
	public void reducePeas(Map<String,Object> changeMap){
		String isChange = (String)changeMap.get("isChange");
		Long dougprice = (Long)changeMap.get("dougprice");
		Long userId = (Long)changeMap.get("userId");
		//璞嗗瓙
		Long peas = (Long)changeMap.get("peas");
		if (isChange == "1" || "1".equals(isChange)){
			peas = peas - dougprice;
			User.dao.updatePeas(userId, peas);
			if (peas <= 0)
			isChange = "0";
		}
	}

	/**
	* 鑾峰緱璞嗚眴
	*/
	public Map<String, Object> getPeas(Long dougprice) {
		int goodsnum = 0;
		if (getParaToInt("goodsnum") != null){
			goodsnum = getParaToInt("goodsnum");
		} 
		User user = getSessionAttr("user");
		if(user == null){
			return null;
		} 
		Long userId = user.get("userid");
		user = user.findById(userId);
		String isChange = "0";
		// 璞嗗瓙
		Long peas = user.get("peas");
		if (goodsnum != 0){
			if ((dougprice*goodsnum) <= peas) {
				isChange = "1";
			}			
		}
		Map<String, Object> changeMap = new HashMap<String, Object>();
		changeMap.put("isChange", isChange);
		changeMap.put("dougprice", dougprice);
		changeMap.put("peas", peas);
		changeMap.put("userId", userId);
		return changeMap;
	}
	/**
	* 淇濆瓨璁㈠崟
	*/
	@Before({OrderValidator.class})
	public void saveOrderDetail(){
		Goods goodsModel = getModel(Goods.class);
		User userModel = getModel(User.class);
		int goodsNum = getParaToInt("goodsnum");
		String goodsId = String.valueOf(goodsModel.get("goodsid"));
		String orderNum = getOrderNum();
		Orders orders = getModel(Orders.class);
		orders.set("ordernum", orderNum);
		orders.set("isPay", "Y");
		orders.set("isDelivery", "N");
		orders.set("orderAddr", userModel.get("address"));
		orders.set("orderPhone", userModel.get("mobile"));
		orders.set("realname", userModel.get("realname"));
		orders.set("creater", userModel.get("userid"));
		orders.save();
		Long orderId = orders.getLong("orderid");
		OrderDetail orderDetail = getModel(OrderDetail.class);
		orderDetail.set("orderid", orderId);
		orderDetail.set("ordernum", orderNum);
		orderDetail.set("goodsnum", goodsNum);
		orderDetail.set("goodsname", goodsModel.get("goodsname"));
		orderDetail.set("goodsid", goodsModel.get("goodsid"));
		orderDetail.set("goodsprice", goodsModel.get("dougprice"));
		orderDetail.set("userid", userModel.get("userid"));
		userModel.update();
		setAttr("orders", orders);
		setAttr("goods", goodsModel);
		setAttr("userModel", userModel);
		orderDetail.save();
		Long dougprice = goodsModel.get("dougprice");
		Map<String,Object> changeMap = getPeas(dougprice);
		reducePeas(changeMap);
		goodsUtil("/ShopCentre.html");
		//render("/ShopCentre.html");
		//redirect("/orders/byGoods");
	}
	/**
	 * 鍟嗗搧涓婃灦涓嬫灦
	 */
	public void goodsShow(){
		String goodsId = getPara("goodsId");
		int flag = getParaToInt("flag");
		Goods.dao.updateShowFlag(goodsId, flag);
		redirect("/goods/renderGoods");
	}
	/**
	 * 鍟嗗搧鏄惁鏀鹃椤�
	 */
	public void goodsIndex(){
		String goodsId = getPara("goodsId");
		int result = Goods.dao.getSelectIstop(goodsId);
		int flag = 0;
		if (result == 0){
			flag = 1;
		}
		Goods.dao.updateIndexShow(goodsId, flag);
		redirect("/goods/renderGoods");
	}
	public String getOrderNum(){
		int r1=(int)(Math.random()*(10));//浜х敓2涓�0-9鐨勯殢鏈烘暟
		int r2=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//涓�涓�13浣嶇殑鏃堕棿鎴�
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(now);// 璁㈠崟ID
		return  paymentID;
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
