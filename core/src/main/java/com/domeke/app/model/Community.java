package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "community", pkName = "communityid")
public class Community extends Model<Community> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Community dao = new Community();

	/**
	 * 查询父模块
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findFatList() {
		String sql = "select * from community where status='10' and level=1 order by position";
		List<Community> communityFatList = Community.dao.findByCache(
				"CommunityFatList", "key", sql);
		return communityFatList;
	}

	/**
	 * 查询子模块
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public List<Community> findSonList() {
		String sql = "select * from community where status='10' and level=2 order by position";
		List<Community> communitySonList = Community.dao.findByCache(
				"CommunitySonList", "key", sql);
		return communitySonList;
	}

}
