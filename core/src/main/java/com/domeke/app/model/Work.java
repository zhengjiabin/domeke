package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Model;

/**
 * 动漫作品的子表
 * 
 * @author chenhailin
 *
 */
@TableBind(tableName = "work", pkName = "workid")
public class Work extends Model<Work> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7086461795232488629L;

	/**
	 * 获取一部动漫的集数
	 * 
	 * @return
	 */
	public List<Work> getWorkByWorksID(Object worksid) {
		String querySql = "select * from work where worksid=? order by worknum asc";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null ? Lists.newArrayList() : worklist;
	}

}
