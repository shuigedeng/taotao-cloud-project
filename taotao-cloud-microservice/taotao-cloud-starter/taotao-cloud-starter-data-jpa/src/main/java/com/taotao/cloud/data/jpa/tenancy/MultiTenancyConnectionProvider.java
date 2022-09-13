package com.taotao.cloud.data.jpa.tenancy;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

import javax.sql.DataSource;

/**
 * <p>Description: 数据库连接提供者 </p>
 * <p>
 * 通过该类明确，在租户系统中具体使用的是哪个 Database 或 Schema
 *
 * @author : gengwei.zheng
 * @date : 2022/9/8 18:14
 */
public class MultiTenancyConnectionProvider extends
	AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final Logger log = LoggerFactory.getLogger(MultiTenancyConnectionProvider.class);

	private final DataSource defaultDataSource;
	private final DataSourceLookup dataSourceLookup;

	public MultiTenancyConnectionProvider(DataSource dataSource,
										  DataSourceLookup dataSourceLookup) {
		this.defaultDataSource = dataSource;
		this.dataSourceLookup = dataSourceLookup;
	}

	/**
	 * 在没有指定 tenantId 的情况下选择的数据源（例如启动处理）
	 *
	 * @return {@link DataSource}
	 */
	@Override
	protected DataSource selectAnyDataSource() {
		log.debug("[Herodotus] |- Select any dataSource: " + defaultDataSource);
		return defaultDataSource;
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		DataSource dataSource = dataSourceLookup.getDataSource(tenantIdentifier);
		log.debug("[Herodotus] |- Select dataSource from [{}] : [{}]", tenantIdentifier,
			dataSource);
		return dataSource;
	}
}
