package com.domeke.app.utils;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

public class MailTemplate {

	public static String getHtml(String tempalteKey, Map<String, Object> params) {
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/template");
		Configuration cfg = null;
		try {
			cfg = Configuration.defaultConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template template = gt.getTemplate("/mail/" + tempalteKey + ".html");
		template.binding("mail", params);
		return template.render();
	}
}
