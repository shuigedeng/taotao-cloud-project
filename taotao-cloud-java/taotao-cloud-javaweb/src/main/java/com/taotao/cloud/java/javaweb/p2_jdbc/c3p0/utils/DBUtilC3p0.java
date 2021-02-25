package com.taotao.cloud.java.javaweb.p2_jdbc.c3p0.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 使用c3p0连接池的工具类
 */
public class DBUtilC3p0 {

    private static ComboPooledDataSource dataSource=new ComboPooledDataSource("c3p0-config");

    public static Connection getConnection() throws SQLException {
        return  dataSource.getConnection();
    }
    public static void close(ResultSet resultSet, Statement statement,Connection connection) throws SQLException {
        if(resultSet!=null) {
			resultSet.close();
		}
        if(statement!=null) {
			statement.close();
		}
        if(connection!=null) {
			connection.close();
		}
    }

}
