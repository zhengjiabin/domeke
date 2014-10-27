package com.domeke.app.model;

import java.time.LocalDate;

import com.domeke.app.utils.TimeKit;
import com.jfinal.plugin.activerecord.Model;

public class History extends Model<History> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4311228884712502862L;
	public static History dao = new History();

	public void saveOrUpdateHitory(Work work) {
		History history = new History();
		history.set("workid", work.get("workid"));
		history.set("workname", work.get("workname"));
		history.set("cover", work.get("cover"));
		history.set("comic", work.get("comic"));
		LocalDate now = LocalDate.now();
		LocalDate weekEnd = TimeKit.getCurrernWeekEnd();
		LocalDate weekStart = TimeKit.getCurrernWeekStart();
		if (TimeKit.isBetween(now, weekStart, weekEnd)) {
			history = getWeekHistory(work, weekStart, weekEnd);
			if (history != null && history.getLong("count") > 0) {
				Long count = history.getLong("count") + 1;
				history.set("count", count);
				history.update();
			}
		} else {
			history.set("count", 1);
			history.set("fromtime", weekStart);
			history.set("totime", weekEnd);
			history.save();
		}
	}

	private History getWeekHistory(Work work, LocalDate weekStart, LocalDate weekEnd) {
		StringBuffer buffer = new StringBuffer("select *  from history  ");
		buffer.append(" where fromtime <= to_date(?,'yyyy-MM-dd') and totime >= to_date(?,'yyyy-MM-dd') ");
		buffer.append(" and workid = ?");
		History find = dao.findFirst(buffer.toString(), weekStart.toString(), weekEnd.toString(), work.get("workid"));
		return find;
	}
}
