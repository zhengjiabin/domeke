package com.domeke.app.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Activity;
import com.domeke.app.model.DownLoad;
import com.domeke.app.model.Favourite;
import com.domeke.app.model.Goods;
import com.domeke.app.model.OfWonders;
import com.domeke.app.model.OrderDetail;
import com.domeke.app.model.Orders;
import com.domeke.app.model.PlayCount;
import com.domeke.app.model.Post;
import com.domeke.app.model.Treasure;
import com.domeke.app.model.User;
import com.domeke.app.utils.EncryptKit;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

public class PersonalController extends  FilesLoadController{
	
	private static String saveFolderName = "/person";
	private static String parameterName = "user.imgurl";
	private static int maxPostSize = 2 * 1024 * 1024;
	private static String encoding = "UTF-8";
	
	@Before(LoginInterceptor.class)
	public void forMyProduction(){
		String mid = getPara("menuId");
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		setAttr("userId", userId);
		setAttr("menuId", mid);
		setAttr("menuid", "1");
		uploadMsg(mid);
		myDownLoad(mid);
		myPlay(mid);
		loadPoint(mid,userId);
		acctiveApply(mid);
		activityPage(mid,userId);
		postPage(mid,userId);
		treasurePage(mid,userId);
		ofWondersPage(mid,userId);
		myOrdersPage(mid,userId);
		myFavourite(mid);
		render("/personalCenter.html");
	}
	public void forMyProductionPage(){
		String mid = getPara("menuId");
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		setAttr("userId", userId);
		setAttr("menuId", mid);
		setAttr("menuid", "1");
		uploadMsg(mid);
		myDownLoad(mid);
		loadPoint(mid,userId);
		myPlay(mid);
		myFavourite(mid);
		acctiveApply(mid);
		if("14".equals(mid)){
			render("/myApplyActive.html");
		}else if("13".equals(mid)){
			render("/mydowload.html");
		}else if("2".equals(mid)){
			render("/playRecord.html");
		}else if("20".equals(mid)){
			render("/favourite.html");
		}
		
	}
	
	/**
	 * 加载我发布的活动
	 */
	private void activityPage(String minuId,Long userId){
		if(!"15".equals(minuId)){
			return;
		}
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Activity> activityPage = Activity.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("activityPage", activityPage);
	}
	
	/**
	 * 加载会员信息等级
	 */
	public void loadPoint(String minuId,Long userId){
		if("3".equals(minuId)){
			User user = getModel(User.class);
			user = user.findById(userId);
			setAttr("user", user);
		}
	}
		
	/**
	 * 加载我发布的帖子
	 */
	private void postPage(String minuId,Long userId){
		if(!"16".equals(minuId)){
			return;
		}
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Post> postPage = Post.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("postPage", postPage);
	}
	
	/**
	 * 加载我发布的宝贝
	 */
	private void treasurePage(String minuId,Long userId){
		if(!"17".equals(minuId)){
			return;
		}
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = Treasure.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
	}
	/**
	 * 加载我发布的宝贝
	 */
	private void myOrdersPage(String minuId,Long userId){
		if(!"19".equals(minuId)){
			return;
		}
		//分页码
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		//订单主表分页
		Orders orders = getModel(Orders.class);
		Page<Orders> ordersList = orders.getAllOrders(pageNumber,pageSize);
		//订单详细表分页
		OrderDetail orderdetail = getModel(OrderDetail.class);
		Page<OrderDetail> orderDetailList = orderdetail.getOrdersByUserId(null, null,pageNumber,pageSize,userId);
		List<Goods> goodsList = Goods.dao.queryAllGoodsInfo();
		setAttr("ordList", ordersList);
		setAttr("ordetailList", orderDetailList);
		setAttr("goodsList", goodsList);
	}
	public void upIsReceipt(){
		Orders orders = getModel(Orders.class);
		String isReceipt = getPara("isreceipt");
		String orderId = getPara("orderid");
		if ("N".equals(isReceipt)){
			isReceipt = "Y";
		}else{
			isReceipt = "N";
		}
		orders.updateIsreceipt(orderId, isReceipt);;
		String menuId = "19";
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		myOrdersPage(menuId, userId);
		renderPersonal(menuId);	
	}
	/**
	 * 加载我发布的无奇不有
	 */
	private void ofWondersPage(String minuId,Long userId){
		if(!"18".equals(minuId)){
			return;
		}
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<OfWonders> ofWondersPage = OfWonders.dao.findByUserId(userId, pageNumber, pageSize);
		setAttr("ofWondersPage", ofWondersPage);
	}
	
	
	/**
	 * 加载我的下载记录
	 */
	public void myDownLoad(String minuId){
		if("13".equals(minuId)){
			User user = getModel(User.class);
			DownLoad down = getModel(DownLoad.class);
			user = getSessionAttr("user");
			Long userid = user.getLong("userid");
			int pageNumber = getParaToInt("pageNumber", 1);
			int pageSize = getParaToInt("pageSize", 10);
			Page<DownLoad> downPage = down.getByUserId(userid, pageNumber, pageSize);
			setAttr("downPage", downPage);
		}
	}
	
	/**
	 * 加载我的动漫收藏
	 */
	public void myFavourite(String minuId){
		if("20".equals(minuId)){
			User user = getModel(User.class);
			Favourite fav = getModel(Favourite.class);
			user = getSessionAttr("user");
			Long userid = user.getLong("userid");
			int pageNumber = getParaToInt("pageNumber", 1);
			int pageSize = getParaToInt("pageSize", 10);
			Page<Favourite> favPage = fav.getFavouriteByUid(userid, pageNumber, pageSize);
			setAttr("favPage", favPage);
		}
	}
	/**
	 * 删除我的下载记录
	 */
	public void deleteMyDownLoad(){
		DownLoad down = getModel(DownLoad.class);
		String downId = getPara("downid");
		String menuId = getPara("menuId");
		down.deleteById(downId);
		renderPersonal(menuId);	
	}
	/**
	 * 加载我的播放记录
	 */
	public void myPlay(String minuId){
		if("2".equals(minuId)){
			User user = getModel(User.class);
			user = getSessionAttr("user");
			Long userid = user.getLong("userid");
			PlayCount down = getModel(PlayCount.class);
			int pageNumber = getParaToInt("pageNumber", 1);
			int pageSize = getParaToInt("pageSize", 10);
			Page<PlayCount> playPage = down.getByUserId(userid, pageNumber, pageSize);
			setAttr("playPage", playPage);
		}
	}
	/**
	 * 删除我的播放记录
	 */
	public void deleteMyPlay(){
		PlayCount play = getModel(PlayCount.class);
		String playId = getPara("playid");
		String menuId = getPara("menuId");
		play.deleteById(playId);
		renderPersonal(menuId);	
	}
	
	/**
	 * @param menuId
	 * 跳转个人中心页面
	 */
	public void renderPersonal(String menuId){
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		setAttr("userId", userId);
		setAttr("menuId", menuId);
		setAttr("menuid", "1");
		uploadMsg(menuId);
		myDownLoad(menuId);
		myPlay(menuId);
		acctiveApply(menuId);
		render("/personalCenter.html");
	}
	/**
	 * 加载我参与的活动
	 */
	public void acctiveApply(String minuId){
		if("14".equals(minuId)){
			User user = getModel(User.class);
			Activity activity = getModel(Activity.class);
			user = getSessionAttr("user");
			Long userid = user.getLong("userid");
			int pageNumber = getParaToInt("pageNumber", 1);
			int pageSize = getParaToInt("pageSize", 10);
			Page<Activity> actPage = activity.getActivity(userid, pageNumber, pageSize);
			setAttr("actPage", actPage);
		}
	}
	/**
	 * 跟据不同的minuId加载不同的数据
	 * @param minuId
	 */
	public 	void uploadMsg(String minuId){
		
		if("12".equals(minuId)){
			//用户资料修改
			User user = getModel(User.class);
			user = getSessionAttr("user");
			Long userid = user.getLong("userid");
			user = user.findById(userid);
			setAttr("user", user);
		}
	}
	/**
	 *  资料修改
	 */
	public void upUser(){
		String imgPath = upLoadFileDealPath(parameterName, saveFolderName, maxPostSize, encoding);
		User user  = getModel(User.class);
		user.set("imgurl", imgPath);
		user.update();
		setAttr("menuId", "12");
		setAttr("menuid", "1");
		setAttr("user", user);
		setAttr("msg", "资料修改成功");
		render("/personalCenter.html");
	}
	public void updatePassword(){
		User user = getModel(User.class);
		Long userid = user.getLong("userid");
		String password = user.getStr("password");
		if(password != null){
			password = EncryptKit.EncryptMd5(password);
		}
		String newPassword = getPara("newPassword");
		String repeatPassword = getPara("repeatPassword");
		String reg = "[a-zA-Z0-9_]{6,16}";
		Pattern pattern = true ? Pattern.compile(reg) : Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(newPassword);
	    user = user.getUserForId(userid);
	    boolean isOk = true;
	    if(password == null ||newPassword == null||repeatPassword == null){
	    	setAttr("nullMsg", "密码不能为空");
	    	isOk = false;
	    	setAttr("userId", userid);
			setAttr("menuId", "4");
			setAttr("menuid", "1");
			render("/personalCenter.html");
			return;
	    }
	    if(!password.equals(user.getStr("password"))){
	    	setAttr("passwordMsg", "原密码错误");
	    	isOk = false;
	    }
	    if(!matcher.matches()){
	    	setAttr("newPasswordMsg", "新密码格式不正确");
	    	isOk = false;
	    }
	    if(!newPassword.equals(repeatPassword)){
	    	setAttr("repeatPasswordMsg", "两次密码输入不一致");
	    	isOk = false;
	    	
	    }
	    if(isOk){
	    	newPassword = password = EncryptKit.EncryptMd5(newPassword);
	    	user.updatePassword(userid,newPassword);
	    	setAttr("nullMsg", "密码修改成功");
			
	    }else{
	    	setAttr("user", user);
	    	setAttr("newPassword", newPassword);
	    	setAttr("repeatPassword", repeatPassword);
	    }
		setAttr("userId", userid);
		setAttr("menuId", "4");
		setAttr("menuid", "1");
		render("/personalCenter.html");
	}
}
