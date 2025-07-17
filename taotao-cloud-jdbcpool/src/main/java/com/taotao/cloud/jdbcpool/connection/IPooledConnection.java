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

package com.taotao.cloud.jdbcpool.connection;

import com.taotao.cloud.jdbcpool.api.IPooledDataSourceConfig;
import java.sql.Connection;

/**
 * 池化的连接池
 * @since 1.1.0
 */
public interface IPooledConnection extends Connection {

    /**
     * 是否繁忙
     * @since 1.1.0
     * @return 状态
     */
    boolean isBusy();

    /**
     * 设置状态
     * @param busy 状态
     * @since 1.1.0
     */
    void setBusy(boolean busy);

    /**
     * 获取真正的连接
     * @return 连接
     * @since 1.1.0
     */
    Connection getConnection();

    /**
     * 设置连接信息
     * @param connection 连接信息
     * @since 1.1.0
     */
    void setConnection(Connection connection);

    /**
     * 设置对应的数据源
     * @param dataSource 数据源
     * @since 1.5.0
     */
    void setDataSource(final IPooledDataSourceConfig dataSource);

    /**
     * 获取对应的数据源信息
     * @return 数据源
     * @since 1.5.0
     */
    IPooledDataSourceConfig getDataSource();
}
