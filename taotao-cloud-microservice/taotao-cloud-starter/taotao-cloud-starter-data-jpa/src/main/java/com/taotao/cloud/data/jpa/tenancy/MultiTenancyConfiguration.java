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

import com.taotao.cloud.data.jpa.tenancy.properties.MultiTenancyProperties;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * <p>Description: Data JPA 模块 多租户配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/9/8 22:15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMultiTenancyEnabled
@EnableConfigurationProperties(MultiTenancyProperties.class)
public class MultiTenancyConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MultiTenancyConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [Engine Data Multi Tenancy] Auto Configure.");
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public DataSourceLookup dataSourceLookup(DataSource dataSource,
		MultiTenancyProperties multiTenancyProperties) {
		MultiTenancyDataSourceLookup multiTenancyDataSourceLookup = new MultiTenancyDataSourceLookup(
			dataSource, multiTenancyProperties);
		log.debug("[Herodotus] |- Bean [Multi Tenancy DataSource Lookup] Auto Configure.");
		return multiTenancyDataSourceLookup;
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider(DataSource dataSource,
		DataSourceLookup dataSourceLookup) {
		MultiTenancyConnectionProvider multiTenancyConnectionProvider = new MultiTenancyConnectionProvider(
			dataSource, dataSourceLookup);
		log.debug("[Herodotus] |- Bean [Multi Tenancy Connection Provider] Auto Configure.");
		return multiTenancyConnectionProvider;
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		MultiTenancyIdentifierResolver multiTenancyIdentifierResolver = new MultiTenancyIdentifierResolver();
		log.debug("[Herodotus] |- Bean [Multi Tenancy Connection Provider] Auto Configure.");
		return multiTenancyIdentifierResolver;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
		HibernateProperties hibernateProperties, JpaVendorAdapter jpaVendorAdapter,
		JpaProperties jpaProperties, MultiTenancyProperties multiTenancyProperties,
		MultiTenantConnectionProvider multiTenantConnectionProvider,
		CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		Supplier<String> defaultDdlMode = hibernateProperties::getDdlAuto;
		Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
			jpaProperties.getProperties(), new HibernateSettings().ddlAuto(defaultDdlMode));

		properties.put(Environment.MULTI_TENANT, multiTenancyProperties.getTenancyStrategy());
		properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
			currentTenantIdentifierResolver);
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		//此处不能省略，哪怕你使用了 @EntityScan，实际上 @EntityScan 会失效
		emf.setPackagesToScan(multiTenancyProperties.getPackageToScan());
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		emf.setJpaPropertyMap(properties);
		return emf;
	}
}
