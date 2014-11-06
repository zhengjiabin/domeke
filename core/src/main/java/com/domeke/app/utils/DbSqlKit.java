package com.domeke.app.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.ModelBuilder;

public class DbSqlKit<M> extends Db{
	
	private static Config getConfig(Class<? extends Model<?>> modelClass) {
		return DbKit.getConfig(modelClass);
	}
	
	/**
	 * 根据集合查询数据
	 * @param <T>
	 * @param M
	 * @param sql
	 * @param list
	 * @return
	 */
	private static <T> List<T> find(Class<? extends Model<?>> modelClass, String sql, List<Object> list) {
		Config config = getConfig(modelClass);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = config.getConnection();
			pst = conn.prepareStatement(sql);

			config.getDialect().fillStatement(pst, list);
			rs = pst.executeQuery();
			List<T> result = ModelBuilder.build(rs, modelClass);
			return result;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			config.close(rs, pst, conn);
		}
	}
	
	/**
	 * 根据groupKeys分组查询
	 * @param <T>
	 * @param sql
	 * @param groupKeys 分组名，多个以逗号分隔
	 * @param paras object/list<Object>
	 * @return
	 */
	public static <T> Map<String, T> findMap(Class<? extends Model<?>> modelClass,String sql, String groupKeys, Object... paras) {
		if(StrKit.isBlank(groupKeys)){
			return null;
		}
		List<Object> list = CollectionKit.toList(paras);
		List<T> records = find(modelClass, sql, list);
		if(CollectionKit.isBlank(records)){
			return null;
		}
		String[] groupKeyValues = groupKeys.split(",");
		Map<String,T> data = new HashMap<String, T>();
		String groupKey = null;
		for(T record:records){
			groupKey = getGroupKey(record, groupKeyValues);
			data.put(groupKey, record);
		}
		return data;
	}
	
	/**
	 * 根据groupKeys分组查询
	 * @param modelClass
	 * @param sql
	 * @param groupKeys 分组名，多个以逗号分隔
	 * @return
	 */
	public static <T> Map<String, List<T>> findMapList(Class<? extends Model<?>> modelClass, String sql, String groupKeys){
		List<Object> list = new ArrayList<Object>();
		return findMapList(modelClass, sql, groupKeys, list);
	}
	
	/**
	 * 根据groupKeys分组查询
	 * @param <T>
	 * @param sql
	 * @param groupKeys 分组名，多个以逗号分隔
	 * @param paras object/list<Object>
	 * @return
	 */
	public static <T> Map<String, List<T>> findMapList(Class<? extends Model<?>> modelClass, String sql, String groupKeys, Object... paras) {
		if(StrKit.isBlank(groupKeys)){
			return null;
		}
		List<Object> list = CollectionKit.toList(paras);
		List<T> records = find(modelClass, sql, list);
		if(CollectionKit.isBlank(records)){
			return null;
		}
		String[] groupKeyValues = groupKeys.split(",");
		Map<String,List<T>> data = new HashMap<String, List<T>>();
		String groupKey = null;
		List<T> dataList= null;
		for(T record:records){
			groupKey = getGroupKey(record, groupKeyValues);
			dataList = data.get(groupKey);
			if(CollectionKit.isNull(dataList)){
				dataList = new ArrayList<T>();
				data.put(groupKey, dataList);
			}
			dataList.add(record);
		}
		return data;
	}
	
	/**
	 * 查询数据库，返回list集合
	 * @param <T>
	 * @param sql
	 * @param paras object/list<Object>
	 * @return
	 */
	public static <T> List<T> findList(Class<? extends Model<?>> modelClass, String sql, Object... paras) {
		List<Object> list = CollectionKit.toList(paras);
		return find(modelClass, sql, list);
	}
	
	/**
	 * 获取分组groupKey
	 * @param <T>
	 * @param record
	 * @param groupKeyValues
	 * @return
	 */
	private static <T> String getGroupKey(T record, String[] groupKeyValues){
		Object groupValue = null;
		StringBuffer groupKey = new StringBuffer();
		for(String groupKeyValue:groupKeyValues){
			if(StrKit.isBlank(groupKeyValue)){
				continue;
			}
			groupValue = ((Model<?>) record).get(groupKeyValue.trim());
			if(groupKey.length() >0){
				groupKey.append(",");
			}
			groupKey.append(groupValue);
		}
		return groupKey.toString();
	}
}



