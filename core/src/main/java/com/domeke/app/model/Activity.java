package com.domeke.app.model;

import java.util.Map;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.DbSqlKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "activity", pkName = "activityid")
public class Activity extends Model<Activity> {
	private static final long serialVersionUID = 1L;

	public static Activity dao = new Activity();
	
	public Page<Activity> getActivity(Long userId,int pageNumber,int pageSize){
    	Page<Activity> page = this.paginate(pageNumber, pageSize, "select * ", "from activity where userid in (select userid from activity_apply where userid=?)",userId);
    	return page;
    }
	
	/**
	 * 查询活动明细信息
	 * @return
	 */
	public Activity findInfoById(Object activityId){
		StringBuffer sql = new StringBuffer("select aty.*,u.username,u.imgurl,c.title ");
		sql.append(" from activity aty,user u,community c where aty.userid=u.userid  ");
		sql.append(" and aty.communityid=c.communityid and aty.status='10' and aty.activityid=? ");
		return this.findFirst(sql.toString(), activityId);
	}
	
	/**
	 * 分页查询活动，不区分状态
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findPageAll(int pageNumber, int pageSize) {
		String select = "select aty.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from activity aty,user u where aty.userid=u.userid ");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc ");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}
	
	/**
	 * 分页查询指定发起人的活动
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findByUserId(Object userId, int pageNumber, int pageSize) {
		String select = "select aty.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from activity aty,user u where aty.userid=u.userid ");
		sqlExceptSelect.append(" and aty.userid=? and aty.status='10' ");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc ");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),userId);
		return page;
	}

	/**
	 * 根据版块信息分页查询活动
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findPageByForum(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.imgurl,u.username,aty.*,app.number as applynumber";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from user u,activity aty left join (select count(1) as number,app.activityid from activity_apply app ");
		sqlExceptSelect.append(" where app.status='10' group by app.activityid) app on aty.activityid=app.activityid where u.userid=aty.userid ");
		sqlExceptSelect.append(" and aty.status=? and aty.communityid=? ");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
		return page;
	}
	
	/**
	 * 根据版块信息分页查询活动
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findPageByForumClassify(int pageNumber, int pageSize,Object communityId,Object status) {
		String select = "select u.imgurl,u.username,aty.*,app.number as applynumber";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from community c,user u,activity aty left join (select count(1) as number,app.activityid from activity_apply app ");
		sqlExceptSelect.append(" where app.status='10' group by app.activityid) app on aty.activityid=app.activityid where u.userid=aty.userid ");
		sqlExceptSelect.append(" and aty.status=? and c.communityid=aty.communityid and c.pid=? ");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),status,communityId);
		return page;
	}

	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object activityId) {
		String sql = "update activity set times = times +1 where activityid=?";
		Db.update(sql, activityId);
	}
	
	/**
     * 根据版块分类信息获取今日发表总活动数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountOfTodayByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from activity aty,community c ");
    	sql.append(" where aty.communityid=c.communityid and aty.status='10' and c.pid=? ");
    	sql.append(" and to_days(aty.createtime)=to_days(now()) ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
	/**
     * 根据版块信息获取今日发表总活动数
     * @param communityid 版块id
     * @return 汇总数
     */
    public Long getCountOfTodayByForum(Object communityId){
    	String sql = "select count(1) from activity where status='10' and communityid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
	/**
     * 今日发表活动数
     * @return 汇总数
     */
    public Long getCountOfToday(){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取昨日发表活动数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountOfYesByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from activity aty,community c ");
    	sql.append(" where aty.communityid=c.communityid and aty.status='10' and c.pid=? ");
    	sql.append(" and date(aty.createtime) = date_sub(curdate(),interval 1 day)");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
    /**
     * 根据版块信息获取昨日发表活动数
     * @param communityid 版块id
     * @return 汇总数
     */
    public Long getCountOfYesByForum(Object communityId){
    	String sql = "select count(1) from activity where status='10' and communityid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 昨日发表活动数
     * @return 汇总数
     */
    public Long getCountOfYes(){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取总活动数
     * @param communityid 版块分类id
     * @return 汇总数
     */
    public Long getCountByForumClassify(Object communityid){
    	StringBuffer sql = new StringBuffer("select count(1) from activity aty,community c ");
    	sql.append(" where aty.communityid=c.communityid and aty.status='10' and c.pid=? ");
    	Long count = Db.queryLong(sql.toString(),communityid);
    	return count;
    }
    
    /**
     * 根据版块信息获取总活动数
     * @param communityid 版块id
     * @return 汇总数
     */
    public Long getCountByForum(Object communityId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and communityid=?",communityId);
    	return count;
    }
    
    /**
     * 总活动数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from activity where status='10'");
    	return count;
    }
    
    /**
     * 根据版块分类信息获取当前登录人总活动数
     * @param communityid 版块分类id
     * @param userId 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForumClassify(Object communityid,Object userId){
    	StringBuffer sql = new StringBuffer("select count(1) from activity aty,community c ");
    	sql.append(" where aty.communityid=c.communityid and aty.status='10' and c.pid=? and aty.userid=? ");
    	Long count = Db.queryLong(sql.toString(),communityid,userId);
    	return count;
    }
    
    /**
     * 根据版块信息获取当前登录人总活动数
     * @param communityid 版块id
     * @param userId 用户id
     * @return 汇总数
     */
    public Long getCountOfUserByForum(Object communityId,Object userId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and communityid=? and userid=?",communityId,userId);
    	return count;
    }
    
    /**
     * 当前登录人总活动数
     * @return 汇总数
     */
    public Long getCountOfUser(Object userId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 根据版块统计活动数
     * @return 汇总数
     */
    public Map<String, Activity> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,aty.communityid from activity aty,community son ");
    	sql.append(" where aty.communityid = son.communityid and aty.status='10' and son.pid = ? ");
    	sql.append(" group by aty.communityid ");
    	return DbSqlKit.findMap(Activity.class, sql.toString(), "communityid", pid);
    }
    
    /**
     * 判断当前日期是否超出活动申请截止日期
     * @return
     */
    public Object findCanApply(Object activityId){
    	String sql = "select 1 from activity where activityid = ? and expiration>=now()";
    	return this.findFirst(sql, activityId);
    }
    
    /**
     * 查询指定时间范围内当前用户发布的活动
     * @return
     */
    public Object findHasPublish(Object communityId,Object userId){
    	String sql = "select 1 from activity where communityid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, communityId, userId);
    }
    
    /**
     * 根据版块分类更新主题状态
     * @param pid 版块分类id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForumClassify(Object pid,Object status){
    	StringBuffer sql = new StringBuffer("update activity aty set aty.status = ? ");
    	sql.append(" where exists(select 1 from community c where aty.communityid = c.communityid and c.pid = ? )");
    	Db.update(sql.toString(), status,pid);
    }
    
	/**
	 * 根据版块分类id更新回复信息
	 * @param pid 版块分类id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForumClassify(Object pid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '20' ");
		sql.append(" and exists(select 1 from activity aty,community cy where cy.communityid = aty.communityid ");
		sql.append(" and aty.activityid = c.commentid and c.idtype = '20' and cy.pid = ?) ");
		Db.update(sql.toString(), status,pid);
	}
    
    /**
     * 根据版块更新主题状态
     * @param community 版块id
     * @param status 显示隐藏状态
     */
    public void updateStatusByForum(Object community,Object status){
    	String sql = "update activity aty set aty.status = ? where aty.communityid = ?";
    	Db.update(sql, status,community);
    }
    
	/**
	 * 根据版块id更新回复信息
	 * @param communityid 版块id
	 * @param status 是否显示状态
	 */
	public void updateCommentStatusByForum(Object communityid,Object status){
		StringBuffer sql = new StringBuffer("update comment c set c.status = ? where c.idtype = '20' ");
		sql.append(" and exists(select 1 from activity aty where aty.activityid = c.commentid and c.idtype = '20' ");
		sql.append(" and aty.communityid = ?) ");
		Db.update(sql.toString(), status,communityid);
	}
    
    /**
     * 根据版块分类删除主题状态
     * @param pid 版块分类id
     */
    public void deleteByForumClassify(Object pid){
    	StringBuffer sql = new StringBuffer("delete from activity aty where exists(select 1 from community c where ");
    	sql.append(" c.communityid = aty.communityid and c.pid = ?)");
    	Db.update(sql.toString(), pid);
    }
    
    /**
     * 根据版块删除主题状态
     * @param community 版块id
     */
    public void deleteByForum(Object community){
    	String sql = "delete from activity aty where aty.communityid = ? ";
    	Db.update(sql, community);
    }
}
