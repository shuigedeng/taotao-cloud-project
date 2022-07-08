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
package com.taotao.cloud.metrics.configuration;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.metrics.druid.DruidDataSourcePoolMetadata;
import com.taotao.cloud.metrics.druid.DruidMetrics;
import com.taotao.cloud.metrics.properties.DruidMetricsProperties;
import com.taotao.cloud.metrics.properties.MetricsProperties;
import com.taotao.cloud.metrics.properties.SentinelMetricsProperties;
import com.taotao.cloud.metrics.properties.UndertowMetricsProperties;
import com.taotao.cloud.metrics.undertow.UndertowMetrics;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * MetricsAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@AutoConfiguration
@EnableConfigurationProperties({MetricsProperties.class,
	DruidMetricsProperties.class,
	SentinelMetricsProperties.class,
	UndertowMetricsProperties.class})
@ConditionalOnProperty(prefix = MetricsProperties.PREFIX, name = "enabled", havingValue = "true")
public class MetricsAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(MetricsAutoConfiguration.class, StarterName.PULSAR_STARTER);
	}

	@Configuration
	@ConditionalOnClass(DruidDataSource.class)
	@ConditionalOnProperty(prefix = DruidMetricsProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class DruidMetricsConfiguration {

		private static final String DATASOURCE_SUFFIX = "dataSource";

		@Bean
		public DataSourcePoolMetadataProvider druidDataSourceMetadataProvider() {
			return (dataSource) -> {
				DruidDataSource druidDataSource = DataSourceUnwrapper.unwrap(dataSource,
					DruidDataSource.class);
				if (druidDataSource != null) {
					return new DruidDataSourcePoolMetadata(druidDataSource);
				}
				return null;
			};
		}

		@Bean
		@ConditionalOnMissingBean
		public StatFilter statFilter() {
			return new StatFilter();
		}

		@Bean
		public DruidMetrics druidMetrics(
			ObjectProvider<Map<String, DataSource>> dataSourcesProvider) {
			Map<String, DataSource> dataSourceMap = dataSourcesProvider.getIfAvailable(HashMap::new);
			Map<String, DruidDataSource> druidDataSourceMap = new HashMap<>(2);
			dataSourceMap.forEach((name, dataSource) -> {
				// 保证连接池数据和 DataSourcePoolMetadataProvider 的一致
				druidDataSourceMap.put(getDataSourceName(name),
					DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class));
			});
			return new DruidMetrics(druidDataSourceMap);
		}

		/**
		 * Get the name of a DataSource based on its {@code beanName}.
		 *
		 * @param beanName the name of the data source bean
		 * @return a name for the given data source
		 */
		private static String getDataSourceName(String beanName) {
			if (beanName.length() > DATASOURCE_SUFFIX.length()
				&& StringUtils.endsWithIgnoreCase(beanName, DATASOURCE_SUFFIX)) {
				return beanName.substring(0, beanName.length() - DATASOURCE_SUFFIX.length());
			}
			return beanName;
		}
	}

	@Configuration
	@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
	@ConditionalOnClass(Undertow.class)
	@ConditionalOnProperty(prefix = UndertowMetricsProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class UndertowMetricsConfiguration {

		@Bean
		public UndertowMetrics undertowMetrics() {
			return new UndertowMetrics();
		}

		@Bean
		public UndertowBuilderCustomizer undertowBuilderCustomizerEnableStatistics() {
			return builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true);
		}

	}

	@Configuration
	@ConditionalOnProperty(prefix = SentinelMetricsProperties.PREFIX, name = "enabled", havingValue = "true")
	public static class SentinelMetricsConfiguration {

	}

}
