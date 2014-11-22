package com.domeke.app.model;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "history", pkName = "hid")
public class History extends Model<History> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4311228884712502862L;
	public static History dao = new History();

	public void saveOrUpdateHitory(Work work) {
		History history = new History();

		LocalDate now = LocalDate.now();
		
		Works dao = new Works();
		Works works = dao.findById(work.get("worksid"));
		Long pageviews = new Long(0);
		Integer playtimes = 0;
		if (works.getLong("pageviews") != null) {
			pageviews = works.getLong("pageviews");
		}
		if(work.getInt("playtimes") != null){
			playtimes = work.getInt("playtimes");
		}
		works.set("pageviews", pageviews + 1);
		work.set("playtimes", playtimes + 1);
		work.update();
		works.update();
		history = getDayHistory(work, now);
		if (history.isNotEmpty() && history.getLong("count") > 0) {
			Long count = history.getLong("count") + 1;
			history.set("count", count);
			history.update();
		} else {
			history.set("workid", work.get("workid"));
			history.set("worksid", work.get("worksid"));
			history.set("workname", work.get("workname"));
			history.set("cover", work.get("cover"));
			history.set("comic", work.get("comic"));
			history.set("count", 1);
			history.set("fromtime", new Date());
			history.set("totime", new Date());
			history.save();
		}
	}
	/**
	 * 获取上周1时间
	 * @return
	 */
	public Date getLastMonday(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		return calendar.getTime();
	}
	/**
	 * 获取上周日时间
	 * @return
	 */
	public Date getLastSunday(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		return calendar.getTime();
	}
	
	/**
	 * 上周点击排行
	 * @return
	 */
	public List<History> getLastWeekClickRank(Integer limit,String startTime,String endTime){
		List<History> historys = Lists.newArrayList();
		try {
			StringBuffer sql = new StringBuffer("select * from history ");
			sql.append(" where  date_format(fromtime,'%Y-%m-%d') between '"+startTime+"' and '"+endTime+"' limit "+limit);
			historys = this.find(sql.toString());
			return historys;
		} catch (Exception e) {
			e.printStackTrace();
			return historys;
		}
	}
	
	private History getDayHistory(Work work, LocalDate now) {
		StringBuffer buffer = new StringBuffer("select * from history ");
		buffer.append(" where  date_format(fromtime,'%Y-%m-%d') = ? ");
		buffer.append(" and workid = ?");
		History find = dao.findFirst(buffer.toString(), now.toString(), work.get("workid"));
		return find == null ? new History() : find;
	}
}
