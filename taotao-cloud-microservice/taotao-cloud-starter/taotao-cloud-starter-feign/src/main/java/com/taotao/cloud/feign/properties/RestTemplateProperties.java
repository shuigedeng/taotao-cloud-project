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
package com.taotao.cloud.feign.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * RestTemplate 配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2017/11/17
 */
@RefreshScope
@ConfigurationProperties(prefix = RestTemplateProperties.PREFIX)
public class RestTemplateProperties {

	public static final String PREFIX = "taotao.cloud.feign.loadbalancer.rest-template";

	private boolean enabled = true;

	/**
	 * 最大链接数
	 */
	private int maxTotal = 200;
	/**
	 * 同路由最大并发数
	 */
	private int maxPerRoute = 50;
	/**
	 * 读取超时时间 ms
	 */
	private int readTimeout = 35000;
	/**
	 * 链接超时时间 ms
	 */
	private int connectTimeout = 10000;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
