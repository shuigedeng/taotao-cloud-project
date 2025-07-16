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

package com.taotao.cloud.hive.spring.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/30 10:09
 */
public class HiveJdbcConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcConfiguration.class);

    @Value("${spring.datasource.hive.url}")
    private String url;

    @Value("${spring.datasource.hive.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.hive.username}")
    private String user;

    @Value("${spring.datasource.hive.password}")
    private String password;

    @Value("${spring.datasource.commonConfig.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.commonConfig.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.commonConfig.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.commonConfig.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.commonConfig.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.commonConfig.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.commonConfig.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.commonConfig.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.commonConfig.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.commonConfig.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.commonConfig.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.commonConfig.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;

    @Value("${spring.datasource.commonConfig.filters}")
    private String filters;

    @Bean(name = "hiveDruidDataSource")
    @Qualifier("hiveDruidDataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        // 配置数据源属性
        datasource.setUrl(url);
        datasource.setDriverClassName(driver);
        datasource.setUsername(user);
        datasource.setPassword(password);
        // 配置统一属性
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("Druid configuration initialization filter error.", e);
        }
        return datasource;
    }

    @Bean(name = "hiveDruidTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
