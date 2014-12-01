/**
 * 
 */
package com.domeke.app.model;

import java.util.List;
import java.util.Map;

import com.domeke.app.message.DomekeMailSender;
import com.domeke.app.tablebind.TableBind;
import com.domeke.app.utils.EncryptKit;
import com.domeke.app.utils.EncryptionDecryption;
import com.domeke.app.utils.HtmlTagKit;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.spring.Inject;

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

	@Inject.BY_NAME
	private DomekeMailSender domekeMailSender;

	public User login(String username) {
		String sql = "select password from user where username=? ";
		return dao.findFirst(sql, username);
	}
	public Long getUidForUN(String username) {
		String sql = "select userid from user where username='"+username+"'";
		Long userid = Db.queryLong(sql);
		return userid;
	}
	
	public String getPassword(String username) {
		String sql = "select password from user where username='"+username+"'";
		String password = Db.queryStr(sql);
		return password;
	}
	
	public Long getCountEmail(String username,String email) {
		String sql = "select count(1) from user where email='"+email+"' and username !='"+username+"'";
		Long count = Db.queryLong(sql);
		return count;
	}
	public void saveUser() {
		HtmlTagKit.processHtmlSpecialTag(this, "username", "email", "mobile");
		String pasword = EncryptKit.EncryptMd5(this.getStr("password"));
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

	public boolean isUser(Long userId) {
		String sql = " select  1 from user where userid= ? limit 1 ";
		User user = dao.findFirst(sql, userId);
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

	public List<User> getUser(String colm, String param) {
		String sql = "";
		if (colm == null || param == null) {
			sql = "select * from user";
		} else {
			sql = "select * from user where " + colm + " LIKE '%" + param + "%'";
		}

		List<User> list = dao.find(sql);
		return list;
	}

	public Page<User> getUserPage(String colm, String param, int pageNumber, int pageSize) {
		String sql = "";
		Page<User> page = null;
		if (colm == null || param == null) {
			page = this.paginate(pageNumber, pageSize, "select * ", "from user");
		} else {
			sql = "select * from user where " + colm + " LIKE '%" + param + "%'";
			page = this.paginate(pageNumber, pageSize, "select * ", "from user where " + colm + " LIKE '%" + param
					+ "%'");
		}

		return page;
	}

	public void updateReset(int userid, String pass) {
		String sql = "update user set password='" + pass + "' where userid='" + userid + "'";
		Db.update(sql);
	}

	public void updateUserMsg(String email, String clom, String param) {
		String sql = "update user set " + clom + "='" + param + "' where email='" + email + "'";
		Db.update(sql);
	}

	public User getCloumValue(String cloum, String param) {
		String sql = "select * from user where " + cloum + " = '" + param + "'";
		User user = dao.findFirst(sql);
		return user;
	}

	public void updatePassword(Long userid, String pass) {
		String sql = "update user set password='" + pass + "' where userid='" + userid + "'";
		Db.update(sql);
	}

	public User getUserForId(Long id) {
		String sql = "select * from user where userid = '" + id + "'";
		User user = dao.findFirst(sql);
		return user;
	}

	public void updatePeas(Long userid, Long peas) {
		String sql = "update user set peas='" + peas + "' where userid='" + userid + "'";
		Db.update(sql);
	}

	/**
	 * @param clomname
	 * @param param
	 * @returnt统计用户的注册情况
	 */
	public Long countUser(String clomname, String param) {
		String sql = "";
		if (clomname != null && param != null) {
			sql = "select count(userid) from user where " + clomname + " = '" + param + "'";
		} else {
			sql = "select count(userid) from user";
		}
		Long count = Db.queryLong(sql);
		return count;
	}

	/**
	 * @param starPeas 
	 * @param endPeas
	 * @return统计用户的豆子数
	 */
	public Long countPeas(String starPeas, String endPeas) {
		String sql = "";
		if (starPeas != null && endPeas != null) {
			sql = "select count(userid) from user where peas BETWEEN '" + starPeas + "' and '" + endPeas + "'";
		}
		Long count = Db.queryLong(sql);
		return count;
	}

	/**
	 * @param starPeas 
	 * @param endPeas
	 * @return统计用户的积分数
	 */
	public Long countPoint(String starPeas, String endPeas) {
		String sql = "";
		if (starPeas != null && endPeas != null) {
			sql = "select count(userid) from user where point BETWEEN '" + starPeas + "' and '" + endPeas + "'";
		}
		Long count = Db.queryLong(sql);
		return count;
	}

	/**
	 * 根据username查询该用户头像
	 * @param username
	 * @return
	 */
	public String getImgURL(String username) {
		String imgUrl = Db.queryStr("select imgurl from user where username = '" + username + "'");
		return imgUrl;
	}
	/**
	 * 根据userid查询该用户头像
	 * @param username
	 * @return
	 */
	public String getIndexImgURL(String userid) {
		String imgUrl = Db.queryStr("select imgurl from user where userid = '" + userid + "'");
		return imgUrl;
	}
	public void sendActivation(User user) {
		try {
			EncryptionDecryption ency = new EncryptionDecryption();
			String email = user.getStr("email");
			user = user.getCloumValue("email", email);
			if (user != null) {
				Map<String, Object> params = Maps.newHashMap();
				String nickname = user.getStr("nickname");
				Long userid = user.getLong("userid");
				// 加密邮箱
				email = ency.encrypt(email);
				String template = "registerSuccess";
				String url = "http://www.dongmark.com/user/activationUser?uid=" + email;
				params.put("url", url);
				params.put("username", nickname);
				domekeMailSender.send(email, template, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DomekeMailSender getDomekeMailSender() {
		return domekeMailSender;
	}

	public void setDomekeMailSender(DomekeMailSender domekeMailSender) {
		this.domekeMailSender = domekeMailSender;
	}

}
