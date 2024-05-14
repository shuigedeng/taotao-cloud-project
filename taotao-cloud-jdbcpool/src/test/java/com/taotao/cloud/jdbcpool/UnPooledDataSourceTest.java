package com.taotao.cloud.jdbcpool;

import com.taotao.cloud.jdbcpool.datasource.UnPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author shuigedeng
 * @since 1.0.0
 */
public class UnPooledDataSourceTest {

    @Test
    public void simpleTest() throws SQLException {
        UnPooledDataSource source = new UnPooledDataSource();
        source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        source.setUser("root");
        source.setPassword("123456");

        Connection connection = source.getConnection();
        System.out.println(connection.getCatalog());
    }

}
