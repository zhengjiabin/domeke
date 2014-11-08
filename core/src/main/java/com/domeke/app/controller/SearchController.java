package com.domeke.app.controller;

import org.apache.solr.client.solrj.SolrServerException;

import com.domeke.app.model.vo.WorksVO;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.solr.utils.SolrKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
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
		setPage();
		render("/searchSite.html");
	}
	
	/**
	 * 分页跳转
	 * 请求 ./search/searchPage?queryKey=${queryKey!}&pageNumber={pageNumber!}&pageSize={pageSize!}
	 */
	public void searchPage(){
		setPage();
		render("/searchPage.html");
	}
	
	/**
	 * 设置分页
	 */
	private void setPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 30);
		Page<WorksVO> page = new Page<WorksVO>(null, pageNumber, pageSize, 0, 0);
		String queryKey = getPara("queryKey");
		if(StrKit.isBlank(queryKey)){
			return;
		}
		try {
			String[] tags = new String[] { "worksname", "desc", "cover" };
			Page<WorksVO> worksVOPage = SolrKit.query(tags, queryKey, WorksVO.class, page);
			setAttr("worksVOPage", worksVOPage);
			keepPara("queryKey");
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
