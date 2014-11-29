package com.domeke.app.utils;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.domeke.app.message.DomekeMailSender;
import com.google.common.collect.Maps;

public class MailTemplateTest {

	private ApplicationContext context = null;

	@Test
	public void testGetHtml() {

		String test = PropKit.getString("mailSenderName");
		Assert.assertEquals("动漫方舟<dongmark_admin@126.com>", test);
		context = new ClassPathXmlApplicationContext("applicationContext-mail.xml");
		DomekeMailSender bean = (DomekeMailSender) context.getBean("domekeMailSender");
		Map<String, Object> params = Maps.newHashMap();
		params.put("username", "username");
		params.put("url", "url");
		bean.send("lijiasen@domeke.com", "registerSuccess", params);

	}
}
