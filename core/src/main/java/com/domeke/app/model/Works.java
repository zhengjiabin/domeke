package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 动漫作品主表
 * 
 * @author chenhailin
 *
 */
@TableBind(tableName = "works", pkName = "worksid")
public class Works extends Model<Works> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 保存数据信息到数据库
	 */
	public void saveWorksInfo() {
		this.save();
	}

	/**
	 * 更新商品信息
	 */
	public void updateWorksInfo() {
		this.update();
	}
	
	public List<Works> loadMyUpload(Long userid){
		String sql="select * from works where creater="+userid+" ORDER BY releasedate DESC";
		List<Works> list = this.find(sql);
		return list;
	}
	/**
	 * 获取动漫页 8个轮播
	 * @param type
	 * @return
	 */
	public int getMaxHomePageValue(String type){
		String sql = "select * from works order by homepage desc  limit 1";
		if(!StrKit.isBlank(type)){
			sql = "select * from works where type="+type+" order by homepage desc limit 1";
		}
		Works works = this.findFirst(sql);
		return works.get("homepage");
	}
	
	/**
	 * 按类型查询   按istop 排名 desc
	 * @param type
	 * @return
	 */
	public int getMaxTopValue(String type){
		String sql = "select * from works order by istop desc  limit 1";
		if(!StrKit.isBlank(type)){
			sql = "select * from works where type="+type+" order by istop desc  limit 1";
		}
		Works works = this.findFirst(sql);
		return works.get("istop");
	}
	
//	/**
//	 * 获取所有信息
//	 * 
//	 * @return 返回所有商品信息
//	 */
//	public List<Works> queryAllWorksInfo() {
//		String querySql = "select * from works";
//		List<Works> workslist = this.find(querySql);
//		return workslist == null ? Lists.newArrayList() : workslist;
//	}
	
	/**
	 * 获取动漫首页轮播
	 * @param limit
	 * @return
	 */
	public List<Works> getHomePage(Integer limit){
		String querySql = "select * from works where homepage > 0 and ischeck = 1 and ispublic = 1 order by homepage desc limit "+limit;
		List<Works> workss = this.find(querySql);
		return workss;
	}
	
	/**
	 * 分页获取动漫首页轮播
	 * @param workstype
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Works> getHomePage(String workstype,Integer pageNumber,Integer pageSize){
		String querySql = "from works where homepage > 0 order by homepage desc";
		if(!StrKit.isBlank(workstype)){
			querySql = "from works where homepage > 0 and workstype = "+workstype+" order by homepage desc";
		}
		Page<Works> workss = this.paginate(pageNumber, pageSize, "select *", querySql);
		return workss;
	}
	
	/**
	 * 获取还未审核的works
	 * @param workstype
	 * @param type
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Works> getWorksNotCheck(String workstype,String type,Integer pageNumber,Integer pageSize){
		try {
			StringBuffer querySql = new StringBuffer();
			querySql.append("from work t1 left join works t2 on t2.worksid = t1.worksid where t1.status = 20");
			if(!StrKit.isBlank(workstype)){
				querySql.append(" and t2.workstype = " + workstype);
			}
			if(!StrKit.isBlank(type)){
				querySql.append(" and t2.type = " + type);
			}
			querySql.append(" order by t2.releasedate desc");
			Page<Works> workss = this.paginate(pageNumber, pageSize, "select DISTINCT t2.*", querySql.toString());
			return workss;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据商品类型返回商品信息
	 * 
	 * @param worksType
	 *            商品类型
	 * @return 返回对应类型的商品信息
	 */
	public List<Works> getWorksInfoByType(String worksType) {
		String querySql = "select * from works where workstype=?";
		List<Works> workslist = this.find(querySql, worksType);
		return workslist == null ? Lists.newArrayList() : workslist;
	}
	
	/**
	 * 根据动漫作品类型返信息 限制条数
	 * 
	 * @param worksType
	 *            作品类型
	 * @return 返回对应类型的作品信息
	 */
	public List<Works> getWorksInfoByType(String worksType,Integer limit) {
		String querySql = "select * from works where workstype=? and ischeck = 1 and ispublic = 1 and maxnum > 0 order by releasedate desc,istop desc limit "+limit;
		List<Works> workslist = this.find(querySql, worksType);
		return workslist == null ? Lists.newArrayList() : workslist;
	}

	/**
	 * 前台， 获取Works信息  需要 确认审核、确认公开
	 * @param worksType
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Works> getWorksInfoByTypePage(String worksType, Integer pageNumber, Integer pageSize) {
		Page<Works> workslist = null;
		if (!StrKit.isBlank(worksType)) {
			workslist = this.paginate(pageNumber, pageSize, "select *", "from works where ischeck = 1 and ispublic = 1 and workstype = ? order by releasedate desc,istop desc", worksType);
		}
		return workslist;
	}

//	/**
//	 * 
//	 * @param worksName
//	 * @return
//	 */
//	public List<Works> getWorksInfoByName(String worksName) {
//		String querySql = "select * from works where worksname like '" + worksName + "'";
//		List<Works> worklist = this.find(querySql);
//		return worklist == null ? Lists.newArrayList() : worklist;
//	}

	/**
	 * 后台， 获取所以Works信息  不判断审核、公开
	 * @param worksType
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Works> getWorksInfoPage(String worksType, Integer pageNum, Integer pageSize) {
		Page<Works> workslist = null;
		if (!StrKit.isBlank(worksType)) {
			workslist = this.paginate(pageNum, pageSize, "select *", "from works where workstype = ? order by releasedate desc", worksType);
		}else {
			workslist = this.paginate(pageNum, pageSize, "select *", "from works order by releasedate desc");
		}
		return workslist;
	}
	
	/**
	 * 后台 按类型查询
	 * @param worksType 动漫类型
	 * @param pageNum 第几页
	 * @param pageSize 一页多少记录
	 * @return 
	 */
	public Page<Works> getWorksInfoPage(String worksType, String type, Integer pageNum, Integer pageSize) {
		try {
			StringBuffer querySql = new StringBuffer();
			querySql.append("from work t1 left join works t2 on t2.worksid = t1.worksid where t1.status = 30");
			if(!StrKit.isBlank(worksType)){
				querySql.append(" and t2.workstype = " + worksType);
			}
			if(!StrKit.isBlank(type)){
				querySql.append(" and t2.type = " + type);
			}
			querySql.append(" order by t2.releasedate desc");
			Page<Works> workss = this.paginate(pageNum, pageSize, "select DISTINCT t2.*", querySql.toString());
			return workss;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 个人中心 按用户查询，自能看见自己上传的
	 * @param worksType
	 * @param type
	 * @param creater
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Works> getWorksInfoPage(String worksType, String type, String creater, Integer pageNum, Integer pageSize) {
		Page<Works> workslist = null;
		String form = "from works where 1=1";
		if (!StrKit.isBlank(worksType)) {
			form = form + " and workstype = "+worksType;
		}
		if (!StrKit.isBlank(type)) {
			form = form + " and type = "+type;
		}
		if(!StrKit.isBlank(creater)){
			form = form + " and creater = "+creater;
		}
		form = form + " order by releasedate desc";
		workslist = this.paginate(pageNum, pageSize, "select *", form);
		return workslist;
	}
	
	/**
	 * 上周点击击排名
	 * @param limit
	 * @return
	 */
	public List<Works> getlastWeekByPageViewsLimit(Integer limit){
		List<Works> workss = null;
		workss = this.find("select * from works where ischeck = 1 and ispublic = 1 order by pageviews desc limit ?", limit);
		return workss;
	}
	
	/**
	 * 最近更新
	 * @return
	 */
	public List<Works> getWorksInfoByUpdateLimit(Integer limit){
		List<Works> workss = null;
		workss = this.find("select * from works where ischeck = 1 and ispublic = 1 order by updatetime desc limit ?", limit);
		return workss;
	}

	/**
	 * 查询最新的作品
	 * 
	 * @param limit
	 *            限制前几个
	 * @return 返回精彩推荐的动漫作品
	 */
	public List<Works> getNewestWorks(Object worksid,Integer type, Integer limit) {
		List<Works> workss = null;
		String querySql = "select * from works where worksid <> ? and type = ? and ischeck = 1 and ispublic = 1 order by releasedate desc limit ?";
		workss = this.find(querySql, worksid, type, limit);
		return workss;
	}

	/**
	 * 根据动漫作品的ID，更新该作品的点赞数<br>
	 * 
	 * @param worksid
	 *            动漫作品的ID
	 */
	public void addPraise(Object worksid) {
		String updatePariseSql = "update works set praise = praise + 1 where worksid = ?";
		Db.update(updatePariseSql, worksid);
	}

}
