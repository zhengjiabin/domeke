/**
 * 
 */
package com.domeke.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import com.domeke.app.message.DomekeMailSender;
import com.domeke.app.model.LoginPic;
import com.domeke.app.model.PointLog;
import com.domeke.app.model.User;
import com.domeke.app.model.UserRole;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.utils.EncryptionDecryption;
import com.domeke.app.utils.MyCaptchaRender;
import com.domeke.app.validator.RegistValidator;
import com.google.common.collect.Maps;
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
    private DomekeMailSender domekeMailSender;
    
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
    
    public void goLogin() {
        User user = getModel(User.class);
        setAttr("msg", "");
        String url = LoginPic.lpDao.getPicUrl();
        setAttr("url", url);
        render("/Login.html");
    }
    
    public void member() {
        render("/Member.html");
    }
    
    /**
     * 跳转管理页面
     */
    public void goToManager() {
        render("/admin/community.html");
    }
    
    // @Before({RegistValidator.class,MailAuthInterceptor.class})
    @Before({RegistValidator.class})
    public void regist() {
        User user = getModel(User.class);
        // 验证码
        String inputRandomCode = getPara("captcha");
        boolean validate = MyCaptchaRender.validate(this, inputRandomCode);
        if (!validate) {
            this.setAttr("user", user);
            setAttr("verification", "验证码错误!");
            render("/register2.html");
            return;
        }
        // 较验用户是否同意注册协议
        String checkbox = getPara("check");
        if (checkbox == null) {
            this.setAttr("user", user);
            setAttr("xieyi", "未同意注册协议!");
            render("/register2.html");
            return;
        }
        setSessionAttr("userReg", user);
        user.saveUser();
        UserRole userrole = getModel(UserRole.class);
        userrole.set("roleid", 0);
        userrole.set("userid", user.getLong("userid"));
        userrole.save();
        // 发送邮箱验证
        sendActivation(user);
        setAttr("succes", "帐号注册成功，请登录你的邮箱进行验证！");
        render("/registerSucces.html");
    }
    
    /**
     * 重新邮箱验证
     */
    public void againReg() {
        User user = getModel(User.class);
        user = getSessionAttr("userReg");
        sendActivation(user);
        setAttr("succes", "帐号注册成功，请登录你的邮箱进行验证！");
        render("/AgainReg.html");
    }
    
    /**
     * /** 验证码
     */
    public void captcha() {
        render(new MyCaptchaRender(60, 22, 4, true));
    }
    
    public void goUserManage() {
        userManage();
        render("/admin/user.html");
    }
    
    public void goUserManages() {
        userManage();
        render("/admin/admin_user.html");
    }
    
    public void goUserPoint() {
        userCountPoint();
        render("/admin/admin_userCountPoint.html");
    }
    
    /**
     * 用户积分变更日志
     */
    public void goCountPoint() {
        userCountPoint();
        render("/admin/admin_Point.html");
    }
    
    /**
     * 获取用户积分记录
     */
    public void userCountPoint() {
        // 分页码
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        PointLog point = getModel(PointLog.class);
        Page<PointLog> pointList = point.getUserPoint(null, null, pageNumber, pageSize);
        this.setAttr("pointList", pointList);
    }
    
    public void userManage() {
        // 分页码
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        User user = getModel(User.class);
        Page<User> userList = user.getUserPage(null, null, pageNumber, pageSize);
        // for(int i=0;i<userList.size();i++){
        // String status = userList.get(i).getStr("status");
        // if("Y".equals(status)){
        // userList.get(i).set("status", "正常");
        // }else{
        // userList.get(i).set("status", "冻结");
        // }
        // }
        this.setAttr("userList", userList);
    }
    
    public void goUserUpdate() {
        getUser();
        render("/admin/admin_updateUser.html");
    }
    
    /**
     * 豆豆积分修改
     */
    public void goUpUserPoint() {
        getUser();
        String td = getPara("td");
        setAttr("td", td);
        render("/admin/admin_userPoint.html");
    }
    
    public void getUser() {
        User user = getModel(User.class);
        String uid = getPara("uid");
        user = user.findById(uid);
        this.setAttr("user", user);
    }
    
    public void update() {
        User user = getModel(User.class);
        user.update();
        goUserManage();
    }
    
    /**
     * 修改用户的豆豆积分
     */
    public void updatePoint() {
        User user = getModel(User.class);
        PointLog point = getModel(PointLog.class);
        point.set("userid", user.get("userid"));
        point.set("username", user.get("username"));
        point.set("title", getPara("title"));
        point.set("type", getPara("type"));
        User nowUser = getSessionAttr("user");
        point.set("creater", nowUser.getLong("userid"));
        point.set("modifier", nowUser.getLong("userid"));
        String rewardStr = getPara("reward");
        Long reward = Long.valueOf(rewardStr);
        String type = getPara("td");
        if (reward > 0) {
            if ("add".equals(type)) {
                point.set("reward", reward);
            } else {
                point.set("reward", -reward);
            }
            if ("豆豆".equals(getPara("type"))) {
                Long peas = user.getLong("peas");
                if ("add".equals(type)) {
                    peas = peas + reward;
                } else if ("delete".equals(type)) {
                    peas = peas - reward;
                }
                user.set("peas", peas);
            } else {
                Long p = user.getLong("point");
                if ("add".equals(type)) {
                    p = p + reward;
                } else if ("delete".equals(type)) {
                    p = p - reward;
                }
                user.set("point", p);
            }
            point.save();
            user.update();
        }
        goUserManage();
    }
    
    public void delete() {
        User user = getModel(User.class);
        user.deleteById(getParaToInt());
        goUserManage();
    }
    
    /**
     * 冻结用户操作
     */
    public void freezeUser() {
        User user = getModel(User.class);
        String uid = getPara("uid");
        user = user.findById(uid);
        user.set("status", "N");
        user.update();
        goUserManage();
    }
    
    /**
     * 密码重置
     */
    public void resetPassword() {
        User user = getModel(User.class);
        String uid = getPara("uid");
        user = user.findById(uid);
        String password1 = "111111";
        String password = EncryptKit.EncryptMd5(password1);
        user.set("password", password);
        user.update();
        setAttr("okmsg", "密码修改成功，重置后密码为111111!");
        goUserManage();
    }
    
    /**
     * 解除冻结用户操作
     */
    public void removeFreezeUser() {
        User user = getModel(User.class);
        String uid = getPara("uid");
        user = user.findById(uid);
        user.set("status", "Y");
        user.update();
        goUserManage();
    }
    
    public void reset() {
        User user = getModel(User.class);
        String password = "111111";
        int userid = getParaToInt();
        password = EncryptKit.EncryptMd5(password);
        user.updateReset(userid, password);
        goUserManage();
    }
    
    public void search() {
        render("/searchPassword.html");
    }
    
    /**
     * 跳转邮箱验证
     */
    public void testEmail() {
        render("/testEmail.html");
    }
    
    /**
     * 按用户名查询积分日志
     */
    public void integralForName() {
        // 分页码
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String userSearch = this.getRequest().getParameter("userSearch");
            Page<PointLog> pointList = PointLog.dao.getUserPoint("username", userSearch, pageNumber, pageSize);
            ;
            
            this.setAttr("pointList", pointList);
            render("/admin/admin_Point.html");
            
        } catch (UnsupportedEncodingException e) {
            
            e.printStackTrace();
        }
        
    }
    
    public void sechUserForName() {
        User user = getModel(User.class);
        // List<User> userList = new ArrayList<User>();
        // 分页码
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String userSearch = this.getRequest().getParameter("userSearch");
            // userSearch = new
            // String(userSearch.getBytes("ISO-8859-1"),"UTF-8");
            Page<User> userList = user.getUserPage("username", userSearch, pageNumber, pageSize);
            ;
            
            this.setAttr("userList", userList);
            render("/admin/user.html");
            
        } catch (UnsupportedEncodingException e) {
            
            e.printStackTrace();
        }
        
    }
    
    public void testUpEmail() {
        User user = getModel(User.class);
        Long userid = user.getUidForUN(user.getStr("username"));
        if (userid == null) {
            setAttr("emailMsg", "该用户名不存在！");
            render("/testEmail.html");
            return;
        }
        String password = EncryptKit.EncryptMd5(user.getStr("password"));
        String resPassword = user.getPassword(user.getStr("username"));
        if (resPassword != null && password != null) {
            if (!resPassword.equals(password)) {
                setAttr("emailMsg", "密码错误！");
                render("/testEmail.html");
                return;
            }
        }
        Long count = user.getCountEmail(user.getStr("username"), user.getStr("email"));
        if (count > 0) {
            setAttr("emailMsg", "邮箱已存在！");
            render("/testEmail.html");
            return;
        }
        user.set("userid", userid);
        user.update();
        // 发送邮箱验证
        sendActivation(user);
        setAttr("succes", "验证信息已发送到邮箱，请登录你的邮箱进行验证！");
        render("/registerSucces.html");
    }
    
    /**
     * 找回密码
     */
    public void sendPassword() {
        User user = getModel(User.class);
        String email = user.getStr("email");
        boolean oneamail = user.containColumn("email", email);
        if (!oneamail) {
            setAttr("emailMsg", "该邮箱地址不存在!");
            render("/searchPassword.html");
            return;
        }
        
        Long count = user.getCountUser(user.getStr("username"), email);
        if (count == 0) {
            setAttr("emailMsg", "用户名邮箱不匹配!");
            render("/searchPassword.html");
            return;
        }
        String inputRandomCode = getPara("captcha");
        boolean validate = MyCaptchaRender.validate(this, inputRandomCode);
        if (!validate) {
            setAttr("emailMsg", "验证码错误！");
            render("/testEmail.html");
            return;
        }
        user = user.getCloumValue("email", email);
        if (user != null) {
            Map<String, Object> params = Maps.newHashMap();
            String password1 = getRandomString(6);
            String password = EncryptKit.EncryptMd5(password1);
            user.updatePassword((Long)user.get("userid"), password);
            params.put("password1", password1);
            params.put("username", user.get("nickname"));
            domekeMailSender.send(email, "repassword", params);
            setAttr("succes", "新密码已发送到邮箱，请登录查看！");
            render("/registerSucces.html");
        }
        
    }
    
    public void sendActivation(User user) {
        try {
            EncryptionDecryption ency = new EncryptionDecryption();
            String email = user.getStr("email");
            user = user.getCloumValue("email", email);
            if (user != null) {
                Map<String, Object> params = Maps.newHashMap();
                String nickname = user.getStr("nickname");
                Long userid = user.getLong("userid");
                // 加密邮箱
                String encryptemail = ency.encrypt(email);
                String template = "registerSuccess";
                String url = "http://www.dongmark.com/user/activationUser?uid=" + encryptemail;
                params.put("url", url);
                params.put("username", nickname);
                domekeMailSender.send(email, template, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void activationUser() {
        try {
            EncryptionDecryption encry = new EncryptionDecryption();
            String email = getPara("uid");
            email = encry.decrypt(email);
            User user = getModel(User.class);
            user = user.findFirst("select * from user where email = '" + email + "' and activation = 'N'");
            if (user != null && user.isNotEmpty()) {
                //如果邮箱未激活 加 10豆子 20积分
                user.updateUserMsg(email, "activation", "Y");
                Integer peas = Integer.parseInt(String.valueOf(user.get("peas")));
                Integer point = Integer.parseInt(String.valueOf(user.get("point")));
                user.set("peas", peas + 10);
                user.set("point", point + 20);
                user.update();
                String url = "http://www.dongmark.com/user/goLogin";
                setAttr("url", url);
                setAttr("succes", "邮箱验证成功");
                render("/Activation.html");
            } else {
                String url = "http://www.dongmark.com/user/goLogin";
                setAttr("url", url);
                setAttr("succes", "邮箱验证已验证过！");
                render("/Activation.html");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static String getRandomString(int length) {
        // length表示生成字符串的长度
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
