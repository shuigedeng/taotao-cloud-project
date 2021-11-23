package com.taotao.cloud.pulsar.model;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

public class DummyMessageListener<T> implements MessageListener<T> {

	@Override
	public void received(Consumer<T> consumer, Message<T> msg) {

	}

}
