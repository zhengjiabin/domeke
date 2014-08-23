package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author 陈智聪
 *DROP TABLE IF EXISTS `code_type`;
CREATE TABLE `code_type` (
  `codetypeid` bigint(20) NOT NULL AUTO_INCREMENT,
  `codetype` varchar(256) DEFAULT NULL,
  `ccodekey` varchar(32) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`codetypeid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8; 
 */
@TableBind(tableName="code_type", pkName="codetypeid")
public class CodeType extends Model<CodeType> {
	
	public static CodeType codeTypeDao = new CodeType();
	
	/**
	 * 新增
	 */
	public void saveCodeType(){
		this.save();
	}
	
	/**
	 * 查询
	 * @return 查询列表
	 */
	public List<CodeType> selectCodeType(){
		List<CodeType> codeTypeList = this.find("select * from code_type");
		return codeTypeList;
	}
	
	/**
	 * 
	 * @param 
	 * @return 
	 */
	public CodeType selectCodeTypeById(int codeTypeId){
		CodeType codeType = this.findById(codeTypeId);
		return codeType;
	}
	
	/**
	 * 更新
	 */
	public void updateCodeType(){
		this.update();
	}
	
	/**
	 * 删除
	 * @param 
	 */
	public void deleteCodeType(int codetypeid){
		this.deleteById(codetypeid);
	}
	
	/**
	 * 删除缓存
	 */
	public void removeCache(){
		CacheKit.removeAll("CodeType");
		CacheKit.removeAll("codeTypeList");
	}	
}
