package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * CREATE TABLE `activity` (
 * `activityid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
 * `uid` mediumint(8) NOT NULL DEFAULT '0' ,
 * `aid` mediumint(8) NOT NULL DEFAULT '0' ,
 * `cost` mediumint(8) NOT NULL DEFAULT '0',
 * `starttimefrom` timestamp NOT NULL ,
 * `starttimeto` timestamp NOT NULL ,
 * `place` varchar(255) NOT NULL,
 * `class` varchar(255) NOT NULL,
 * `gender` tinyint(1) NOT NULL DEFAULT '0',
 * `number` smallint(5) unsigned NOT NULL DEFAULT '0',
 * `applynumber` smallint(5) unsigned NOT NULL DEFAULT '0',
 * `expiration` timestamp NOT NULL ,
 * `ufield` text NOT NULL,
 * `credit` smallint(6) unsigned NOT NULL DEFAULT '0',
 * `status` tinyint(1) DEFAULT NULL,
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`activityid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "activity", pkName = "activityid")
public class Activity extends Model<Activity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Activity dao = new Activity();

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
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from activity aty left join (select count(1) as number,app.activityid from activity_apply app where app.status='10' ");
		sqlExceptSelect.append(" group by app.activityid) app on aty.activityid=app.activityid where aty.status='10'");
		sqlExceptSelect.append(" order by aty.createtime");
		Page<Activity> page = this.paginate(pageNumber, pageSize, "select aty.*,app.number",sqlExceptSelect.toString());
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
	public Page<Activity> findPage(int pageNumber, int pageSize,
			Object communityId) {
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from activity aty left join (select count(1) as number,app.activityid from activity_apply app where app.status='10' ");
		sqlExceptSelect.append(" group by app.activityid) app on aty.activityid=app.activityid where aty.status='10' and aty.communityid=? ");
		sqlExceptSelect.append("order by aty.createtime");
		Page<Activity> page = this.paginate(pageNumber, pageSize, "select aty.*,app.number as applynumber",sqlExceptSelect.toString(),communityId);
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
	public Page<Activity> findByUserId(Object userId, int pageNumber,
			int pageSize) {
		Page<Activity> page = this.paginate(pageNumber, pageSize, "select *",
				"from activity where userid=? order by status,createtime",
				userId);
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
     * 昨日发表活动数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from activity where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
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
}
