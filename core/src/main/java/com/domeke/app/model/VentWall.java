/**
 * 
 */
package com.domeke.app.model;


import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author zhouying
 * DROP TABLE IF EXISTS `vent_wall`;
 * CREATE TABLE `vent_wall` (
 * `ventwallid` bigint(20) NOT NULL AUTO_INCREMENT,
 * `userid` bigint(20) DEFAULT NULL,
 * `moodid` bigint(20) DEFAULT NULL COMMENT '心情id',
 * `message` varchar(255) DEFAULT NULL COMMENT '留言',
 * `createtime`  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` bigint(20) NULL,
 * `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * `modifier` bigint(20) NULL,
 * PRIMARY KEY (`ventwallid`));
 */
@TableBind(tableName = "vent_wall", pkName = "ventwallid")
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
		List<VentWall> ventWallList = venWdao.findByCache("ventWallList", "key", "select * from vent_wall order by createtime");
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
	/**
	 * 
	 */
    public void removeCache(){
        CacheKit.removeAll("VentWall");
        CacheKit.removeAll("ventWallList");
    }
}
