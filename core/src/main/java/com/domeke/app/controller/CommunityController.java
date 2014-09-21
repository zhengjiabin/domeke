package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Activity;
import com.domeke.app.model.Community;
import com.domeke.app.model.Post;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {

	public void index() {
		setCommunityFatList();
		setCommunitySonList();
		setVentWall();
		setPublishNumber();
		
		render("/community/community.html");
	}
	
	/**
	 * 创建主题
	 */
	public void create() {
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/create";
		forwardAction(actionKey);
	}
	
	/**
	 * 跳转到选择主题页面
	 */
	public void skipCommunity(){
		setCommunityFatList();
		render("/community/selectCommunity.html");
	}
	
	/**
	 * 下拉选择发布的主题
	 */
	public void selectSon(){
		String pId = getPara("pId");
		if(pId != null && pId.length()>0){
			setCommunitySonListByPid(pId);
		}
		render("/community/selectCommunitySon.html");
	}
	
	/**
	 * 显示更多的版块
	 */
	public void showMoreCommunity(){
		setCommunitySonList();
		setAttr("communitySize", 81);
		render("/community/community_showMore.html");
	}
	
	/**
	 * 跳转到指定主题
	 */
	public void goToCommunity(){
		String communityId = getPara("communityId");
		if(communityId == null || communityId.length()<=0){
			renderNull();
			return;
		}
		Community community = Community.dao.findById(communityId);
		String actionKey = community.getStr("actionkey");
		actionKey = actionKey + "/skipCreate";
		forwardAction(actionKey);
	}
	
	private void setVentWall(){
		Object ventWallCount = VentWall.venWdao.getTodayCount();
		setAttr("ventWallCount", ventWallCount);
	}
	
	/**
	 * 设置发帖数
	 */
	private void setPublishNumber(){
		//今日发帖数
		Long postTodayCount = Post.dao.getTodayCount();
		Long activityTodayCount = Activity.dao.getTodayCount();
		Long todayCount = postTodayCount + activityTodayCount;
		setAttr("todayCount", todayCount);
		//昨日发帖数
		Long postYesCount = Post.dao.getYesterdayCount();
		Long activityYesCount = Activity.dao.getYesterdayCount();
		Long yesCount = postYesCount + activityYesCount;
		setAttr("yesCount", yesCount);
		//总发帖数
		Long postCount = Post.dao.getCount();
		Long activityCount = Activity.dao.getCount();
		Long count = postCount + activityCount;
		setAttr("count", count);
		
		
	}

	/**
	 * 跳转指定版块
	 */
	public void goToOrderCommunity() {
		String communityId = getPara("communityId");
		Community.dao.updateTimes(communityId);
		
		String actionKey = getPara("actionKey");
		redirect(actionKey, true);
	}

	/**
	 * admin管理中对应的社区管理入口
	 */
	public void goToManager() {
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_community.html");
	}
	
	/**
	 * 
	 */
	public void saveSon(){
		Object communityId = getPara("communityId");
		Community community = getModel(Community.class);
		if(communityId != null){
			updateCommuntiy(community);
		}else{
			insertCommunity(community);
		}
		
		Object pId = getPara("pId");
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
	}
	
	/**
	 * 
	 */
	public void saveFat(){
		Object communityId = getPara("communityId");
		Community community = getModel(Community.class);
		if(communityId != null){
			updateCommuntiy(community);
			setCommunityFat(communityId);
			setCommunitySonListByPid(communityId);
		}else{
			insertCommunity(community);
			setAttr("communityFat", community);
		}
		render("/admin/admin_communityFat.html");
	}
	
	private void setCommunityFat(Object pId){
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
	}
	
	/**
	 * 
	 * @param community
	 */
	private void updateCommuntiy(Community community){
		setCommunity(community);
		community.update();
	}
	
	private void insertCommunity(Community community){
		setCommunity(community);
		community.save();
	}
	
	/**
	 * 
	 */
	public void deleteSon(){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length() > 0){
			Community.dao.deleteById(communityId);
		}
		
		Object pId = getPara("pId");
		Community community = Community.dao.findById(pId);
		setAttr("communityFat", community);
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
	}
	
	/**
	 * 
	 */
	public void deleteFat(){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length() > 0){
			Community.dao.deleteFatAndSonByCommunityId(communityId);
		}
		
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_detailCommunity.html");
	}
	
	public void addCommunitySon(){
		Object pId = getPara("pId");
		Community community = getModel(Community.class);
		community.set("pid", pId);
		community.set("level", 2);
		community.set("title", "请填写标题");
		community.save();
		
		Community communityFat = Community.dao.findById(pId);
		setAttr("communityFat", communityFat);
		
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
	}
	
	public void addCommunityFat(){
		Community community = getModel(Community.class);
		community.set("level", 1);
		community.set("title", "请填写标题");
		community.save();
		
		setCommunityFatList();
		setCommunitySonList();
		render("/admin/admin_detailCommunity.html");
	}
	
	/**
	 * 设置版块
	 * @param communityId
	 */
	private void setCommunity(Community community){
		String communityId = getPara("communityId");
		if(communityId != null && communityId.length()>0){
			community.set("communityid", communityId);
		}
		
		String title = getPara("title");
		if(title != null && title.length()>0){
			community.set("title", title);
		}
		
		String content = getPara("content");
		if(content != null && content.length()>0){
			community.set("content", content);
		}
		
		String actionkey = getPara("actionkey");
		if(actionkey != null && actionkey.length()>0){
			community.set("actionkey", actionkey);
		}
		
		String level = getPara("level");
		if(level != null && level.length()>0){
			community.set("level", level);
		}
		
		String position = getPara("position");
		if(position != null && position.length()>0){
			community.set("position", position);
		}
		
		String pId = getPara("pId");
		if(pId != null && pId.length()>0){
			community.set("pid", pId);
		}
	}
	
	/**
	 * 设置父版块
	 */
	private void setCommunityFatList(){
		List<Community> communityFatList = Community.dao.findFatList();
		setAttr("communityFatList", communityFatList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setCommunitySonList(){
		List<Community> communitySonList = Community.dao.findSonList();
		setAttr("communitySonList", communitySonList);
	}
	
	/**
	 * 设置子版块
	 */
	private void setCommunitySonListByPid(Object pId){
		List<Community> communitySonList = Community.dao.findSonListByPid(pId);
		setAttr("communitySonList", communitySonList);
	}
}
