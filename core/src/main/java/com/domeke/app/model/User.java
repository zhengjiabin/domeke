/**
 * 
 */
package com.domeke.app.model;

import com.domeke.app.utils.EncryptUtils;
import com.domeke.app.utils.HtmlTagKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
 *CREATE TABLE `user` (
  `userid` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `moblie` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creater` varchar(64) DEFAULT NULL,
  `modifier` varchar(64) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_idx` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
 */
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;

	public static User dao = new User();

	public User login(String username) {
		String sql = "select password from user where username=? ";
		return dao.findFirst(sql, username);
	}

	public void saveUser() {
		HtmlTagKit.processHtmlSpecialTag(this, "username", "email", "mobile");
		String pasword = EncryptUtils.encryptMd5(this.getStr("password"));
		this.set("password", pasword);
		this.save();
	}

	public void updateUser() {
		HtmlTagKit.processHtmlSpecialTag(this, "email", "mobile");
		this.update();
	}

	public void updatePassword() {
		this.update();
	}

	public boolean containColumn(String column, String paras) {
		String sql = " select  1 from user where " + column + "= ? limit 1 ";
		User user = dao.findFirst(sql, paras);
		return user != null;
	}

}
