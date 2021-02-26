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
package com.taotao.cloud.log.properties;

import com.taotao.cloud.common.enums.LogTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 审计日志配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 11:19
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "taotao.cloud.log")
public class RequestLogProperties {

	/**
	 * 是否开启审计日志
	 */
	private Boolean enabled = false;

	/**
	 * 日志记录类型(logger/redis/kafka)
	 */
	private String[] types = new String[]{LogTypeEnum.LOGGER.getName()};

}
