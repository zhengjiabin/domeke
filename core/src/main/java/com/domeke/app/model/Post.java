package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
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
				"from post order by status,createtime");
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
	public Page<Post> findByAuthorid(Object userId, int pageNumber, int pageSize) {
		Page<Post> page = this.paginate(pageNumber, pageSize, "select *",
				"from post where userid=? order by status,createtime", userId);
		return page;
	}
}
