package com.taotao.cloud.jdbcpool.api;

/**
 * 配置接口
 * @author shuigedeng
 * @since 1.0.0
 */
public interface IConfig {

    /**
     * 设置驱动类
     *
     * @param driverClass 驱动类
     * @since 1.0.0
     */
    void setDriverClass(final String driverClass);

    /**
     * jdbc url
     * @param jdbcUrl url
     * @since 1.0.0
     */
    void setJdbcUrl(final String jdbcUrl);

    /**
     * 设置用户信息
     * @param user 用户信息
     * @since 1.0.0
     */
    void setUser(final String user);

    /**
     * 设置密码
     * @param password 密码
     * @since 1.0.0
     */
    void setPassword(final String password);

}
