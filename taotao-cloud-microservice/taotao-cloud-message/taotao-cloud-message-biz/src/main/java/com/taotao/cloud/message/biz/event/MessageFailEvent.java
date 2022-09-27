package com.taotao.cloud.message.biz.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class MessageFailEvent extends ApplicationEvent {
	public MessageFailEvent(Object source) {
		super(source);
	}

	public MessageFailEvent(Object source, Clock clock) {
		super(source, clock);
	}
}
