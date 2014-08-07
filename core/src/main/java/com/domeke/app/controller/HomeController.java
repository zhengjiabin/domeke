/**
 * 
 */
package com.domeke.app.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;

/**
 * @author lijiasen@domeke.com
 * 登录后的首页显示控制类
 */
@Before(ShiroInterceptor.class)
public class HomeController extends Controller {

	@RequiresRoles(value = { "user", "admin" }, logical = Logical.OR)
	public void index() {
		setAttr("message", "hello word");
		render("/index.html");
	}

	@RequiresRoles({ "user", "admin" })
	public void userAdmin() {
		setAttr("message", "role:user,admin");
		render("/message.html");

	}

	@RequiresRoles(value = { "user", "admin" }, logical = Logical.OR)
	public void userOradmin() {
		setAttr("message", "role:user or admin");
		render("/message.html");
	}

	@RequiresRoles("admin")
	public void admin() {
		setAttr("message", "role:admin");
		render("/message.html");
	}

	@RequiresRoles("user")
	public void user() {
		setAttr("message", "role:user");
		render("/message.html");
	}

	public void showUser() {

	}

	@RequiresPermissions("addUser")
	public void addUser() {
		setAttr("message", "permission:addUser");
		render("/message.html");
	}

}
