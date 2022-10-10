package com.taotao.cloud.promotion.biz.event.message;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageFailEventListener {

	@EventListener(MessageFailEvent.class)
	public void messageFailEventListener(MessageFailEvent event) {

	}

}
