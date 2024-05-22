package com.taotao.cloud.mq.example.broker;


/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerMain {

	public static void main(String[] args) {
		MqBroker broker = new MqBroker();
		broker.start();
	}

}
