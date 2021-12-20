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
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.AvailableSettings.IMPLICIT_NAMING_STRATEGY;
import static org.hibernate.cfg.AvailableSettings.INTERCEPTOR;
import static org.hibernate.cfg.AvailableSettings.JDBC_TIME_ZONE;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER;
import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_INSPECTOR;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.data.jpa.bean.AuditorBean;
import com.taotao.cloud.data.jpa.bean.TenantConnectionProvider;
import com.taotao.cloud.data.jpa.bean.TenantIdentifierResolver;
import com.taotao.cloud.data.jpa.listener.HibernateInspector;
import com.taotao.cloud.data.jpa.properties.HibernateProperties;
import com.taotao.cloud.data.jpa.properties.TenantProperties;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
@EnableConfigurationProperties({TenantProperties.class, HibernateProperties.class,
	JpaProperties.class})
@ConditionalOnProperty(prefix = HibernateProperties.PREFIX, name = "enabled", havingValue = "true")
public class HibernateAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HibernateAutoConfiguration.class, StarterName.JPA_STARTER);
	}

	private final JpaProperties jpaProperties;
	private final HibernateProperties hibernateProperties;

	public HibernateAutoConfiguration(JpaProperties jpaProperties,
		HibernateProperties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
		this.jpaProperties = jpaProperties;
	}

	@Bean
	public AuditorBean auditorBean() {
		return new AuditorBean();
	}

	@Bean
	@ConditionalOnBean
	public MultiTenantConnectionProvider tenantConnectionProvider(DataSource dataSource) {
		return new TenantConnectionProvider(dataSource);
	}

	@Bean
	public CurrentTenantIdentifierResolver tenantIdentifierResolver() {
		return new TenantIdentifierResolver();
	}

	@Bean
	JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(hibernateProperties.isShowSql());
		hibernateJpaVendorAdapter.setGenerateDdl(hibernateProperties.isGenerateDdl());
		hibernateJpaVendorAdapter.setDatabase(hibernateProperties.getDatabase());
		return hibernateJpaVendorAdapter;
	}

	//@Bean
	//@ConditionalOnBean
	//public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
	//	return new JdbcTemplate(dataSource);
	//}

	@Bean
	@ConditionalOnBean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
		final DataSource dataSource,
		final JpaVendorAdapter jpaVendorAdapter,
		final MultiTenantConnectionProvider multiTenantConnectionProvider,
		final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		final Map<String, Object> newJpaProperties = new HashMap<>(jpaProperties.getProperties());

		newJpaProperties.put(MULTI_TENANT, hibernateProperties.getMultiTenancy());
		newJpaProperties.put(FORMAT_SQL, hibernateProperties.isFormatSql());
		newJpaProperties.put(HIGHLIGHT_SQL, hibernateProperties.isHighlightSql());
		newJpaProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		newJpaProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

		newJpaProperties.put(IMPLICIT_NAMING_STRATEGY,
			hibernateProperties.getImplicitNamingStrategy());
		newJpaProperties.put(
			PHYSICAL_NAMING_STRATEGY, hibernateProperties.getPhysicalNamingStrategy());
		newJpaProperties.put(DIALECT, hibernateProperties.getDialect());
		newJpaProperties.put(JDBC_TIME_ZONE, hibernateProperties.getTimeZone());

		newJpaProperties.put(STATEMENT_INSPECTOR, hibernateProperties.getStatementInspector());
		newJpaProperties.put(INTERCEPTOR, hibernateProperties.getInterceptor());

		final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
			new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaPropertyMap(newJpaProperties);
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

		entityManagerFactoryBean.setPackagesToScan(hibernateProperties.getPackages());
		entityManagerFactoryBean.setPersistenceUnitName(
			hibernateProperties.getPersistenceUnitName());

		return entityManagerFactoryBean;
	}

	@Configuration
	public static class HibernateListener {

		@PersistenceUnit
		private EntityManagerFactory entityManagerFactory;

		@PostConstruct
		public void registerListener() {
			if (entityManagerFactory != null) {
				SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(
					SessionFactoryImpl.class);
				EventListenerRegistry registry = sessionFactory.getServiceRegistry()
					.getService(EventListenerRegistry.class);
				registry.getEventListenerGroup(EventType.SAVE_UPDATE)
					.appendListener(new HibernateInspector.SaveOrUpdateListener());
				registry.getEventListenerGroup(EventType.DELETE)
					.appendListener(new HibernateInspector.DeleteListener());
				registry.getEventListenerGroup(EventType.LOAD)
					.appendListener(new HibernateInspector.LoadListener());
			}
		}
	}
}
