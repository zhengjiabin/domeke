package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.LoginPic;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@ControllerBind(controllerKey="loginpic")
@Before(LoginInterceptor.class)
public class LoginPicController extends FilesLoadController {
	
	public void renderLoginPic(){
		setPicPage();
		render("/admin/admin_loginPic.html");
	}
	
	public void find() {
		setPicPage();
		render("/admin/admin_loginPicPage.html");
	}
	
	/**
	 * 分页查询
	 */
	public void setPicPage() {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<LoginPic> lpList = null;
		lpList = LoginPic.lpDao.findPage(pageNumber, pageSize);
		setAttr("lpList", lpList);
		List<CodeTable> statusList = CodeKit.getList("status");
		setAttr("statusList", statusList);
	}
	
	/**
	 * 跳转新增界面
	 */
	public void addPic() {
		List<CodeTable> statusList = CodeKit.getList("status");
		setAttr("statusList", statusList);
		render("/admin/admin_addLoginPic.html");
	}
	
	/**
	 * 新增
	 */
	public void savePic() {
		long timeMillis = System.currentTimeMillis();
		String fileNmae = Long.toString(timeMillis);
		String picturePath = upLoadFileNotDealPath("loginpic.picurl", fileNmae, 200 * 1024 * 1024, "utf-8");
		String[] strs = picturePath.split("\\\\");
		int leng = strs.length;
		LoginPic loginPic = getModel(LoginPic.class);
		loginPic.set("picurl", strs[leng - 1]);
		String status = loginPic.getPicUrl();
		if (!"".equals(status) && status != null && loginPic.getInt("status") == 70){
			setAttr("loginPic", loginPic);
			setAttr("msg", "系统已存在启用图片！");
			List<CodeTable> statusList = CodeKit.getList("status");
			setAttr("statusList", statusList);
			render("/admin/admin_addLoginPic.html");
		} else {
			loginPic.saveLoginPic();
			redirect("/loginpic/renderLoginPic");
		}
	}
	
	/**
	 * 删除
	 */
	public void deletePic() {
		LoginPic loginPic = getModel(LoginPic.class);
		loginPic.deleteById(getParaToInt("picId"));
		redirect("/loginpic/renderLoginPic");
	}
	
	/**
	 * 跳转修改界面
	 */
	public void upPic() {
		LoginPic loginPic = LoginPic.lpDao.findLoginPicById(getParaToInt("picId"));
		setAttr("loginPic", loginPic);
		List<CodeTable> statusList = CodeKit.getList("status");
		setAttr("statusList", statusList);
		render("/admin/admin_upLoginPic.html");
	}
	
	/**
	 * 修改
	 */
	public void updatePic() {
		LoginPic loginPic = getModel(LoginPic.class);
		String status = loginPic.getPicUrl();
		if (!"".equals(status) && status != null && loginPic.getInt("status") == 70){
			setAttr("loginPic", loginPic);
			setAttr("msg", "系统已存在启用图片！");
			List<CodeTable> statusList = CodeKit.getList("status");
			setAttr("statusList", statusList);
			render("/admin/admin_upLoginPic.html");
		} else {
			loginPic.upLoginPic();
			redirect("/loginpic/renderLoginPic");
		}
	}
}
