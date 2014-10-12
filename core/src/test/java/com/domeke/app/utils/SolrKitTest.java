package com.domeke.app.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.domeke.app.model.vo.BaseVO;
import com.domeke.app.model.vo.WorksVO;
import com.domeke.app.solr.utils.SolrKit;
import com.jfinal.plugin.activerecord.Page;

public class SolrKitTest {

	@Test
	public void test() {
		Page page = new Page(null, 1, 2, 0, 0);
		try {
			List<BaseVO> list = SolrKit.query(new String[] { "worksname", "desc" }, "虐瞳", new WorksVO(), page);
			assertEquals(2, list.size());
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}
}
