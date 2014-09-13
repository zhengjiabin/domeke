/**
 * 
 */
package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.domeke.app.interceptor.ActionInterceptor;
import com.domeke.app.message.impl.DomekeMaiSenderImpl;
import com.domeke.app.model.User;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.utils.MyCaptchaRender;
import com.domeke.app.validator.RegistValidator;
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
		User user = getModel(User.class);
		this.setAttr("user", user);
		render("/register2.html");
	}
	
	public void goRegistSucces() {
		render("/registerSucces.html");
	}
	
	public void goLogin(){
		User user = getModel(User.class);
		setAttr("msg", "");
		render("/Login.html");
	}
	public void member(){
		render("/Member.html");
	}
	//@Before({RegistValidator.class,MailAuthInterceptor.class})
	@Before({RegistValidator.class})
	public void regist() {
		User user = getModel(User.class);
		//验证码
		String inputRandomCode = getPara("captcha");
        boolean validate = MyCaptchaRender.validate(this, inputRandomCode);
        if(!validate){
        	this.setAttr("user", user);
        	setAttr("verification", "验证码错误!");
			render("/register2.html");
			return;
        }
		user.saveUser();
		//发送邮箱验证
		sendActivation(user);
		setAttr("succes", "帐号注册成功，请登录你的邮箱进行验证！");
		render("/registerSucces.html");
	}
	
	/**
	 * 验证码
	 */
	public void captcha()
    {
        render(new MyCaptchaRender(60,22,4,true));
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
		 render("/searchPassword.html");	 
	}
	public void sendPassword(){
		User user = getModel(User.class);
		String email = user.getStr("email");
		boolean oneamail = user.containColumn("email", email);
		if(!oneamail){
			setAttr("emailMsg", "该邮箱地址不存在!");
			 render("/searchPassword.html");
			 return;
		}
		user = user.getCloumValue("email",email);
		if(user!=null){
			List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
			String[] to = new String[]{email};
			String password1  = getRandomString(6);
			String password = EncryptKit.encryptMd5(password1);
			user.updatePassword(user.get("userid"),password);
			String msg = "你好你的密码已重置为："+password1+"，请及时登录 系统修改";
			domekeMailSender.send("testehr@126.com", to, null, msg, params);
			setAttr("succes", "新密码已发送到邮箱，请登录查看！");
			render("/registerSucces.html");
		}
		
	}
	
	public void sendActivation(User user){
		
		String email = user.getStr("email");
		user = user.getCloumValue("email",email);
		if(user!=null){
			List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
			String[] to = new String[]{email};
			String nickname = user.getStr("nickname");
			Long userid = user.getLong("userid");
			String msg = "欢迎你："+nickname+":" + "\n" + "请点击以下的路径进行邮箱验证" + "\n" + "http://localhost:8080/core/user/activationUser?uid="+userid+"";
			domekeMailSender.send("testehr@126.com", to, null, msg, params);
		}
		
	}
	
	public void activationUser(){
		String userid = getPara("uid");
		User user = getModel(User.class);
		user.updateUserMsg(userid,"activation","Y");
		render("/Login.html");
	}
	
	public DomekeMaiSenderImpl getDomekeMailSender() {
		return domekeMailSender;
	}

	public void setDomekeMailSender(DomekeMaiSenderImpl domekeMailSender) {
		this.domekeMailSender = domekeMailSender;
	}
	
	public static String getRandomString(int length) { 
		//length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 } 
}
