package com.domeke.app.utils;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class MailTemplateTest {
	@Test
	public void testGetHtml() {
		
		MailTemplate mail = new MailTemplate();
		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "test");
		String html = mail.getHtml("mailValidate", params);
		System.out.println(html);
		
	}
}
