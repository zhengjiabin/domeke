package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.SearchKey;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.validator.SearchKeyValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;


@ControllerBind(controllerKey="searchkey")
@Before(LoginInterceptor.class)
public class SearchKeyController extends Controller{
	/**
	 * 新增关键字
	 */
	@Before({SearchKeyValidator.class})
	public void save(){
		SearchKey searchKey = getModel(SearchKey.class);	
		searchKey.saveSearchKey();
		select();
		render("/admin/admin_keywordsManage.html");
	}
	/**
	 * 查询关键字
	 */
	public void select(){
		selectUtil();		
		render("/admin/admin_keywords.html");
	}
	private void selectUtil() {
		SearchKey.searchdao.removeCache();		
		List<SearchKey> searchKeyList = SearchKey.searchdao.getSearchKey();
		setAttr("searchKeyList", searchKeyList);
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
		render("/admin/admin_keywordsManage.html");
	}
	/**
	 * 删除关键字
	 */
	public void delete(){
		int keyid = getParaToInt();
		SearchKey.searchdao.deleteSearchKey(keyid);
		select();
		render("/admin/admin_keywordsManage.html");
	}
	public void goToManager(){
		selectUtil();
		render("/admin/admin_keywords.html");
	}
}
