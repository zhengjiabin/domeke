package com.ximi.app.base.cofig;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.config.ConfigTools;
import com.jfinal.plugin.druid.DruidPlugin;

public class DruidDatasouceUtil {
	
	private static Configuration config;

	private static Logger logger = LoggerFactory.getLogger(AppBaseConfig.class);
	
	public static DruidPlugin getDruidPlugin() {
		
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
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
		druidPlugin.setDriverClass(driveClass);
		druidPlugin.setFilters(getProperty("jdbc.filters"));
		
		
		int initialSize = getIntProperty("jdbc.initialSize");
		druidPlugin.setInitialSize(initialSize);
		int minIdle =getIntProperty("jdbc.minIdle");
		druidPlugin.setMinIdle(minIdle);
		int maxActive=getIntProperty("jdbc.maxActive");
		druidPlugin.setMaxActive(maxActive);
		long maxWait=getLongPropertiy("jdbc.maxWait");
		druidPlugin.setMaxWait(maxWait);
		long timeBetweenConnectErrorMillis=getLongPropertiy("jdbc.timeBetweenConnectErrorMillis");
		druidPlugin.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
		long timeBetweenEvictionRunsMillis=getLongPropertiy("jdbc.timeBetweenEvictionRunsMillis");
		druidPlugin.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		long minEvictableIdleTimeMillis=getLongPropertiy("jdbc.minEvictableIdleTimeMillis");
		druidPlugin.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		
		String validationQuery=getProperty("jdbc.validationQuery");
		druidPlugin.setValidationQuery(validationQuery);
		boolean testWhileIdle= getBoolenProperty("jdbc.testWhileIdle");
		druidPlugin.setTestWhileIdle(testWhileIdle);
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
		//只要maxPoolPreparedStatementPerConnectionSize>0,poolPreparedStatements就会被自动设定为true，参照druid的源码
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		
		
		return druidPlugin;

	}
	
	public static String getHandlerProperty(){
		if(config == null) {
			loadConfigurationFile("datasource.properties");
		}
		return config.getString("jdbc.stat");
	}
	
	private static Configuration loadConfigurationFile(String file) {
		try {
			config = new PropertiesConfiguration(file);
		} catch (ConfigurationException e) {
			logger.error("Config file loading failed！", e);
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
