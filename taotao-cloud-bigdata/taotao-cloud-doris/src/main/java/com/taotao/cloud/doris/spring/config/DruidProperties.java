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

package com.taotao.cloud.doris.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * DruidProperties
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Configuration
public class DruidProperties {

    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    public DruidDataSource dataSource( DruidDataSource datasource ) {
        /** Configure initial size, minimum, maximum */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** Configure the timeout period to wait for the connection to be acquired */
        datasource.setMaxWait(maxWait);

        /** Configure how long the interval is to perform a check, check idle connections that need to be closed, the unit is milliseconds */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** Configure the minimum and maximum survival time of a connection in the pool, in milliseconds */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * The sql used to check whether the connection is valid, the requirement is a query statement, usually select'x'. If the validationQuery is null, testOnBorrow, testOnReturn, testWhileIdle will not work.
         */
        datasource.setValidationQuery(validationQuery);
        /** It is recommended to configure it as true to not affect performance and ensure safety. Check when applying for a connection. If the idle time is greater than timeBetweenEvictionRunsMillis, execute a validationQuery to check whether the connection is valid. */
        datasource.setTestWhileIdle(testWhileIdle);
        /** When applying for a connection, execute validationQuery to check whether the connection is valid. This configuration will reduce performance. */
        datasource.setTestOnBorrow(testOnBorrow);
        /** When the connection is returned, the validationQuery is executed to check whether the connection is valid. This configuration will reduce performance. */
        datasource.setTestOnReturn(testOnReturn);
        return datasource;
    }
}
