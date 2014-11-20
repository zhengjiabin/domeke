package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "order_detail", pkName = "orderdetailid")
public class OrderDetail extends Model<OrderDetail>{
	private static OrderDetail order = new OrderDetail();
	
	public Page<OrderDetail> getAllOrder(Long orderid,String param,int pageNumber,int pageSize){
		Page<OrderDetail> page = this.paginate(pageNumber, pageSize, "select od.* ", "from orders o ,order_detail od where o.orderid=od.orderid order by creattime desc");
		return page;
	}
	public Page<OrderDetail> getOrdersByUserId(Long orderid,String param,int pageNumber,int pageSize,Long userId){
		Page<OrderDetail> page = this.paginate(pageNumber, pageSize, "select od.* ", "from orders o ,order_detail od where o.orderid=od.orderid and userid = " + userId + " order by creattime desc");
		return page;
	}
	
	public void deleteOrd(String ordId){
		String sql = "select orderdetailid from order_detail where orderid='"+ordId+"'";
		OrderDetail ord = order.findFirst(sql);
		order.deleteById(ord.getLong("orderdetailid"));
	}
}
