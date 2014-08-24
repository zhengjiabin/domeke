/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Approve;
import com.domeke.app.model.Goods;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;

/**
 * @author Administrator
 *
 */
public class ApproveController extends Controller {
	private static Goods goods = new Goods();
	public void save(){
		System.err.println("********************************************************");
		System.out.println(getPara("status"));
		Approve app = getModel(Approve.class);
		app.saveApprove();
		render("/demo/approve.html");
	}
	public void index(){
		render("/demo/approve.html");
	}
	/**
	 * 更新审批
	 */
	public void update() {
		Approve app  = getModel(Approve.class);
		app.update();
		goApprove();
	}
	/**
	 * 跳转审批修改页面
	 */
	public void goApprove() {
		query();
		render("/demo/approveList.html");
	}
	
	/**
	 * 查询出所有审批
	 */
	public void query() {
		Approve approve = getModel(Approve.class);
		List<Approve> approveList = approve.getApprove();
		this.setAttr("approveList", approveList);
	}
	/**
	 * 根据ID获取审批信息
	 */
	public void getApproveById() {
		Approve app = getModel(Approve.class);
		Approve approve = app.findById(getParaToInt());
		setAttr("approve", approve);
		render("/demo/approveupdate.html");
	}
	
	/**
	 * 删除审批信息
	 */
	public void delete() {
		Approve app = getModel(Approve.class);
		app.deleteById(getParaToInt());
		goApprove();
	}
	
	/**
	 * 获取商品的详细信息
	 */
	public void getGoods(){
		Approve app = getModel(Approve.class);
		List<Goods> goodsList = app.getGoods();
		setAttr("goodsList", goodsList);
	}
	
	/**
	 * 跳转商品审核页面
	 */
	public void goGoods(){
		getGoods();
		render("/demo/approveGoods.html");
	}
	
	/**
	 * 更改商品记录
	 */
	public void upGoods(){
		Approve app = getModel(Approve.class);
		int paramId = getParaToInt();
		String type = getPara("type");
		app.approveGoods(paramId, type);
		save(paramId,null);
		goGoods();
	}
	/**
	 * 跳转审批
	 */
	public void approveNoPass(){
		Approve app = getModel(Approve.class);
		int paramId = getParaToInt();
		String type = getPara("type");
		app.approveGoods(paramId, type);
		String remark = getPara("remark");
		save(paramId,remark);
		renderHtml("<html><body onload=\"alert('审批成功!');window.opener.location.reload();window.close()\"></body></html>");
	}
	/**
	 * 跳转审批不通过
	 */
	public void goApproveNoPass(){
		String id = getPara("id");
		goods = goods.findById(id);
		setAttr("goods", goods);
		render("/demo/approveNoPass.html");
	}
	
	/**
	 * 保存审批记录
	 * @param id
	 * @param remark
	 */
	public void save(int id,String remark){
		goods = goods.findById(id);
		Approve app = new Approve();
		User user = getSessionAttr("user");
		app.set("userid", goods.get("creater"));
		app.set("typeid", "商品");
		app.set("detailid", goods.get("goodsid"));
		app.set("status", goods.get("status"));
		app.set("remark", remark);
		app.save();
	}
}
