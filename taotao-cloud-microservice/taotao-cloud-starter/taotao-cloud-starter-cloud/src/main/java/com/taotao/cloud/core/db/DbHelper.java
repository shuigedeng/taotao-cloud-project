package com.taotao.cloud.core.db;

import com.taotao.cloud.core.model.Callable;
import java.util.HashMap;
import javax.sql.DataSource;

/**
 * @author: chejiangyi
 * @version: 2019-10-23 16:05 Dbconn 简化使用和事务支持
 **/
public class DbHelper {

	private static final ThreadLocal<HashMap<DataSource, DbConn>> connTransactionTheadLocal = new ThreadLocal<>();

	public static <T> T transactionGet(DataSource dataSource, Callable.Func1<T, DbConn> action0) {
		//若有线程同一个数据源事务,则使用事务
		if (connTransactionTheadLocal.get() != null && connTransactionTheadLocal.get()
			.containsKey(dataSource)) {
			return action0.invoke(connTransactionTheadLocal.get().get(dataSource));
		} else {
			return get(dataSource, action0);
		}
	}

	public static void transactionCall(DataSource dataSource, Callable.Action1<DbConn> action0) {
		transactionGet(dataSource, (c) -> {
			action0.invoke(c);
			return true;
		});
	}

	public static void call(DataSource dataSource, Callable.Action1<DbConn> action0) {
		get(dataSource, (db) -> {
			action0.invoke(db);
			return true;
		});
	}

	public static <T> T get(DataSource dataSource, Callable.Func1<T, DbConn> action0) {
		try (DbConn db2 = new DbConn(dataSource)) {
			return action0.invoke(db2);
		}
	}

	public static void transaction(DataSource dataSource, int level, Callable.Action0 action0) {
		//事务嵌套,则以最外层事务优先（事务仅对同一个数据源有效,不同数据源不互相影响）
		if (connTransactionTheadLocal.get() != null && connTransactionTheadLocal.get()
			.containsKey(dataSource)) {
			action0.invoke();
		} else {
			DbConn db = null;
			try {
				//如果设置事务隔离级别,则开启事务;否则不使用事务。
				if (level > 0) {
					if (connTransactionTheadLocal.get() == null) {
						connTransactionTheadLocal.set(new HashMap<DataSource, DbConn>());
					}
					connTransactionTheadLocal.get().put(dataSource, new DbConn(dataSource));
					db = connTransactionTheadLocal.get().get(dataSource);
					db.beginTransaction(level);
				}
				action0.invoke();
				if (db != null) {
					db.commit();
				}
			} catch (Exception e) {
				if (db != null) {
					db.rollback();
				}
				throw e;
			} finally {
				if (db != null) {
					db.close();
					connTransactionTheadLocal.set(null);
				}
			}
		}
	}
}
