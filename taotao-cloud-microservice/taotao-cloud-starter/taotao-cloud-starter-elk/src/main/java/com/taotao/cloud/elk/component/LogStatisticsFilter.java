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
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/15 11:28
 */
public class LogStatisticsFilter extends AbstractMatcherFilter<ILoggingEvent> {

	private static long lastCollectTime = System.currentTimeMillis() / 60000;
	private static volatile AtomicLong errorCount = new AtomicLong(0);
	private static volatile AtomicLong logCount = new AtomicLong(0);

	@Override
	public FilterReply decide(ILoggingEvent event) {
		logCount.incrementAndGet();
		if (event.getLevel().equals(Level.ERROR)) {
			errorCount.incrementAndGet();
		}
		if (System.currentTimeMillis() / 60000 > lastCollectTime) {
			lastCollectTime = System.currentTimeMillis() / 60000;
			logCount.set(0);
			errorCount.set(0);
		}

		return FilterReply.NEUTRAL;
	}
}
