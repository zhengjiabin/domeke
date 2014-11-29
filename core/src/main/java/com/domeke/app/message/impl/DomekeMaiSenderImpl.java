package com.domeke.app.message.impl;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.domeke.app.message.DomekeMailSender;
import com.domeke.app.utils.PropKit;

import freemarker.template.Template;

public class DomekeMaiSenderImpl implements DomekeMailSender {

	@Autowired
	private JavaMailSender javaMailSender;

	private FreeMarkerConfigurer freeMarkerConfigurer = null;

	@Override
	public void send(String from, String[] to, String template, Map<String, Object> params) {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(getMailText(template, params, 0));
			helper.setText(getMailText(template, params, 1), true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void send(String to, String template, Map<String, Object> params) {
		String[] emailto = new String[] { to };
		send(PropKit.getString("mailSenderName"), emailto, template, params);
	}

	/**
	 * 
	 * @param template 模板名称
	 * @param map 参数
	 * @param messageType 模板类别：0 为 subect 1 为message
	 * @return
	 */
	private String getMailText(String template, Map<String, Object> map, int messageType) {

		String prefix = "";
		if (messageType == 0) {
			prefix = "_subject";
		}

		if (messageType == 1) {
			prefix = "_message";
		}

		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(template + prefix + ".ftl");
			// FreeMarker通过Map传递动态数据
			// 解析模板并替换动态数据，最终将替换模板文件中的标签。
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}

}
