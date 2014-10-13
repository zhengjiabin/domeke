package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

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
	 * 获取某一部动漫的集数
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的集数
	 */
	public List<Work> getWorkByWorksID(Object worksid) {
		String querySql = "select * from work where worksid=? order by worknum asc";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null ? Lists.newArrayList() : worklist;
	}

	/**
	 * 获取某一部动漫的集数
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的集数
	 */
	public Page<Work> getWorkByWorksID(Integer worksid,Integer pageNumber,Integer pageSize) {
		Page<Work> pageWorks = this.paginate(pageNumber, pageSize, "select * ", "from work where worksid=? order by worknum asc", worksid);
		return pageWorks;
	}
	
	/**
	 * 获取某一部动漫作品的第一集
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的第一集
	 */
	public Work getFirstWork(Object worksid) {
		String querySql = "select * from work where worksid=? and worknum=1";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null || worklist.size() == 0 ? this : worklist.get(0);
	}

}
