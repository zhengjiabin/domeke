package com.domeke.app.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.User;
import com.domeke.app.model.VentWall;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.domeke.app.utils.GradeKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;


/**
 * @author zhouying
 *
 */
@ControllerBind(controllerKey="ventwall")
@Before(LoginInterceptor.class)
public class VentWallController extends Controller {
	public static int pageTest=0;
	/**
	 * 新增留言记录
	 */
	public void save(){
		VentWall ventWall = getModel(VentWall.class);	
		ventWall.set("creater", 1);
		Long userId = getUser();
		ventWall.set("userid", userId);
		ventWall.saveVentWall();
		select();
		render("/VentWallDtl.html");
	}
	public void index() {			
		int menuid = getParaToInt("menuid");
		setAttr("menuid", menuid);	
		select();
		render("/VentWall.html");
	}
	/**
	 * 查询留言
	 */
	public void select(){
		selectUtil();		
		getCount();
		isSignIn();
		getNowTime();
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
		int num = getParaToInt();
		VentWall.venWdao.findById(ventwallid).setAttrs(ventWall).update();
		setActivityPage(num);
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 删除留言
	 */
	public void delete(){
		VentWall ventWall = getModel(VentWall.class);
		Long ventwallid = ventWall.get("ventwallid");
		VentWall.venWdao.deleteVentWall(ventwallid);
		int num = getParaToInt();
		setActivityPage(num);
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 统计签到
	 */
	public void getCount(){
		Long userId = getUser();;
		VentWall ventWall = getModel(VentWall.class);
		Object todayCount = ventWall.getTodayCount();
		Object yesterdayCount = ventWall.getYesterdayCount();
		Object totalCount = ventWall.getTotalCount();
		Object userIdCount = ventWall.getUserIdCount(userId);
		Object monthCount = ventWall.getMonthCount(userId);
		Timestamp createtime = ventWall.getCreatetime(userId);
		String time = "";
		if (createtime != null){
			Date date =new Date();
			date = createtime;
		
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			time = sdf.format(date);
		}
		getGrade(userId);
		setAttr("todayCount", todayCount);
		setAttr("yesterdayCount", yesterdayCount);
		setAttr("totalCount", totalCount);
		setAttr("userIdCount", userIdCount);
		setAttr("monthCount", monthCount);
		setAttr("createtime", time);
	}
	/**
	 * 是否签到
	 */
	public void isSignIn(){
		User user = getSessionAttr("user");
		Long userId = user.get("userid");
		String userName = user.getStr("username");
		String issignin = VentWall.venWdao.isSignIn(userId);
		setAttr("issignin", issignin);
		setAttr("userName", userName);
	}
	private Long getUser() {
		User user = getSessionAttr("user");
		Long userId = user.get("userid");
		return userId;
	}
	/**
	 * 签到墙管理
	 */
	public void goToManager(){
		setActivityPage(0);
		render("/admin/admin_ventWall.html");
	}
	/**
	 * 查询所以签到墙工具方法
	 */
	private void selectUtil() {
		VentWall.venWdao.removeCache();		
		List<VentWall> ventWallList = VentWall.venWdao.getVentWall();
		List<User> userList = new ArrayList<User>();
		User user = getModel(User.class);
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
		String createTime = "";
		boolean isUser = true;
		String test = "";
		for (VentWall vent : ventWallList){
			isUser = user.isUser(vent.get("userid"));
			if (!isUser){
				continue;
			}
			user = user.findById(vent.get("userid"));
			createTime = sdf.format(vent.get("createtime"));
			vent.set("createtime", createTime);
			userList.add(user);
		}
		setAttr("ventWallList", ventWallList);
		setAttr("userList", userList);
	}
	/**
	 * 分页查询签到墙
	 */
	public void find() {
		setActivityPage(0);	
		render("/admin/admin_ventWallManage.html");
	}
	/**
	 * 分页查询
	 */
	public void setActivityPage(int num){
		int pageNumber = getParaToInt("pageNumber", 1);
		if (num != 0){
			pageNumber = num;
		}
		int pageSize = getParaToInt("pageSize", 5);
		Page<VentWall> ventWallList = VentWall.venWdao.findPage(pageNumber,pageSize);
		List<VentWall> ventWalls = VentWall.venWdao.getVentWall();
		List<User> userList = new ArrayList<User>();
		User user = getModel(User.class);
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
		String createTime = "";
		for (VentWall vent : ventWalls){
			user = user.findById(vent.get("userid"));
			userList.add(user);
		}
		setAttr("userList", userList);
		setAttr("pageNumber", pageNumber);
		setAttr("ventWallList", ventWallList);
	}
	/**
	 * 获得日期与星期数
	 */
	public void getNowTime(){
		Calendar date = Calendar.getInstance();
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		String[] weekDays ={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		int week = date.get(Calendar.DAY_OF_WEEK)-1;
		String weekDay ="";
		if (week < 0){
			week = 0;		
		}
		weekDay = weekDays[week];
		setAttr("month", month);
		setAttr("day", day);
		setAttr("weekDay", weekDay);
	}
	/**
	 * 获得等级
	 * @param userId 
	 */
	public void getGrade(Long userId){
		List<CodeTable> codetables = CodeKit.getList("grade");
		User user = getModel(User.class);
		user = user.findById(userId);
		GradeKit gradeKit = new GradeKit();
		Map<String,Object> gradeMap = gradeKit.getGrade(user);
		setAttr("pointName", gradeMap.get("pointName"));
		setAttr("poin", gradeMap.get("poin"));
		setAttr("peas", gradeMap.get("peas"));
	}
}
