package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.kit.StrKit;
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
	
	public List<WorksType> getWorksTypes(String type){
		String sql = "select * from works_type order by indextop desc";
		if(!StrKit.isBlank(type)){
			sql = "select * from works_type where type="+type+" order by indextop desc";
		}
		List<WorksType> worksTypes = this.find(sql);
		return worksTypes;
	}
	public Page<WorksType> getWorksTypes(String type,Integer pageNumber, Integer pageSize){
		String sql = "from works_type order by indextop desc";
		if(!StrKit.isBlank(type)){
			sql = "from works_type where type="+type+" order by indextop desc";
		}
		Page<WorksType> worksTypes = this.paginate(pageNumber, pageSize, "select *", sql);
		return worksTypes;
	}
	public Page<WorksType> getWorksTypesNoRank(String type,Integer pageNumber, Integer pageSize){
		String sql = "from works_type";
		if(!StrKit.isBlank(type)){
			sql = "from works_type where type="+type;
		}
		Page<WorksType> worksTypes = this.paginate(pageNumber, pageSize, "select *", sql);
		return worksTypes;
	}
	public List<WorksType> getWorksTypesByCartoonDesc(String type){
		String sql = "select * from works_type order by cartoontop desc";
		if(!StrKit.isBlank(type)){
			sql = "select * from works_type where type="+type+" order by cartoontop desc";
		}
		List<WorksType> worksTypes = this.find(sql);
		return worksTypes;
	}
	public Page<WorksType> getWorksTypesByCartoonDesc(String type,Integer pageNumber, Integer pageSize){
		String sql = "from works_type order by cartoontop desc";
		if(!StrKit.isBlank(type)){
			sql = "from works_type where type="+type+" order by cartoontop desc";
		}
		Page<WorksType> worksTypes = this.paginate(pageNumber, pageSize, "select *", sql);
		return worksTypes;
	}
	
	
	public int getMaxIndexTopValue(String type){
		String sql = "select * from works_type order by indextop desc limit 1";
		if(!StrKit.isBlank(type)){
			sql = "select * from works_type where type="+type+" order by indextop desc  limit 1";
		}
		WorksType worksTypeModel = this.findFirst(sql);
		return worksTypeModel.get("indextop");
	}
	public int getMaxCartoonTopValue(String type){
		String sql = "select * from works_type order by cartoontop desc limit 1";
		if(!StrKit.isBlank(type)){
			sql = "select * from works_type where type="+type+" order by cartoontop desc  limit 1";
		}
		WorksType worksTypeModel = this.findFirst(sql);
		return worksTypeModel.get("cartoontop");
	}
}
