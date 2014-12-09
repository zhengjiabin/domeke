package com.domeke.app.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.base.config.DruidDatasouceUtil;
import com.domeke.app.model.CodeTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

public class CodeKit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 785539256371426468L;

	private static Map<String, Record> codeTableMap;

	private static Map<String, Map<String, Record>> codeTypeMap;

	private Logger logger = LoggerFactory.getLogger(CodeKit.class);

	private static String LOAD_CODE_TABLE = "SELECT T.CODETYPE,T.TYPENAME,C.CODEKEY,C.CODENAME,C.CODEVALUE,C.SORTNUM FROM CODE_TYPE T,CODE_TABLE C WHERE T.CODETYPE=C.CODETYPE AND C.STATUS = '0' order by c.sortnum";

	public void init() throws ServletException {
		load();
	}

	private void load() {
		List<Record> codetableList = query(LOAD_CODE_TABLE);
		logger.info("======加载码表数量={}==========", codetableList.size());
		codeTypeMap = Maps.newHashMap();
		for (Record codeTable : codetableList) {
			String codeType = codeTable.getStr("codetype");
			String codeKey = codeTable.getStr("codekey");
			if (!codeTypeMap.containsKey(codeType)) {
				codeTableMap = Maps.newHashMap();
				codeTypeMap.put(codeType, codeTableMap);
				codeTableMap.put(codeKey, codeTable);
			} else {
				codeTableMap = codeTypeMap.get(codeType);
				codeTableMap.put(codeKey, codeTable);
			}
		}
	}

	/**
	 * 获取码表值
	 * 
	 * @param codeType
	 *            码表名称
	 * @param codeKey
	 *            码表对应键
	 * @return
	 */
	public static String getValue(String codeType, String codeKey) {
		Map<String, Record> codeMap = Maps.newHashMap();
		if (codeTypeMap != null) {
			codeMap = codeTypeMap.get(codeType);
		} else {
			CodeKit kit = new CodeKit();
			try {
				kit.init();
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		return codeMap != null && codeMap.get(codeKey) != null ? codeMap.get(codeKey).get("codevalue") : "";
	}

	/**
	 * 返回码表列表
	 * 
	 * @param codeType
	 * @return
	 */
	public static List<CodeTable> getList(String codeType) {
		codeTableMap = codeTypeMap.get(codeType);
		Set<String> keySet = codeTableMap.keySet();
		CodeTable codeTable = null;
		List<CodeTable> list = Lists.newArrayList();
		for (String key : keySet) {
			codeTable = new CodeTable();
			codeTable.set("codekey", codeTableMap.get(key).get("codekey"));
			codeTable.set("codetype", codeTableMap.get(key).get("codetype"));
			codeTable.set("codevalue", codeTableMap.get(key).get("codevalue"));
			codeTable.set("codename", codeTableMap.get(key).get("codename"));
			codeTable.set("sortnum", codeTableMap.get(key).get("sortnum"));
			list.add(codeTable);
		}
		Collections.sort(list);
		return list;
	}

	private List<Record> query(String sql) {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		druidPlugin.start();
		DataSource dataSource = druidPlugin.getDataSource();
		List<Record> codeTableList = Lists.newArrayList();
		Connection connection = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			Record codeTable = null;
			while (rs.next()) {
				codeTable = new Record();
				codeTable.set("codetype", rs.getString("codetype"));
				codeTable.set("typename", rs.getString("typename"));
				codeTable.set("codekey", rs.getString("codekey"));
				codeTable.set("codename", rs.getString("codename"));
				codeTable.set("codevalue", rs.getString("codevalue"));
				codeTable.set("sortnum", rs.getString("sortnum"));
				codeTableList.add(codeTable);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbClose(connection, pst, rs);
		}
		return codeTableList;
	}

	private void dbClose(Connection conn, PreparedStatement pst, ResultSet rs) {
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
}
