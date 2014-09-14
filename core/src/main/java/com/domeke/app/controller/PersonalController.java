package com.domeke.app.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.utils.EncryptKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class PersonalController extends Controller{
	@Before(LoginInterceptor.class)
	public void forMyProduction(){
		String mid = getPara("menuId");
		if(mid!=null && mid.equals("4")){
			User user = getSessionAttr("user");
			Long userId = user.getLong("userid");
			setAttr("userId", userId);
		}
		setAttr("menuId", mid);
		setAttr("menuid", "1");
		render("/personalCenter.html");
	}
	
	public void updatePassword(){
		User user = getModel(User.class);
		Long userid = user.getLong("userid");
		String password = user.getStr("password");
		if(password != null){
			password = EncryptKit.encryptMd5(password);
		}
		String newPassword = getPara("newPassword");
		String repeatPassword = getPara("repeatPassword");
		String reg = "[a-zA-Z0-9_]{6,16}";
		Pattern pattern = true ? Pattern.compile(reg) : Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(newPassword);
	    user = user.getUserForId(userid);
	    boolean isOk = true;
	    if(password == null ||newPassword == null||repeatPassword == null){
	    	setAttr("nullMsg", "密码不能为空");
	    	isOk = false;
	    	setAttr("userId", userid);
			setAttr("menuId", "4");
			setAttr("menuid", "1");
			render("/personalCenter.html");
			return;
	    }
	    if(!password.equals(user.getStr("password"))){
	    	setAttr("passwordMsg", "原密码错误");
	    	isOk = false;
	    }
	    if(!matcher.matches()){
	    	setAttr("newPasswordMsg", "新密码格式不正确");
	    	isOk = false;
	    }
	    if(!newPassword.equals(repeatPassword)){
	    	setAttr("repeatPasswordMsg", "两次密码输入不一致");
	    	isOk = false;
	    	
	    }
	    if(isOk){
	    	newPassword = EncryptKit.encryptMd5(newPassword);
	    	user.updatePassword(userid,newPassword);
	    	setAttr("nullMsg", "密码修改成功");
			
	    }else{
	    	setAttr("user", user);
	    	setAttr("newPassword", newPassword);
	    	setAttr("repeatPassword", repeatPassword);
	    }
		setAttr("userId", userid);
		setAttr("menuId", "4");
		setAttr("menuid", "1");
		render("/personalCenter.html");
	}
}
