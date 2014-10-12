/**
 * 
 */
package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.utils.HtmlTagKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

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
  `creater` BIGINT(20) NULL,
  `modifier` BIGINT(20) NULL,
  `modify_time` TIMESTAMP NULL ,
  PRIMARY KEY (`userid`),
  UNIQUE INDEX `username_idx` (`username` ASC),
  UNIQUE INDEX `email` (`email` ASC));
 */
@TableBind(tableName = "user", pkName = "userid")
public class User extends Model<User> {

	private static final long serialVersionUID = 1L;

	public static User dao = new User();

	public User login(String username) {
		String sql = "select password from user where username=? ";
		return dao.findFirst(sql, username);
	}

	public void saveUser() {
		HtmlTagKit.processHtmlSpecialTag(this, "username", "email", "mobile");
		String pasword = EncryptKit.encryptMd5(this.getStr("password"));
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

	public User findUserIdByUsername(String username) {
		String sql = "select userid from user where username = ? ";
		User user = dao.findFirst(sql, username);
		return user;
	}

	public User findUserByUsername(String username) {
		String sql = "select userid,username,nickname,email,mobile,activation from user where username = ? ";
		User user = dao.findFirst(sql, username);
		return user;
	}

	public List<Record> getUserRoleByUsername(String username) {
		List<Record> recordList = Db.find("select roleid,rolename from role r,user u,user_role ur "
				+ "where u.userid=ur.userid and ur.roleid = r.roleid and u.username = ?", username);
		return recordList;
	}

	public List<Record> getUserPermissionsByUsername(String username) {

		return null;
	}
	public List<User> getUser(String colm,String param){
		String sql="";
		if(colm == null || param == null){
			 sql = "select * from user";
		}else {
			 sql = "select * from user where "+colm+" LIKE '%"+param+"%'";
		}
		
		List<User> list = dao.find(sql);
		return list;
	}
	
	public void updateReset(int userid,String pass){
		String sql="update user set password='"+pass+"' where userid='"+userid+"'";
		Db.update(sql);
	}
	
	public void updateUserMsg(String email,String clom,String param){
		String sql="update user set "+clom+"='"+param+"' where email='"+email+"'";
		Db.update(sql);
	}
	
	public User getCloumValue(String cloum,String param){
		String sql = "select * from user where "+cloum+" = '"+param+"'";
		User user = dao.findFirst(sql);
		return user;
	}
	
	public void updatePassword(Long userid,String pass){
		String sql="update user set password='"+pass+"' where userid='"+userid+"'";
		Db.update(sql);
	}
	public User getUserForId(Long id){
		String sql = "select * from user where userid = '"+id+"'";
		User user = dao.findFirst(sql);
		return user;
	}
	public void updatePeas(Long userid,Long peas){
		String sql="update user set peas='"+peas+"' where userid='"+userid+"'";
		Db.update(sql);
	}
	
}
