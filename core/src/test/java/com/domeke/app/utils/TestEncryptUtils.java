package com.domeke.app.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jfinal.kit.EncryptionKit;

public class TestEncryptUtils {

	@Test
	public void testEncryptMd5() {
		String params = EncryptKit.EncryptMd5("admin");
		assertEquals("ISMvKXpXpadDiUoOSoAfww==", params);
	}

}
