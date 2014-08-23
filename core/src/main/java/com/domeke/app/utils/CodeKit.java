package com.domeke.app.utils;

import java.util.List;
import java.util.Map;

import com.domeke.app.model.CodeTable;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CodeKit {
	
	private static Map<String,String>  codeTableMap;
	
	private static Map<String,Map<String,String>> codeTypeMap;
	
	private static String loadCodeTable = "selct t.codetype,t.typename,c.codekey,c.codename,c.codevalue from codetype t,codetable c where t.codetype=c.codetype";
	
	
	public void init(){
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
}
