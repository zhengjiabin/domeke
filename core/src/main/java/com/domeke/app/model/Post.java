package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "post", pkName = "postid")
public class Post extends Model<Post> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Post dao = new Post();
	
	/**
	 * 分页查询论坛，不区分状态
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Post> findPageAll(int pageNumber, int pageSize) {
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from post p,user u where p.userid=u.userid ");
		sqlExceptSelect.append("  order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}

	/**
	 * 分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Post> findPage(int pageNumber, int pageSize) {
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from post p,user u where p.userid=u.userid ");
		sqlExceptSelect.append("  and p.status='10' ");
		sqlExceptSelect.append("  order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}
	/**
	 * 分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Post> findPageByCommunityId(int pageNumber, int pageSize,Object communityId) {
		String select = "select u.username,u.imgurl,p.*,cm.number as viewcount,c.title";
		StringBuffer sqlExceptSelect = new StringBuffer("from community c,user u,post p left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where status='10' and idtype='10' group by targetid) cm ");
		sqlExceptSelect.append(" on p.postid=cm.targetid where c.communityid=p.communityid and u.userid=p.userid and p.status='10' ");
		sqlExceptSelect.append(" and p.communityid=? ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),communityId);
		return page;
	}

	/**
	 * 分页查询指定发帖人的帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Post> findByUserId(Object userId, int pageNumber, int pageSize) {
		Page<Post> page = this.paginate(pageNumber, pageSize, "select *",
				"from post where userid=? order by to_days(createtime) desc,top desc,essence desc", userId);
		return page;
	}

	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object postId) {
		String sql = "update post set times = times +1 where postid=?";
		Db.update(sql, postId);
	}
	
	/**
     * 今日发帖数
     * @return 汇总数
     */
    public Long getTodayCount(){
    	Long count = Db.queryLong("select count(1) from post where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
	/**
     * 今日发帖数
     * @return 汇总数
     */
    public Long getTodayCountByCommunityId(Object communityId){
    	String sql = "select count(1) from post where status='10' and communityid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from post where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCountByCommunityId(Object communityId){
    	String sql = "select count(1) from post where status='10' and communityid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 总发帖数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from post where status='10'");
    	return count;
    }
    
    /**
     * 总发帖数
     * @return 汇总数
     */
    public Long getCountByCommunityId(Object communityId){
    	Long count = Db.queryLong("select count(1) from post where status='10' and communityid=?",communityId);
    	return count;
    }
    
    /**
     * 根据版块统计总发帖数
     * @return 汇总数
     */
    public List<Post> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,p.communityid from post p,community son ");
    	sql.append(" where p.communityid = son.communityid and p.status='10' and son.pid = ? ");
    	sql.append(" group by p.communityid ");
    	List<Post> postList = this.find(sql.toString(), pid);
    	return postList;
    }
    
    /**
     * 当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByUserId(Object userId){
    	Long count = Db.queryLong("select count(1) from post where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByCommunityAndUsr(Object communityId,Object userId){
    	String sql = "select count(1) from post where status='10' and communityid=? and userid=?";
    	Long count = Db.queryLong(sql,communityId,userId);
    	return count;
    }
    
    /**
     * 查询指定时间范围内当前用户发布的帖子
     * @return
     */
    public Object findHasPublish(Object communityId,Object userId){
    	String sql = "select 1 from post where communityid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, communityId, userId);
    }
}
