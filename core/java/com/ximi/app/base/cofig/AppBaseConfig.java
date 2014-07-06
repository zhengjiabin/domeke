package com.ximi.app.base.cofig;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;

public class AppBaseConfig extends JFinalConfig {
	
	
	private Configuration config;
	
	private Logger logger = LoggerFactory.getLogger(AppBaseConfig.class);
	

	@Override
	public void configConstant(Constants constants) {

	}

	@Override
	public void configHandler(Handlers handlers) {
		DruidStatViewHandler dsvh = new DruidStatViewHandler("/druid");
		handlers.add(dsvh);
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {

	}

	@Override
	public void configPlugin(Plugins plugins) {
		plugins.add(getDruidPlugin());
	}
	
	private DruidPlugin getDruidPlugin(){
		loadPropertyFile("dataSource.properties");
		String url = getProperty("jdbc.url");
		String password = getProperty("jdbc.password");
		String username = getProperty("jdbc.username");
		String driveClass = getProperty("jdbc.driverClass");
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
		druidPlugin.setDriverClass(driveClass);
		return druidPlugin;
		
	}

	@Override
	public void configRoute(Routes arg0) {

	}
	
	public Configuration loadConfigurationFile(String file) {
		try {
			config = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			logger.error("Config file loading failed！",e);
			throw new RuntimeException("Config file loading failed: " + file);
		}		
		return config;
	}
	
	public String getProperty(String key) {
		return config.getString(key);
	}
	

}
