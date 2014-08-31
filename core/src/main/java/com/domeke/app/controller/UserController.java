/**
 * 
 */
package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.domeke.app.message.impl.DomekeMaiSenderImpl;
import com.domeke.app.model.User;
import com.domeke.app.utils.EncryptKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.plugin.spring.IocInterceptor;

/**
 * @author lijiasen@domeke.com
 *
 */
@Before(IocInterceptor.class)
public class UserController extends Controller {
	
	@Inject.BY_NAME
	private DomekeMaiSenderImpl domekeMailSender;

	public void save() {
		User user = getModel(User.class);
		user.saveUser();
	}

	public void goRegist() {
		render("/demo/regist.html");
	}
	
	public void goLogin(){
		render("/demo/login.html");
	}
	//@Before({RegistValidator.class,MailAuthInterceptor.class})
	public void regist() {
		User user = getModel(User.class);
		user.saveUser();
		render("/demo/login.html");
	}
	public void goUserManage(){
		userManage();
		render("/demo/userManage.html");
	}
	
	public void userManage(){
		User user = getModel(User.class);
		List<User> userList = user.getUser();
		this.setAttr("userList", userList);
	}
	public void goUserUpdate(){
		getUser();
		render("/demo/userUpdate.html");
	}
	public void getUser(){
		User user = getModel(User.class);
		user = user.findById(getParaToInt());
		this.setAttr("user", user);
	}
	public void update(){
		User user = getModel(User.class);
		user.update();
		goUserManage();
	}
	public void delete(){
		User user = getModel(User.class);
		user.deleteById(getParaToInt());
		goUserManage();
	}
	public void reset(){
		User user = getModel(User.class);
		String password = "111111";
		int userid = getParaToInt();
		password = EncryptKit.encryptMd5(password);
		user.updateReset(userid, password);
		goUserManage();
	}
	public void search(){
		 List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
		 String[] to = new String[]{"634775305@qq.com"};
		 domekeMailSender.send("testerh@126.com", to, null, "", params);
	}

	public DomekeMaiSenderImpl getDomekeMailSender() {
		return domekeMailSender;
	}

	public void setDomekeMailSender(DomekeMaiSenderImpl domekeMailSender) {
		this.domekeMailSender = domekeMailSender;
	}
}
