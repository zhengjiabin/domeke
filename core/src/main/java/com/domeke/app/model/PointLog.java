package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "pointlog", pkName = "pointlogid")
public class PointLog extends Model<PointLog>{
	public static PointLog dao = new PointLog();
	/**
	 * 新增
	 */
	public void savePointLog(){
		this.save();
	}
}
