package com.domeke.app.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.domeke.app.utils.FileKit;
import com.domeke.app.utils.FileLoadKit;
import com.domeke.app.validator.OrderValidator;
import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * 商品model
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
     * 查询商品列表
     */
    public void renderGoods() {
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
        goodsList = Goods.dao.findPage(pageNumber, pageSize);
        goods = "0";
        setAttr("goods", goods);
        setAttr("goodsList", goodsList);
    }
    
    /**
     * 分页查询
     */
    public void find() {
        setGoodsPage(null);
        render("/admin/admin_goodsPage.html");
    }
    
    /**
     * 保持商品
     */
    public void saveGoods() {
        try {
            UUID uuid = UUID.randomUUID();
            String saveDirectory = saveFolderName + "/" + uuid.toString();
            Map<String, String> uploadPath = FileLoadKit.uploadImgs(this, saveDirectory, maxPostSize, encoding);
            String pic = "";
            String headimg = "";
            if (uploadPath != null) {
                pic = uploadPath.get("pic");
                headimg = uploadPath.get("headimg");
                String str = headimg.substring(headimg.indexOf(saveDirectory) + saveDirectory.length(), headimg.length());
                if (str.length() > 0) {
                    headimg = headimg.substring(0, headimg.lastIndexOf("/"));
                }
            }
            Goods goods = getModel(Goods.class);
            goods.set("pic", pic);
            goods.set("headimg", headimg);
            User user = getSessionAttr("user");
            user = User.dao.getUserForId(user.getLong("userid"));
            //创建人id，修改人id
            goods.set("creater", user.getNumber("userid"));
            goods.set("modifier", user.getNumber("userid"));
            goods.set("username", user.get("username"));
            goods.saveGoodsInfo();
            redirect("/goods/renderGoods");
        } catch (Exception e) {
            e.printStackTrace();
            List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
            setAttr("goodsType", goodsType);
            List<CodeTable> statusList = CodeKit.getList("status");
            setAttr("statusList", statusList);
            setAttr("msg", "保持失败！");
            render("/admin/admin_addGoods.html");
        }
    }
    
    /**
     * 根据图片文件夹路径，返回该文件夹所有图片
     * 
     * @param url
     * @return
     */
    public List<String> getFileUrls(String url) {
        List<String> fileUrls = FileKit.getVirtualDirectorys(url);
        return fileUrls;
    }
    
    /**
     * 根据图片路径删除图片
     */
    public void deleteImg() {
        String url = getPara("url");
        FileKit.deleteFiles(url);
        Goods goods = Goods.dao.findById(getParaToInt("goodsId"));
        url = url.substring(0, url.lastIndexOf("/"));
        List<String> headimgs = FileKit.getVirtualDirectorys(url);
        List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
        setAttr("goodsType", goodsType);
        setAttr("headimgs", headimgs);
        setAttr("goods", goods);
        render("/admin/admin_goodsMsg.html");
    }
    
    /**
     * 跳转新增商品界面
     */
    public void addGoods() {
        List<CodeTable> statusList = CodeKit.getList("status");
        setAttr("statusList", statusList);
        List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
        setAttr("goodsType", goodsType);
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
        List<CodeTable> statusList = CodeKit.getList("status");
        setAttr("statusList", statusList);
        List<GoodsType> goodsType = GoodsType.gtDao.getGoodsTypeList();
        setAttr("goodsType", goodsType);
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
        redirect("/goods/renderGoods");
    }
    
    /**
     * 修改商品
     */
    public void updateGoods() {
        String goodsId = getPara("goodsId");
        String headimg = Goods.dao.getHeadImg(goodsId);
        String pic = Goods.dao.getPic(goodsId);
        String saveDirectory = headimg.substring(headimg.lastIndexOf("/") + 1, headimg.length());
        saveDirectory = saveFolderName + "/" + saveDirectory;
        Map<String, String> uploadPath = FileLoadKit.uploadImgs(this, saveDirectory, maxPostSize, encoding);
        if (uploadPath != null && uploadPath.get("pic") != null) {
            FileKit.deleteFiles(pic);
            pic = uploadPath.get("pic");
        }
        UploadFile file = getFile();
        Goods gs = getModel(Goods.class);
        gs.set("pic", pic);
        try {
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
    }
    
    /**
     * 根据商品分类，返回满足该类型的商品
     * 
     * @param params
     * @return
     */
    public List<Goods> getGoodsType(Map<String, Object> params) {
        Set<String> key = params.keySet();
        Map<String, Object> gtMap = new HashMap<String, Object>();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String k = (String)it.next();
            gtMap.put(k, GoodsType.gtDao.getGoodsType(params.get(k).toString()));
        }
        Goods goods = getModel(Goods.class);
        List<Goods> goodsList = goods.goodsType(gtMap);
        return goodsList;
    }
    
    public void shop() {
        String goodstypeid = getPara("goodstype");
        String level = getPara("level");
        if (StrKit.isBlank(level)) {
            level = "1";
        }
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
        List<GoodsType> goodsTypeStack2 = Lists.newArrayList();
        List<GoodsType> goodsTypeList = Lists.newArrayList();
        GoodsType goodsTypeModel = getModel(GoodsType.class);
        String attr = "";
        String leve2 = "";
        if (!StrKit.isBlank(goodstypeid)) {
            attr = getModel(GoodsType.class).getGoodsType(goodstypeid);
            goodsTypeStack = getModel(GoodsType.class).getGoodssTypeById(attr);
            if ("1".equals(level)) {
                //leve2 = getModel(GoodsType.class).getGoodsType(goodstypeid);
                goodsTypeStack2 = getModel(GoodsType.class).getGoodsTypeByParId(goodstypeid);
            }
            //goodsTypeList = goodsTypeModel.getGoodsTypeByParId(String.valueOf(goodsTypeModel.get("goodstype")));
        }
        if (goodsTypeModel.get("goodstypeid") != null) {
            goodsTypeStack.add(goodsTypeModel);
            //			String goodstypeid = String.valueOf(goodsTypeModel.get("parenttypeid"));
            //			while (!StrKit.isBlank(goodstypeid)) {
            //				GoodsType goodsType = getModel(GoodsType.class).getGoodsTypeById(Integer.parseInt(goodstypeid));
            //				goodsTypeStack.add(goodsType);
            //				goodstypeid = String.valueOf(goodsType.get("parenttypeid"));
            //			}
            Collections.reverse(goodsTypeStack);
        }
        //		if (!goodsTypeStack.isEmpty()) {
        //			goodsTypeModel = goodsTypeStack.get(goodsTypeStack.size() - 1);
        //			goodsTypeList = goodsTypeModel.getGoodsTypeByParId(String.valueOf(goodsTypeModel.get("goodstypeid")));
        //		} else {
        //			goodsTypeList = goodsTypeModel.getGoodsTypeByParId("");
        //		}
        
        // 鎸夌被鍨嬭幏鍙栧晢鍝佸垎绫�
        Page<Goods> goodss = getModel(Goods.class).getGoodsPageByType(goodstypeid, pageNumber, pageSize);
        List<Map<String, Object>> datas = goodsParse(goodss.getList());
        Page<List<Map<String, Object>>> pageMap = new Page(datas, goodss.getPageNumber(), goodss.getPageSize(), goodss.getTotalPage(), goodss.getTotalRow());
        if (!StrKit.isBlank(goodstypeid)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goodsattr" + level, goodstypeid);
            Goods goods = getModel(Goods.class);
            goodss = goods.getGoodsByType(pageSize, pageNumber, map);
            datas = goodsParse(goodss.getList());
            pageMap = new Page(datas, goodss.getPageNumber(), goodss.getPageSize(), goodss.getTotalPage(), goodss.getTotalRow());
        }
        Map<List<GoodsType>, GoodsType> test = getModel(GoodsType.class).getGType("1", "1");
        setAttr("goodsTypeStack", goodsTypeStack);
        setAttr("goodsTypeList", goodsTypeList);
        setAttr("goodstype", goodstypeid);
        setAttr("goodss", pageMap);
        setAttr("goodsTypeStack2", goodsTypeStack2);
        render("/shop.html");
    }
    
    public void getGoodsByType() {
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 25;
        }
        int goodstypeid = getParaToInt("goodstypeid");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsattr1", goodstypeid);
        //		Set<String> key = map.keySet();
        //		Map<String, Object> gtMap = new HashMap<String, Object>();
        //		for (Iterator it = key.iterator(); it.hasNext();) {
        //			String k = (String) it.next();
        //			gtMap.put(k, GoodsType.gtDao.getGoodsType(map.get(k).toString()));
        //		}
        Goods goods = getModel(Goods.class);
        Page<Goods> goodss = goods.getGoodsByType(pageSize, pageNumber, map);
        List<Map<String, Object>> datas = goodsParse(goodss.getList());
        Page<List<Map<String, Object>>> pageMap = new Page(datas, goodss.getPageNumber(), goodss.getPageSize(), goodss.getTotalPage(), goodss.getTotalRow());
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
    public void getGoodsDetail() {
        goodsUtil("/ShopCentre.html");
        //render("/ShopCentre.html");
    }
    
    /**
     * 鍟嗗搧淇℃伅鎷艰
     */
    public void goodsUtil(String url) {
        Goods goodsModel = getModel(Goods.class);
        Goods goods = goodsModel.findById(getParaToInt("goodsid"));
        if (goods == null) {
            render("/shop/shopErro.html");
            return;
        }
        //String goodsattr = String.valueOf(getParaToInt("goodsattr"));
        String goodsattr = String.valueOf(goods.getInt("goodsattr1"));
        if (!"".equals(goodsattr) && goodsattr != null) {
            List<GoodsType> goodsTypes = GoodsType.gtDao.getTypeUrl(goodsattr);
            setAttr("goodsTypes", goodsTypes);
        }
        setAttr("goods", goods);
        String headimg = goods.getStr("headimg");
        List<String> headimgs = new ArrayList<String>();
        if (headimg != null && !"".equals(headimg)) {
            headimgs = this.getFileUrls(headimg);
        }
        setAttr("images", headimgs);
        //Long dougprice = getParaToLong("dougprice");
        Long dougprice = goods.getLong("dougprice");
        Map<String, Object> changeMap = getPeas(dougprice);
        String isChange = "";
        if (changeMap != null) {
            isChange = (String)changeMap.get("isChange");
            setAttr("userId", changeMap.get("userId"));
            setAttr("peas", changeMap.get("peas"));
        }
        int sumLove = Goods.dao.getSumLove(getParaToInt("goodsid"));
        setAttr("sumLove", sumLove);
        setAttr("isChange", isChange);
        int key = 0;
        if (getSessionAttr(getParaToInt("goodsid") + "") != null) {
            key = getSessionAttr(getParaToInt("goodsid") + "");
        }
        int flag = 0;
        if (key == getParaToInt("goodsid")) {
            flag = 1;
        }
        setAttr("flag", flag);
        String action = "/comment/setPage";
        setAttr("render", url);
        setAttr("targetId", getParaToInt("goodsid"));
        setAttr("idtype", "60");
        forwardAction(action);
    }
    
    public void loveCount() {
        int goodsId = getParaToInt("goodsid");
        Goods.dao.updateLoveCount(goodsId);
        int sumLove = Goods.dao.getSumLove(goodsId);
        setSessionAttr(goodsId + "", goodsId);
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
    public void peasChange() {
        Long dougprice = getParaToLong("dougprice");
        int goodsnum = getParaToInt("goodsnum");
        Map<String, Object> changeMap = getPeas(dougprice);
        String isChange = (String)changeMap.get("isChange");
        User user = getSessionAttr("user");
        if (user == null) {
            return;
        }
        Long userId = user.get("userid");
        user = user.findById(userId);
        //璞嗗瓙
        Long peas = (Long)changeMap.get("peas");
        Goods goodsModel = getModel(Goods.class);
        Goods goods = goodsModel.findById(getParaToInt("goodsid"));
        Long sumDougprice = goods.getLong("dougprice") * goodsnum;
        Long surplus = peas - sumDougprice;
        String goodsattr = String.valueOf(getParaToInt("goodsattr"));
        List<GoodsType> goodsTypes = GoodsType.gtDao.getTypeUrl(goodsattr);
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
    
    public void reducePeas(Map<String, Object> changeMap) {
        String isChange = (String)changeMap.get("isChange");
        Long dougprice = (Long)changeMap.get("dougprice");
        Long userId = (Long)changeMap.get("userId");
        //璞嗗瓙
        Long peas = (Long)changeMap.get("peas");
        if (isChange == "1" || "1".equals(isChange)) {
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
        if (getParaToInt("goodsnum") != null) {
            goodsnum = getParaToInt("goodsnum");
        }
        User user = getSessionAttr("user");
        if (user == null) {
            return null;
        }
        Long userId = user.get("userid");
        user = user.findById(userId);
        String isChange = "0";
        // 璞嗗瓙
        Long peas = user.get("peas");
        if (goodsnum != 0) {
            if ((dougprice * goodsnum) <= peas) {
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
    public void saveOrderDetail() {
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
        orderDetail.save();
        Long dougprice = goodsModel.get("dougprice");
        Map<String, Object> changeMap = getPeas(dougprice);
        reducePeas(changeMap);
        goodsModel = getModel(Goods.class).getUserForId((Long)goodsModel.get("goodsid"));
        float oldNum = goodsModel.getFloat("goodsnumber");
        int newNums = (int)oldNum - goodsNum;
        goodsModel.set("goodsnumber", newNums);
        goodsModel.updateGoodsNum((Long)goodsModel.get("goodsid"), newNums);
        if (newNums == 0) {
            goodsModel.updateShowFlag(goodsId, 0);
        }
        setAttr("orders", orders);
        setAttr("goods", goodsModel);
        setAttr("userModel", userModel);
        goodsUtil("/ShopCentre.html");
        //render("/ShopCentre.html");
        //redirect("/orders/byGoods");
    }
    
    /**
     * 鍟嗗搧涓婃灦涓嬫灦
     */
    public void goodsShow() {
        String goodsId = getPara("goodsId");
        int flag = getParaToInt("flag");
        Goods.dao.updateShowFlag(goodsId, flag);
        redirect("/goods/renderGoods");
    }
    
    /**
     * 鍟嗗搧鏄惁鏀鹃椤�
     */
    public void goodsIndex() {
        String goodsId = getPara("goodsId");
        int result = Goods.dao.getSelectIstop(goodsId);
        int flag = 0;
        if (result == 0) {
            flag = 1;
        }
        Date date = new Date();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Timestamp newdate = Timestamp.valueOf(nowTime);
        Goods.dao.updateIndexShow(goodsId, flag, newdate);
        redirect("/goods/renderGoods");
    }
    
    public String getOrderNum() {
        int r1 = (int)(Math.random() * (10));//浜х敓2涓�0-9鐨勯殢鏈烘暟
        int r2 = (int)(Math.random() * (10));
        long now = System.currentTimeMillis();//涓�涓�13浣嶇殑鏃堕棿鎴�
        String paymentID = String.valueOf(r1) + String.valueOf(r2) + String.valueOf(now);// 璁㈠崟ID
        return paymentID;
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
                map.put("price", goods.get("price"));
                map.put("oldprice", goods.get("oldprice"));
                map.put("amount", goods.get("amount"));
                map.put("pic", goods.get("pic"));
                map.put("goodsattr1", goods.get("goodsattr1"));
                map.put("dougprice", goods.get("dougprice"));
                map.put("showflag", goods.get("showflag"));
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
