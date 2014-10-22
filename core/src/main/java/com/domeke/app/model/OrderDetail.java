package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "order_detail", pkName = "orderdetailid")
public class OrderDetail extends Model<OrderDetail>{
	private static OrderDetail order = new OrderDetail();
	
	public List<OrderDetail> getAllOrder(Long orderid,String param){
		String sql = "select * from order_detail";
		List<OrderDetail> orderList = order.find(sql);
		return orderList;
	}
	
	public void deleteOrd(String ordId){
		String sql = "select orderdetailid from order_detail where orderid='"+ordId+"'";
		OrderDetail ord = order.findFirst(sql);
		order.deleteById(ord.getLong("orderdetailid"));
	}
}
