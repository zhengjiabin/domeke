package com.domeke.app.message.impl;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.domeke.app.message.DomekeMailSender;
import com.domeke.app.utils.MailTemplate;

public class DomekeMaiSenderImpl implements DomekeMailSender {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@Override
	public void send(String from, String[] to, String[] cc, String template, List<Map<String,Object>> params) {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setCc(cc);
			helper.setSubject("test");
			for (Map<String, Object> map : params) {
				
				String text = MailTemplate.getHtml("mailValidate", map);
				helper.setText(text);
				javaMailSender.send(message);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
}
