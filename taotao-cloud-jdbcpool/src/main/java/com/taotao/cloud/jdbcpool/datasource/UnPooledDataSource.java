package com.taotao.cloud.jdbcpool.datasource;

import com.taotao.cloud.jdbcpool.util.DriverClassUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author shuigedeng
 * @since 1.0.0
 */
public class UnPooledDataSource extends AbstractDataSourceConfig {

    @Override
    public Connection getConnection() throws SQLException {
        DriverClassUtil.loadDriverClass(super.driverClass, super.jdbcUrl);

        return DriverManager.getConnection(super.getJdbcUrl(),
                super.getUser(), super.getPassword());
    }

}
