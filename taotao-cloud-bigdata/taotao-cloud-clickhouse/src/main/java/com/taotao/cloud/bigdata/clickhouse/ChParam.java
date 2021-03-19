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
package com.taotao.cloud.bigdata.clickhouse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ChParam
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/03/16 17:04
 */
@Component
public class ChParam {
	private String driverClassName;
	private String url;
	private String password;
	private Integer initialSize;
	private Integer maxActive;
	private Integer minIdle;
	private Integer maxWait;

	@Value("${clickhouse.driverClassName}")
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Value("${clickhouse.url}")
	public void setUrl(String url) {
		this.url = url;
	}

	@Value("${clickhouse.password}")
	public void setPassword(String password) {
		this.password = password;
	}

	@Value("${clickhouse.initialSize}")
	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	@Value("${clickhouse.maxActive}")
	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	@Value("${clickhouse.minIdle}")
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	@Value("${clickhouse.maxWait}")
	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}


	public String getDriverClassName() {
		return driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public String getPassword() {
		return password;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public Integer getMaxWait() {
		return maxWait;
	}
}
