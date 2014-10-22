package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "activity", pkName = "activityid")
public class Activity extends Model<Activity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Activity dao = new Activity();
	
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
		sqlExceptSelect.append("  order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc ");
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
	 * 分页查询活动
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findPage(int pageNumber, int pageSize) {
		String select = "select u.imgurl,u.username,aty.*,app.number as applynumber";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from user u,activity aty left join (select count(1) as number,app.activityid from activity_apply app ");
		sqlExceptSelect.append(" where app.status='10' group by app.activityid) app on aty.activityid=app.activityid where u.userid=aty.userid ");
		sqlExceptSelect.append(" and aty.status='10'");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}

	/**
	 * 分页查询活动
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Activity> findPageByCommunityId(int pageNumber, int pageSize,Object communityId) {
		String select = "select u.imgurl,u.username,aty.*,app.number as applynumber";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from user u,activity aty left join (select count(1) as number,app.activityid from activity_apply app ");
		sqlExceptSelect.append(" where app.status='10' group by app.activityid) app on aty.activityid=app.activityid where u.userid=aty.userid ");
		sqlExceptSelect.append(" and aty.status='10' and aty.communityid=? ");
		sqlExceptSelect.append(" order by to_days(aty.createtime) desc,aty.top desc,aty.essence desc");
		Page<Activity> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),communityId);
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
     * 今日发表活动数
     * @return 汇总数
     */
    public Long getTodayCount(){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
	/**
     * 今日发表活动数
     * @return 汇总数
     */
    public Long getTodayCountByCommunityId(Object communityId){
    	String sql = "select count(1) from activity where status='10' and communityid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,communityId);
    	return count;
    }
    
    /**
     * 昨日发表活动数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 昨日发表活动数
     * @return 汇总数
     */
    public Long getYesterdayCountByCommunityId(Object communityId){
    	String sql = "select count(1) from activity where status='10' and communityid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,communityId);
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
     * 总活动数
     * @return 汇总数
     */
    public Long getCountByCommunityId(Object communityId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and communityid=?",communityId);
    	return count;
    }
    
    /**
     * 当前登录人总活动数
     * @return 汇总数
     */
    public Long getCountByUserId(Object userId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 当前登录人总活动数
     * @return 汇总数
     */
    public Long getCountByCommunityAndUser(Object communityId,Object userId){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and communityid=? and userid=?",communityId,userId);
    	return count;
    }
    
    /**
     * 根据版块统计活动数
     * @return 汇总数
     */
    public List<Activity> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,aty.communityid from activity aty,community son ");
    	sql.append(" where aty.communityid = son.communityid and aty.status='10' and son.pid = ? ");
    	sql.append(" group by aty.communityid ");
    	List<Activity> activityList = this.find(sql.toString(), pid);
    	return activityList;
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
    
    public Page<Activity> getActivity(Long userId,int pageNumber,int pageSize){
    	Page<Activity> page = this.paginate(pageNumber, pageSize, "select * ", "from activity where userid in (select userid from activity_apply where userid=?)",userId);
    	return page;
    }
}
