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
package com.taotao.cloud.core.initializer;

import com.taotao.cloud.core.utils.ContextUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用上下文初始化器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/15 10:45
 */
public class CoreApplicationContextInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext context) {
		if (ContextUtil.mainClass == null) {
			ContextUtil.mainClass = deduceMainApplicationClass();
		}
		ContextUtil.setApplicationContext(context);
	}

	private Class<?> deduceMainApplicationClass() {
		try {
			StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
			for (StackTraceElement stackTraceElement : stackTrace) {
				if ("main".equals(stackTraceElement.getMethodName())) {
					return Class.forName(stackTraceElement.getClassName());
				}
			}
		} catch (ClassNotFoundException ex) {
			// Swallow and continue
		}
		return null;
	}
}
