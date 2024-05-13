package com.github.houbb.thread.pool.bs;

import com.github.houbb.thread.pool.constant.DriverNameConst;
import com.github.houbb.thread.pool.constant.PooledConst;
import com.github.houbb.thread.pool.datasource.PooledDataSource;
import com.github.houbb.thread.pool.datasource.UnPooledDataSource;

import javax.sql.DataSource;

/**
 * jdbc 线程池引导类
 * @since 1.6.0
 */
public class JdbcPoolBs {

    /**
     * 驱动类
     * @since 0.0.9
     */
    private String driverClass = DriverNameConst.MYSQL_8;

    private String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";

    private String username = "root";

    private String password = "123456";

    /**
     * 最小尺寸
     * @since 1.1.0
     */
    private int minSize = PooledConst.DEFAULT_MIN_SIZE;

    /**
     * 最大尺寸
     * @since 1.1.0
     */
    private int maxSize = PooledConst.DEFAULT_MAX_SIZE;

    /**
     * 最大的等待时间
     * @since 1.3.0
     */
    private long maxWaitMills = PooledConst.DEFAULT_MAX_WAIT_MILLS;

    /**
     * 验证查询
     * @since 1.5.0
     */
    private String validQuery = PooledConst.DEFAULT_VALID_QUERY;

    /**
     * 验证的超时时间
     * @since 1.5.0
     */
    private int validTimeOutSeconds = PooledConst.DEFAULT_VALID_TIME_OUT_SECONDS;

    /**
     * 获取时验证
     * @since 1.5.0
     */
    private boolean testOnBorrow = PooledConst.DEFAULT_TEST_ON_BORROW;

    /**
     * 归还时验证
     * @since 1.5.0
     */
    private boolean testOnReturn = PooledConst.DEFAULT_TEST_ON_RETURN;

    /**
     * 闲暇时验证
     * @since 1.5.0
     */
    private boolean testOnIdle = PooledConst.DEFAULT_TEST_ON_IDLE;

    /**
     * 闲暇时验证的时间间隔
     * @since 1.5.0
     */
    private long testOnIdleIntervalSeconds = PooledConst.DEFAULT_TEST_ON_IDLE_INTERVAL_SECONDS;

    public JdbcPoolBs driverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public JdbcPoolBs url(String url) {
        this.url = url;
        return this;
    }

    public JdbcPoolBs username(String username) {
        this.username = username;
        return this;
    }

    public JdbcPoolBs password(String password) {
        this.password = password;
        return this;
    }

    public JdbcPoolBs minSize(int minSize) {
        this.minSize = minSize;
        return this;
    }

    public JdbcPoolBs maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public JdbcPoolBs maxWaitMills(long maxWaitMills) {
        this.maxWaitMills = maxWaitMills;
        return this;
    }

    public JdbcPoolBs validQuery(String validQuery) {
        this.validQuery = validQuery;
        return this;
    }

    public JdbcPoolBs validTimeOutSeconds(int validTimeOutSeconds) {
        this.validTimeOutSeconds = validTimeOutSeconds;
        return this;
    }

    public JdbcPoolBs testOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }

    public JdbcPoolBs testOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        return this;
    }

    public JdbcPoolBs testOnIdle(boolean testOnIdle) {
        this.testOnIdle = testOnIdle;
        return this;
    }

    public JdbcPoolBs testOnIdleIntervalSeconds(long testOnIdleIntervalSeconds) {
        this.testOnIdleIntervalSeconds = testOnIdleIntervalSeconds;
        return this;
    }

    public static JdbcPoolBs newInstance() {
        return new JdbcPoolBs();
    }

    /**
     * 简单的池化功能
     * @return 池化
     */
    public DataSource pooled() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(url);

        dataSource.setMinSize(minSize);
        dataSource.setMaxSize(maxSize);
        dataSource.setMaxWaitMills(maxWaitMills);

        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnIdle(testOnIdle);
        dataSource.setTestOnIdleIntervalSeconds(testOnIdleIntervalSeconds);
        dataSource.setTestOnReturn(testOnReturn);

        dataSource.setValidQuery(validQuery);
        dataSource.setValidTimeOutSeconds(validTimeOutSeconds);

        dataSource.init();
        return dataSource;
    }

    /**
     * 非池化
     * @return 结果
     */
    public DataSource unPooled() {
        UnPooledDataSource dataSource = new UnPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl(url);
        return dataSource;
    }

}
