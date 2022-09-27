package com.taotao.cloud.message.biz.event.listener;

import com.taotao.cloud.sms.common.event.SmsSendFailEvent;
import com.taotao.cloud.sms.common.event.SmsSendFinallyEvent;
import com.taotao.cloud.sms.common.event.SmsSendSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SmsSendListener {

	@EventListener(SmsSendFailEvent.class)
	public void smsSendFailEventListener(SmsSendFailEvent event) {

	}

	@EventListener(SmsSendSuccessEvent.class)
	public void smsSendSuccessEventListener(SmsSendSuccessEvent event) {

	}

	@EventListener(SmsSendFinallyEvent.class)
	public void smsSendFinallyEventListener(SmsSendFinallyEvent event) {

	}
}
