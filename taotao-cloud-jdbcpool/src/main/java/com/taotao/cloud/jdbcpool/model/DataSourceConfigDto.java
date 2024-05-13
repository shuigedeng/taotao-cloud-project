package com.github.houbb.thread.pool.model;

import java.io.Serializable;

/**
 * 配置对象
 *
 * @since 1.7.0
 */
public class DataSourceConfigDto implements Serializable {

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

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DataSourceConfigDto{" +
                "driverClass='" + driverClass + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
