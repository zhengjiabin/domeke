package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "pointlog", pkName = "pointlogid")
public class PointLog extends Model<PointLog>{
	public static PointLog dao = new PointLog();
	/**
	 * 新增
	 */
	public void savePointLog(){
		this.save();
	}
	public Page<PointLog> getUserPoint(String colm,String param,int pageNumber,int pageSize){
		String sql="";
		Page<PointLog> point = null;
		if(colm == null || param == null){
			point = this.paginate(pageNumber, pageSize, "select * ", "from pointlog");
		}else {
			 sql = "select * from user where "+colm+" LIKE '%"+param+"%'";
			 point = this.paginate(pageNumber, pageSize, "select * ", "from pointlog where "+colm+" LIKE '%"+param+"%'");
		}
		
		return point;
	}
}
