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
package com.taotao.cloud.data.mybatis.plus.configuration;

/**
 * DynamicDataSourceAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/08/02 23:12
 */

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.taotao.cloud.data.mybatis.plus.configuration.DynamicDataSourceAutoConfiguration.DataSourceProperties;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 动态数据源切换配置
 */
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnProperty(prefix = "taotao.cloud.data.dynamic.datasource", name = "enabled",havingValue = "true")
public class DynamicDataSourceAutoConfiguration {

	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider(
		DataSourceProperties properties) {
		return new JdbcDynamicDataSourceProvider(properties);
	}

	@Bean
	public DsProcessor dsProcessor() {
		return new LastParamDsProcessor();
	}

	/**
	 * <p>
	 * 参数数据源解析 @DS("#last)
	 */
	public class LastParamDsProcessor extends DsProcessor {

		private static final String LAST_PREFIX = "#last";

		/**
		 * 抽象匹配条件 匹配才会走当前执行器否则走下一级执行器
		 *
		 * @param key DS注解里的内容
		 * @return 是否匹配
		 */
		@Override
		public boolean matches(String key) {
			if (key.startsWith(LAST_PREFIX)) {
				// https://github.com/baomidou/dynamic-datasource-spring-boot-starter/issues/213
				DynamicDataSourceContextHolder.clear();
				return true;
			}
			return false;
		}

		/**
		 * 抽象最终决定数据源
		 *
		 * @param invocation 方法执行信息
		 * @param key        DS注解里的内容
		 * @return 数据源名称
		 */
		@Override
		public String doDetermineDatasource(MethodInvocation invocation, String key) {
			Object[] arguments = invocation.getArguments();
			return String.valueOf(arguments[arguments.length - 1]);
		}
	}

	/**
	 * <p>
	 * 从数据源中获取 配置信息
	 */
	public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

		private final DataSourceProperties properties;

		public JdbcDynamicDataSourceProvider(DataSourceProperties properties) {
			super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(),
				properties.getPassword());
			this.properties = properties;
		}

		/**
		 * 执行语句获得数据源参数
		 *
		 * @param statement 语句
		 * @return 数据源参数
		 * @throws SQLException sql异常
		 */
		@Override
		protected Map<String, DataSourceProperty> executeStmt(Statement statement)
			throws SQLException {
			ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

			Map<String, DataSourceProperty> map = new HashMap<>(8);
			while (rs.next()) {
				String name = rs.getString(DataSourceConstants.DS_NAME);
				String username = rs.getString(DataSourceConstants.DS_USER_NAME);
				String password = rs.getString(DataSourceConstants.DS_USER_PWD);
				String url = rs.getString(DataSourceConstants.DS_JDBC_URL);
				DataSourceProperty property = new DataSourceProperty();
				property.setDriverClassName(DataSourceConstants.DS_DRIVER);
				property.setUsername(username);
				property.setLazy(true);
				property.setPassword(password);
				property.setUrl(url);
				map.put(name, property);
			}

			// 添加默认主数据源
			DataSourceProperty property = new DataSourceProperty();
			property.setUsername(properties.getUsername());
			property.setPassword(properties.getPassword());
			property.setUrl(properties.getUrl());
			property.setLazy(true);
			property.setDriverClassName(DataSourceConstants.DS_DRIVER);
			map.put(DataSourceConstants.DS_MASTER, property);
			return map;
		}

	}

	/**
	 * @author lengleng
	 * @date 2019-04-01
	 * <p>
	 * 数据源相关常量
	 */
	public interface DataSourceConstants {

		/**
		 * 数据源名称
		 */
		String DS_NAME = "name";

		/**
		 * 默认驱动
		 */
		String DS_DRIVER = "com.mysql.cj.jdbc.Driver";

		/**
		 * 默认数据源（master）
		 */
		String DS_MASTER = "master";

		/**
		 * jdbcurl
		 */
		String DS_JDBC_URL = "url";

		/**
		 * 用户名
		 */
		String DS_USER_NAME = "username";

		/**
		 * 密码
		 */
		String DS_USER_PWD = "password";
	}

	/**
	 * @author lengleng
	 * @date 2019-05-14
	 * <p>
	 */
	@ConfigurationProperties("spring.datasource")
	public static class DataSourceProperties {

		/**
		 * 用户名
		 */
		private String username;

		/**
		 * 密码
		 */
		private String password;

		/**
		 * jdbcurl
		 */
		private String url;

		/**
		 * 驱动类型
		 */
		private String driverClassName;

		/**
		 * 查询数据源的SQL
		 */
		private String queryDsSql = "select * from gen_datasource_conf where del_flag = 0";

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public String getQueryDsSql() {
			return queryDsSql;
		}

		public void setQueryDsSql(String queryDsSql) {
			this.queryDsSql = queryDsSql;
		}
	}

}
