package com.taotao.cloud.web.configuration.bigdata;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ConditionalOnProperty(name = "taotao.cloud.data.clickhouse.enabled", havingValue = "true")
public class ClickHouseConfiguration {

	@ConfigurationProperties("spring.datasource.clickhouse")
	@Bean(name = "clickHouseDataSource", destroyMethod = "close")
	@Lazy
	public DruidDataSource clickHouseDataSource() {
		return (DruidDataSource) DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

}

