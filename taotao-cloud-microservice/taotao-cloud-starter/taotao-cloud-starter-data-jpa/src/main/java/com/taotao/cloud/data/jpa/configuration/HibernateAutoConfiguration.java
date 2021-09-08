/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.data.jpa.configuration;

import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.IMPLICIT_NAMING_STRATEGY;
import static org.hibernate.cfg.AvailableSettings.JDBC_TIME_ZONE;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER;
import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.data.jpa.bean.AuditorBean;
import com.taotao.cloud.data.jpa.bean.TenantConnectionProvider;
import com.taotao.cloud.data.jpa.bean.TenantIdentifierResolver;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * HibernateAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:31:41
 */
@Configuration
@EnableJpaAuditing
public class HibernateAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HibernateAutoConfiguration.class, StarterName.JPA_STARTER);
	}

	private final JpaProperties jpaProperties;

	public HibernateAutoConfiguration(@Autowired final JpaProperties jpaProperties) {
		this.jpaProperties = jpaProperties;
	}

	@Bean
	public AuditorBean auditorBean(){
		LogUtil.started(AuditorBean.class, StarterName.JPA_STARTER);
		return new AuditorBean();
	}

	@Bean
	@ConditionalOnBean(DataSource.class)
	public TenantConnectionProvider tenantConnectionProvider(DataSource dataSource){
		LogUtil.started(TenantConnectionProvider.class, StarterName.JPA_STARTER);
		return new TenantConnectionProvider(dataSource);
	}

	@Bean
	public TenantIdentifierResolver tenantIdentifierResolver(){
		LogUtil.started(TenantIdentifierResolver.class, StarterName.JPA_STARTER);
		return new TenantIdentifierResolver();
	}

	@Bean
	JpaVendorAdapter jpaVendorAdapter() {
		LogUtil.started(JpaVendorAdapter.class, StarterName.JPA_STARTER);
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	@ConditionalOnBean(DataSource.class)
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
		final DataSource dataSource,
		final JpaVendorAdapter jpaVendorAdapter,
		final MultiTenantConnectionProvider multiTenantConnectionProvider,
		final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
		LogUtil.started(LocalContainerEntityManagerFactoryBean.class, StarterName.JPA_STARTER);

		final Map<String, Object> newJpaProperties = new HashMap<>(jpaProperties.getProperties());

		newJpaProperties.put(MULTI_TENANT, MultiTenancyStrategy.DISCRIMINATOR);
		newJpaProperties.put(
			MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		newJpaProperties.put(
			MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

		newJpaProperties.put(
			IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
		newJpaProperties.put(
			PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
		newJpaProperties.put(DIALECT, MySQL8Dialect.class.getName());
		newJpaProperties.put(JDBC_TIME_ZONE, "Asia/Shanghai");

		final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
			new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaPropertyMap(newJpaProperties);
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

		entityManagerFactoryBean
			.setPackagesToScan("com.taotao.cloud.*.biz.entity", "com.taotao.cloud.*.entity");
		entityManagerFactoryBean.setPersistenceUnitName("default");

		return entityManagerFactoryBean;
	}
}
