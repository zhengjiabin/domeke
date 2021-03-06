package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "orders", pkName = "orderid")
public class Orders extends Model<Orders>{
	private static Orders orders = new Orders();
	
	public Page<Orders> getAllOrders(int pageNumber,int pageSize){
		Page<Orders> page = this.paginate(pageNumber, pageSize, "select o.* ", "from orders o ,order_detail od where o.orderid=od.orderid order by creattime desc");
		return page;
	}
	public Page<Orders> getOrdersByUserId(int pageNumber,int pageSize,Long userId){
		Page<Orders> page = this.paginate(pageNumber, pageSize, "select o.* ", "from orders o ,order_detail od where o.orderid=od.orderid and userid = " + userId + " order by creattime desc");
		return page;
	}
	public void updateIsreceipt(String orderId, String flag) {
		String sql="update orders set isreceipt='" + flag + "' where orderid = "+orderId;
		Db.update(sql);
	}
}
