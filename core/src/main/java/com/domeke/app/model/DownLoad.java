package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName="downloadcount", pkName="DownLoadCountId")
public class DownLoad extends Model<DownLoad>{
	public static DownLoad down = new DownLoad();
	
	
	
	  public Page<DownLoad> getByUserId(Long userId,int pageNumber,int pageSize){
	    	Page<DownLoad> page = this.paginate(pageNumber, pageSize, "select * ", "from downloadcount where userid =?",userId);
	    	return page;
	    }
}
