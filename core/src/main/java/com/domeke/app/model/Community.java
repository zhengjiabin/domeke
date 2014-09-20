package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "community", pkName = "communityid")
public class Community extends Model<Community> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Community dao = new Community();
	
	public void deleteFatAndSonByCommunityId(Object communityId){
		String sql = "delete from community where communityid=? or pid=?";
		Db.batch(sql, new Object[][] { { communityId, communityId } }, 1);
	}

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
		List<Community> communityFatList = this.find(sql);
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
		List<Community> communitySonList = this.find(sql);
		return communitySonList;
	}

	/**
	 * 根据父模块查询子模块
	 * 
	 * @return
	 */
	public List<Community> findSonListByPid(Object pId) {
		String sql = "select * from community where status='10' and level=2 and pid=? order by position";
		List<Community> communitySonList = this.find(sql, pId);
		return communitySonList;
	}
	
	/**
	 * 浏览次数+1
	 */
	public void updateTimes(Object communityId) {
		String sql = "update community set times = times +1 where communityid=?";
		Db.update(sql, communityId);
	}
}
