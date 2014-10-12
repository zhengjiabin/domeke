package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="playcount", pkName="PlayCountId")
public class PlayCount extends Model<PlayCount>{
	public static DownLoad down = new DownLoad();
	
	public List<PlayCount> getByUserId(Long userid){
		String sql="select * from playcount where userid = '"+userid+"'";
		List<PlayCount> downList = this.find(sql);
		return downList;
	}
}
