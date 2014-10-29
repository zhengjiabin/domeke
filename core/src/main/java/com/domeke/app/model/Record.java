package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
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
	public Record getRecordsOfComment(Object userId, Object commentId, Object recordType) {
		StringBuffer sql = new StringBuffer("select r.*,u.username,u.imgurl ");
		sql.append(" from record r,user u ");
		sql.append(" where r.userid = u.userid and r.userid = ? and r.commentid = ? and r.recordtype = ? ");
		sql.append(" and r.status = '10' ");
		return this.findFirst(sql.toString(), userId, commentId, recordType);
	}
	
	/**
	 * 获取回复记录次数
	 * @return
	 */
	public Long getRecordsNumberOfComment(Object commentId, Object recordType){
		StringBuffer sql = new StringBuffer("select count(1) as number from record r ");
		sql.append(" where r.status='10' and r.commentid = ? and r.recordtype = ? ");
		return Db.queryLong(sql.toString(),commentId,recordType);
	}
}
