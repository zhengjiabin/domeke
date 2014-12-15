/**
 * 
 */
package com.domeke.app.model;

import java.sql.Timestamp;
import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author zhouying
 * DROP TABLE IF EXISTS `vent_wall`;
 * CREATE TABLE `vent_wall` (
 * `ventwallid` bigint(20) NOT NULL AUTO_INCREMENT,
 * `userid` bigint(20) DEFAULT NULL,
 * `moodid` varchar(100) DEFAULT NULL COMMENT '心情id',
 * `message` varchar(255) DEFAULT NULL COMMENT '留言',
 * `createtime`  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
 * `creater` bigint(20) NULL,
 * `modifytime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * `modifier` bigint(20) NULL,
 * PRIMARY KEY (`ventwallid`));
 */
@TableBind(tableName = "vent_wall", pkName = "ventwallid")
public class VentWall extends Model<VentWall> {

	private static final long serialVersionUID = -1568398434037230642L;
	public static VentWall venWdao = new VentWall();

	/**
	 * 新增留言
	 */
	public void saveVentWall() {
		this.save();
	}

	/**
	 * 查询留言
	 * @return 所有留言
	 */
	public List<VentWall> getVentWall() {
		List<VentWall> ventWallList = venWdao.findByCache("ventWallList", "key",
				"SELECT * FROM VENT_WALL   ORDER BY CREATETIME");
		return ventWallList;
	}

	/**
	 * 删除留言
	 * @param 留言表ID
	 */
	public void deleteVentWall(Long ventwallid) {
		venWdao.deleteById(ventwallid);
	}

	/**
	 * 更新留言
	 * @param 留言表ID
	 */
	public void updateVentWall(int ventwallid) {

	}

	/**
	 * 删除缓存
	 */
	public void removeCache() {
		CacheKit.removeAll("VentWall");
		CacheKit.removeAll("ventWallList");
	}

	/**
	 * 当日签到汇总
	 * @return 汇总数
	 */
	public Object getTodayCount() {
		Object count = Db.queryLong("SELECT COUNT(VENTWALLID) FROM VENT_WALL WHERE TO_DAYS(CREATETIME)=TO_DAYS(NOW())");
		return count;
	}

	/**
	 * 昨日汇总
	 * @return 汇总数
	 */
	public Object getYesterdayCount() {
		Object count = Db
				.queryLong("SELECT COUNT(VENTWALLID) FROM VENT_WALL WHERE DATE(CREATETIME) = DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
		return count;
	}

	/**
	 * 用户签到汇总
	 * @return 汇总数
	 */
	public Object getUserIdCount(Long userId) {
		Object count = Db.queryLong("SELECT COUNT(VENTWALLID) FROM VENT_WALL WHERE USERID=" + userId);
		return count;
	}

	/**
	 * 本月当前用户汇总
	 * @return 汇总数
	 */
	public Object getMonthCount(Long userId) {
		Object count = Db
				.queryLong("SELECT COUNT(VENTWALLID) FROM VENT_WALL WHERE DATE_FORMAT(CREATETIME,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m') AND USERID="
						+ userId);
		return count;
	}

	/**
	 * 当前用户上次签到时间
	 * @return 签到时间
	 */
	public Timestamp getCreatetime(Long userId) {
		Timestamp createtime = Db.queryTimestamp("SELECT CREATETIME FROM VENT_WALL WHERE USERID=" + userId
				+ " ORDER BY CREATETIME LIMIT 1");
		return createtime;
	}

	/**
	 * 汇总历史最高
	 * @return 汇总数
	 */
	public Object getTotalCount() {
		Object count = Db
				.queryLong("SELECT NUM  FROM(SELECT COUNT(VENTWALLID) AS NUM FROM VENT_WALL GROUP BY  DATE(CREATETIME))A ORDER BY A.NUM DESC LIMIT 1");
		if (count == null) {
			count = 0;
		}
		return count;
	}

	/**
	 * 根据UserId查询是否签到
	 * @param userId 用户ID
	 * @return 是否签到：0  已签到，1  未签到
	 */
	public String isSignIn(Long userId) {
		String issignin = "0";
		Object userid = Db
				.queryLong("SELECT USERID FROM VENT_WALL WHERE TO_DAYS(CREATETIME)=TO_DAYS(NOW()) AND USERID=" + userId);
		if (userid == null || "".equals(userid)) {
			issignin = "1";
		}
		return issignin;
	}

	/**
	 * 分页查询发泄墙
	 * @param pageNumber  页号
	 * @param pageSize    页数
	 * @return
	 */
	public Page<VentWall> findPage(int pageNumber, int pageSize) {
		Page<VentWall> page = this
				.paginate(pageNumber, pageSize, "SELECT *", "FROM VENT_WALL ORDER BY CREATETIME desc");
		return page;
	}
}
