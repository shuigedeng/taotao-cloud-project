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
package com.taotao.cloud.dubbo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * configuration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 08:00
 */
@RefreshScope
@ConfigurationProperties(prefix = DubboProperties.PREFIX)
public class DubboProperties {

	public static final String PREFIX = "taotao.cloud.dubbo";

	private boolean enabled = true;

	private boolean requestLog = true;

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isRequestLog() {
		return requestLog;
	}

	public void setRequestLog(boolean requestLog) {
		this.requestLog = requestLog;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
