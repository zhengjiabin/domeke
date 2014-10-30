package com.domeke.app.controller;

import com.domeke.app.model.LoginPic;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@ControllerBind(controllerKey="loginpic")
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
	}
	
	/**
	 * 跳转新增界面
	 */
	public void addPic() {
		render("/admin/admin_addLoginPic.html");
	}
	
	/**
	 * 新增
	 */
	public void savePic() {
		long timeMillis = System.currentTimeMillis();
		String fileNmae = Long.toString(timeMillis);
		String picturePath = upLoadFileNotDealPath("picurl", fileNmae, 200 * 1024 * 1024, "utf-8");
		String[] strs = picturePath.split("\\\\");
		int leng = strs.length;
		LoginPic loginPic = getModel(LoginPic.class);
		loginPic.set("picurl", strs[leng - 1]);
		loginPic.saveLoginPic();
		redirect("/loginpic/renderLoginPic");
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
		render("/admin/admin_upLoginPic.html");
	}
	
	/**
	 * 修改
	 */
	public void updatePic() {
		LoginPic loginPic = getModel(LoginPic.class);
		loginPic.upLoginPic();
		redirect("/loginpic/renderLoginPic");
	}
}
