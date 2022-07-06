/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.properties;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * DINGTALK线程池参数配置-用于异步处理
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:24:32
 */
@RefreshScope
@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

	public static final String PREFIX = "taotao.cloud.dinger.httpclient";

	private boolean enabled = true;

	/**
	 * 连接超时时间
	 */
	@DurationUnit(ChronoUnit.SECONDS)
	private Duration connectTimeout = Duration.ofSeconds(30);

	/**
	 * 读超时时间
	 */
	@DurationUnit(ChronoUnit.SECONDS)
	private Duration readTimeout = Duration.ofSeconds(30);

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Duration getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Duration connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Duration getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Duration readTimeout) {
		this.readTimeout = readTimeout;
	}
}
