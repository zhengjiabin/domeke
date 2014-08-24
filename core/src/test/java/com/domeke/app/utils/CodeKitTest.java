package com.domeke.app.utils;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;

public class CodeKitTest {
	@Test
	public void testInit() {
		
		CodeKit code = new CodeKit();
		try {
			code.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		String value = CodeKit.getValue("STATUS", "Y");
		Assert.assertEquals("Y", value);
		 value = CodeKit.getValue("STATUS", "N");
		Assert.assertEquals("N", value);
		value = CodeKit.getValue("TASK_STATUS", "S001");
		Assert.assertEquals("001", value);
	}
}
