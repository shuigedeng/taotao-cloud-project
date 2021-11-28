package com.taotao.cloud.bigdata.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.bigdata.properties.BigdataProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableConfigurationProperties(BigdataProperties.class)
@ConditionalOnProperty(name = "bsf.clickhouse.enabled", havingValue = "true")
public class ClickHouseConfiguration {

	@ConfigurationProperties("spring.datasource.clickhouse")
	@Bean(name = "clickHouseDataSource", destroyMethod = "close")
	@Lazy
	public DruidDataSource clickHouseDataSource() {
		return (DruidDataSource) DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

}

