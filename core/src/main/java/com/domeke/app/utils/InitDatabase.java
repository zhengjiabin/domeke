package com.domeke.app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDatabase {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String url = null;
		String user = null;
		String password = null;
		String sql = null;
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载mysq驱动
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载错误");
			e.printStackTrace();// 打印出错详细信息
		}
		try {
			url = "jdbc:mysql://localhost/domeke?characterEncoding=UTF-8&autoReconnect = true";// 简单写法：url
																								// =
																								// "jdbc:myqsl://localhost/test(数据库名)? user=root(用户)&password=yqs2602555(密码)";
			user = "demeke";
			password = "demeke";
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("数据库链接错误");
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
			while (rs.next()) {
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 关闭数据库
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			System.out.println("数据库关闭错误");
			e.printStackTrace();
		}
	}
}
