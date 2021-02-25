package com.taotao.cloud.java.javaweb.p2_jdbc.druid.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description 使用druid连接池的工具类
 */
public class DBUtilDruid {

    private static DataSource dataSource;

    static {
        InputStream resourceAsStream = DBUtilDruid.class.getClassLoader().getResourceAsStream("druid.properties");
        Properties properties=new Properties();
        try {
            properties.load(resourceAsStream);

            dataSource = DruidDataSourceFactory.createDataSource(properties);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) throws SQLException {
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
