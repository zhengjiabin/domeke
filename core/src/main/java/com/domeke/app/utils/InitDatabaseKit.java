package com.domeke.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import com.domeke.app.base.config.DruidDatasouceUtil;
import com.google.common.collect.Lists;
import com.jfinal.plugin.druid.DruidPlugin;

public class InitDatabaseKit {

	private static void execute(List<String> sqlList) {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		druidPlugin.start();
		DataSource dataSource = druidPlugin.getDataSource();
		Connection connection = null;
		Statement pst = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			pst = connection.createStatement();
			for (String sql : sqlList) {
				
				pst.addBatch(sql);
			}
			pst.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(connection, pst, rs);
		}
	}

	private static void dbClose(Connection conn, Statement pst, ResultSet rs) {
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
	 * @throws Exception 
	 */
	private static List<String> readSqlScript(String path) throws Exception {
		
		List<String> list = Lists.newArrayList();

		File fileDir = new File(path);
		
		if (fileDir.isDirectory()) {
			
			File[] files = fileDir.listFiles();
			List<File> fileList = Lists.newArrayList();
			
			for (int i = 0; i < files.length; i++) {
				if(files[i].getName().endsWith(".sql"))
				fileList.add(files[i]);
			}
			
			Collections.sort(fileList, new Comparator<File>() {
			    @Override
			    public int compare(File o1, File o2) {
			        if (o1.isDirectory() && o2.isFile())
			            return -1;
			        if (o1.isFile() && o2.isDirectory())
			            return 1;
			        return o1.getName().compareTo(o2.getName());
			    }
			});
			
			for (File sqlScript : fileList) {
				if (sqlScript.isDirectory()) {
					readSqlScript(sqlScript.getAbsolutePath());
				} else {
					InputStream sqlFileIn = null;
					try {
						sqlFileIn = new FileInputStream(sqlScript);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}  
					  
		            StringBuffer sqlSb = new StringBuffer();  
		            byte[] buff = new byte[1024];  
		            int byteRead = 0;  
		            while ((byteRead = sqlFileIn.read(buff)) != -1) {  
		                sqlSb.append(new String(buff, 0, byteRead));  
		            }  
		  
		            // Windows 下换行是 /r/n, Linux 下是 /n  
		            String[] sqlArr = sqlSb.toString().split(";");
		            for (int i = 0; i < sqlArr.length; i++) {  
		            	String sql = sqlArr[i];
		            	 sql = sql.replaceAll("--.*", "").trim();  
		                if (!sql.equals("")) {  
		                    list.add(sql);  
		                    System.out.println("====start===");
		                    System.out.println(sql);
		                    
		                    System.out.println("====end===");
		                }  
		            }

				}
			}
		}

		return list;
	}

	public static void main(String[] args) {
		URL classpath = InitDatabaseKit.class.getClass().getResource("/");
		String path = classpath.getPath().substring(0,classpath.getPath().lastIndexOf("/")-14);
		 path = path + "init-db-mysql";
		 System.out.println(path);
		try {
			List<String> sqlScript = InitDatabaseKit.readSqlScript(path);
			InitDatabaseKit.execute(sqlScript);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
