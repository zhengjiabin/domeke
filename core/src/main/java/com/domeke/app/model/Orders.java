package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "orders", pkName = "orderid")
public class Orders extends Model<Orders>{
	private static Orders orders = new Orders();
	
	public Page<Orders> getAllOrders(int pageNumber,int pageSize){
		Page<Orders> page = this.paginate(pageNumber, pageSize, "select * ", "from orders order by creattime desc");
		return page;
	}
	
}
