//package com.taotao.cloud.sys.biz.pulsar;
//
//import io.github.majusko.pulsar.consumer.DefaultConsumerInterceptor;
//import org.apache.pulsar.client.api.Consumer;
//import org.apache.pulsar.client.api.Message;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PulsarConsumerInterceptor extends DefaultConsumerInterceptor<Object> {
//
//	@Override
//	public Message beforeConsume(Consumer<Object> consumer, Message<Object> message) {
//		System.out.println("do something");
//		return super.beforeConsume(consumer, message);
//	}
//}
