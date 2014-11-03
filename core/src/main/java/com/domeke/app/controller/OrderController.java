package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Goods;
import com.domeke.app.model.OrderDetail;
import com.domeke.app.model.Orders;
import com.domeke.app.model.User;
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
		for(int i=0;i<ordersList.size();i++){
			String isDelivery = ordersList.get(i).getStr("isDelivery");
			if("Y".equals(isDelivery)){
				ordersList.get(i).set("isDelivery", "已发货");
			}else{
				ordersList.get(i).set("isDelivery", "未发货");
			}
		}
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
	public void sendOrder(){
		Orders orders = getModel(Orders.class);
		String ordid = getPara("ordid");
		orders = orders.findById(ordid);
		orders.set("isDelivery","Y");
		orders.update();
		renderOrder();	
	}
	
	/**
	 * 购物下单  
	 * @param goodsId 商品名称
	 * @param orders  订单
	 * @param orderdetail 订单详细
	 */
	public void byGoods(String goodsId,Orders orders,OrderDetail orderdetail){
		Goods goods = getAttr("goods");
		//Goods goods = getModel(Goods.class);
		goods = goods.findById(goodsId);
		//获取商品的豆豆兑换价格
		Long point = goods.getLong("point");
		//获取当前登录的用户
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		user = user.findById(userId);
		//获取用户当前的豆豆
		Long userPoint = user.getLong("point");
		if(point>userPoint){
			String orderMsg = "对不起你当请的豆豆总数不足";
			//页面处理
		}else{
			//减去用户用掉的豆豆
			Long p = userPoint-point;
			user.set("point", p);
			user.update();
			orders.save();
			orderdetail.save();
		}
		render("/shop.html");
		
	}
}
