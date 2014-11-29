package com.domeke.app.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.ConversionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.common.collect.Maps;

public class PropKit extends PropertyPlaceholderConfigurer {

	private final static Map<String, Object> propertiesMap = Maps.newHashMap();

	static {
		init();
	}

	@Override
	public void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
	}

	public static void init() {
		String resource = PropKit.class.getResource("/").getPath();
		File files = new File(resource);
		File[] listFiles = files.listFiles();
		for (File file : listFiles) {
			String name = file.getName();
			if (name.indexOf(".properties") > 0) {
				setProperties(name);
			}
		}

	}

	/**
	 * 设置properteis文件中的属性到map对象当中
	 * 这里暂时没有考虑属性文件中键重复的情况
	 * 一般也不建议在属性文件中存在重复的属性
	 * 如有重复请在属性文件中删除或重命名
	 * @param name
	 */
	private static void setProperties(String name) {
		Resource resource = new ClassPathResource(name);
		EncodedResource encRes = new EncodedResource(resource, "UTF-8");
		Properties props = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(encRes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Object> keySet = props.keySet();

		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			propertiesMap.put(key, props.getProperty(key));
		}
	}

	private boolean isPropertiesFile(String filename) {
		return false;
	}

	private static Object getProperty(String key) {
		return propertiesMap.get(key);
	}

	public static String getString(String key) {
		String s = getString(key, null);
		if (s != null) {
			return s;
		} else {
			return null;
		}
	}

	private static String getString(String key, String defaultValue) {
		Object value = resolveContainerStore(key);
		if (value instanceof String) {
			return value.toString();
		} else if (value == null) {
			return "";
		} else {
			throw new ConversionException('\'' + key + "' doesn't map to a String object");
		}
	}

	private static Object resolveContainerStore(String key) {
		Object value = getProperty(key);
		if (value != null) {
			if (value instanceof Collection) {
				Collection<?> collection = (Collection<?>) value;
				value = collection.isEmpty() ? null : collection.iterator().next();
			} else if (value.getClass().isArray() && Array.getLength(value) > 0) {
				value = Array.get(value, 0);
			}
		}

		return value;
	}

	public static void main(String[] args) {
		String test = PropKit.getString("RESOURCE.root");
		System.out.println(test);
	}
}
