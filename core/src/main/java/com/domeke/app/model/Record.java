package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(pkName = "recordid", tableName = "record")
public class Record extends Model<Record> {
	
	private static final long serialVersionUID = 1L;
	
	public static Record dao = new Record();
	
	/**
	 * 获取回复相关记录
	 * 
	 * @return
	 */
	public List<Record> getRecordsOfComment(Object userId, Object commentId, Object recordType) {
		StringBuffer sql = new StringBuffer("select r.*,u.username,u.imgurl ");
		sql.append(" from record r,user u ");
		sql.append(" where r.userid = u.userid and r.userid = ? and r.commentid = ? and r.recordtype = ? ");
		sql.append(" and r.status = '10' ");
		return this.find(sql.toString(), userId, commentId, recordType);
	}
}
