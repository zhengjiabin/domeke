/**
 * 
 */
package com.domeke.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.Approve;
import com.jfinal.core.Controller;

/**
 * @author Administrator
 *
 */
public class ApproveController extends Controller {
	
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
}
