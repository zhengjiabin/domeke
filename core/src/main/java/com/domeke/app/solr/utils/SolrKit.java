package com.domeke.app.solr.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.domeke.app.solr.SolrServerClient;

public class SolrKit {

	public static void query(String tags, String queryKey) throws SolrServerException {
		SolrServer server = SolrServerClient.getInstance().getServer();
		SolrQuery query = new SolrQuery(tags + ":" + queryKey);
		QueryResponse response = null;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		SolrDocumentList docs = response.getResults();
		System.out.println("文档个数：" + docs.getNumFound());
		System.out.println("查询时间：" + response.getQTime());
		for (SolrDocument doc : docs) {
			System.out.println("workid: " + doc.getFieldValue("id"));
			System.out.println("workname: " + doc.getFieldValue("worksname"));
			System.out.println();
		}
	}

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();

		final Semaphore semp = new Semaphore(50);
		long start = System.currentTimeMillis();
		System.out.println(start);
		Runnable run = null;

		for (int i = 0; i < 2000; i++) {
			run = new Runnable() {
				@Override
				public void run() {
					try {
						semp.acquire();
						SolrKit.query("id", "2");
						semp.release();
					} catch (SolrServerException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			executorService.execute(run);
		}
		executorService.shutdown();
		long end = System.currentTimeMillis();
		System.out.println(end);

		System.out.println("耗时======:" + (end - start));
	}
}
