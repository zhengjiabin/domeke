package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
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
	 * 分页查询宝贝
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Treasure> findPage(int pageNumber, int pageSize) {
		String select = "select u.username,u.imgurl,t.*";
		StringBuffer sqlExceptSelect = new StringBuffer("from treasure t,user u ");
		sqlExceptSelect.append(" where t.userid=u.userid and t.status='10' ");
		sqlExceptSelect.append(" order by to_days(t.createtime) desc,t.top desc,t.essence desc");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}
	/**
	 * 分页查询宝贝
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<Treasure> findPageByCommunityId(int pageNumber, int pageSize,Object communityId) {
		String select = "select u.username,u.imgurl,t.*,cm.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer("from user u,treasure t left join ");
		sqlExceptSelect.append(" (select count(1) as number,targetid from comment where idtype='30' group by targetid) cm on t.treasureid=cm.targetid ");
		sqlExceptSelect.append(" where u.userid=t.userid and t.status='10' and t.communityid=? ");
		sqlExceptSelect.append(" order by to_days(t.createtime) desc,t.top desc,t.essence desc");
		Page<Treasure> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),communityId);
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
		Page<Treasure> page = this.paginate(pageNumber, pageSize, "select *",
				"from treasure where userid=? order by to_days(createtime) desc,top desc,essence desc", userId);
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
     * 今日发宝贝数
     * @return 汇总数
     */
    public Long getTodayCount(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and to_days(createtime)=to_days(now())");
    	return count;
    }
    
    /**
     * 昨日宝贝数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
    	return count;
    }
    
    /**
     * 总宝贝数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from treasure where status='10'");
    	return count;
    }
    
    /**
     * 当前登录人总宝贝数
     * @return 汇总数
     */
    public Long getCountByUserId(Object userId){
    	Long count = Db.queryLong("select count(1) from treasure where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 根据版块统计总宝贝数
     * @return 汇总数
     */
    public List<Treasure> getCountByCommunityPid(Object pid){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(1) as number,p.communityid from treasure p,community son ");
    	sql.append(" where p.communityid = son.communityid and p.status='10' and son.pid = ? ");
    	sql.append(" group by p.communityid ");
    	List<Treasure> treasureList = this.find(sql.toString(), pid);
    	return treasureList;
    }
    
    /**
     * 查询指定时间范围内当前用户发布的宝贝
     * @return
     */
    public Object findHasPublish(Object communityId,Object userId){
    	String sql = "select 1 from treasure where communityid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, communityId, userId);
    }
}
