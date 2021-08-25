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

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * FilterProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:15
 */
@RefreshScope
@ConfigurationProperties(prefix = FilterProperties.PREFIX)
public class FilterProperties {

	public static final String PREFIX = "taotao.cloud.web.filter";

	/**
	 * 开启负载均衡隔离规则
	 */
	private Boolean version = true;

	/**
	 * 开启租户过滤器
	 */
	private Boolean tenant = true;

	/**
	 * 开启日志链路追踪过滤器
	 */
	private Boolean trace = true;

	/**
	 * 开启日志链路追踪过滤器
	 */
	private Boolean webContext = true;

	public FilterProperties() {
	}

	public FilterProperties(Boolean version, Boolean tenant, Boolean trace,
		Boolean webContext) {
		this.version = version;
		this.tenant = tenant;
		this.trace = trace;
		this.webContext = webContext;
	}

	@Override
	public String toString() {
		return "FilterProperties{" +
			"version=" + version +
			", tenant=" + tenant +
			", trace=" + trace +
			", webContext=" + webContext +
			'}';
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FilterProperties that = (FilterProperties) o;
		return Objects.equals(version, that.version) && Objects.equals(tenant,
			that.tenant) && Objects.equals(trace, that.trace) && Objects.equals(
			webContext, that.webContext);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, tenant, trace, webContext);
	}

	public Boolean getVersion() {
		return version;
	}

	public void setVersion(Boolean version) {
		this.version = version;
	}

	public Boolean getTenant() {
		return tenant;
	}

	public void setTenant(Boolean tenant) {
		this.tenant = tenant;
	}

	public Boolean getTrace() {
		return trace;
	}

	public void setTrace(Boolean trace) {
		this.trace = trace;
	}

	public Boolean getWebContext() {
		return webContext;
	}

	public void setWebContext(Boolean webContext) {
		this.webContext = webContext;
	}
}
