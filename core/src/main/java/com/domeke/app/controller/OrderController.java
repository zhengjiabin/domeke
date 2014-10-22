package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.OrderDetail;
import com.domeke.app.model.Orders;
import com.jfinal.core.Controller;

public class OrderController extends Controller{
	public void renderOrder(){
		orderManage();
		render("/admin/admin_order.html");
	}
	
	public void orderManage(){
		Orders orders = getModel(Orders.class);
		List<Orders> ordersList = orders.getAllOrders();
		OrderDetail orderdetail = getModel(OrderDetail.class);
		List<OrderDetail> orderDetailList = orderdetail.getAllOrder(null, null);
		setAttr("ordList", ordersList);
		setAttr("ordetailList", orderDetailList);
	}
	
	public void deleteOrder(){
		Orders orders = getModel(Orders.class);
		OrderDetail orderdetail = getModel(OrderDetail.class);
		String orderid = getPara("orderid");
		orders.deleteById(orderid);
		orderdetail.deleteOrd(orderid);
		renderOrder();
	}
	
	public void upOrder(){
		Orders orders = getModel(Orders.class);
		OrderDetail orderdetail = getModel(OrderDetail.class);
		String ordelid = getPara("ordelid");
		String ordid = getPara("ordid");
		orderdetail = orderdetail.findById(ordelid);
		orders = orders.findById(ordid);
		setAttr("orders",orders);
		setAttr("orderdetail",orderdetail);
		render("/admin/admin_updateOrder.html");
	}
	public void updateOrder(){
		Orders orders = getModel(Orders.class);
		OrderDetail orderdetail = getModel(OrderDetail.class);
		orders.update();
		orderdetail.update();
		renderOrder();
	}
}
