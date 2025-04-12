package com.taotao.cloud.mq.broker;

import com.taotao.cloud.mq.broker.core.MqBroker;

public class MqBrokerBootstrap {

	public static void main(String[] args) {
		MqBroker broker = new MqBroker();
		broker.start();
	}
}
