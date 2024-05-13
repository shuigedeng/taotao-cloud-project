package com.github.houbb.thread.pool.datasource;

/**
 * @author shuigedeng
 * @since 1.0.0
 */
public class AbstractDataSourceConfig extends DataSourceConfigAdaptor {

    /**
     * 驱动类
     * @since 1.0.0
     */
    protected String driverClass;

    /**
     * jdbc url
     * @since 1.0.0
     */
    protected String jdbcUrl;

    /**
     * 用户
     * @since 1.0.0
     */
    protected String user;

    /**
     * 密码
     * @since 1.0.0
     */
    protected String password;

    public String getDriverClass() {
        return driverClass;
    }

    @Override
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
