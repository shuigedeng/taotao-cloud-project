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

import com.taotao.cloud.data.jpa.tenancy.properties.MultiTenancyDataSource;
import com.taotao.cloud.data.jpa.tenancy.properties.MultiTenancyProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;

/**
 * <p>Description: DataSource 查询 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/9/8 18:55
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
