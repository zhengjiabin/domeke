package com.domeke.app.solr;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.domeke.app.utils.PropKit;


public class SolrServerClient {

	private String SOLR_ADMIN_URL;
	private static HttpSolrServer server = null;
	private volatile static SolrServerClient solrServiceClient = null;

	private SolrServerClient() {
		this.getServer();
	}

	/** 
	 * SolrServerClient 是线程安全的 需要采用单例模式 
	 * 此处实现方法适用于高频率调用查询 
	 * 
	 * @return SolrServerClient 
	 */
	public static SolrServerClient getInstance() {
		if (solrServiceClient == null) {
			synchronized (SolrServerClient.class) {
				if (solrServiceClient == null) {
					solrServiceClient = new SolrServerClient();
				}
			}
		}
		return solrServiceClient;
	}

	/** 
	 * 初始化的HttpSolrServer 对象,并获取此唯一对象 
	 * 配置按需调整 
	 * @return 此server对象 
	 */
	public HttpSolrServer getServer() {
		if (server == null) {
			SOLR_ADMIN_URL = PropKit.getString("solr_admin_url");
			server = new HttpSolrServer(SOLR_ADMIN_URL);
			server.setConnectionTimeout(3000);
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
		}
		return server;
	}

}