package com.taotao.cloud.idea.plugin.generateDBEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class DatabaseUtil {

    public static Connection connectToDatabase( String url, String user, String password ) throws SQLException {
        // 加载数据库驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 对于 MySQL
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, password);
    }
}
