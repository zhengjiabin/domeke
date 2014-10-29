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
		String sql = "select * from works_type where type="+type+" order by indextop desc";
		List<WorksType> worksTypes = this.find(sql);
		return worksTypes;
	}
	public Page<WorksType> getWorksTypes(Integer type,Integer pageIndex, Integer pageSize){
		String sql = "from works_type where type="+type+" order by indextop desc";
		Page<WorksType> worksTypes = this.paginate(pageIndex, pageSize, "select *", sql);
		return worksTypes;
	}
	
//	public List<WorksType> getWorksTypes(){
//		String sql = "select * from works_type order by indextop desc";
//		List<WorksType> worksTypes = this.find(sql);
//		return worksTypes;
//	}
	public List<WorksType> getWorksTypesByCartoonDesc(Integer type){
		String sql = "select * from works_type where type="+type+" order by cartoontop desc";
		List<WorksType> worksTypes = this.find(sql);
		return worksTypes;
	}
	public Page<WorksType> getWorksTypesByCartoonDesc(Integer type,Integer pageIndex, Integer pageSize){
		String sql = "select * from works_type where type="+type+" order by cartoontop desc";
		Page<WorksType> worksTypes = this.paginate(pageIndex, pageSize, "select *", sql);
		return worksTypes;
	}
}
