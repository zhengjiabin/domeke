package com.domeke.app.model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Homepage extends Model<Homepage>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<Homepage> findHomepagesByStatusRank(String status){
		String sql = "select * from homepage order by rank";
		if(!StrKit.isBlank(status)){
			sql = "select * from homepage where status="+status+" order by rank desc";
		}
		List<Homepage> homepages = this.find(sql);
		return homepages;
	}
	public Page<Homepage> findHomepagesByStatusRank(String status, Integer pageNumber, Integer pageSize){
		String sql = "from homepage order by rank desc";
		if(!StrKit.isBlank(status)){
			sql = "from homepage where status="+status+" order by rank desc";
		}
		Page<Homepage> homepages = this.paginate(pageNumber, pageSize, "select *", sql);
		return homepages;
	}
	
	public Integer getMaxRank(){
		Integer result = 0;
		String sql = "select * from homepage order by rank desc";
		Homepage homepage = this.findFirst(sql);
		if(homepage.isNotEmpty()){
			result = Integer.parseInt(String.valueOf(homepage.get("rank")));
		}
		return result;
	}
}
