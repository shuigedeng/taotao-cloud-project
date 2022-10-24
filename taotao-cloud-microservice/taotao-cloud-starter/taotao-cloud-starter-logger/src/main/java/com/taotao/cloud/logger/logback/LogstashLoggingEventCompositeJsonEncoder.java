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
package com.taotao.cloud.logger.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.ContextAware;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import net.logstash.logback.composite.AbstractCompositeJsonFormatter;
import net.logstash.logback.composite.loggingevent.LoggingEventCompositeJsonFormatter;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;

/**
 * LogstashLoggingEventCompositeJsonEncoder
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-14 12:00
 */
public class LogstashLoggingEventCompositeJsonEncoder extends LoggingEventCompositeJsonEncoder {

	@Override
	protected AbstractCompositeJsonFormatter<ILoggingEvent> createFormatter() {
		return new LogstashLoggingEventCompositeJsonFormatter(this);
	}

	public static class LogstashLoggingEventCompositeJsonFormatter extends
		LoggingEventCompositeJsonFormatter {

		@Override
		protected void writeEventToGenerator(JsonGenerator generator, ILoggingEvent event)
			throws IOException {
			try {
				String loggerName = event.getLoggerName();
				if ("org.apache.zookeeper.Environment".equals(loggerName)) {
					return;
				}
				
				generator.writeStartObject();
				super.getProviders().writeTo(generator, event);
				generator.writeEndObject();
				generator.flush();
			} catch (Exception e) {
			}
		}

		public LogstashLoggingEventCompositeJsonFormatter(ContextAware declaredOrigin) {
			super(declaredOrigin);
		}
	}
}
