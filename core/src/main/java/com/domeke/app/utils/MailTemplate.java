package com.domeke.app.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

public class MailTemplate {
	
	public String getHtml(String tempalteKey,Map<String,Object> params){
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
		Configuration cfg = null ;
		try {
			PropertiesConfiguration configuration = new PropertiesConfiguration("config_tempalte.properties");
			Properties properties = configuration.getProperties("mail_tempalte");
			cfg = new Configuration(properties);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		
		Template template = gt.getTemplate("/mail/"+tempalteKey+".html");
		template.binding(params);
		return template.render();
	}
}
