package com.domeke.app.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.domeke.app.model.vo.BaseVO;
import com.domeke.app.model.vo.WorksVO;
import com.domeke.app.solr.utils.SolrKit;

public class SolrKitTest {

	@Test
	public void test() {

		try {
			List<BaseVO> list = SolrKit.query(new String[] { "worksname", "id" }, "瞳", new WorksVO());
			assertEquals(2, list.size());
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}
}
