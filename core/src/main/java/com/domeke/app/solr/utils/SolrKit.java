package com.domeke.app.solr.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.solr.SolrServerClient;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;

public class SolrKit<T> {
	
	private volatile static	SolrKit<?> solrKit;

	private static Logger logger = LoggerFactory.getLogger(SolrKit.class);
	
	private boolean isSetFacet = false;
	
	private SolrKit(){
        
    }
	
	@SuppressWarnings("unchecked")
	public static <T> SolrKit<T> getInstance(){
        if(solrKit == null){
            synchronized (SolrKit.class){
                if(solrKit == null){
                	solrKit = new SolrKit<T>();
                }
            }
        }
        return (SolrKit<T>) solrKit;
    }

	public static <T> Page<T> query(String[] tags, String queryKey, Class<T> c, Page<T> page) throws SolrServerException {
		SolrKit<T> solrKit = SolrKit.getInstance();
		return solrKit.querys(tags, queryKey, c, page);
	}
	
	private Page<T> querys(String[] tags, String queryKey, Class<T> c, Page<T> page) throws SolrServerException{
		SolrQuery query = setQueryFilter(page, tags,queryKey);
		if(query == null){
			return null;
		}
		QueryResponse response = null;
		try {
			SolrServer server = SolrServerClient.getInstance().getServer();
			response = server.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		SolrDocumentList docs = response.getResults();

		List<T> voList = getBaseVoList(c, docs);
		page = getPage(page, docs, voList);

		logger.info("=======文档个数：{}", docs.getNumFound());
		logger.info("=======查询时间：{}", response.getQTime());

		return page;
	}

	private Page<T> getPage(Page<T> page, SolrDocumentList docs, List<T> voList) {
		int numFound = (int) docs.getNumFound();
		int totalPageSize = 1;
		if (numFound % page.getPageSize() != 0) {
			totalPageSize = numFound / page.getPageSize() + 1;
		} else {
			totalPageSize = numFound / page.getPageSize();
		}
		page = new Page<T>(voList, page.getPageNumber(), page.getPageSize(), totalPageSize, numFound);
		return page;
	}

	private List<T> getBaseVoList(Class<T> c, SolrDocumentList docs) {
		List<T> voList = Lists.newArrayList();
		for (SolrDocument doc : docs) {
			T t = newModel(c);
			if (t == null){
				break;
			}
			Collection<String> fieldNames = doc.getFieldNames();
			for (String fieldName : fieldNames) {
				Object fieldValue = doc.getFieldValue(fieldName);
				logger.debug("{}====== {}", fieldName, doc.getFieldValue(fieldName));
				try {
					BeanUtils.setProperty(t, fieldName, fieldValue);
				} catch (IllegalAccessException e) {
					logger.error("IllegalAccessException", e);
				} catch (InvocationTargetException e) {
					logger.error("InvocationTargetException", e);
				}
			}
			voList.add(t);
		}
		return voList;
	}
	
	private T newModel(Class<T> c){
		try {
			T t = c.newInstance();
			return t;
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private SolrQuery setQueryFilter(Page<T> page, String[] tags, String queryKey) {
		if (tags == null || tags.length <= 0) {
			return null;
		}
		SolrQuery query = new SolrQuery();
		
		setQuery(query, tags, queryKey);
		setHighlight(query, tags);
		if(isSetFacet){
			setFacet(query, tags);
		}
		setPage(query, page);
		setSort(query, tags);
		
		query.setTimeAllowed(1000);
		return query;
	}
	
	/**
	 * 设置分页
	 * @param queryKey
	 * @param page
	 */
	private void setPage(SolrQuery query, Page<T> page){
		int pageNumber = page.getPageNumber();
		int pageSize = page.getPageSize();
		query.setStart((pageNumber-1)*pageSize);
		query.setRows(pageSize);
	}
	
	/**
	 * 设置排序
	 * @param query
	 * @param tags
	 */
	private void setSort(SolrQuery query, String[] tags){
		for(String tag:tags){
			query.addSort(tag, SolrQuery.ORDER.desc);
		}
	}
	
	/**
	 * 设置查询条件
	 * @param query
	 * @param tags
	 */
	private void setQuery(SolrQuery query, String[] tags, String queryKey){
		StringBuffer queryStrBuffer = new StringBuffer();
		for (int i = 0; i < tags.length; i++) {
			queryStrBuffer.append(tags[i] + ":" + queryKey);
			if (i != tags.length - 1) {
				queryStrBuffer.append(" OR ");
			}
		}
		logger.info("queryStrBuffer:==========="+queryStrBuffer.toString());
		query.setQuery(queryStrBuffer.toString());
	}
	
	/**
	 * 设置高亮
	 * @param query
	 * @param tags
	 */
	private void setHighlight(SolrQuery query, String[] tags){
		query.setHighlight(true);
		for(String tag : tags){
			query.addHighlightField(tag);
		}
		// 标记，高亮关键字前缀
		query.setHighlightSimplePre("<font color='red'>");
		// 后缀
		query.setHighlightSimplePost("</font>");
		// 结果分片数，默认为1
		query.setHighlightSnippets(tags.length);
		// 每个分片的最大长度，默认为100
		query.setHighlightFragsize(1000);
	}
	
	/**
	 * 设置分片信息，用于统计查询字段出现次数
	 * @param query
	 * @param tags
	 */
	private void setFacet(SolrQuery query, String[] tags){
		query.setFacet(true);
        query.setFacetMinCount(1);
        // 段
        query.setFacetLimit(tags.length);
        // 分片字段
        query.addFacetField(tags);
	}

	public boolean isSetFacet() {
		return isSetFacet;
	}

	public void setSetFacet(boolean isSetFacet) {
		this.isSetFacet = isSetFacet;
	}
}
