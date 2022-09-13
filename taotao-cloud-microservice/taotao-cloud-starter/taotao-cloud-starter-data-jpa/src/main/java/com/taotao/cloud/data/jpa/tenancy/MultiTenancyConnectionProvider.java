/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.data.jpa.tenancy;

import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

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
