/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
