package com.taotao.cloud.jdbcpool;

import com.taotao.cloud.jdbcpool.datasource.PooledDataSource;
import com.taotao.cloud.jdbcpool.exception.JdbcPoolException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @author shuigedeng
 * @since 1.1.0
 */
public class PooledDataSourceTest {

    @Test
    public void simpleTest() throws SQLException {
        PooledDataSource source = new PooledDataSource();
        source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        source.setUser("root");
        source.setPassword("123456");
        source.setMinSize(1);

        // 初始化
        source.init();

        Connection connection = source.getConnection();
        System.out.println(connection.getCatalog());

        Connection connection2 = source.getConnection();
        System.out.println(connection2.getCatalog());
    }

    @Test
    public void notWaitTest() throws SQLException {
        PooledDataSource source = new PooledDataSource();
        source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        source.setUser("root");
        source.setPassword("123456");
        source.setMinSize(1);
        source.setMaxSize(1);
        source.setMaxWaitMills(0);

        // 初始化
        source.init();

        Connection connection = source.getConnection();
        System.out.println(connection.getCatalog());

        // 新的线程执行
        newThreadExec(source);

        DateUtil.sleep(100);
    }

    private void newThreadExec(final  PooledDataSource source) {
        // 另起一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 预期报错
                Connection connection2 = null;
                try {
                    connection2 = source.getConnection();
                    System.out.println(connection2.getCatalog());
                } catch (SQLException e) {
                    throw new JdbcPoolException(e);
                }
            }
        }).start();
    }

    @Test
    public void waitTest() throws SQLException {
        PooledDataSource source = new PooledDataSource();
        source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        source.setUser("root");
        source.setPassword("123456");
        source.setMinSize(1);
        source.setMaxSize(1);
        source.setMaxWaitMills(100);

        // 初始化
        source.init();

        Connection connection = source.getConnection();
        System.out.println(connection.getCatalog());

        // 新的线程执行
        newThreadExec(source);

        DateUtil.sleep(10);
        connection.close();
        System.out.println("释放第一个线程的资源。。。");

        DateUtil.sleep(100);
    }

    @Test
    public void testOnIdleTest() throws SQLException {
        PooledDataSource source = new PooledDataSource();
        source.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        source.setUser("root");
        source.setPassword("123456");
        source.setTestOnIdleIntervalSeconds(5);

        // 初始化配置
        source.init();

        Connection connection = source.getConnection();
        System.out.println(connection.getCatalog());

        DateUtil.sleep(30 * 1000);

        connection.close();
    }

}
