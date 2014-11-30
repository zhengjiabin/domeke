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
	
	public static Work dao = new Work();

	/**
	 * 
	 */
	private static final long serialVersionUID = -7086461795232488629L;
	
	/**
	 * 获取未压缩视频
	 * @return 
	 */
	public List<Work> getWorkNotHandled() {
		String sql = "select * from work where status='10' order by createtime ";
		List<Work> workList = this.find(sql);
		return workList;
	}

	/**
	 * 获取所有审核过的 和公开的视频
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的集数
	 */
	public List<Work> getWorkByWorksID(Object worksid) {
		String querySql = "select * from work where worksid=? and status = '30' and isdisable = 1 order by worknum asc";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null ? Lists.newArrayList() : worklist;
	}

	/**
	 * 获取所有审核过的 和公开的视频
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的集数
	 */
	public List<Work> getWorkByWorksIdCheck(Object worksid) {
		String querySql = "select * from work where worksid=? and status = '30' and isdisable = 1 order by worknum asc";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null ? Lists.newArrayList() : worklist;
	}
	/**
	 * 获取  视频 分页
	 * 
	 * @param worksid
	 *            动漫作品的主键值
	 * @return 某一部动漫的集数
	 */
	public Page<Work> getWorkByWorksID(Integer worksid,Integer pageNumber,Integer pageSize) {
		Page<Work> pageWorks = this.paginate(pageNumber, pageSize, "select *", "from work where worksid=? order by worknum asc", worksid);
		return pageWorks;
	}
	
	/**
	 * 获取未审核的视频
	 * @return
	 */
	public List<Work> getNotCheckWork(Object worksid){
		String querySql = "select * from work where status = '20' and worksid = "+worksid+" order by worknum asc";
		return this.find(querySql);
	}
	
	/**
	 * 获取编号最小的
	 * @param worksid
	 * @return
	 */
	public Work getFirstWork(Object worksid) {
		String querySql = "select * from work where worksid=? order by worknum asc limit 1";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null || worklist.size() == 0 ? this : worklist.get(0);
	}
	
	/**
	 * 获取编号最大的
	 * @param worksid
	 * @return
	 */
	public Work getMaxWork(Object worksid){
		String querySql = "select * from work where worksid=? order by worknum desc limit 1";
		List<Work> worklist = this.find(querySql, worksid);
		return worklist == null || worklist.size() == 0 ? this : worklist.get(0);
	}
	
	public Work getWorkByWorkNum(Object worksid,Object worknum){
		Work workModel = this.findFirst("select * from work where worksid = ? and worknum = ?",worksid,worknum);
		return workModel;
	}
	public List<Integer> getLackNum(Object worksid){
		List<Integer> nums = Lists.newArrayList();
		Work maxWork = this.getMaxWork(worksid);
		Integer endNum = maxWork.getInt("worknum");
		if(endNum == null){
			endNum = 0;
		}
		for(int i = 1; i < endNum; i++){
			Work workModel = maxWork.getWorkByWorkNum(worksid, i);
			if(!workModel.isNotEmpty()){
				//如果有编号缺失 把编号记住
				nums.add(i);
			}
		}
		nums.add(endNum + 1);
		return nums;
	}
	public Work getUpWork(Object worksid,Object worknum){
		Work workModel = this.findFirst("select * from work where worksid = ? and worknum < ? order by worknum desc limit 1",worksid,worknum);
		return workModel;
	}
	public Work getDownWork(Object worksid,Object worknum){
		Work workModel = this.findFirst("select * from work where worksid = ? and worknum > ? order by worknum asc limit 1",worksid,worknum);
		return workModel;
	}
}
