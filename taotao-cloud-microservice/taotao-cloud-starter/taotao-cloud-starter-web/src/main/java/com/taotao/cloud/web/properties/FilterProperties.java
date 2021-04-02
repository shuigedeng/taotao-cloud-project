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
package com.taotao.cloud.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 网关配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 11:15
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = FilterProperties.BASE_WEB_FILTER_PREFIX)
public class FilterProperties {

	public static final String BASE_WEB_FILTER_PREFIX = "taotao.cloud.web.filter";

	/**
	 * 开启负载均衡隔离规则
	 */
	private Boolean lbIsolation = true;

	/**
	 * 开启租户过滤器
	 */
	private Boolean tenant = true;

	/**
	 * 开启日志链路追踪过滤器
	 */
	private Boolean trace = true;
}
