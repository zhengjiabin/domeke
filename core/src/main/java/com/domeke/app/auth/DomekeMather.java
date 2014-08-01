package com.domeke.app.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class DomekeMather extends HashedCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String password = String.valueOf(upToken.getPassword());
		Object credentials = getCredentials(info);

		String username = (String) token.getPrincipal();

		return false;
	}

}
