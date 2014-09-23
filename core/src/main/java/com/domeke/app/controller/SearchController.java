package com.domeke.app.controller;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.domeke.app.model.vo.BaseVO;
import com.domeke.app.model.vo.WorksVO;
import com.domeke.app.solr.utils.SolrKit;
import com.jfinal.core.Controller;

/**
 * 站内搜索
 * 
 * @author chenhailin
 *
 */
public class SearchController extends Controller {


	/**
	 * 站内搜索功能
	 */
	public void search() {
		String queryKey = getPara("queryKey");
		WorksVO worksVO = new WorksVO();
		try {
			String[] tags = new String[] { "worksname", "describle", "cover" };
			List<BaseVO> worksVOList = SolrKit.query(tags, queryKey, worksVO);
			setAttr("worksVOList", worksVOList);
			render("/searchSite.html");
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
