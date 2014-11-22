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
	 * 根据targetId查询子回复信息
	 * 
	 * @param targetId 回复目标
	 * @param idtype 10:帖子，20:活动，30:宝贝，40:动漫，50:无奇不有，60:商城
	 * 
	 * @return
	 */
	public List<Comment> findFollow(Object targetId,Object idtype,List<Object> commentIdList) {
		StringBuffer sql = new StringBuffer("select u.username,u.imgurl,t.username as tousername,t.imgurl as toimgurl,c.* ");
		sql.append(" from comment c left join user t on c.touserid = t.userid,user u where c.userid=u.userid ");
		sql.append(" and c.status='10' and c.targetid =? and c.idtype=? and c.level=2  ");
		
		StringBuffer filter = new StringBuffer();
		int size = commentIdList.size();
		for(int i = 0; i<size;i++){
			if(i == 0 ){
				filter.append("?");
			}else{
				filter.append(",?");
			}
		}
		sql.append(" and c.pid in ( ");
		sql.append(filter);
		sql.append(" ) order by c.createtime");
		return DbSqlKit.findList(Comment.class, sql.toString(), targetId,idtype,commentIdList);
	}
	
	/**
	 * 根据targetId查询子回复信息
	 * 
	 * @param targetId 回复目标
	 * @param idtype 10:帖子，20:活动，30:宝贝，40:动漫，50:无奇不有，60:商城
	 * 
	 * @return
	 */
	public List<Comment> findFollow(Object targetId,Object idtype,Object pId) {
		StringBuffer sql = new StringBuffer("select u.username,u.imgurl,t.username as tousername,t.imgurl as toimgurl,c.* ");
		sql.append(" from comment c left join user t on c.touserid = t.userid,user u where c.userid=u.userid ");
		sql.append(" and c.status='10' and c.idtype=? and c.level=2 and c.targetid =? and c.pid=? ");
		sql.append(" order by c.createtime");
		List<Comment> list = this.find(sql.toString(), idtype,targetId,pId);
		return list;
	}

	/**
	 * 根据targetId查询主回复信息
	 * 
	 * @return
	 */
	public Page<Comment> findPageByTargetId(Object targetId,Object idtype, int pageNumber,int pageSize) {
		String sqlExceptSelect = "select u.username,u.imgurl,c.* ";
		StringBuffer sql = new StringBuffer(" from comment c,user u where c.userid=u.userid and c.status='10' ");
		sql.append("  and c.idtype=? and c.level=1 and c.targetid=? order by c.createtime");
		Page<Comment> page = this.paginate(pageNumber, pageSize, sqlExceptSelect,sql.toString(), idtype,targetId);
		return page;
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
	 * 删除指定主题对应的所有回复信息
	 */
	public void deleteByTheme(Object targetId,Object idtype){
		String sql = "delete from comment where targetid=? and idtype=? ";
		Db.batch(sql, new Object[][] { { targetId, idtype } }, 1);
	}
}
