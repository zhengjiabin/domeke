package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "activity_apply", pkName = "activityapplyid")
public class ActivityApply extends Model<ActivityApply> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static ActivityApply dao = new ActivityApply();

	/**
	 * 分页查询指定活动的参与者
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<ActivityApply> findByActivityId(Object activityId,
			int pageNumber, int pageSize) {
		String select = "select u.username,u.imgurl,app.*";
		StringBuffer sqlExceptSelect = new StringBuffer("from activity_apply app,user u ");
		sqlExceptSelect.append("where app.userid=u.userid and app.activityid=? order by app.createtime");
		Page<ActivityApply> page = this.paginate(pageNumber, pageSize,select,sqlExceptSelect.toString(),activityId);
		return page;
	}
	
    /**
     * 总活动数
     * @return 汇总数
     */
    public Long getCount(Object activityId){
    	Long count = Db.queryLong("select count(1) from activity_apply where status='10' and activityid=?",activityId);
    	return count;
    }
    
    /**
     * 当前活动是否可报名
     * @return
     */
    public Object findByUserId(Object activityId,Object userId){
    	String sql = "select 1 from activity_apply where activityid=? and userid=?";
    	return this.findFirst(sql, activityId,userId);
    }
}
