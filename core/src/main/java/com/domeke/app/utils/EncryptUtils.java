/**
 * 
 */
package com.domeke.app.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author lijiasen@domeke.com
 *
 */
public class EncryptUtils {

	public static String encryptMd5(String data) {
		String md5 = new Md5Hash(data).toBase64();
		return md5;
	}

}
