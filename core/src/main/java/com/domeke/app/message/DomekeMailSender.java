package com.domeke.app.message;

import java.util.Map;

public interface DomekeMailSender {

	/**
	 * 发送邮件
	 * @param from 发送人
	 * @param to 接收人 数组
	 * @param cc 抄送人 数组
	 * @param template 邮件模板
	 * @param params 内容参数
	 */
	public void send(String from, String[] to, String template, Map<String, Object> params);

	/**
	 * 发送邮件
	 * @param from 发送人
	 * @param to 接收人 数组
	 * @param cc 抄送人 数组
	 * @param template 邮件模板
	 * @param params 内容参数
	 */
	public void send(String to, String template, Map<String, Object> params);

}
