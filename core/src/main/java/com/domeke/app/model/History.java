package com.domeke.app.model;

import java.time.LocalDate;

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
		history.set("worksid", work.get("worksid"));
		history.set("workname", work.get("workname"));
		history.set("cover", work.get("cover"));
		history.set("comic", work.get("comic"));
		LocalDate now = LocalDate.now();

		Works dao = new Works();
		Works works = dao.findById(work.get("worksid"));
		works.set("pageviews", works.getLong("pageviews") + 1);
		works.update();
		history = getWeekHistory(work, now);
		if (history != null && history.getLong("count") > 0) {
			Long count = history.getLong("count") + 1;
			history.set("count", count);
			history.update();
		} else {
			history.set("count", 1);
			history.save();
		}
	}

	private History getWeekHistory(Work work, LocalDate now) {
		StringBuffer buffer = new StringBuffer("select *  from history  ");
		buffer.append(" where fromtime <= str_to_date(?,'%Y-%m-%d') and totime >= str_to_date(?,'%Y-%m-%d') ");
		buffer.append(" and workid = ?");
		History find = dao.findFirst(buffer.toString(), now.toString(), now.toString(), work.get("workid"));
		return find;
	}
}
