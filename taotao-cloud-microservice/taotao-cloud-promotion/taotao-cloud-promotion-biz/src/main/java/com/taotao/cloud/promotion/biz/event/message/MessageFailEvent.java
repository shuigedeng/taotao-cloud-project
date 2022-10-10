package com.taotao.cloud.promotion.biz.event.message;

import java.time.Clock;
import org.springframework.context.ApplicationEvent;

public class MessageFailEvent extends ApplicationEvent {
	public MessageFailEvent(Object source) {
		super(source);
	}

	public MessageFailEvent(Object source, Clock clock) {
		super(source, clock);
	}
}
