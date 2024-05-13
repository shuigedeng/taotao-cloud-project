package com.taotao.cloud.jdbcpool.api;

import com.taotao.cloud.jdbcpool.connection.IPooledConnection;

/**
 * 池化的接口
 * @author shuigedeng
 * @since 1.0.0
 */
public interface IPooledDataSourceConfig extends IDataSourceConfig {

    /**
     * 归还连接
     * @param pooledConnection 连接池信息
     * @since 1.5.0
     */
    void returnConnection(IPooledConnection pooledConnection);

    /**
     * 设置最小尺寸
     *
     * @param minSize 大小
     * @since 1.1.0
     */
    void setMinSize(final int minSize);

    /**
     * 设置最大的大小
     *
     * @param maxSize 最大的大小
     * @since 1.1.0
     */
    void setMaxSize(final int maxSize);

    /**
     * 设置最大的等待时间
     * @param maxWaitMills 最大的等待时间
     * @since 1.1.0
     */
    void setMaxWaitMills(final long maxWaitMills);

    /**
     * 设置验证查询的语句
     *
     * 如果这个值为空，那么 {@link #setTestOnBorrow(boolean)}
     * {@link #setTestOnIdle(boolean)}}
     * {@link #setTestOnReturn(boolean)}
     * 都将无效
     * @param validQuery 验证查询的语句
     * @since 1.5.0
     */
    void setValidQuery(final String validQuery);

    /**
     * 验证的超时秒数
     * @param validTimeOutSeconds 验证的超时秒数
     * @since 1.5.0
     */
    void setValidTimeOutSeconds(final int validTimeOutSeconds);

    /**
     * 获取连接时进行校验
     *
     * 备注：影响性能
     * @param testOnBorrow 是否
     * @since 1.5.0
     */
    void setTestOnBorrow(final boolean testOnBorrow);

    /**
     * 归还连接时进行校验
     *
     * 备注：影响性能
     * @param testOnReturn 归还连接时进行校验
     * @since 1.5.0
     */
    void setTestOnReturn(final boolean testOnReturn);

    /**
     * 闲暇的时候进行校验
     * @param testOnIdle 闲暇的时候进行校验
     * @since 1.5.0
     */
    void setTestOnIdle(final boolean testOnIdle);

    /**
     * 闲暇时进行校验的时间间隔
     * @param testOnIdleIntervalSeconds 时间间隔
     * @since 1.5.0
     */
    void setTestOnIdleIntervalSeconds(final long testOnIdleIntervalSeconds);

}
