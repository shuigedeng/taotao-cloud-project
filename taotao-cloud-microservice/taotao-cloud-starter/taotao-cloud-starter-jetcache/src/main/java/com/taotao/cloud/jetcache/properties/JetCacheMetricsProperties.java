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


package com.taotao.cloud.jetcache.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * jetcache metrics 配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:50:41
 */
@RefreshScope
@ConfigurationProperties(JetCacheMetricsProperties.PREFIX)
public class JetCacheMetricsProperties {
	public static final String PREFIX = "taotao.cloud.jetcache.metrics";

	/**
	 * 开启 jetcache metrics，默认：true
	 */
	private boolean enabled = true;
	/**
	 * 开启 StatInfoLogger
	 */
	private boolean enabledStatInfoLogger;
	/**
	 * StatInfoLogger 打印明细，默认：false
	 */
	private boolean verboseLog = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabledStatInfoLogger() {
		return enabledStatInfoLogger;
	}

	public void setEnabledStatInfoLogger(boolean enabledStatInfoLogger) {
		this.enabledStatInfoLogger = enabledStatInfoLogger;
	}

	public boolean isVerboseLog() {
		return verboseLog;
	}

	public void setVerboseLog(boolean verboseLog) {
		this.verboseLog = verboseLog;
	}
}
