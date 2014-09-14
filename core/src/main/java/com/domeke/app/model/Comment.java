package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * CREATE TABLE `comment` (
 * `commentid` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
 * `uid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `id` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `idtype` varchar(20) NOT NULL,
 * `authorid` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `author` varchar(15) NOT NULL,
 * `ip` varchar(20) NOT NULL,
 * `dateline` int(14) unsigned NOT NULL DEFAULT '0',
 * `message` text NOT NULL,
 * `status` tinyint(1) NOT NULL DEFAULT '0',
 * `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `modifier` mediumint(8) unsigned NOT NULL DEFAULT '0',
 * PRIMARY KEY (`commentid`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName = "comment", pkName = "commentid")
public class Comment extends Model<Comment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Comment dao = new Comment();

	/**
	 * 根据targetId查询子回复信息
	 * 
	 * @param targetId 回复目标
	 * @param idtype 10:帖子，20:活动
	 * 
	 * @return
	 */
	public List<Comment> findFollowByTargetId(Object targetId,Object idtype) {
		String sql = "select * from comment where status='10' and idtype=? and level=2 and targetid =? order by createtime";
		List<Comment> list = this.find(sql, idtype,targetId);
		return list;
	}

	/**
	 * 根据targetId查询主回复信息
	 * 
	 * @return
	 */
	public Page<Comment> findPageByTargetId(Object targetId,Object idtype, int pageNumber,
			int pageSize) {
		String sql = "from comment where status='10' and idtype=? and level=1 and targetid=? order by createtime";
		Page<Comment> page = this.paginate(pageNumber, pageSize, "select *",
				sql, idtype,targetId);
		return page;
	}

	/**
	 * 删除主子回复信息
	 * 
	 * @return
	 */
	public void deleteReplyAll(Object targetId) {
		String sql = "delete from comment where commentid=? or pid=?";
		Db.batch(sql, new Object[][] { { targetId, targetId } }, 1);
	}
	
}
