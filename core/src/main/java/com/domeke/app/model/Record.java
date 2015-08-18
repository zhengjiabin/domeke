package com.domeke.app.model;

import java.util.List;
import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
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
	
	/**
	 * 根据回复的目标id及类型获取记录分组次数
	 * @param targetId 回复的目标id
	 * @param idtype 回复类型
	 * @return ((commentid,recordtype),data)
	 */
	public Map<String, Record> getRecordsGroupOfComment(Object targetId,Object idtype,List<Object> objectList){
		StringBuffer sql = new StringBuffer("select count(1) as number,r.commentid,r.recordtype ");
		sql.append(" from record r,comment c where r.commentid = c.commentid  and c.targetid = ? ");
		sql.append(" and c.idtype = ? and r.status = '10' ");
		
		StringBuffer filter = new StringBuffer();
		int size = objectList.size();
		for(int i = 0; i<size;i++){
			if(i == 0 ){
				filter.append("?");
			}else{
				filter.append(",?");
			}
		}
		
		sql.append(" and c.commentid in ( ");
		sql.append(filter);
		sql.append(" ) group by r.commentid,r.recordtype ");
		return DbSqlKit.findMap(Record.class, sql.toString(), "commentid,recordtype", targetId, idtype, objectList);
	}
	
	/**
	 * 根据回复的目标id及类型获取当前用户的记录分组次数
	 * @param targetId 回复的目标id
	 * @param idtype 回复类型
	 * @return ((commentid,recordtype),data)
	 */
	public Map<String, Record> getRecordsGroupOfCommentByUser(Object targetId,Object idtype,Object userid,List<Object> objectList){
		StringBuffer sql = new StringBuffer("select r.commentid,r.recordtype ");
		sql.append(" from record r,comment c where r.commentid = c.commentid  and c.targetid = ? ");
		sql.append(" and c.idtype = ? and r.userid = ? and r.status = '10' ");
		
		StringBuffer filter = new StringBuffer();
		int size = objectList.size();
		for(int i = 0; i<size;i++){
			if(i == 0 ){
				filter.append("?");
			}else{
				filter.append(",?");
			}
		}
		
		sql.append(" and c.commentid in ( ");
		sql.append(filter);
		sql.append(" ) ");
		return DbSqlKit.findMap(Record.class, sql.toString(), "commentid,recordtype", targetId, idtype, userid, objectList);
	}
}
