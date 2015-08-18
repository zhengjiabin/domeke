package com.domeke.app.model;

import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
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
	 * 查询帖子明细信息
	 * @return
	 */
	public Post findInfoById(Object postId){
		StringBuffer sql = new StringBuffer("select p.*,u.username,u.imgurl,c.title ");
		sql.append(" from post p,user u,community c where p.userid=u.userid  ");
		sql.append(" and p.communityid=c.communityid and p.status='10' and p.postid=? ");
		return this.findFirst(sql.toString(), postId);
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
	public Page<Post> findPageAll(int pageNumber, int pageSize) {
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from post p,user u where p.userid=u.userid ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
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
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from post p,user u where p.userid=u.userid ");
		sqlExceptSelect.append(" and p.userid=? and p.status='10' ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(), userId);
		return page;
	}
	
	/**
	 * 根据版块信息分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @param status
	 *            显示隐藏状态
	 * @return
	 */
	public Page<Post> findPageByForum(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.username,u.imgurl,p.*,cm.number as viewcount,c.title";
		StringBuffer sqlExceptSelect = new StringBuffer("from community c,user u,post p left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where status='10' and idtype='10' group by targetid) cm ");
		sqlExceptSelect.append(" on p.postid=cm.targetid where c.communityid=p.communityid and u.userid=p.userid and p.status=? ");
		sqlExceptSelect.append(" and p.communityid=? ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
		return page;
	}
	
	/**
	 * 根据版块分类信息分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @param communityid
	 *            版块分类id
	 * @param status
	 *            显示隐藏状态
	 * @return
	 */
	public Page<Post> findPageByForumClassify(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.username,u.imgurl,p.*,cm.number as viewcount,c.title";
		StringBuffer sqlExceptSelect = new StringBuffer("from community c,user u,post p left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where status='10' and idtype='10' group by targetid) cm ");
		sqlExceptSelect.append(" on p.postid=cm.targetid where c.communityid=p.communityid and u.userid=p.userid and p.status=? ");
		sqlExceptSelect.append(" and c.pid=? ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc ");
		Page<Post> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
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
     * 根据版块分类信息获取今日发帖数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountOfTodayByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from post p,community c ");
    	sql.append(" where p.communityid=c.communityid and p.status='10' and c.pid=? ");
    	sql.append(" and to_days(p.createtime)=to_days(now()) ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
	/**
     * 根据版块信息获取今日发帖数
     * @param communityid 版块信息
     * @return 汇总数
     */
    public Long getCountOfTodayByForum(Object communityId){
    	String sql = "select count(1) from post where status='10' and communityid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
	/**
     * 今日发表帖子数
     * @return 汇总数
     */
    public Long getCountOfToday(){
    	Long count = Db.queryLong("select count(1) from post where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取昨日发帖数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountOfYesByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from post p,community c ");
    	sql.append(" where p.communityid=c.communityid and p.status='10' and c.pid=? ");
    	sql.append(" and date(p.createtime) = date_sub(curdate(),interval 1 day) ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
    /**
     * 根据版块信息获取昨日发帖数
     * @param communityid 版块信息
     * @return 汇总数
     */
    public Long getCountOfYesByForum(Object communityId){
    	String sql = "select count(1) from post where status='10' and communityid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 昨日发表帖子数
     * @return 汇总数
     */
    public Long getCountOfYes(){
    	Long count = Db.queryLong("select count(1) from post where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取总发帖数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountByForumClassify(Object communtiyid){
    	StringBuffer sql = new StringBuffer("select count(1) from post p,community c ");
    	sql.append(" where p.communityid=c.communityid and p.status='10' and c.pid=? ");
    	Long count = Db.queryLong(sql.toString(),communtiyid);
    	return count;
    }
    
    /**
     * 根据版块信息获取总发帖数
     * @param communityid 版块信息
     * @return 汇总数
     */
    public Long getCountByForum(Object communityId){
    	Long count = Db.queryLong("select count(1) from post where status='10' and communityid=?",communityId);
    	return count;
    }
    
    /**
     * 总帖子数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from post where status='10'");
    	return count;
    }
    
    /**
     * 根据版块统计总发帖数
     * @return 汇总数
     */
    public Map<String,Post> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,p.communityid from post p,community son ");
    	sql.append(" where p.communityid = son.communityid and p.status='10' and son.pid = ? ");
    	sql.append(" group by p.communityid ");
    	return DbSqlKit.findMap(Post.class, sql.toString(), "communityid", pid);
    }
    
    /**
     * 根据版块分类信息获取当前登录人总发帖数
     * @param communityid 版块分类id
     * @param userId 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForumClassify(Object communityid,Object userId){
    	StringBuffer sql = new StringBuffer("select count(1) from post p,community c");
    	sql.append(" where p.communityid=c.communityid and p.status='10' and c.pid=? and p.userid=? ");
    	Long count = Db.queryLong(sql.toString(),communityid,userId);
    	return count;
    }
    
    /**
     * 根据版块信息获取当前登录人总发帖数
     * @param communityid 版块id
     * @param userId 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForum(Object communityId,Object userId){
    	String sql = "select count(1) from post where status='10' and communityid=? and userid=?";
    	Long count = Db.queryLong(sql,communityId,userId);
    	return count;
    }
    
    /**
     * 当前登录人总帖子数
     * @return 汇总数
     */
    public Long getCountOfUser(Object userId){
    	Long count = Db.queryLong("select count(1) from post where status='10' and userid=?",userId);
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
    
    /**
     * 根据版块分类更新主题状态
     * @param pid 版块分类id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForumClassify(Object pid,Object status){
    	StringBuffer sql = new StringBuffer("update post p set p.status = ? ");
    	sql.append(" where exists(select 1 from community c where p.communityid = c.communityid and c.pid = ? )");
    	Db.update(sql.toString(), status,pid);
    }
    
	/**
	 * 根据版块分类id更新回复信息
	 * @param pid 版块分类id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForumClassify(Object pid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '10' ");
		sql.append(" and exists(select 1 from post p,community cy where cy.communityid = p.communityid ");
		sql.append(" and p.postid = c.commentid and c.idtype = '10' and cy.pid = ?) ");
		Db.update(sql.toString(), status,pid);
	}
    
    /**
     * 根据版块更新主题状态
     * @param community 版块id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForum(Object community,Object status){
    	String sql = "update post p set p.status = ? where p.communityid = ?";
    	Db.update(sql, status,community);
    }
    
	/**
	 * 根据版块id更新回复信息
	 * @param communityid 版块id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForum(Object communityid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '10' ");
		sql.append(" and exists(select 1 from post p where p.postid = c.commentid and c.idtype = '10' ");
		sql.append(" and p.communityid = ?) ");
		Db.update(sql.toString(), status,communityid);
	}
    
    /**
     * 根据版块分类删除主题状态
     * @param pid 版块分类id
     */
    public void deleteByForumClassify(Object pid){
    	StringBuffer sql = new StringBuffer("delete from post p where exists(select 1 from community c where ");
    	sql.append(" c.communityid = p.communityid and c.pid =? )");
    	Db.update(sql.toString(), pid);
    }
    
    /**
     * 根据版块删除主题状态
     * @param communityid 版块id
     */
    public void deleteByForum(Object communityid){
    	String sql = "delete from post p where p.communityid = ? ";
    	Db.update(sql, communityid);
    }
}
