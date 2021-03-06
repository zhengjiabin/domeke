package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "of_wonders", pkName = "ofwondersid")
public class OfWonders extends Model<OfWonders> {
	
	private static final long serialVersionUID = 1L;
	
	public static OfWonders dao = new OfWonders();
	
	/**
	 * 查询帖子明细信息
	 * @return
	 */
	public OfWonders findInfoById(Object ofWondersId){
		StringBuffer sql = new StringBuffer("select o.*,u.username,u.imgurl,w.title ");
		sql.append(" from of_wonders o,user u,wonders_type w where o.userid=u.userid  ");
		sql.append(" and o.wonderstypeid=w.wonderstypeid and o.status='10' and o.ofwondersid=? ");
		return this.findFirst(sql.toString(), ofWondersId);
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
	public Page<OfWonders> findPageAll(int pageNumber, int pageSize) {
		String select = "select o.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from of_wonders o,user u where o.userid=u.userid ");
		sqlExceptSelect.append("  order by to_days(o.createtime) desc,o.top desc,o.essence desc ");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
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
		String select = "select o.*,u.username,u.imgurl";
		StringBuffer sqlExceptSelect = new StringBuffer("from of_wonders o,user u where o.userid=u.userid ");
		sqlExceptSelect.append(" and o.userid=? and o.status='10' ");
		sqlExceptSelect.append(" order by to_days(o.createtime) desc,o.top desc,o.essence desc ");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(), userId);
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
		StringBuffer sqlExceptSelect = new StringBuffer("from of_wonders p,user u where p.userid=u.userid and p.status='10' ");
		sqlExceptSelect.append("  order by to_days(createtime) desc,top desc,essence desc ");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString());
		return page;
	}
	
	/**
	 * 根据版块明细分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @param wondersTypeId
	 * 			     版块明细
	 * @return
	 */
	public Page<OfWonders> findPageByWondersTypeId(int pageNumber, int pageSize,Object wondersTypeId) {
		String select = "select u.username,u.imgurl,p.*,c.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer("from user u,of_wonders p left join ");
		sqlExceptSelect.append("(select count(1) as number,targetid from comment where status='10' and idtype='50' group by targetid) c ");
		sqlExceptSelect.append(" on p.ofwondersid=c.targetid where u.userid=p.userid and p.status='10' and p.wonderstypeid=? ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),wondersTypeId);
		return page;
	}
	
	/**
	 * 根据版块分页查询帖子
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @param wondersTypeId
	 * 			     版块
	 * @return
	 */
	public Page<OfWonders> findPageByWondersTypePid(int pageNumber, int pageSize,Object wondersTypeId) {
		String select = "select u.username,u.imgurl,p.*,c.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer("from user u,wonders_type w,of_wonders p left join ");
		sqlExceptSelect.append("(select count(1) as number,targetid from comment where status='10' and idtype='50' group by targetid) c ");
		sqlExceptSelect.append(" on p.ofwondersid=c.targetid where u.userid=p.userid and w.wonderstypeid = p.wonderstypeid ");
		sqlExceptSelect.append(" and p.status='10' and w.pid=? ");
		sqlExceptSelect.append(" order by to_days(p.createtime) desc,p.top desc,p.essence desc");
		Page<OfWonders> page = this.paginate(pageNumber, pageSize, select,sqlExceptSelect.toString(),wondersTypeId);
		return page;
	}

	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object ofWondersidId) {
		String sql = "update of_wonders set times = times +1 where ofwondersid=?";
		Db.update(sql, ofWondersidId);
	}
	
	/**
     * 根据版块明细查询今日发帖数
     * @return 汇总数
     */
    public Long getTodayCountByWondersTypeId(Object wondersTypeId){
    	String sql ="select count(1) from of_wonders where status='10' and wonderstypeid=? and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql,wondersTypeId);
    	return count;
    }
    
	/**
     * 根据版块查询今日发帖数
     * @return 汇总数
     */
    public Long getTodayCountByWondersTypePid(Object wondersTypeId){
    	StringBuffer sql = new StringBuffer("select count(1) as number ");
    	sql.append(" from of_wonders o,wonders_type w ");
    	sql.append(" where o.wonderstypeid=w.wonderstypeid and o.status='10' and w.pid=? and to_days(o.createtime)=to_days(now())");
    	Long count = Db.queryLong(sql.toString(),wondersTypeId);
    	return count;
    }
    
	/**
     * 今日发帖数
     * @return 汇总数
     */
    public Long getTodayCount(){
    	String sql ="select count(1) from of_wonders where status='10' and to_days(createtime)=to_days(now())";
    	Long count = Db.queryLong(sql);
    	return count;
    }
    
    /**
     * 根据版块明细查昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCountByWondersTypeId(Object wondersTypeId){
    	String sql ="select count(1) from of_wonders where status='10' and wonderstypeid=? and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql,wondersTypeId);
    	return count;
    }
    
    /**
     * 根据版块查查询昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCountByWondersTypePid(Object wondersTypeId){
    	StringBuffer sql = new StringBuffer("select count(1) as number ");
    	sql.append(" from of_wonders o,wonders_type w ");
    	sql.append(" where o.wonderstypeid=w.wonderstypeid and o.status='10' and w.pid=? ");
    	sql.append(" and date(o.createtime) = date_sub(curdate(),interval 1 day) ");
    	Long count = Db.queryLong(sql.toString(),wondersTypeId);
    	return count;
    }
    
    /**
     * 昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	String sql ="select count(1) from of_wonders where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)";
    	Long count = Db.queryLong(sql);
    	return count;
    }
    
    /**
     * 根据版块明细查询总发帖数
     * @return 汇总数
     */
    public Long getCountByWondersTypeId(Object wondersTypeId){
    	Long count = Db.queryLong("select count(1) from of_Wonders where status='10' and wonderstypeid=?",wondersTypeId);
    	return count;
    }
    
    /**
     * 根据版块查询总发帖数
     * @return 汇总数
     */
    public Long getCountByWondersTypePid(Object wondersTypeId){
    	StringBuffer sql = new StringBuffer("select count(1) as number ");
    	sql.append(" from of_Wonders o,wonders_type w ");
    	sql.append(" where o.wonderstypeid=w.wonderstypeid and o.status='10' and w.pid=? ");
    	Long count = Db.queryLong(sql.toString(),wondersTypeId);
    	return count;
    }
    
    /**
     * 总发帖数
     * @return 汇总数
     */
    public Long getCount(){
    	Long count = Db.queryLong("select count(1) from of_Wonders where status='10' ");
    	return count;
    }
    
    /**
     * 根据版块统计总发帖数
     */
    public List<OfWonders> getCountList(){
    	StringBuffer sql = new StringBuffer("select count(1) as number,w.wonderstypeid ");
    	sql.append(" from of_wonders w ");
    	sql.append(" where w.status='10' group by w.wonderstypeid");
    	List<OfWonders> ofWondersidList = this.find(sql.toString());
    	return ofWondersidList;
    }
    
    /**
     * 根据版块明细查询当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByTypeAndUser(Object wondersTypeId,Object userId){
    	Long count = Db.queryLong("select count(1) from of_wonders where status='10' and wonderstypeid=? and userid=?",wondersTypeId,userId);
    	return count;
    }
    
    /**
     * 根据版块查询当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByTypePidAndUser(Object wondersTypeId,Object userId){
    	StringBuffer sql = new StringBuffer("select count(1) as number ");
    	sql.append(" from of_wonders o,wonders_type w ");
    	sql.append(" where o.wonderstypeid=w.wonderstypeid and o.status='10' and w.pid=? and o.userid=? ");
    	Long count = Db.queryLong(sql.toString(),wondersTypeId,userId);
    	return count;
    }
    
    /**
     * 当前登录人总发帖数
     * @return 汇总数
     */
    public Long getCountByUserId(Object userId){
    	Long count = Db.queryLong("select count(1) from of_wonders where status='10' and userid=?",userId);
    	return count;
    }
    
    /**
     * 查询指定时间范围内当前用户发布的主题
     * @return
     */
    public Object findHasPublish(Object wondersTypeId,Object userId){
    	String sql = "select 1 from of_wonders where wonderstypeid=? and userid=? and createtime >= date_sub(now(),interval 5 minute)";
    	return this.findFirst(sql, wondersTypeId, userId);
    }
    
    /**
     * 查询热门主题
     * @return
     */
    public List<OfWonders> findPic(int limit){
    	StringBuffer sql = new StringBuffer("select c.number as viewcount,p.* from of_wonders p left join ");
    	sql.append(" (select count(1) as number,targetid from comment where status='10' and idtype='50' group by targetid) c ");
    	sql.append(" on p.ofwondersid=c.targetid where p.status='10' ");
    	sql.append(" order by p.top desc,p.essence desc,to_days(p.createtime) desc limit ?");
    	return this.find(sql.toString(),limit);
    }
}
