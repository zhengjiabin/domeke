package com.domeke.app.base.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.config.ConfigTools;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;

public class DruidDatasouceUtil {
	
	private static Configuration config;

	private static Logger logger = LoggerFactory.getLogger(AppBaseConfig.class);
	
	public static DruidPlugin getDruidPlugin() {
		
		logger.info("===加载数据库配置文件===start");
		loadConfigurationFile("datasource.properties");
		String url = getProperty("jdbc.url");
		String password = getProperty("jdbc.password");
		try {
			password = ConfigTools.decrypt(getProperty("jdbc.password"));
		} catch (Exception e) {
			logger.error("解密配置文件出错", e);
			throw new RuntimeException(e);
		}
		String username = getProperty("jdbc.username");
		String driveClass = getProperty("jdbc.driverClass");
		logger.info("====数据库URL==={}",url);
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
		druidPlugin.setDriverClass(driveClass);
		
		if(StringUtils.isNotBlank(getProperty("jdbc.filters"))){
			druidPlugin.setFilters(getProperty("jdbc.filters"));
		}
		
		
		if(getIntProperty("jdbc.initialSize") > 0){
			int initialSize = getIntProperty("jdbc.initialSize");
			druidPlugin.setInitialSize(initialSize);
		}
		if(getIntProperty("jdbc.minIdle")>0){
			
			int minIdle =getIntProperty("jdbc.minIdle");
			druidPlugin.setMinIdle(minIdle);
		}
		
		if(getIntProperty("jdbc.maxActive") > 0) {
			
			int maxActive=getIntProperty("jdbc.maxActive");
			druidPlugin.setMaxActive(maxActive);
		}
		
		if(getLongPropertiy("jdbc.maxWait")  > 0) {
			long maxWait=getLongPropertiy("jdbc.maxWait");
			
			druidPlugin.setMaxWait(maxWait);
		}
		
		if(getLongPropertiy("jdbc.timeBetweenConnectErrorMillis") > 0) {
			
			long timeBetweenConnectErrorMillis=getLongPropertiy("jdbc.timeBetweenConnectErrorMillis");
			druidPlugin.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
		}
		
		if(getLongPropertiy("jdbc.timeBetweenEvictionRunsMillis") > 0 ){
			long timeBetweenEvictionRunsMillis=getLongPropertiy("jdbc.timeBetweenEvictionRunsMillis");
			druidPlugin.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			
		}
		
		if(getLongPropertiy("jdbc.minEvictableIdleTimeMillis") > 0){
			
			long minEvictableIdleTimeMillis=getLongPropertiy("jdbc.minEvictableIdleTimeMillis");
			druidPlugin.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		}
		
		
		if(StringUtils.isNotBlank(getProperty("jdbc.validationQuery"))){
			
			String validationQuery=getProperty("jdbc.validationQuery");
			druidPlugin.setValidationQuery(validationQuery);
		}
		if(getBoolenProperty("jdbc.testWhileIdle")){
			boolean testWhileIdle= getBoolenProperty("jdbc.testWhileIdle");
			druidPlugin.setTestWhileIdle(testWhileIdle);
		}
		boolean testOnBorrow =getBoolenProperty("jdbc.testOnBorrow");
		druidPlugin.setTestOnBorrow(testOnBorrow);
		boolean testOnReturn=getBoolenProperty("jdbc.testOnReturn");
		druidPlugin.setTestOnReturn(testOnReturn);
		
		boolean removeAbandoned=getBoolenProperty("jdbc.removeAbandoned");
		druidPlugin.setRemoveAbandoned(removeAbandoned);
		long removeAbandonedTimeoutMillis =getLongPropertiy("jdbc.removeAbandonedTimeoutMillis");
		druidPlugin.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
		boolean logAbandoned=getBoolenProperty("jdbc.logAbandoned");
		druidPlugin.setLogAbandoned(logAbandoned);
		
		int maxPoolPreparedStatementPerConnectionSize= getIntProperty("jdbc.maxPoolPreparedStatementPerConnectionSize");
		//只要maxPoolPreparedStatementPerConnectionSize>0,poolPreparedStatements就会被自动设定为true，参照druid的源�?
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		logger.info("===加载数据库配置文件===end");
		return druidPlugin;

	}
	
	public static DruidStatViewHandler getHandlerProperty(){
		if(config == null) {
			loadConfigurationFile("datasource.properties");
		}
		DruidStatViewHandler dsvh = new DruidStatViewHandler(config.getString("jdbc.stat"));
		return dsvh;
	}
	
	
	
	private static Configuration loadConfigurationFile(String file) {
		try {
			config = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			logger.error("Config file loading failed?", e);
			throw new RuntimeException("Config file loading failed: " + file);
		}
		return config;
	}

	private static String getProperty(String key) {
		return config.getString(key);
	}
	
	private static int getIntProperty(String key) {
		return config.getInt(key);
	}
	private static boolean getBoolenProperty(String key) {
		return config.getBoolean(key);
	}
	
	private static long getLongPropertiy(String key){
		return config.getLong(key);
	}

}
