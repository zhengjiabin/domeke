package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * CREATE TABLE `treasure` (
 * `treasureid` int(10) unsigned NOT NULL AUTO_INCREMENT,
 * `author` varchar(15) NOT NULL,
 * `authorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `subject` varchar(80) NOT NULL,
 * `dateline` timestamp NOT NULL ,
 * `message` text NOT NULL,
 * `useip` varchar(15) NOT NULL,
 * `invisible` tinyint(1) NOT NULL DEFAULT '0',
 * `anonymous` tinyint(1) NOT NULL DEFAULT '0',
 * `usesig` tinyint(1) NOT NULL DEFAULT '0',
 * `attachment` tinyint(1) NOT NULL DEFAULT '0',
 * `rate` smallint(6) NOT NULL DEFAULT '0',
 * `ratetimes` tinyint(3) unsigned NOT NULL DEFAULT '0',
 * `status` int(10) NOT NULL DEFAULT '0',
 * `comment` tinyint(1) NOT NULL DEFAULT '0',
 * `replycredit` int(10) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`treasureid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
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
		Page<Treasure> page = this.paginate(pageNumber, pageSize, "select *",
				"from treasure where status='10' order by to_days(createtime) desc,top desc,essence desc");
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
		String select = "select u.username,u.imgurl,p.*,c.number as viewcount";
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from user u,treasure p left join (select count(1) as number,targetid from comment where idtype='30' group by targetid) c on p.treasureid=c.targetid ");
		sqlExceptSelect.append(" where u.userid=p.userid and p.status='10' and p.communityid=? order by to_days(p.createtime) desc,p.top desc,p.essence desc");
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
