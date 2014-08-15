/**
 * 
 */
package com.domeke.app.model;

import com.domeke.app.utils.EncryptUtils;
import com.domeke.app.utils.HtmlTagKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author Administrator
CREATE TABLE IF NOT EXISTS `domeke`.`user` (
  `userid` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
   `nickname` VARCHAR(64) NULL,
  `password` VARCHAR(32) NOT NULL,
  `email` VARCHAR(255) NULL,
  `mobile` VARCHAR(32) NULL,
  `create_time` TIMESTAMP NULL ,
  `creater` VARCHAR(64) NULL,
  `modifier` VARCHAR(64) NULL,
  `modify_time` TIMESTAMP NULL ,
  PRIMARY KEY (`userid`),
  UNIQUE INDEX `username_idx` (`username` ASC),
  UNIQUE INDEX `email` (`email` ASC));
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
