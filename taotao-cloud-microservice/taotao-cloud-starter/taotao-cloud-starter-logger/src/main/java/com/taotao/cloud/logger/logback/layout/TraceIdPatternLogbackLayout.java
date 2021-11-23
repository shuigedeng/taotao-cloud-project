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
package com.taotao.cloud.logger.logback.layout;

import ch.qos.logback.classic.PatternLayout;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackSkyWalkingContextPatternConverter;

/**
 * TraceIdPatternLogbackLayout
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/09/16 08:57
 */
public class TraceIdPatternLogbackLayout extends PatternLayout {

	public TraceIdPatternLogbackLayout() {
	}

	static {
		defaultConverterMap.put("tid", LogbackPatternConverter.class.getName());
		defaultConverterMap.put("sw_ctx", LogbackSkyWalkingContextPatternConverter.class.getName());
	}
}
