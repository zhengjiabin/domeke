package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "of_wonders", pkName = "ofwondersid")
public class OfWonders extends Model<OfWonders> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static OfWonders dao = new OfWonders();
	
	/**
	 * 分页查询论坛，不区分状态
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<OfWonders> findPageAll(int pageNumber, int pageSize) {
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from ofWondersid p,user u where p.userid=u.userid ");
		sqlExceptSelect.append("  order by to_days(createtime) desc,top desc,essence desc ");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
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
	public Page<OfWonders> findPage(int pageNumber, int pageSize) {
		String select = "select p.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from ofWondersid p,user u where p.userid=u.userid and p.status='10' ");
		sqlExceptSelect.append("  order by to_days(createtime) desc,top desc,essence desc ");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
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
	public Page<OfWonders> findPageByCommunityId(int pageNumber, int pageSize,Object communityId) {
		String select = "select u.username,u.imgurl,p.*,c.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from user u,ofWondersid p left join (select count(1) as number,targetid from comment where idtype='10' group by targetid) c on p.ofWondersidid=c.targetid ");
		sqlExceptSelect.append(" where u.userid=p.userid and p.status='10' and p.communityid=? order by to_days(p.createtime) desc,p.top desc,p.essence desc");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),communityId);
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
	public Page<OfWonders> findByUserId(Object userId, int pageNumber, int pageSize) {
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, "select *",
				"from ofWondersid where userid=? order by to_days(createtime) desc,top desc,essence desc", userId);
		return page;
	}

	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object ofWondersidId) {
		String sql = "update ofWondersid set times = times +1 where ofWondersidid=?";
		Db.update(sql, ofWondersidId);
	}
	
	/**
     * 今日发帖数
     * @return 汇总数
     */
    public Long getTodayCount(){
    	Long count = Db.queryLong("select count(1) from ofWondersid where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
    /**
     * 昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from ofWondersid where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 总发帖数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from ofWondersid where status='10'");
    	return count;
    }
    
    /**
     * 当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByUserId(Object userId){
    	Long count = Db.queryLong("select count(1) from ofWondersid where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 根据版块统计总发帖数
     * @return 汇总数
     */
    public List<OfWonders> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,p.communityid from ofWondersid p,community son ");
    	sql.append(" where p.communityid = son.communityid and p.status='10' and son.pid = ? ");
    	sql.append(" group by p.communityid ");
    	List<OfWonders> ofWondersidList = this.find(sql.toString(), pid);
    	return ofWondersidList;
    }
    
    /**
     * 查询指定时间范围内当前用户发布的帖子
     * @return
     */
    public Object findHasPublish(Object communityId,Object userId){
    	String sql = "select 1 from ofWondersid where communityid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, communityId, userId);
    }
}
