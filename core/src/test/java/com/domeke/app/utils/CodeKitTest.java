package com.domeke.app.utils;

import org.junit.Assert;
import org.junit.Test;

public class CodeKitTest {
	@Test
	public void testInit() {
		
		CodeKit code = new CodeKit();
		code.init();
		String value = CodeKit.getValue("status", "Y");
		Assert.assertEquals("Y", value);
	}
}
