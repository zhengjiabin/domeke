package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "community", pkName = "communityid")
public class Community extends Model<Community> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Community dao = new Community();

}
