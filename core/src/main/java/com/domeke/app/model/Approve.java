/**
 * 
 */
package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 *
 */
@TableBind(tableName = "approve", pkName = "approveid")
public class Approve extends Model<Approve> {
	public static Approve dao = new Approve();
	public List<Approve> getApprove(){
		String sql = "select * from approve";
		List<Approve> app = dao.find(sql);
		return app;
	}
	public boolean updateApprove(){
		this.update();
		return true;
	}
	public boolean saveApprove(){
		return this.save();
	}
	
	public List<Goods> getGoods(){
		String sql ="select * from goods where status != '40'";
		Goods gooddao = new Goods();
		List<Goods> goods = gooddao.find(sql);
		return goods;
	}
	public void approveGoods(int id,String type){
		Goods gooddao = new Goods();
		gooddao =gooddao.findById(id);
		if("Y".equals(type)){
			String sql = "update goods set status='10' where goodsid = '"+id+"'";
			Db.update(sql);
		}else if("N".equals(type)){
			String sql = "update goods set status='20' where goodsid = '"+id+"'";
			Db.update(sql);
		}else if("D".equals(type)){
			String sql = "update goods set status='40' where goodsid = '"+id+"'";
			Db.update(sql);
		}
	}
}
