/**
 * 
 */
package com.domeke.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.domeke.app.message.impl.DomekeMaiSenderImpl;
import com.domeke.app.model.LoginPic;
import com.domeke.app.model.User;
import com.domeke.app.model.UserRole;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.utils.EncryptionDecryption;
import com.domeke.app.utils.MyCaptchaRender;
import com.domeke.app.validator.RegistValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
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
		String url = LoginPic.lpDao.getPicUrl();
		setAttr("url", url);
		render("/Login.html");
	}
	public void member(){
		render("/Member.html");
	}
	
	/**
	 * 跳转管理页面
	 */
	public void goToManager(){
		render("/admin/community.html");
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
		UserRole userrole = getModel(UserRole.class);
		userrole.set("roleid", 0);
		userrole.set("userid", user.getLong("userid"));
		userrole.save();
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
		render("/admin/user.html");
	}
	
	public void goUserManages(){
		userManage();
		render("/admin/admin_user.html");
	}
	public void userManage(){
		//分页码
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		User user = getModel(User.class);
		Page<User> userList = user.getUserPage(null,null,pageNumber,pageSize);
//		for(int i=0;i<userList.size();i++){
//			String status = userList.get(i).getStr("status");
//			if("Y".equals(status)){
//				userList.get(i).set("status", "正常");
//			}else{
//				userList.get(i).set("status", "冻结");
//			}
//		}
		this.setAttr("userList", userList);
	}
	public void goUserUpdate(){
		getUser();
		render("/admin/admin_updateUser.html");
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
	
	/**
	 * 冻结用户操作
	 */
	public void freezeUser(){
		User user = getModel(User.class);
		String uid = getPara("uid");
		user = user.findById(uid);
		user.set("status","N");
		user.update();
		goUserManage();
	}
	/**
	 * 密码重置
	 */
	public void resetPassword(){
		User user = getModel(User.class);
		String uid = getPara("uid");
		user = user.findById(uid);
		String password1  = "111111";
		String password = EncryptKit.encryptMd5(password1);
		user.set("password",password);
		user.update();
		goUserManage();
	}
	/**
	 * 解除冻结用户操作
	 */
	public void removeFreezeUser(){
		User user = getModel(User.class);
		String uid = getPara("uid");
		user = user.findById(uid);
		user.set("status","Y");
		user.update();
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
	
	public void sechUserForName(){
		User user = getModel(User.class);
		//List<User> userList = new ArrayList<User>();
		//分页码
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		try {
			this.getRequest().setCharacterEncoding("utf-8");
			String userSearch = this.getRequest().getParameter("userSearch");
			userSearch =  new String(userSearch.getBytes("ISO-8859-1"),"UTF-8");
			Page<User> userList = user.getUserPage("username",userSearch,pageNumber,pageSize);;
			
			this.setAttr("userList", userList);
			render("/admin/user.html");
			
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
	
	
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
		try {
			EncryptionDecryption ency = new EncryptionDecryption();
			String email = user.getStr("email");
			user = user.getCloumValue("email",email);
			if(user!=null){
				List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
				String[] to = new String[]{email};
				String nickname = user.getStr("nickname");
				Long userid = user.getLong("userid");
				//加密邮箱
				email = ency.encrypt(email); 
				String msg = "欢迎你："+nickname+":" + "\n" + "请点击以下的路径进行邮箱验证" + "\n" + "http://218.85.136.199/core/user/activationUser?uid="+email+"";
				domekeMailSender.send("testehr@126.com", to, null, msg, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void activationUser(){
		try {
			EncryptionDecryption encry = new EncryptionDecryption();
			String email = getPara("uid");
			email = encry.decrypt(email);
			User user = getModel(User.class);
			user.updateUserMsg(email,"activation","Y");
			String url = LoginPic.lpDao.getPicUrl();
			setAttr("url", url);
			render("/Login.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
