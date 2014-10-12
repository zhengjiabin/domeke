package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.model.Comment;
import com.domeke.app.model.Community;
import com.domeke.app.model.Treasure;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "treasure")
public class TreasureController extends Controller {

	/**
	 * 查询指定社区的分页宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void find() {
		String communityId = getPara("communityId");
		setTreasurePage(communityId);
		
		render("/community/treasurePage.html");
	}
	
	/**
	 * 分页查询宝贝
	 */
	public void findPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = Treasure.dao.findPage(pageNumber, pageSize);
		setAttr("treasurePage", treasurePage);
		render("/admin/admin_detailtreasure.html");
	}

	/**
	 * 查询当前用户所有宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void findByUserId() {
		Object userId = getUserId();
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);

		Treasure treasure = getModel(Treasure.class);
		Page<Treasure> page = treasure.findByUserId(userId, pageNumber, pageSize);
		setAttr("page", page);
		render("/community/myTreasure.html");
	}
	
	/**
	 * 置顶功能
	 */
	public void setTop(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson("false");
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("top", 1);
		treasure.update();
		renderJson("true");
	}
	
	/**
	 * 精华功能
	 */
	public void setEssence(){
		String treasureId = getPara("targetId");
		if(treasureId == null || treasureId.length()<=0){
			renderJson("false");
			return;
		}
		Treasure treasure = getModel(Treasure.class);
		treasure.set("treasureid", treasureId);
		treasure.set("essence", 1);
		treasure.update();
		renderJson("true");
	}

	/**
	 * 查询指定宝贝信息
	 * 
	 * @return 指定宝贝信息
	 */
	public void findById() {
		String treasureId = getPara("targetId");
		Treasure.dao.updateTimes(treasureId);
		
		setTreasure(treasureId);
		setCommunity();
		setCommentPage(treasureId);
		setFollowList(treasureId);
		
		keepPara("communityId");
		render("/community/detailTreasure.html");
	}
	
	/**
	 * 设置版块信息
	 */
	private void setCommunity(){
		String communityId = getPara("communityId");
		Community communitySon = Community.dao.findById(communityId);
		setAttr("communitySon", communitySon);
		
		Object pId = communitySon.get("pid");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 设置宝贝信息
	 * @param treasureId
	 */
	private void setTreasure(Object treasureId){
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
	}
	
	/**
	 * 分页设置父回复信息
	 */
	public void setCommentPage(Object treasureId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Comment> commentPage = Comment.dao.findPageByTargetId(treasureId,
				"30", pageNumber, pageSize);
		setAttr("commentPage", commentPage);
	}
	
	/**
	 * 设置子回复信息
	 */
	public void setFollowList(Object treasureId) {
		List<Comment> followPage = Comment.dao.findFollowByTargetId(treasureId,
				"30");
		setAttr("followPage", followPage);
	}

	/**
	 * 异步分页查询回复信息
	 */
	public void findCommentByTreasureId() {
		Object treasureId = getPara("treasureId");
		setCommentPage(treasureId);
		setFollowList(treasureId);
		setReplyCommentData();
		render("/comment/comment.html");
	}

	/**
	 * 查询修改的宝贝信息
	 * 
	 * @return 宝贝信息
	 */
	public void modifyById() {
		String treasureId = getPara("treasureId");
		Treasure treasure = Treasure.dao.findById(treasureId);
		setAttr("treasure", treasure);
		render("/community/modifyTreasure.html");
	}

	/**
	 * 修改宝贝信息
	 */
	public void modify() {
		Treasure treasure = getModel(Treasure.class);
		treasure.update();

		findByUserId();
	}

	/**
	 * 删除指定宝贝
	 */
	public void deleteById() {
		String treasure = getPara("treasureId");
		Treasure.dao.deleteById(treasure);

		findByUserId();
	}

	/**
	 * 跳转宝贝申请
	 */
	public void skipCreate() {
		String communityId = getPara("communityId");
		Object userId = getUserId();
		Object treasure = Treasure.dao.findHasPublish(communityId, userId);
		if(treasure != null){
			renderJson(false);
			return;
		}
		
		keepPara("communityId");
		render("/community/createTreasure.html");
	}
	
	/**
	 * 获取用户Id
	 * @return
	 */
	private Object getUserId(){
		User user = getUser();
		if(user == null){
			return null;
		}
		return user.get("userid");
	}

	/**
	 * 创建宝贝申请
	 */
	public void create() {
		Treasure treasure = getModel(Treasure.class);
		Object communityId = treasure.get("communityid");
		Object userId = getUserId();
		Object treasureOld = Treasure.dao.findHasPublish(communityId, userId);
		if(treasureOld != null){
			renderJson(false);
			return;
		}
		
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();

		treasure.set("userid", userId);
		treasure.set("userip", remoteIp);
		treasure.save();

		setTreasurePage(communityId);
		
		render("/community/treasure.html");
	}

	/**
	 * 添加回复信息
	 */
	public void replyComment() {
		addComment();

		String treasureId = getPara("treasureId");
		setTreasure(treasureId);
		setCommentPage(treasureId);
		setFollowList(treasureId);
		
		setReplyCommentData();

		render("/comment/comment.html");
	}
	
	/**
	 * 添加回复信息
	 */
	public void replyFollow() {
		addComment();

		String treasureId = getPara("treasureId");
		setTreasure(treasureId);
		setFollowList(treasureId);
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			setComment(pId);
		}
		setReplyCommentData();

		render("/comment/followPage.html");
	}
	
	/**
	 * 设置回复信息
	 * 
	 * @param commentId
	 */
	private void setComment(Object commentId) {
		Comment comment = Comment.dao.findById(commentId);
		setAttr("comment", comment);
	}

	/**
	 * 保存回复信息
	 */
	private void addComment() {
		Comment comment = getModel(Comment.class);

		Object targetId = getPara("targetId");
		comment.set("targetid", targetId);

		Object userId = getUserId();
		comment.set("userid", userId);

		comment.set("idtype", "30");

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set("userip", remoteIp);

		//默认回复楼层
		comment.set("level", "1");
		
		String pId = getPara("pId");
		if (pId != null && pId.length() > 0) {
			comment.set("pid", pId);
			comment.set("level", "2");
		}

		String toUserId = getPara("toUserID");
		if (toUserId != null && toUserId.length() > 0) {
			comment.set("touserid", toUserId);
		}

		Object message = getPara("message");
		comment.set("message", message);
		comment.save();
	}

	/**
	 * 添加额外的回复信息
	 */
	private void setReplyCommentData() {
		Object targetId = getPara("targetId");
		setAttr("targetId", targetId);
		keepPara("pageAction", "fatherNode","commentAction","followAction");
	}

	/**
	 * 删除回复信息
	 */
	public void deleteComment() {
		Object commentId = getPara("commentId");
		Comment.dao.deleteReplyAll(commentId);

		findById();
	}

	public void index() {
		String communityId = getPara("communityId");
		setTreasurePage(communityId);
		setPublishNumber();
		
		render("/community/treasure.html");
	}
	
	/**
	 * 设置宝贝数
	 */
	private void setPublishNumber(){
		// 当前登录人宝贝数
		Object userId = getUserId();
		Long userTreasureCount = Treasure.dao.getCountByUserId(userId);
		setAttr("userCount", userTreasureCount);
		
		//今日宝贝数
		Long treasureTodayCount = Treasure.dao.getTodayCount();
		setAttr("todayCount", treasureTodayCount);
		//昨日宝贝数
		Long treasureYesCount = Treasure.dao.getYesterdayCount();
		setAttr("yesCount", treasureYesCount);
		//总宝贝数
		Long treasureCount = Treasure.dao.getCount();
		setAttr("count", treasureCount);
	}
	
	/**
	 * 点击宝贝菜单
	 */
	public void home(){
		setTreasurePage(null);
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
		render("/community/treasureAll.html");
	}

	/**
	 * 设置分页宝贝
	 */
	private void setTreasurePage(Object communityId) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Treasure> treasurePage = null;
		if(communityId == null){
			treasurePage =Treasure.dao.findPage(pageNumber, pageSize);
		}else{
			treasurePage = Treasure.dao.findPageByCommunityId(pageNumber,pageSize, communityId);
		}
		setAttr("treasurePage", treasurePage);
		setAttr("communityId", communityId);
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr("user");
		return user;
	}
}
