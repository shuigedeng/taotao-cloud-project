package com.taotao.cloud.sys.biz.pulsar;

import io.github.majusko.pulsar.producer.DefaultProducerInterceptor;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Component;

@Component
public class PulsarProducerInterceptor extends DefaultProducerInterceptor {

	@Override
	public Message beforeSend(Producer producer, Message message) {
		super.beforeSend(producer, message);
		System.out.println("do something");
		return message;
	}

	@Override
	public void onSendAcknowledgement(Producer producer, Message message, MessageId msgId,
		Throwable exception) {
		super.onSendAcknowledgement(producer, message, msgId, exception);
	}
}
