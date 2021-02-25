package com.taotao.cloud.java.javaweb.p2_jdbc.dbcp.utils;


import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 使用dbcp的数据库封装类
 */
public class DBUtilDBCP {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static int maxTotal;
	private static int maxIdle;
	private static int minIdle;
	private static int initialSize;
	private static long maxWaitMillis;

	private static BasicDataSource dataSource;

	static {
		dataSource = new BasicDataSource();
		ResourceBundle bundle = ResourceBundle.getBundle("dbcp");
		driver = bundle.getString("driver");
		url = bundle.getString("url");
		username = bundle.getString("username");
		password = bundle.getString("pwd");
		maxTotal = Integer.parseInt(bundle.getString("maxTotal"));
		maxIdle = Integer.parseInt(bundle.getString("maxIdle"));
		minIdle = Integer.parseInt(bundle.getString("minIdle"));
		initialSize = Integer.parseInt(bundle.getString("initialSize"));
		maxWaitMillis = Long.parseLong(bundle.getString("maxWaitMillis"));

		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		dataSource.setMaxTotal(maxTotal);
		dataSource.setMaxIdle(maxIdle);
		dataSource.setMinIdle(minIdle);
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxWaitMillis(maxWaitMillis);

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static void close(ResultSet resultSet, Statement statement, Connection connection) throws SQLException {
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
		if (connection != null)
			connection.close();
	}

}
