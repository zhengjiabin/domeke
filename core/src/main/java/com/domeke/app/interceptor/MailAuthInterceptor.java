package com.domeke.app.interceptor;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;

import com.domeke.app.utils.EncryptKit;
import com.google.common.collect.Maps;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 @author lijiasen
 *
 */
public class MailAuthInterceptor implements Interceptor {

	private JavaMailSender mailSender;

	@Override
	public void intercept(ActionInvocation ai) {
		ai.invoke();
	}

	/**
	 加密验证码
	 @param email
	 @return
	 */
	private String getValidateCode(String email) {
		String validateCode = EncryptKit.encryptMd5(email);
		return validateCode;
	}

	private void sendMail(Controller c) {
		String email = c.getPara("email");
		String nickname = c.getPara("nickname");
		String validateCode = getValidateCode(email);
		Map<String, String> params = Maps.newHashMap();
		params.put("nickname", nickname);
		params.put("email", email);
		// 创建MimeMessageHelper对象，处理MimeMessage的辅助类
	}
}
