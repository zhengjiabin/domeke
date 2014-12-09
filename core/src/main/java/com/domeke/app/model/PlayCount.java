package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName="playcount", pkName="PlayCountId")
public class PlayCount extends Model<PlayCount>{
	public static DownLoad down = new DownLoad();
	
	public List<PlayCount> getByUserId(Long userid){
		String sql="select * from playcount where userid = '"+userid+"'";
		List<PlayCount> downList = this.find(sql);
		return downList;
	}
	
  public Page<PlayCount> getByUserId(Long userId,int pageNumber,int pageSize){
    	Page<PlayCount> page = this.paginate(pageNumber, pageSize, "select * ", "from playcount where userid=? ORDER BY create_time DESC",userId);
    	return page;
    }
}
