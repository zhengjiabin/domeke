/**
 * 
 */
package com.domeke.app.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 *
 */
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;

	public static User dao = new User();

	public User login(String username) {
		String sql = "select password from user where username=? ";
		return dao.findFirst(sql, username);
	}

}
