package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.SearchKey;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

@ControllerBind(controllerKey="searchkey")
public class SearchKeyController extends Controller{
	/**
	 * 查询关键字
	 */
	public void select(){
		SearchKey.searchdao.removeCache();		
		List<SearchKey> searchKeyList = SearchKey.searchdao.getVentWall();
		setAttr("searchKeyList", searchKeyList);		
		render("/demo/addSearch.html");
	}
	public void index(){
		select();
	}
	/**
	 * 更新关键字
	 */
	public void updateById(){
		SearchKey searchKey = getModel(SearchKey.class);
		Long keyid = searchKey.get("keyid");
		SearchKey.searchdao.findById(keyid).setAttrs(searchKey).update();
		select();
	}
	/**
	 * 删除关键字
	 */
	public void delete(){
		int keyid = getParaToInt();
		SearchKey.searchdao.deleteSearchKey(keyid);
		select();
	}
}
