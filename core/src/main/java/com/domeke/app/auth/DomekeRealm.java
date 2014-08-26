package com.domeke.app.auth;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.model.User;
import com.jfinal.plugin.activerecord.Record;

public class DomekeRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(DomekeRealm.class);

	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pri) {
		String username = (String) pri.fromRealm(getName()).iterator().next();
		List<Record> roleList = User.dao.getUserRoleByUsername(username);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Record record : roleList) {
			info.addRole(record.getStr("rolename"));
		}
		List<Record> permissionList = User.dao.getUserPermissionsByUsername(username);
		for (Record record : permissionList) {
			info.addStringPermission(record.getStr("permission"));
		}

		return info;
	}

	/**
	 * 获取认证信息
	 */
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
