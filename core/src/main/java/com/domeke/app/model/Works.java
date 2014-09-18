package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
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
	private static final long serialVersionUID = -6887430065087508156L;

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

	/**
	 * 获取所有商品信息
	 * 
	 * @return 返回所有商品信息
	 */
	public List<Works> queryAllWorksInfo() {
		String querySql = "select * from works";
		List<Works> workslist = this.find(querySql);
		return workslist == null ? Lists.newArrayList() : workslist;
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
		String querySql = "select * from works where workstype=? order by istop limit "+limit;
		List<Works> workslist = this.find(querySql, worksType);
		return workslist == null ? Lists.newArrayList() : workslist;
	}

	
	
	/**
	 * 根据商品类型返回商品信息
	 * 
	 * @param worksType
	 *            商品类型
	 * @return 返回对应类型的商品信息
	 */
	public List<Works> getWorksInfoByTypePage(String worksType, Integer pageIndex, Integer pageSize) {
		List<Works> workslist = null;
		if (!StrKit.isBlank(worksType)) {
			workslist = this.paginate(pageIndex, pageSize, "select *", "from works where workstype = ?", worksType)
					.getList();
		}
		return workslist;
	}

	/**
	 * 
	 * @param worksName
	 * @return
	 */
	public List<Works> getWorksInfoByName(String worksName) {
		String querySql = "select * from works where worksname like '" + worksName + "'";
		List<Works> worklist = this.find(querySql);
		return worklist == null ? Lists.newArrayList() : worklist;
	}

	/**
	 * 分页
	 * @param worksType 动漫类型
	 * @param pageNum 第几页
	 * @param pageSize 一页多少记录
	 * @return 
	 */
	public Page<Works> getWorksInfoPage(String worksType, Integer pageNum, Integer pageSize) {
		Page<Works> workslist = null;
		if (!StrKit.isBlank(worksType)) {
			workslist = this.paginate(pageNum, pageSize, "select *", "from works where workstype = ?", worksType);
		}
		return workslist;
	}
	
	/**
	 * 按点击排名
	 * @param limit
	 * @return
	 */
	public List<Works> getWorksInfoByPageViewsLimit(Integer limit){
		List<Works> workss = null;
		workss = this.find("select * from works order by pageviews desc limit ?", limit);
		return workss;
	}
	/**
	 * 最近更新
	 * @return
	 */
	public List<Works> getWorksInfoByUpdateLimit(Integer limit){
		List<Works> workss = null;
		workss = this.find("select * from works order by updatetime desc limit ?", limit);
		return workss;
	}

	/**
	 * 根据点赞、收藏、浏览次数、评论次数优先级顺序排序
	 * 
	 * @param limit
	 *            限制前几个
	 * @return 返回精彩推荐的动漫作品
	 */
	public List<Works> getWorksByStatistics(Integer limit) {
		List<Works> workss = null;
		String querySql = "select * from works order by parise, collection, pageviews, comment desc limit ?";
		workss = this.find(querySql, limit);
		return workss;
	}
}
