package com.domeke.app.utils;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.domeke.app.base.config.DruidDatasouceUtil;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.CodeType;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class CodeKit implements ServletContextListener{
	
	private static Map<String,String>  codeTableMap;
	
	private static Map<String,Map<String,String>> codeTypeMap;
	
	private static String loadCodeTable = "select t.codetype,t.typename,c.codekey,c.codename,c.codevalue from code_type t,code_table c where t.codetype=c.codetype";
	
	
	public void init(){
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		druidPlugin.start();
		ActiveRecordPlugin record = new ActiveRecordPlugin(druidPlugin);
		record.addMapping("code_table", CodeTable.class);
		record.addMapping("code_type", CodeType.class);
		record.start();
		load();
		
	}
	@SuppressWarnings("unchecked")
	private void load() {
		List<Record> codetableList = Db.find(loadCodeTable);
		
		for (Record codeTable : codetableList) {
			codeTypeMap = Maps.newHashMap();
			String codeType = codeTable.getStr("codetype");
			String codeKey = codeTable.getStr("codekey");
			String codeValue = codeTable.getStr("codevalue");
			if(!codeTypeMap.containsKey(codeType)){
				codeTableMap = Maps.newHashMap();
				codeTypeMap.put(codeType, codeTableMap);
			}else {
				codeTableMap = codeTypeMap.get(codeType);
				codeTableMap.put(codeKey, codeValue);
			}
		}
	}
	/**
	 * 获取码表值
	 * @param codeType  码表名称
	 * @param codeKey 码表对应键
	 * @return
	 */
	public static String getValue(String codeType,String codeKey){
		Map<String,String> codeMap  = Maps.newHashMap();
		if(codeTypeMap != null) {
			codeMap = codeTypeMap.get(codeType);
		} else {
			CodeKit kit = new CodeKit();
			kit.init();
		}
		return codeMap.get(codeKey)!=null ? codeMap.get(codeKey):"";
	}
	/**
	 * 返回码表列表
	 * @param codeType
	 * @return
	 */
	public static List<CodeTable> getList(String codeType){
		
		return null;
		
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		init();
	}
	
	
}
