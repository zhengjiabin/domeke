/**
 * 
 */
package com.domeke.app.controller;

import java.sql.Timestamp;

import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

/**
 * @author zhouying
 *
 */
@ControllerBind(controllerKey="ventwall")
public class VentWallController extends Controller {

	/**
	 * 新增留言记录
	 */
	public void save(){
		VentWall ventWall = getModel(VentWall.class);
//		Timestamp nowdate = new Timestamp(System.currentTimeMillis());
//		ventWall.set("createtime", nowdate);
		ventWall.saveVentWall();
		render("/demo/selectventwall.html");
	}
	
	public void index() {
		render("/demo/ventwall.html");
	}
	/**
	 * 查询留言
	 */
	public void select(){
		VentWall.venWdao.removeCache();
		setAttr("ventWallList", VentWall.venWdao.getVentWall());
		render("/demo/selectventwall.html");
	}
	/**
	 * 通过ID查询留言
	 */
	public void selectById(){
		int ventwallid = getParaToInt();
		VentWall ventWall = VentWall.venWdao.findById(ventwallid);
		setAttr("ventWall", ventWall);
		render("/demo/ediventwall.html");
	}
	/**
	 * 删除留言
	 */
	public void uodateById(){
		VentWall ventWall = getModel(VentWall.class);
		int ventwallid = ventWall.getInt("VentWallId");
		VentWall.venWdao.findById(ventwallid).setAttrs(ventWall).update();
		select();
	}
	/**
	 * 删除留言
	 */
	public void delete(){
		int ventwallid = getParaToInt();
		VentWall.venWdao.deleteVentWall(ventwallid);
		select();
	}
}
