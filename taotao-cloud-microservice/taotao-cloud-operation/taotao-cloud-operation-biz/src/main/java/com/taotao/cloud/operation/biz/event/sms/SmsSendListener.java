package com.taotao.cloud.operation.biz.event.sms;

import com.taotao.cloud.sms.common.event.SmsSendFailEvent;
import com.taotao.cloud.sms.common.event.SmsSendFinallyEvent;
import com.taotao.cloud.sms.common.event.SmsSendSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SmsSendListener {

	@Async
	@EventListener(SmsSendFailEvent.class)
	public void smsSendFailEventListener(SmsSendFailEvent event) {

	}

	@Async
	@EventListener(SmsSendSuccessEvent.class)
	public void smsSendSuccessEventListener(SmsSendSuccessEvent event) {

	}

	@Async
	@EventListener(SmsSendFinallyEvent.class)
	public void smsSendFinallyEventListener(SmsSendFinallyEvent event) {

	}
}
