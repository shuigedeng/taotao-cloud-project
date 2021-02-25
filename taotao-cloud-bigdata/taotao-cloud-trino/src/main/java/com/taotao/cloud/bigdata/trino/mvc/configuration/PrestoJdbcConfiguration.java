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
package com.taotao.cloud.bigdata.trino.mvc.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author dengtao
 * @date 2020/10/30 10:09
 * @since v1.0
 */
public class PrestoJdbcConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(PrestoJdbcConfiguration.class);

	@Bean(name = "trinoDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.trino")
	public DataSource prestoDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "trinoTemplate")
	public JdbcTemplate prestoJdbcTemplate(@Qualifier("prestoDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
