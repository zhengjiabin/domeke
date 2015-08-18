package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "comment", pkName = "commentid")
public class Comment extends Model<Comment> {

	private static final long serialVersionUID = 1L;

	public static Comment dao = new Comment();

	/**
	 * 根据目标主键及回复类型查询回复信息
	 * 
	 * @param targetid 目标id
	 * @param idtype 10:帖子，20:活动，30:宝贝，40:专辑，50:无奇不有，60:商城，70:动漫 , 80:漫画
	 * @param publishList 回复信息
	 * 
	 * @return
	 */
	public List<Comment> findReply(Object targetid, Object idtype, List<Object> publishList) {
		StringBuffer sql = new StringBuffer("select u.username,u.imgurl,t.username as tousername,t.imgurl as toimgurl,c.* ");
		sql.append(" from comment c left join user t on c.touserid = t.userid,user u where c.userid=u.userid ");
		sql.append(" and c.status='10' and c.targetid =? and c.idtype=? and c.level=2  ");

		StringBuffer filter = new StringBuffer();
		int size = publishList.size();
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				filter.append("?");
			} else {
				filter.append(",?");
			}
		}
		sql.append(" and c.pid in ( ");
		sql.append(filter);
		sql.append(" ) order by c.createtime");
		return DbSqlKit.findList(Comment.class, sql.toString(), targetid, idtype, publishList);
	}
	
	/**
	 * 根据目标主键、回复类型、发表信息id查询回复信息
	 * @param targetId 目标id
	 * @param idtype 回复类型
	 * @param pid 发表信息id
	 * @return
	 */
	public List<Comment> findReply(Object targetid, Object idtype, Object pid) {
		StringBuffer sql = new StringBuffer("select u.username,u.imgurl,t.username as tousername,t.imgurl as toimgurl,c.* ");
		sql.append(" from comment c left join user t on c.touserid = t.userid,user u where c.userid=u.userid ");
		sql.append(" and c.status='10' and c.level=2 and c.idtype=? and c.targetid =? and c.pid=? ");
		sql.append(" order by c.createtime");
		List<Comment> list = this.find(sql.toString(), idtype, targetid, pid);
		return list;
	}
	
	/**
	 * 根据目标主键及回复类型获取发表信息
	 * @param targetid 目标主键
	 * @param idtype 回复类型
	 * @param pageNumber 当前页号
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	public Page<Comment> findPageOfPublish(Object targetid, Object idtype, int pageNumber, int pageSize) {
		String sqlExceptSelect = "select u.username,u.imgurl,c.* ";
		StringBuffer sql = new StringBuffer(" from comment c,user u where c.userid=u.userid and c.status='10' ");
		sql.append("  and c.idtype=? and c.level=1 and c.targetid=? order by c.createtime");
		Page<Comment> page = this.paginate(pageNumber, pageSize, sqlExceptSelect, sql.toString(), idtype, targetid);
		return page;
	}

	/**
	 * 删除指定主题对应的所有回复信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	public void deleteByTheme(Object targetId, Object idtype) {
		String sql = "delete from comment where targetid=? and idtype=? ";
		Db.batch(sql, new Object[][] { { targetId, idtype } }, 1);
	}
	
	/**
	 * 根据目标主键及回复类型获取回复总记录数
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	public Long getCount(Object targetid, Object idtype){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from comment c where c.targetid=? and c.idtype=? ");
		sql.append(" and c.status='10' and c.level=1 ");
		return Db.queryLong(sql.toString(), targetid,idtype);
	}
	
	/**
	 * 删除主子回复信息
	 * 
	 * @return
	 */
	public void deleteReplyAll(Object targetId) {
		String sql = "delete from comment where commentid=? or pid=?";
		Db.batch(sql, new Object[][] { { targetId, targetId } }, 1);
	}
	
	/**
	 * 根据目标id及类型更新回复信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 * @param status 是否显示状态
	 */
	public void updateStatus(Object targetid,Object idtype,Object status){
		String sql = "update comment c set c.status = ? where c.idtype = ? and c.targetid = ? ";
		Db.update(sql, status,idtype,targetid);
	}
}
