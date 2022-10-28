package com.taotao.cloud.logger.logback.layout;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackSkyWalkingContextPatternConverter extends ClassicConverter {
	public LogbackSkyWalkingContextPatternConverter() {
	}

	@Override
	public String convert(ILoggingEvent iLoggingEvent) {
		return "SW_CTX: N/A";
	}
}
