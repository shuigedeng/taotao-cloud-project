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
package com.taotao.cloud.elk.component;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 错误日志统计拦截器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:28
 */
public class LogStatisticsFilter extends AbstractMatcherFilter<ILoggingEvent> {

	public static final int DEFAULT_TIME = 60000;
	private long lastCollectTime = System.currentTimeMillis() / DEFAULT_TIME;
	private static final AtomicLong ERROR_COUNT = new AtomicLong(0);
	private static final AtomicLong LOG_COUNT = new AtomicLong(0);

	@Override
	public FilterReply decide(ILoggingEvent event) {
		LOG_COUNT.incrementAndGet();
		if (event.getLevel().equals(Level.ERROR)) {
			ERROR_COUNT.incrementAndGet();
		}
		if (System.currentTimeMillis() / DEFAULT_TIME > lastCollectTime) {
			lastCollectTime = System.currentTimeMillis() / DEFAULT_TIME;
			LOG_COUNT.set(0);
			ERROR_COUNT.set(0);
		}

		return FilterReply.NEUTRAL;
	}
}
