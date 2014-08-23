package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author 陈智聪
 *DROP TABLE IF EXISTS `code_table`;
CREATE TABLE `code_table` (
  `codetableid` bigint(20) NOT NULL AUTO_INCREMENT,
  `codekey` varchar(32) DEFAULT NULL,
  `ccodevalue` varchar(256) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`codetableid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName="code_table", pkName="codetableid")
public class CodeTable extends Model<CodeTable> {
	
	public static CodeTable codeTableDao = new CodeTable();
	
	public void saveCodeTable(){
		this.save();
	}
	
	public List<CodeTable> selectCodeTable(){
		List<CodeTable> codeTableList = this.find("select * from code_table");
		return codeTableList;
	}
	
	public CodeTable selectCodeTableById(int codeTableId){
		CodeTable codeTable = this.findById(codeTableId);
		return codeTable;
	}
	
	public void updateCodeTable(){
		this.update();
	}
	
	public void deleteCodeTable(int codeTableId){
		this.deleteById(codeTableId);
	}
	
	public void removeCache(){
		CacheKit.removeAll("CodeTable");
		CacheKit.removeAll("codeTableList");
	}
}
