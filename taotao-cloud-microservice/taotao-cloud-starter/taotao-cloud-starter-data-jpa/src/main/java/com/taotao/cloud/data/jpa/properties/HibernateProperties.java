/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.data.jpa.properties;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.dialect.MySQL8Dialect;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.orm.jpa.vendor.Database;


/**
 * TenantProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:32:13
 */
@RefreshScope
@ConfigurationProperties(prefix = HibernateProperties.PREFIX)
public class HibernateProperties {

	public static final String PREFIX = "taotao.cloud.data.jpa";

	/**
	 * 是否开启Hibernate
	 */
	private Boolean enabled = true;

	private boolean showSql = true;
	private boolean generateDdl = true;
	private Database database = Database.MYSQL;

	private MultiTenancyStrategy multiTenancy = MultiTenancyStrategy.DISCRIMINATOR;
	private boolean formatSql = true;
	private boolean highlightSql = true;
	private String implicitNamingStrategy = SpringImplicitNamingStrategy.class.getName();
	private String physicalNamingStrategy = CamelCaseToUnderscoresNamingStrategy.class.getName();
	private String dialect = MySQL8Dialect.class.getName();
	private String timeZone = "Asia/Shanghai";
	private String statementInspector = "com.taotao.cloud.data.jpa.listener.HibernateInspector";
	private String interceptor = "com.taotao.cloud.data.jpa.listener.HibernateInterceptor";
	private String persistenceUnitName = "default";


	/**
	 * basePackages
	 */
	private String packages = "com.taotao.cloud.*.biz.model.entity";

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public boolean isGenerateDdl() {
		return generateDdl;
	}

	public void setGenerateDdl(boolean generateDdl) {
		this.generateDdl = generateDdl;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public MultiTenancyStrategy getMultiTenancy() {
		return multiTenancy;
	}

	public void setMultiTenancy(MultiTenancyStrategy multiTenancy) {
		this.multiTenancy = multiTenancy;
	}

	public boolean isFormatSql() {
		return formatSql;
	}

	public void setFormatSql(boolean formatSql) {
		this.formatSql = formatSql;
	}

	public boolean isHighlightSql() {
		return highlightSql;
	}

	public void setHighlightSql(boolean highlightSql) {
		this.highlightSql = highlightSql;
	}

	public String getImplicitNamingStrategy() {
		return implicitNamingStrategy;
	}

	public void setImplicitNamingStrategy(String implicitNamingStrategy) {
		this.implicitNamingStrategy = implicitNamingStrategy;
	}

	public String getPhysicalNamingStrategy() {
		return physicalNamingStrategy;
	}

	public void setPhysicalNamingStrategy(String physicalNamingStrategy) {
		this.physicalNamingStrategy = physicalNamingStrategy;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getStatementInspector() {
		return statementInspector;
	}

	public void setStatementInspector(String statementInspector) {
		this.statementInspector = statementInspector;
	}

	public String getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}
}
