package com.domeke.app.model;

import java.util.List;
import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "community", pkName = "communityid")
public class Community extends Model<Community> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Community dao = new Community();
	
	public void deleteFatAndSonByCommunityId(Object communityId){
		String sql = "delete from community where communityid=? or pid=?";
		Db.batch(sql, new Object[][] { { communityId, communityId } }, 1);
	}

	/**
	 * 查询父模块
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findFatList() {
		String sql = "select * from community where status='10' and level=1 order by position";
		List<Community> communityFatList = this.find(sql);
		return communityFatList;
	}
	
	/**
	 * 查询子模块
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findSonList() {
		StringBuffer sql = new StringBuffer("select c.* ");
		sql.append(" from community c,community p where c.status='10' and c.level = 2 ");
		sql.append(" and c.pid = p.communityid and p.level=1 and p.status='10' ");
		sql.append(" order by c.pid,c.position ");
		List<Community> communitySonList = this.find(sql.toString());
		return communitySonList;
	}
	
	/**
	 * 根据版块，查询明细
	 * 
	 * @param <T>
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public <T> Map<String, List<Community>> findSonMapList(List<T> list) {
		StringBuffer sql = new StringBuffer("select * ");
		sql.append(" from community where status='10' and level = 2 ");

		StringBuffer filter = new StringBuffer();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				filter.append("?");
			} else {
				filter.append(",?");
			}
		}
		sql.append(" and pid in ( ");
		sql.append(filter.toString());
		sql.append(" ) order by pid asc,position ");
		return DbSqlKit.findMapList(Community.class, sql.toString(), "pid", list);
	}

	/**
	 * 根据父模块查询子模块
	 * 
	 * @return
	 */
	public List<Community> findSonListByPid(Object pId) {
		String sql = "select * from community where status='10' and level=2 and pid=? order by position";
		List<Community> communitySonList = this.find(sql, pId);
		return communitySonList;
	}
	
	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object communityId) {
		String sql = "update community set times = times +1 where communityid=?";
		Db.update(sql, communityId);
	}
}
