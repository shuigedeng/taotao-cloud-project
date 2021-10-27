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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * FilterProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:04:30
 */
@RefreshScope
@ConfigurationProperties(prefix = InterceptorProperties.PREFIX)
public class InterceptorProperties {

	public static final String PREFIX = "taotao.cloud.web.interceptor";

	/**
	 * 开启负载均衡隔离规则
	 */
	private Boolean doubtApi = true;
	/**
	 * 增长内存统计阈值，默认3M
	 */
	private int doubtApiThreshold = 3 * 1024 * 1024;

	private Boolean header = true;
	private Boolean prometheus = true;


	public Boolean getDoubtApi() {
		return doubtApi;
	}

	public void setDoubtApi(Boolean doubtApi) {
		this.doubtApi = doubtApi;
	}

	public Boolean getHeader() {
		return header;
	}

	public void setHeader(Boolean header) {
		this.header = header;
	}

	public Boolean getPrometheus() {
		return prometheus;
	}

	public void setPrometheus(Boolean prometheus) {
		this.prometheus = prometheus;
	}

	public int getDoubtApiThreshold() {
		return doubtApiThreshold;
	}

	public void setDoubtApiThreshold(int doubtApiThreshold) {
		this.doubtApiThreshold = doubtApiThreshold;
	}
}
