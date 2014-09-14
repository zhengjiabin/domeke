package com.domeke.app.utils;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import com.domeke.app.base.config.DruidDatasouceUtil;
import com.google.common.collect.Lists;
import com.jfinal.plugin.druid.DruidPlugin;

public class InitDatabaseKit {

	private void execute(String sql) {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		druidPlugin.start();
		DataSource dataSource = druidPlugin.getDataSource();
		Connection connection = null;
		Statement pst = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			pst = connection.createStatement();
			pst.addBatch(sql);
			pst.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(connection, pst, rs);
		}
	}

	private void dbClose(Connection conn, Statement pst, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (pst != null) {
				conn.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取SQL脚本 默认放在项目路径中的init-db-mysql
	 * @return
	 */
	private static List<String> readSqlScript(String path) {

		File fileDir = new File(path);

		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			for (File sqlScript : files) {
				if (sqlScript.isDirectory()) {
					readSqlScript(sqlScript.getAbsolutePath());
				} else {

				}
			}
		}

		List<String> list = Lists.newArrayList();

		return list;
	}

	public static void main(String[] args) {
		URL classpath = InitDatabaseKit.class.getClass().getResource("/");
		String path = classpath.getPath() + "/init-db-mysql";
		InitDatabaseKit.readSqlScript(path);
	}
}
