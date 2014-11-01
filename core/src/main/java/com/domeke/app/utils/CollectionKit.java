package com.domeke.app.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class CollectionKit {

	public static <T> boolean isBlank(Collection<T> collection) {
		return collection == null || collection.size() <= 0 ? true : false;
	}

	public static <T> boolean isNotBlank(Collection<T> collection) {
		return !isBlank(collection);
	}

	public static <T> boolean isNull(Collection<T> collection) {
		return collection == null ? true : false;
	}
	
	/**
	 * 根据key截取集合指定值
	 * @param <T>
	 * @param <E>
	 * @param collection
	 * @param key
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> List<T> getFieldValueList(Collection<? extends Model<?>> collection, String key, Class<T> c) {
		if(isBlank(collection)){
			return null;
		}
		List<T> list = new ArrayList<T>();
		T value = null;
		for(Model<?> model : collection){
			value = (T) model.get(key);
			list.add(value);
		}
		return list;
	}
	
	/**
	 * 将参数转成list形式
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(T... params) {
		if (params == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for (T param : params) {
			if(param instanceof Collection<?>){
				list.addAll((Collection<T>)param);
			}else{
				list.add(param);
			}
		}
		return list;
	}
}
