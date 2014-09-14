package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 我的作品model
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
	 * 根据商品类型返回商品信息
	 * 
	 * @param worksType
	 *            商品类型
	 * @return 返回对应类型的商品信息
	 */
	public List<Works> getWorksInfoByTypePage(String worksType,Integer pageIndex,Integer pageSize) {
		List<Works> workslist = null;
		if(!StrKit.isBlank(worksType)){
			workslist = this.paginate(pageIndex, pageSize, "select *", "from works where workstype = ?",worksType).getList();
		}
		return workslist == null ? Lists.newArrayList() : workslist;
	}
	
	/**
	 * 
	 * @param worksName
	 * @return
	 */
	public List<Works> getWorksInfoByName(String worksName) {
		String querySql = "select * from works where worksname like '"
				+ worksName + "'";
		List<Works> worklist = this.find(querySql);
		return worklist == null ? Lists.newArrayList() : worklist;
	}
}
