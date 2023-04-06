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

package com.taotao.cloud.data.analysis.datasource.tidb;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.data.analysis.datasource.DataSourceCommonProperties;
import com.taotao.cloud.data.analysis.datasource.DataSourceProperties;
import com.taotao.cloud.data.analysis.datasource.trino.TrinoDataSourceConfiguration;
import java.sql.SQLException;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.tidb.enabled", havingValue = "true")
@MapperScan(basePackages = "com.taotao.cloud.data.analysis.tidb.mapper", sqlSessionFactoryRef = "tidbSqlSessionFactory")
public class TidbDataSourceConfiguration {

    private static Logger logger = LoggerFactory.getLogger(TrinoDataSourceConfiguration.class);

    // 这里是mapper.xml路径， 根据自己的项目调整
    private static final String MAPPER_LOCATION = "classpath*:mapper/tidb/*.xml";
    // 这里是数据库表对应的entity实体类所在包路径， 根据自己的项目调整
    private static final String TYPE_ALIASES_PACKAGE = "com.taotao.cloud.data.analysis.tidb.*";

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DataSourceCommonProperties dataSourceCommonProperties;

    @Bean("tidbDruidDataSource") // 新建bean实例
    @Qualifier("tidbDruidDataSource") // 标识
    public DataSource dataSource() {
        TimeZone.setDefault(TimeZone.getTimeZone("+08:00"));
        DruidDataSource datasource = new DruidDataSource();

        // 配置数据源属性
        datasource.setUrl(dataSourceProperties.getTidb().getUrl());
        ;
        datasource.setUsername(dataSourceProperties.getTidb().getUsername());
        datasource.setPassword(dataSourceProperties.getTidb().getPassword());
        datasource.setDriverClassName(dataSourceProperties.getTidb().getDriverClassName());

        // 配置统一属性
        datasource.setInitialSize(dataSourceCommonProperties.getInitialSize());
        datasource.setMinIdle(dataSourceCommonProperties.getMinIdle());
        datasource.setMaxActive(dataSourceCommonProperties.getMaxActive());
        datasource.setMaxWait(dataSourceCommonProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceCommonProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceCommonProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceCommonProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceCommonProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceCommonProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceCommonProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(dataSourceCommonProperties.isPoolPreparedStatements());
        try {
            datasource.setFilters(dataSourceCommonProperties.getFilters());
        } catch (SQLException e) {
            logger.error("Druid configuration initialization filter error.", e);
        }
        return datasource;
    }

    @Bean(name = "tidbTemplate")
    public JdbcTemplate prestoJdbcTemplate(@Qualifier("tidbDruidDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("tidbSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("tidbDruidDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        bean.setTypeAliasesPackage(TYPE_ALIASES_PACKAGE);
        return bean.getObject();
    }

    @Bean("tidbSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("tidbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
