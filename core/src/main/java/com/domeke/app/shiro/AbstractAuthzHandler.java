package com.domeke.app.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 访问控制抽象基类
 * @author dafei
 */
abstract class AbstractAuthzHandler implements AuthzHandler {

	/**
	 * 获得Shiro的Subject对象。
	 * @return
	 */
	protected Subject getSubject() {
		return SecurityUtils.getSubject();
	}
}
