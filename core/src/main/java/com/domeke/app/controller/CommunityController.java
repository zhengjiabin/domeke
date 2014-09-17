package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.List;

import com.domeke.app.model.Community;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey = "community")
public class CommunityController extends Controller {

	public void index() {
		setCommunityFatList();
		setCommunitySonList();
		
		setPublishNumber();
		render("/community/community.html");
	}
	
	/**
	 * 获取发帖数
	 */
	private void setPublishNumber(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 跳转指定版块
	 */
	public void goToOrderCommunity() {
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
		setCommunityFat(communityId);
		
		Object pId = getPara("pId");
		Community community = Community.dao.findById(pId);
		setAttr("communityFat", community);
		setCommunitySonListByPid(pId);
		render("/admin/admin_communityFat.html");
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
	private void setCommunityFat(Object communityId){
		Object title = getPara("title");
		Object content = getPara("content");
		Object actionkey = getPara("actionkey");
		Object level = getPara("level");
		Object position = getPara("position");
		Object pId = getPara("pId");
		
		Community community = null;
		if(communityId != null){
			community = Community.dao.findById(communityId);
		}else{
			community = getModel(Community.class);
			community.set("pid", pId);
		}
		community.set("title", title);
		community.set("content", content);
		community.set("actionkey", actionkey);
		community.set("level", level);
		community.set("position", position);
		community.save();
		setAttr("communityFat", community);
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
