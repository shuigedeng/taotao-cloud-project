package com.taotao.cloud.java.javaweb.p12_myshop.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class C3P0Utils {
	//获取数据源 C3P0配置文件信息 自动获取
	private static DataSource ds = new ComboPooledDataSource();
	//对外提供一个get方法 可以通过外部访问到DataSource
	public static DataSource getDataSource(){
		return ds;
	}
	//获取连接
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("服务器繁忙....");
		}
	}
	//释放资源
	public static void release(ResultSet rs,Statement stmt,Connection conn){
		try {
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
