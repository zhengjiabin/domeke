package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 动漫作品主表
 * 
 * @author chenhailin
 *
 */
@TableBind(tableName = "works_type", pkName = "id")
public class WorksType extends Model<WorksType> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<WorksType> getWorksTypes(Integer type){
		String sql = "select * from works_type where type="+type+" order by istop desc";
		List<WorksType> worksTypes = this.find(sql);
		return worksTypes;
	}
	
}
