package com.domeke.app.utils;

import java.io.File;
import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

public class MailTemplate {
	
	public void getHtml(String tempalteKey){
		String root = System.getProperty("user.dir")+File.separator+"s";
		FileResourceLoader resourceLoader = new FileResourceLoader(root,"utf-8");
		Configuration cfg = null ;
		try {
			cfg = Configuration.defaultConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = gt.getTemplate("/mail/hello.txt");
		String str = t.render();
		System.out.println(str);
	}
}
