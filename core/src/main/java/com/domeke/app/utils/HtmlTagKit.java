/**
 * 
 */
package com.domeke.app.utils;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author lijiasen@domeke.com
 *
 */
public class HtmlTagKit {
	public static void processHtmlSpecialTag(Model<?> model, String... attrNames) {
		for (String attrName : attrNames) {
			String content = model.getStr(attrName);
			if (StrKit.notBlank(content)) {
				String temp = content.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				model.set(attrName, temp);
			}
		}
	}
}
