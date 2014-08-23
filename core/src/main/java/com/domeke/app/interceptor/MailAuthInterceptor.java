/**
 * 
 */
package com.domeke.app.interceptor;
import org.springframework.mail.javamail.JavaMailSender;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;


/**
 * @author lijiasen
 *
 */
public class MailAuthInterceptor implements Interceptor {
	
	

	@Override
	public void intercept(ActionInvocation ai) {
			ai.invoke();
			Controller c = ai.getController();
			String email = c.getPara("email");
			JavaMailSender sender = null;
	}
}
