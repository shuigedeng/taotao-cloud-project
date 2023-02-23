//package com.taotao.cloud.sys.biz.pulsar.example.consumer;
//
//import com.taotao.cloud.sys.biz.pulsar.example.configuration.Topics;
//import com.taotao.cloud.sys.biz.pulsar.example.data.MyMsg;
//import io.github.majusko.pulsar.annotation.PulsarConsumer;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Service
//public class ConsumerService {
//	public AtomicBoolean stringReceived = new AtomicBoolean(false);
//	public AtomicBoolean classReceived = new AtomicBoolean(false);
//
//	@PulsarConsumer(topic = Topics.STRING, clazz = String.class)
//	public void consumeString(String message) {
//		System.out.println(message);
//		stringReceived.set(true);
//	}
//
//	@PulsarConsumer(topic = Topics.CLASS, clazz = MyMsg.class)
//	public void consumeClass(MyMsg message) {
//		System.out.println(message.getData());
//		classReceived.set(true);
//	}
//}
