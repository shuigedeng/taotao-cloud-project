package com.taotao.cloud.generator.maku.utils;

import com.taotao.cloud.generator.maku.config.DbType;
import com.taotao.cloud.generator.maku.config.GenDataSource;
//import oracle.jdbc.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB工具类
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class DbUtils {
    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    /**
     * 获得数据库连接
     */
    public static Connection getConnection(GenDataSource dataSource) throws ClassNotFoundException, SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        Class.forName(dataSource.getDbType().getDriverClass());

        Connection connection = DriverManager.getConnection(dataSource.getConnUrl(), dataSource.getUsername(), dataSource.getPassword());
//        if (dataSource.getDbType() == DbType.Oracle) {
//            ((OracleConnection) connection).setRemarksReporting(true);
//        }

        return connection;
    }


}
