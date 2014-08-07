/**
 * 
 */
package com.domeke.app.model;

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

}
