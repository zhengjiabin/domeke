package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * CREATE TABLE `post` (
 * `postid` int(10) unsigned NOT NULL AUTO_INCREMENT,
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
 * PRIMARY KEY (`postid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "post", pkName = "postid")
public class Post extends Model<Post> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Post dao = new Post();

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
		Page<Post> page = this.paginate(pageNumber, pageSize, "select *",
				"from post where status='10' order by createtime");
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
		StringBuffer sqlExceptSelect = new StringBuffer();
		sqlExceptSelect.append("from post p left join (select count(1) as number,targetid from comment group by targetid) c on p.postid=c.targetid ");
		sqlExceptSelect.append(" where p.status='10' and p.communityId=? order by createtime");
		Page<Post> page = this.paginate(pageNumber, pageSize, "select p.*,c.number as viewcount",sqlExceptSelect.toString(),communityId);
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
				"from post where userid=? order by status,createtime", userId);
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
     * 昨日发帖数
     * @return 汇总数
     */
    public Long getYesterdayCount(){
    	Long count = Db.queryLong("select count(1) from post where status='10' and date(createtime) = date_sub(curdate(),interval 1 day)");
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
}
