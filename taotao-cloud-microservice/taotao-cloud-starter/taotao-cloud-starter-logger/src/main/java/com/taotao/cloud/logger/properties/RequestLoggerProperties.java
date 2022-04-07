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
package com.taotao.cloud.logger.properties;

import com.taotao.cloud.logger.enums.RequestLoggerTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 审计日志配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:19
 */
@RefreshScope
@ConfigurationProperties(prefix = RequestLoggerProperties.PREFIX)
public class RequestLoggerProperties {

	public static final String PREFIX = "taotao.cloud.logger.request";

	/**
	 * 是否开启请求日志
	 */
	private Boolean enabled = true;

	/**
	 * 日志记录类型(logger/redis/kafka)
	 */
	private RequestLoggerTypeEnum[] types = new RequestLoggerTypeEnum[]{
		RequestLoggerTypeEnum.LOGGER};

	public RequestLoggerProperties() {
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public RequestLoggerTypeEnum[] getTypes() {
		if (types == null || types.length == 0) {
			return new RequestLoggerTypeEnum[]{RequestLoggerTypeEnum.LOGGER};
		}
		return types;
	}

	public void setTypes(RequestLoggerTypeEnum[] types) {
		this.types = types;
	}

}
