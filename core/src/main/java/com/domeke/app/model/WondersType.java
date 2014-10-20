package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "wonders_type", pkName = "wonderstypeid")
public class WondersType extends Model<WondersType> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static WondersType dao = new WondersType();

	/**
	 * 查询父模块
	 */
	public List<WondersType> findFatList() {
		String sql = "select * from wonders_type where status='10' and level=1 order by position";
		List<WondersType> wondersTypeFatList = this.find(sql);
		return wondersTypeFatList;
	}

	/**
	 * 查询子模块
	 */
	public List<WondersType> findSonList() {
		String sql = "select * from wonders_type where status='10' and level=2 order by position";
		List<WondersType> wondersTypeSonList = this.find(sql);
		return wondersTypeSonList;
	}
	
	/**
	 * 查询子版块和对应的主题总数
	 * @return
	 */
	public List<WondersType> findSonListCount(){
		StringBuffer sql = new StringBuffer("select o.number as ofwondersnumber,w.* ");
		sql.append(" from wonders_type w left join (select count(1) as number,of.wonderstypeid  from of_wonders of ");
		sql.append(" where of.status='10' group by of.wonderstypeid) o on o.wonderstypeid=w.wonderstypeid ");
		sql.append(" where w.status='10' and w.level = 2 order by w.position ");
		List<WondersType> wondersTypeSonList = this.find(sql.toString());
		return wondersTypeSonList;
	}

	/**
	 * 根据父模块查询子模块
	 * 
	 * @return
	 */
	public List<WondersType> findSonListByPid(Object pId) {
		String sql = "select * from wonders_type where status='10' and level=2 and pid=? order by position";
		List<WondersType> wondersTypeSonList = this.find(sql, pId);
		return wondersTypeSonList;
	}
	
	/**
	 * 删除版块及子版块
	 * @param wondersTypeId
	 */
	public void deleteFatAndSonByWondersTypeId(Object wondersTypeId){
		String sql = "delete from wonders_type where wonderstypeid=? or pid=?";
		Db.batch(sql, new Object[][] { { wondersTypeId, wondersTypeId } }, 1);
	}
}
