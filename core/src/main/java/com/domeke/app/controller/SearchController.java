package com.domeke.app.controller;

import org.apache.solr.client.solrj.SolrServerException;

import com.domeke.app.model.vo.WorksVO;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.solr.utils.SolrKit;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * 站内搜索
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/search")
public class SearchController extends Controller {

	/**
	 * 站内搜索功能
	 */
	public void search() {
		Page page = new Page(null, 0, 0, 0, 0);
		String queryKey = getPara("queryKey");
		WorksVO worksVO = new WorksVO();
		try {
			String[] tags = new String[] { "worksname", "desc", "cover" };
			Page worksVOList = SolrKit.query(tags, queryKey, worksVO, page);
			setAttr("worksVOList", worksVOList);
			render("/searchSite.html");
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
