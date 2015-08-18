package com.domeke.app.model;

import java.util.List;
import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "community", pkName = "communityid")
public class Community extends Model<Community> {
	private static final long serialVersionUID = 1L;

	public static Community dao = new Community();
	
	/**
	 * 查询版块及版块分类信息
	 * @param communityid 版块id或者版块分类id
	 * @return
	 */
	public Community findCommunityInfo(Object communityid){
		StringBuffer sql = new StringBuffer("select p.title as ptitle,p.status as pstatus,c.* ");
		sql.append(" from community c left join community p on c.pid = p.communityid where c.communityid = ?");
		Community community = this.findFirst(sql.toString(), communityid);
		return community;
	}

	/**
	 * 查询版块分类信息
	 * 
	 * @param status
	 *            显示状态
	 * @return
	 */
	public List<Community> findForumClassifyList(Object status) {
		String sql = "select * from community where status=? and level=1 order by position";
		List<Community> forumClassifyList = this.find(sql,status);
		return forumClassifyList;
	}
	
	/**
	 * 查询版块分类信息
	 * 
	 * @return
	 */
	public List<Community> findForumClassifyList() {
		String sql = "select * from community where level=1 order by position";
		List<Community> forumClassifyList = this.find(sql);
		return forumClassifyList;
	}
	
	/**
	 * 根据版块分类id查询版块信息
	 * @param pid
	 * @param status
	 * @return
	 */
	public List<Community> findForumList(Object pid,Object status){
		String sql = "select * from community where status = ? and pid = ? order by position";
		List<Community> forumList = this.find(sql,status,pid);
		return forumList;
	}
	
	/**
	 * 查询版块信息
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findForumList(Object status) {
		String sql = "select * from community where status=? and level=2 order by pid,position";
		List<Community> forumList = this.find(sql,status);
		return forumList;
	}
	
	/**
	 * 查询版块信息
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findForumList() {
		StringBuffer sql = new StringBuffer("select t.title as ptitle,c.* from community c,community t ");
		sql.append(" where c.pid = t.communityid and c.level=2 order by c.pid,c.position");
		List<Community> forumList = this.find(sql.toString());
		return forumList;
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
	 * 浏览次数+1
	 * @param communityid 社区id
	 */
	public void updateTimes(Object communityid) {
		String sql = "update community set times = times +1 where communityid=?";
		Db.update(sql, communityid);
	}
	
	/**
	 * 根据版块分类信息更新版块信息
	 * @param pid 版块分类信息
	 * @param actionkey 版块根路径
	 * @param status 显示状态
	 */
	public void updateForum(Object pid,Object actionkey,Object status){
		String sql = "update community c set c.actionkey = ? , c.status = ? where c.level = 2 and c.pid = ? ";
		Db.update(sql, actionkey,status,pid);
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
	
	public void deleteFatAndSonByCommunityId(Object communityId){
		String sql = "delete from community where communityid=? or pid=?";
		Db.batch(sql, new Object[][] { { communityId, communityId } }, 1);
	}
}
