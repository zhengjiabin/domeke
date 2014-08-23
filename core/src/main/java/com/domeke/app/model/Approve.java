/**
 * 
 */
package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
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
}
