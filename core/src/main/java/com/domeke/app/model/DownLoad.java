package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="downloadcount", pkName="DownLoadCountId")
public class DownLoad extends Model<DownLoad>{
	public static DownLoad down = new DownLoad();
	
	public List<DownLoad> getByUserId(Long userid){
		String sql="select * from downloadcount where userid = '"+userid+"'";
		List<DownLoad> downList = this.find(sql);
		return downList;
	}
}
