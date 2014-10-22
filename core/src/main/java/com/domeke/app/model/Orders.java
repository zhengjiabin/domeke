package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "orders", pkName = "orderid")
public class Orders extends Model<Orders>{
	private static Orders orders = new Orders();
	
	public List<Orders> getAllOrders(){
		String sql = "select * from orders";
		List<Orders> ordersList = orders.find(sql);
		return ordersList;
	}
	
}
