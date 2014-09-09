package com.domeke.app.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.domeke.app.base.config.DomekeConstants;
import com.domeke.app.utils.EncryptKit;

public class DomekeCredentialsMatcher extends HashedCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		setHashAlgorithmName(DomekeConstants.HASH_ALGORITHM_NAME);
		String password = EncryptKit.encryptMd5(String.valueOf(upToken.getPassword()));
		Object credentials = getCredentials(info);
		return equals(password, credentials);
	}

}
