package com.domeke.app.model;

import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "treasure", pkName = "treasureid")
public class Treasure extends Model<Treasure> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Treasure dao = new Treasure();
	
	/**
	 * 查询宝贝明细信息
	 * @return
	 */
	public Treasure findInfoById(Object treasureId){
		StringBuffer sql = new StringBuffer("select t.*,u.username,u.imgurl,c.title ");
		sql.append(" from treasure t,user u,community c where t.userid=u.userid  ");
		sql.append(" and t.communityid=c.communityid and t.status='10' and t.treasureid=? ");
		return this.findFirst(sql.toString(), treasureId);
	}
	
	/**
	 * 分页查询论坛，不区分状态
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Treasure> findPageAll(int pageNumber, int pageSize) {
		String select = "select t.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from treasure t,user u where t.userid=u.userid ");
		sqlExceptSelect.append("  order by to_days(t.createtime) desc,t.top desc,t.essence desc ");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}

	/**
	 * 分页查询指定用户的宝贝
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Treasure> findByUserId(Object userId, int pageNumber, int pageSize) {
		String select = "select t.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from treasure t,user u where t.userid=u.userid ");
		sqlExceptSelect.append(" and t.userid=? and t.status='10' ");
		sqlExceptSelect.append(" order by to_days(t.createtime) desc,t.top desc,t.essence desc ");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(), userId);
		return page;
	}
	
	/**
	 * 根据版块信息获取分页查询宝贝
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Treasure> findPageByForum(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.username,u.imgurl,t.*,cm.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer("from user u,treasure t left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where status='10' and idtype='30' group by targetid) cm ");
		sqlExceptSelect.append(" on t.treasureid=cm.targetid where u.userid=t.userid and t.status=? and t.communityid=? ");
		sqlExceptSelect.append(" order by to_days(t.createtime) desc,t.top desc,t.essence desc");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
		return page;
	}
	
	/**
	 * 根据版块分类信息获取分页查询宝贝
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @param communityid
	 *            版块分类id
	 * @return
	 */
	public Page<Treasure> findPageByForumClassify(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.username,u.imgurl,t.*,cm.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer("from community c,user u,treasure t left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where status='10' and idtype='30' group by targetid) cm ");
		sqlExceptSelect.append(" on t.treasureid=cm.targetid where u.userid=t.userid and t.status=? and t.communityid=c.communityid ");
		sqlExceptSelect.append(" and c.pid=? ");
		sqlExceptSelect.append(" order by to_days(t.createtime) desc,t.top desc,t.essence desc");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
		return page;
	}

	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object treasureId) {
		String sql = "update treasure set times = times +1 where treasureid=?";
		Db.update(sql, treasureId);
	}
	
	/**
     * 根据版块分类信息获取今日发宝贝数
     * @param community 版块分类id
     * @return 汇总数
     */
    public Long getCountOfTodayByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from treasure t,community c ");
    	sql.append(" where t.communityid=c.communityid and t.status='10' and c.pid=? ");
    	sql.append(" and to_days(t.createtime)=to_days(now()) ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
	/**
     * 根据版块信息获取今日发宝贝数
     * @param community 版块信息
     * @return 汇总数
     */
    public Long getCountOfTodayByForum(Object communityId){
    	String sql = "select count(1) from treasure where status='10' and communityid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
	/**
     * 今日发表宝贝数
     * @return 汇总数
     */
    public Long getCountOfToday(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取昨日宝贝数
     * @param communityid　版块分类id
     * @return 汇总数
     */
    public Long getCountOfYesByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from treasure t,community c ");
    	sql.append(" where t.communityid=c.communityid and t.status='10' and c.pid=? ");
    	sql.append(" and date(t.createtime) = date_sub(curdate(),interval 1 day) ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
    /**
     * 根据版块信息获取昨日宝贝数
     * @param communityid　版块id
     * @return 汇总数
     */
    public Long getCountOfYesByForum(Object communityId){
    	String sql = "select count(1) from treasure where status='10' and communityid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 昨日发表宝贝数
     * @return 汇总数
     */
    public Long getCountOfYes(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取总宝贝数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from treasure t,community c ");
    	sql.append(" where t.communityid=c.communityid and t.status='10' and c.pid=? ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
    /**
     * 根据版块信息获取总宝贝数
     * @param communityid 版块id
     * @return 汇总数
     */
    public Long getCountByForum(Object communityId){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and communityid=?",communityId);
    	return count;
    }
    
    /**
     * 总帖子数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10'");
    	return count;
    }
    
    /**
     * 根据版块统计总宝贝数
     * @return 汇总数
     */
    public Map<String, Treasure> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,p.communityid from treasure p,community son ");
    	sql.append(" where p.communityid = son.communityid and p.status='10' and son.pid = ? ");
    	sql.append(" group by p.communityid ");
    	return DbSqlKit.findMap(Activity.class, sql.toString(), "communityid", pid);
    }
    
    /**
     * 根据版块分类信息获取当前登录人总宝贝数
     * @param communityid 版块分类id
     * @param userid 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForumClassify(Object communityId,Object userId){
    	StringBuffer sql = new StringBuffer("select count(1) from treasure t,community c ");
    	sql.append(" where t.communityid=c.communityid and t.status='10' and c.pid=? and t.userid=? ");
    	Long count = Db.queryLong(sql.toString(),communityId,userId);
    	return count;
    }
    
    /**
     * 根据版块信息获取当前登录人总宝贝数
     * @param communityid 版块id
     * @param userid 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForum(Object communityId,Object userId){
    	String sql = "select count(1) from treasure where status='10' and communityid=? and userid=?";
    	Long count = Db.queryLong(sql,communityId,userId);
    	return count;
    }
    
    /**
     * 当前登录人总宝贝数
     * @return 汇总数
     */
    public Long getCountOfUser(Object userId){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 查询指定时间范围内当前用户发布的宝贝
     * @return
     */
    public Object findHasPublish(Object communityId,Object userId){
    	String sql = "select 1 from treasure where communityid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, communityId, userId);
    }
    
    /**
     * 根据版块分类更新主题状态
     * @param pid 版块分类id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForumClassify(Object pid,Object status){
    	StringBuffer sql = new StringBuffer("update treasure t set t.status = ? ");
    	sql.append(" where exists(select 1 from community c where t.communityid = c.communityid and c.pid = ? )");
    	Db.update(sql.toString(), status,pid);
    }
    
	/**
	 * 根据版块分类id更新回复信息
	 * @param pid 版块分类id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForumClassify(Object pid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '30' ");
		sql.append(" and exists(select 1 from treasure t,community cy where cy.communityid = t.communityid ");
		sql.append(" and t.treasureid = c.commentid and c.idtype = '30' and cy.pid = ?) ");
		Db.update(sql.toString(), status,pid);
	}
    
    /**
     * 根据版块更新主题状态
     * @param community 版块id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForum(Object community,Object status){
    	String sql = "update treasure t set t.status = ? where t.communityid = ?";
    	Db.update(sql, status,community);
    }
    
	/**
	 * 根据版块id更新回复信息
	 * @param communityid 版块id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForum(Object communityid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '30' ");
		sql.append(" and exists(select 1 from treasure t where t.treasureid = c.commentid and c.idtype = '30' ");
		sql.append(" and t.communityid = ?) ");
		Db.update(sql.toString(), status,communityid);
	}
    
    /**
     * 根据版块分类删除主题状态
     * @param pid 版块分类id
     */
    public void deleteByForumClassify(Object pid){
    	StringBuffer sql = new StringBuffer("delete from treasure t where exists(select 1 from community c where ");
    	sql.append(" c.communityid = t.communityid and c.pid = ? )");
    	Db.update(sql.toString(), pid);
    }
    
    /**
     * 根据版块删除主题状态
     * @param community 版块id
     */
    public void deleteByForum(Object community){
    	String sql = "delete from treasure t where t.communityid = ? ";
    	Db.update(sql, community);
    }
}
