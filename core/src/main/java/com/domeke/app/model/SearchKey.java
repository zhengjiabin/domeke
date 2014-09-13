package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

@TableBind(tableName = "search_key", pkName = "keyid")
public class SearchKey extends Model<SearchKey>{
	public static SearchKey searchdao =new SearchKey();
	/**
	 * 新增搜索关键字
	 */
	public void saveSearchKey(){
		this.save();
	}
    public void removeCache(){
        CacheKit.removeAll("searchKey");
        CacheKit.removeAll("searchKeyList");
    }
	/**
	 * 查询关键字
	 * @return 所有关键字
	 */
	public List<SearchKey> getVentWall(){
		List<SearchKey> searchKeyList = searchdao.findByCache("searchKeyList", "key", "SELECT * FROM SEARCH_KEY");
		return searchKeyList;	
	}
	/**
	 * 删除关键字
	 * @param 关键字ID
	 */
	public void deleteSearchKey(int keyid){
		searchdao.deleteById(keyid);
	}
	/**
	 * 更新关键字
	 * @param 关键字ID
	 */
	public void updateSearchKey(int keyid){
		//searchdao.findById(keyid).setAttrs();
	}
}
