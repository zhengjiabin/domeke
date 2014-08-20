/**
 * 
 */
package com.domeke.app.model;


import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author zhouying
 *
 */
public class VentWall extends Model<VentWall>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1568398434037230642L;
	public static VentWall venWdao =new VentWall();
	
	/**
	 * 新增留言
	 */
	public void saveVentWall(){
		this.save();
	}
	/**
	 * 查询留言
	 * @return 所有留言
	 */
	public List<VentWall> getVentWall(){
		List<VentWall> ventWallList = venWdao.findByCache("ventWallList", "key", "select * from ventwall order by create_time");
		return ventWallList;	
	}
	/**
	 * 删除留言
	 * @param 留言表ID
	 */
	public void deleteVentWall(int ventwallid){
		venWdao.deleteById(ventwallid);
	}
	/**
	 * 更新留言
	 * @param 留言表ID
	 */
	public void updateVentWall(int ventwallid){
		//venWdao.findById(ventwallid).setAttrs();
	}
    public void removeCache(){
        CacheKit.removeAll("VentWall");
        CacheKit.removeAll("ventWallList");
    }
}
