package com.domeke.app.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestEncryptUtils {

	@Test
	public void testEncryptMd5() {
		String params = EncryptKit.encryptMd5("admin");
		assertEquals("ISMvKXpXpadDiUoOSoAfww==", params);
	}

}
