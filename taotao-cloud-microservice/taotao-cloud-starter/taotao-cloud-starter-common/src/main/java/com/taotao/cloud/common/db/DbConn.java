package com.taotao.cloud.common.db;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.TimeWatchUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * DbConn
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:04:17
 */
public final class DbConn implements AutoCloseable {

	/**
	 * conn
	 */
	private Connection conn;

	public DbConn(Connection conn) {
		this.conn = conn;
	}

	public DbConn() {
		try {
			conn = ContextUtil.getBean(DataSource.class, true).getConnection();
		} catch (Exception e) {
			throw new DbException("获取数据库连接异常", "", e);
		}
	}

	public DbConn(DataSource dataSource) {
		try {
			conn = dataSource.getConnection();
		} catch (Exception e) {
			throw new DbException("获取数据库连接异常", "", e);
		}
	}

	public DbConn(String url, String user, String password, String driver) {
		try {
			// 加载数据库驱动
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new DbException("获取数据库连接异常", "", e);
		}
	}

	/**
	 * getPrintSql
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:28
	 */
	private boolean getPrintSql() {
		return true;
	}

	/**
	 * 关闭数据库连接
	 */
	@Override
	public void close() {
		TimeWatchUtil.print(getPrintSql(), "[db]close", () -> {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				throw new DbException("close", "", e);
			}
		});
	}

	/**
	 * beginTransaction
	 *
	 * @param level level
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:33
	 */
	public void beginTransaction(int level) {
		TimeWatchUtil.print(getPrintSql(), "[db]beginTransaction", () -> {
			try {
				if (conn != null) {
					conn.setAutoCommit(false);
					if (level > 0) {
						// Connection.
						conn.setTransactionIsolation(level);
					}
				}
			} catch (Exception e) {
				throw new DbException("beginTransaction", "", e);
			}
		});
	}

	/**
	 * commit
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:37
	 */
	public void commit() {
		TimeWatchUtil.print(getPrintSql(), "[db]commit", () -> {
			try {
				if (conn != null) {
					conn.commit();
					conn.setAutoCommit(true);
				}
			} catch (Exception e) {
				throw new DbException("commit", "", e);
			}
		});
	}

	/**
	 * rollback
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:39
	 */
	public void rollback() {
		TimeWatchUtil.print(getPrintSql(), "[db]rollback", () -> {
			try {
				if (conn != null) {
					conn.rollback();
					conn.setAutoCommit(true);
				}
			} catch (Exception e) {
				// 此处不抛异常
				throw new DbException("rollback", "", e);
			}
		});
	}

	/**
	 * executeSql
	 *
	 * @param sql             sql
	 * @param parameterValues parameterValues
	 * @return int
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:44
	 */
	public int executeSql(final String sql, final Object[] parameterValues) {
		return TimeWatchUtil.print(getPrintSql(), "[db]" + sql, () -> {
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				attachParameterObjects(statement, parameterValues);
				return statement.executeUpdate();
			} catch (Exception e) {
				throw new DbException("executeSql", sql, e);
			}
		});
	}

	/**
	 * executeScalar
	 *
	 * @param sql             sql
	 * @param parameterValues parameterValues
	 * @return {@link java.lang.Object }
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:48
	 */
	public Object executeScalar(final String sql, final Object[] parameterValues) {
		try {
			Object value = null;
			try (ResultSet rs = executeResultSet(sql, parameterValues)) {
				if (rs != null && rs.next()) {
					value = rs.getObject(1);
				}
				return value;
			}
		} catch (Exception e) {
			throw new DbException("executeScalar", sql, e);
		}
	}

	/**
	 * executeResultSet
	 *
	 * @param sql             sql
	 * @param parameterValues parameterValues
	 * @return {@link java.sql.ResultSet }
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:53
	 */
	public ResultSet executeResultSet(final String sql, final Object[] parameterValues) {
		return TimeWatchUtil.print(getPrintSql(), "[db]" + sql, () -> {
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				attachParameterObjects(statement, parameterValues);
				ResultSet rs = statement.executeQuery();
				// statement.clearParameters();
				return rs;
			} catch (Exception e) {
				throw new DbException("executeResultSet", sql, e);
			}
		});
	}

	/**
	 * executeList
	 *
	 * @param sql             sql
	 * @param parameterValues parameterValues
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 20:04:57
	 */
	public List<Map<String, Object>> executeList(final String sql, final Object[] parameterValues) {
		return TimeWatchUtil.print(getPrintSql(), "[db]" + sql, () -> {
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				attachParameterObjects(statement, parameterValues);
				List<Map<String, Object>> map = null;
				try (ResultSet rs = statement.executeQuery()) {
					map = toMapList(rs);
				}
				return map;

			} catch (Exception e) {
				throw new DbException("executeResultSet", sql, e);
			}
		});

	}

	/**
	 * toMapList
	 *
	 * @param rs rs
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 20:05:00
	 */
	public List<Map<String, Object>> toMapList(ResultSet rs) {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			if (rs != null && !rs.isClosed()) {
				ResultSetMetaData meta = rs.getMetaData();
				int colCount = meta.getColumnCount();
				int rowsCount = -1;
				while (rs.next()) {
					HashMap<String, Object> map = (rowsCount > 0 ? new HashMap<>(rowsCount)
						: new HashMap<>());
					for (int i = 1; i <= colCount; i++) {
						String key = meta.getColumnName(i);
						Object value = rs.getObject(i);
						map.put(key, value);
					}
					rowsCount = map.size();
					list.add(map);
				}
			}
			return list;
		} catch (Exception exp) {
			throw new DbException("toMapList", "", exp);
		}
	}

	/**
	 * attachParameterObjects
	 *
	 * @param statement statement
	 * @param values    values
	 * @author shuigedeng
	 * @since 2021-09-02 20:05:03
	 */
	private void attachParameterObjects(PreparedStatement statement, Object[] values)
		throws Exception {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] instanceof java.util.Date) {
					statement.setObject(i + 1,
						new Timestamp(((java.util.Date) values[i]).getTime()));
				} else {
					statement.setObject(i + 1, values[i]);
				}
			}
		}
	}

	/**
	 * tableIsExist
	 *
	 * @param tablename tablename
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 20:05:06
	 */
	public boolean tableIsExist(String tablename) {
		List<Map<String, Object>> ds = executeList("Select name from sysobjects where Name=?",
			new Object[]{tablename});
		return ds != null && ds.size() != 0;
	}

	/**
	 * DbException
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:05:11
	 */
	public static class DbException extends RuntimeException {

		public DbException(String message, String sql, Exception exp) {
			super(message, exp);

			LogUtil.error("错误sql:" + sql);
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
