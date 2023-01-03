package com.taotao.cloud.data.analysis.datasource.hive;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.data.analysis.datasource.DataSourceCommonProperties;
import com.taotao.cloud.data.analysis.datasource.DataSourceProperties;
import com.taotao.cloud.data.analysis.datasource.trino.TrinoDataSourceConfiguration;
import java.sql.SQLException;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.clickhouse.enabled", havingValue = "true")
public class HiveDataSourceConfiguration {

	private static Logger logger = LoggerFactory.getLogger(TrinoDataSourceConfiguration.class);

	@Autowired
	private DataSourceProperties dataSourceProperties;

	@Autowired
	private DataSourceCommonProperties dataSourceCommonProperties;

	@Bean("hiveDruidDataSource") //新建bean实例
	@Qualifier("hiveDruidDataSource")//标识
	public DataSource dataSource(){
		TimeZone.setDefault(TimeZone.getTimeZone("+08:00"));
		DruidDataSource datasource = new DruidDataSource();

		//配置数据源属性
		datasource.setUrl(dataSourceProperties.getHive().getUrl());
		datasource.setUsername(dataSourceProperties.getHive().getUsername());
        datasource.setPassword(dataSourceProperties.getHive().getPassword());
		datasource.setDriverClassName(dataSourceProperties.getHive().getDriverClassName());

		//配置统一属性
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

	@Bean(name = "hiveTemplate")
	public JdbcTemplate prestoJdbcTemplate(@Qualifier("hiveDruidDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}


}

