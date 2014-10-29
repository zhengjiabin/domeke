package com.domeke.app.model;

import java.time.LocalDate;
import java.util.Date;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

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
		if (works.getLong("pageviews") != null) {
			pageviews = works.getLong("pageviews");
		}
		works.set("pageviews", pageviews + 1);
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

	private History getDayHistory(Work work, LocalDate now) {
		StringBuffer buffer = new StringBuffer("select *  from history  ");
		buffer.append(" where  date_format(fromtime,'%Y-%m-%d') = ? ");
		buffer.append(" and workid = ?");
		History find = dao.findFirst(buffer.toString(), now.toString(), work.get("workid"));
		return find == null ? new History() : find;
	}
}
