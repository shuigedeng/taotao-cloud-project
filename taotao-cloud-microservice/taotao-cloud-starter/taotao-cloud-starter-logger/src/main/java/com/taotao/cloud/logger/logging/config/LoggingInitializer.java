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

package com.taotao.cloud.logger.logging.config;

import com.taotao.cloud.logger.logging.utils.LoggingUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * logging 日志初始化
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:42:50
 */
public class LoggingInitializer implements EnvironmentPostProcessor, Ordered {
	public static final String LOGGING_FILE_PATH_KEY = "logging.file.path";
	public static final String LOGGING_FILE_NAME_KEY = "logging.file.name";
	public static final String MICA_LOGGING_PROPERTY_SOURCE_NAME = "micaLoggingPropertySource";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 读取系统配置的日志目录，默认为项目下 logs
		String logBase = environment.getProperty(LOGGING_FILE_PATH_KEY, LoggingUtil.DEFAULT_LOG_DIR);
		// 用于 spring boot admin 中展示日志
		if (!environment.containsProperty(LOGGING_FILE_NAME_KEY)) {
			Map<String, Object> map = new HashMap<>(2);
			map.put(LOGGING_FILE_NAME_KEY, logBase + "/${spring.application.name}/" + LoggingUtil.LOG_FILE_ALL);
			MapPropertySource propertySource = new MapPropertySource(MICA_LOGGING_PROPERTY_SOURCE_NAME, map);
			environment.getPropertySources().addLast(propertySource);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
