package com.domeke.app.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.model.User;

public class DomekeRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(DomekeRealm.class);

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pri) {
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		User dao = new User();
		User user = dao.login(username);
		SimpleAuthenticationInfo info = null;
		if (user != null) {
			String password = user.getStr("password");
			info = new SimpleAuthenticationInfo(username, password, getName());
		} else {
			logger.error("未知的用户名！", new UnknownAccountException());
			throw new UnknownAccountException();
		}
		return info;
	}
}
