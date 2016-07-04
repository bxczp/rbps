package Util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @date 2016年3月5日 DbUtil.java
 * @author CZP
 * @parameter
 */
public class DbUtil {

	String dbUrl = PropertiesUtil.getValue("dbUrl");
	String userName = PropertiesUtil.getValue("userName");
	String password = PropertiesUtil.getValue("password");
	String jdbcName = PropertiesUtil.getValue("jdbcName");

	public Connection getConn() throws Exception {
		Class.forName(jdbcName);
		return DriverManager.getConnection(dbUrl, userName, password);
	}

	public void close(Connection conn) throws Exception {
		if (conn != null) {
			conn.close();
		}
	}

}
