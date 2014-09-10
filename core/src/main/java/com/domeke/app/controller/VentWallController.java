/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.domeke.app.model.Menu;
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
		String msg = ventWall.getStr("moodid");
		//String msg = ventWall.getStr("message");
		msg = getImg(msg);
		ventWall.set("moodid", msg);
		ventWall.saveVentWall();
		select();
		//renderJson();
		//index();
	}
	/**
	 * 正则表达式转换表情
	 * @param msg
	 * @return
	 */
	private String getImg(String msg) {
		Pattern p=Pattern.compile("\\[em_(\\d+)\\]");
		Matcher matcher=p.matcher(msg);
		while(matcher.find()){
			String imgNo=matcher.group(1);
			msg = msg.replace("[em_"+imgNo+"]","<img src='images/face/"+imgNo +".jpg'/>");
		}
		return msg;
	}
	
	public void index() {
		
		//render("/demo/ventwall.html");
		Menu.menuDao.removeCache();
		Menu menu = getModel(Menu.class);
		List<Menu> menuOneMenu = menu.getOneMenu();		
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);
		setAttr("menuOneMenu", menuOneMenu);
		render("/demo/addVentWall.html");
	}
	/**
	 * 查询留言
	 */
	public void select(){
		VentWall.venWdao.removeCache();
		setAttr("ventWallList", VentWall.venWdao.getVentWall());
		//List<VentWall> ventWallList = VentWall.venWdao.getVentWall();
		//render("/demo/selectventwall.html");
		
		renderJson("/demo/selectventwall.html");
		//renderJson("ventWallList",ventWallList);
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
	 * 更新留言
	 */
	public void updateById(){
		VentWall ventWall = getModel(VentWall.class);
		Long ventwallid = ventWall.get("ventwallid");
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
