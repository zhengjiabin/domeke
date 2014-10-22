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

import com.domeke.app.model.vo.BaseVO;
import com.domeke.app.solr.SolrServerClient;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;

public class SolrKit {

	private static Logger logger = LoggerFactory.getLogger(SolrKit.class);

	public static Page query(String[] tags, String queryKey, BaseVO vo, Page page) throws SolrServerException {
		SolrServer server = SolrServerClient.getInstance().getServer();
		StringBuffer queryStrBuffer = new StringBuffer();

		if (tags == null || tags.length <= 0) {
			return null;
		}
		for (int i = 0; i < tags.length; i++) {
			queryStrBuffer.append(tags[i] + ":" + queryKey);
			if (i != tags.length - 1) {
				queryStrBuffer.append(" OR ");
			}
		}

		SolrQuery query = setQueryFilter(page, queryStrBuffer);

		QueryResponse response = null;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		SolrDocumentList docs = response.getResults();

		List<BaseVO> voList = getBaseVoList(vo, docs);
		page = getPage(page, docs, voList);

		logger.info("=======文档个数：{}", docs.getNumFound());
		logger.info("=======查询时间：{}", response.getQTime());

		return page;
	}

	private static Page getPage(Page page, SolrDocumentList docs, List<BaseVO> voList) {
		int numFound = (int) docs.getNumFound();
		int totalPageSize = 1;
		if (numFound % page.getPageSize() != 0) {
			totalPageSize = numFound / page.getPageSize() + 1;
		} else {
			totalPageSize = numFound / page.getPageSize();
		}
		page = new Page(voList, page.getPageNumber(), page.getPageSize(), numFound, totalPageSize);
		return page;
	}

	private static List<BaseVO> getBaseVoList(BaseVO vo, SolrDocumentList docs) {
		List<BaseVO> voList = Lists.newArrayList();
		for (SolrDocument doc : docs) {
			Collection<String> fieldNames = doc.getFieldNames();
			for (String fieldName : fieldNames) {
				Object fieldValue = doc.getFieldValue(fieldName);
				logger.debug("{}====== {}", fieldName, doc.getFieldValue(fieldName));
				try {
					BeanUtils.setProperty(vo, fieldName, fieldValue);
				} catch (IllegalAccessException e) {
					logger.error("IllegalAccessException", e);
				} catch (InvocationTargetException e) {
					logger.error("InvocationTargetException", e);
				}
			}
			voList.add(vo);
		}
		return voList;
	}

	private static SolrQuery setQueryFilter(Page page, StringBuffer queryStrBuffer) {
		SolrQuery query = new SolrQuery(queryStrBuffer.toString());
		query.setHighlight(true);
		query.addHighlightField("worksname");
		query.addHighlightField("desc");
		query.setHighlightSimplePre("<font color='red'>");// 标记，高亮关键字前缀
		query.setHighlightSimplePost("</font>");// 后缀
		query.setHighlightSnippets(2);// 结果分片数，默认为1
		query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100
		query.setStart(page.getPageNumber());
		query.setRows(page.getPageSize());
		return query;
	}
}
