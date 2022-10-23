package com.taotao.cloud.data.jpa.tenancy;

import com.taotao.cloud.data.jpa.tenancy.properties.MultiTenancyDataSource;
import com.taotao.cloud.data.jpa.tenancy.properties.MultiTenancyProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Description: DataSource 查询 </p>
 */
public class MultiTenancyDataSourceLookup extends MapDataSourceLookup {

	private final MultiTenancyProperties multiTenancyProperties;

	public MultiTenancyDataSourceLookup(DataSource defaultDataSource,
										MultiTenancyProperties multiTenancyProperties) {
		this.multiTenancyProperties = multiTenancyProperties;
		initDefaultDataSource(defaultDataSource);
		initDataSource(defaultDataSource);
	}

	private void initDefaultDataSource(DataSource defaultDataSource) {
		addDataSource("master", defaultDataSource);
	}

	private void initDataSource(DataSource defaultDataSource) {
		Map<String, MultiTenancyDataSource> dataSources = multiTenancyProperties.getDataSources();
		if (MapUtils.isNotEmpty(dataSources)) {
			dataSources.forEach((tenantIdentifier, multiTenancyDataSource) -> {
				addDataSource(tenantIdentifier,
					createDataSource(defaultDataSource, multiTenancyDataSource));
			});
		}
	}

	private DataSource createDataSource(DataSource defaultDataSource,
										MultiTenancyDataSource multiTenancyDataSource) {
		if (defaultDataSource instanceof HikariDataSource) {
			HikariDataSource defaultHikariDataSource = (HikariDataSource) defaultDataSource;
			Properties defaultDataSourceProperties = defaultHikariDataSource.getDataSourceProperties();
			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setDriverClassName(multiTenancyDataSource.getDriverClassName());
			hikariConfig.setJdbcUrl(multiTenancyDataSource.getUrl());
			hikariConfig.setUsername(multiTenancyDataSource.getUsername());
			hikariConfig.setPassword(multiTenancyDataSource.getPassword());

			if (ObjectUtils.isNotEmpty(defaultDataSource)) {
				defaultDataSourceProperties.forEach(
					(key, value) -> hikariConfig.addDataSourceProperty(String.valueOf(key), value));
			}

			return new HikariDataSource(hikariConfig);
		} else {
			return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.url(multiTenancyDataSource.getUrl())
				.driverClassName(multiTenancyDataSource.getDriverClassName())
				.username(multiTenancyDataSource.getUsername())
				.password(multiTenancyDataSource.getPassword())
				.build();
		}
	}
}
