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

public class SolrKit {

	private static Logger logger = LoggerFactory.getLogger(SolrKit.class);

	public static List<BaseVO> query(String[] tags, String queryKey, BaseVO vo) throws SolrServerException {
		SolrDocumentList docs = query(tags, queryKey);
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

	public static SolrDocumentList query(String[] tags, String queryKey) throws SolrServerException {
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

		SolrQuery query = new SolrQuery(queryStrBuffer.toString());
		QueryResponse response = null;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		SolrDocumentList docs = response.getResults();
		logger.info("=======文档个数：{}", docs.getNumFound());
		logger.info("=======查询时间：{}", response.getQTime());

		return docs;
	}
}
