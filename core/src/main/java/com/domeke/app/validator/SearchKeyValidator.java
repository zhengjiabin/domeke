package com.domeke.app.validator;

import java.util.List;

import com.domeke.app.model.SearchKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

/**
 * 
 * @author zhouying
 *
 */
public class SearchKeyValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		String keyname = c.getPara("searchKey.keyname");
		if (StrKit.isBlank(keyname)){
			addError("searchKeyMsg", "请输入关键字后，点击新增！");
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepPara();
		SearchKey.searchdao.removeCache();		
		List<SearchKey> searchKeyList = SearchKey.searchdao.getSearchKey();
		c.setAttr("searchKeyList", searchKeyList);
		c.render("/admin/admin_keywordsManage.html");
	}
}
