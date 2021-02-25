package com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.utils;


import com.taotao.cloud.java.javaweb.p2_jdbc.jdbc.advanced.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 复用 增、删、改、查方法
 */
public class DaoUtils<T> {

	/**
	 * 公共处理增、删、改的方法
	 *
	 * @param sql  执行的sql语句
	 * @param args 参数列表
	 * @return 受影响的行数
	 */
	public int commonsUpdate(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		connection = DbUtils.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			int result = preparedStatement.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeAll(null, preparedStatement, null);
		}
		return 0;
	}

	/**
	 * 公共的查询方法(可查询任意一张表、可查询单个对象、也可查询多个对象)
	 *
	 * @param sql  执行sql语句
	 * @param args 参数列表
	 * @return 集合
	 */
	public List<T> commonsSelect(String sql, RowMapper<T> rowMapper, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> list = new ArrayList<>();

		connection = DbUtils.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				// id name age  borndate email address
				//如何根据查询结果完成ORM，如何进行对象的创建及赋值
				T t = rowMapper.getRow(resultSet);//回调 -->调用者提供的一个封装方法ORM
				list.add(t);
			}
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
}
