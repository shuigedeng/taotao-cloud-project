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

import cn.hutool.core.collection.CollUtil;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 忽略XSS 配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:23
 */
@RefreshScope
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {

	public static final String PREFIX = "taotao.cloud.web.xss";

	private Boolean enabled = true;

	/**
	 * 是否启用 RequestBody 注解标记的参数 反序列化时过滤XSS
	 */
	private Boolean requestBodyEnabled = false;

	private int order = 1;

	private List<String> patterns = CollUtil.newArrayList("/*");

	private List<String> ignorePaths = CollUtil.newArrayList(
		"favicon.ico",
		"/**/doc.html",
		"/**/swagger-ui.html",
		"/csrf",
		"/webjars/**",
		"/v3/**",
		"/swagger-resources/**",
		"/resources/**",
		"/static/**",
		"/public/**",
		"/classpath:*",
		"/actuator/**",
		"/**/noxss/**",
		"/**/activiti/**",
		"/**/service/model/**",
		"/**/service/editor/**"
	);
	private List<String> ignoreParamValues = CollUtil.newArrayList("noxss");

	public XssProperties() {
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getRequestBodyEnabled() {
		return requestBodyEnabled;
	}

	public void setRequestBodyEnabled(Boolean requestBodyEnabled) {
		this.requestBodyEnabled = requestBodyEnabled;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<String> patterns) {
		this.patterns = patterns;
	}

	public List<String> getIgnorePaths() {
		return ignorePaths;
	}

	public void setIgnorePaths(List<String> ignorePaths) {
		this.ignorePaths = ignorePaths;
	}

	public List<String> getIgnoreParamValues() {
		return ignoreParamValues;
	}

	public void setIgnoreParamValues(List<String> ignoreParamValues) {
		this.ignoreParamValues = ignoreParamValues;
	}
}
