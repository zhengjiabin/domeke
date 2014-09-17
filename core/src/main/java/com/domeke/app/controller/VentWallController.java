/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

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
		msg = getImg(msg);
		ventWall.set("moodid", msg);	
		ventWall.saveVentWall();
		select();
	}
	/**
	 * 正则表达式转换表情
	 * @param msg
	 * @return 已转换格式
	 */
	private String getImg(String msg) {
		Pattern p=Pattern.compile("\\[em_(\\d+)\\]");
		Matcher matcher=p.matcher(msg);
		while(matcher.find()){
			String imgNo=matcher.group(1);
			msg = msg.replace("[em_"+imgNo+"]",imgNo +".jpg");
		}
		return msg;
	}
	
	public void index() {			
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);	
		select();
		render("/demo/addVentWall.html");
	}
	/**
	 * 查询留言
	 */
	public void select(){
		selectUtil();		
		getCount();
		isSignIn();
		render("/demo/ventwall.html");
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
		selectUtil();
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 删除留言
	 */
	public void delete(){
		int ventwallid = getParaToInt();
		VentWall.venWdao.deleteVentWall(ventwallid);
		selectUtil();
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 统计签到
	 */
	public void getCount(){
		VentWall ventWall = getModel(VentWall.class);
		Object todayCount = ventWall.getTodayCount();
		Object yesterdayCount = ventWall.getYesterdayCount();
		Object totalCount = ventWall.getTotalCount();
		setAttr("todayCount", todayCount);
		setAttr("yesterdayCount", yesterdayCount);
		setAttr("totalCount", totalCount);
	}
	/**
	 * 是否签到
	 */
	public void isSignIn(){
		String issignin = VentWall.venWdao.isSignIn(1);
		setAttr("issignin", issignin);
	}
	public void goToManager(){
		selectUtil();		
		render("/admin/admin_ventWall.html");
	}
	private void selectUtil() {
		VentWall.venWdao.removeCache();		
		List<VentWall> ventWallList = VentWall.venWdao.getVentWall();
		setAttr("ventWallList", ventWallList);
	}
	/**
	 * 分页查询指定社区的活动
	 * 
	 * @return 活动信息
	 */
	public void find() {
		//Object ventwallid = getPara("ventwallid");
		setActivityPage();	
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 分页查询指定社区模块的活动
	 * @param communityId
	 */
	public void setActivityPage(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 2);
		Page<VentWall> activityPage = VentWall.venWdao.findPage(pageNumber,pageSize);
		setAttr("activityPage", activityPage);
	}
}
