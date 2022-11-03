// package com.taotao.cloud.sys.biz.pulsar;
//
// import io.github.majusko.pulsar.PulsarMessage;
// import io.github.majusko.pulsar.annotation.PulsarConsumer;
// import io.github.majusko.pulsar.producer.ProducerFactory;
// import io.github.majusko.pulsar.producer.PulsarTemplate;
// import org.apache.pulsar.client.api.Consumer;
// import org.apache.pulsar.client.api.MessageId;
// import org.apache.pulsar.client.api.Messages;
// import org.apache.pulsar.client.api.PulsarClientException;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.stereotype.Service;
//
// import java.util.ArrayList;
// import java.util.List;
//
// @Configuration
// public class TestProducerConfiguration {
//
// 	@Bean
// 	public ProducerFactory producerFactory() {
// 		return new ProducerFactory()
// 			.addProducer("my-topic", MyMsg.class)
// 			.addProducer("other-topic", String.class);
// 	}
//
// 	@Service
// 	public static class MyProducer6 {
//
// 		@Autowired
// 		private PulsarTemplate<MyMsg> producer;
//
// 		void sendHelloWorld() throws PulsarClientException {
// 			producer.send("my-topic", new MyMsg("Hello world!"));
// 		}
// 	}
//
// 	@Service
// 	public static class MyConsumer5 {
//
// 		@PulsarConsumer(topic = "my-topic", clazz = MyMsg.class)
// 		void consume(MyMsg msg) {
// 			// TODO process your message
// 			System.out.println(msg.getData());
// 		}
// 	}
//
// 	@Service
// 	public static class MyBatchConsumer2 {
//
// 		@PulsarConsumer(topic = "my-topic",
// 			clazz = MyMsg.class,
// 			consumerName = "my-consumer",
// 			subscriptionName = "my-subscription")
// 		public void consumeString(Messages<MyMsg> msgs) {
// 			msgs.forEach((msg) -> {
// 				System.out.println(msg);
// 			});
// 		}
//
// 	}
//
// 	@Service
// 	public static class MyBatchTestConsumer1 {
//
// 		@PulsarConsumer(topic = "my-topic",
// 			clazz = MyMsg.class,
// 			consumerName = "my-consumer",
// 			subscriptionName = "my-subscription")
// 		public List<MessageId> consumeString(Messages<MyMsg> msgs) {
// 			List<MessageId> ackList = new ArrayList<>();
// 			msgs.forEach((msg) -> {
// 				System.out.println(msg);
// 				ackList.add(msg.getMessageId());
// 			});
// 			return ackList;
// 		}
//
// 	}
//
// 	@Service
// 	public static class MyBatchTest1Consumer {
//
// 		@PulsarConsumer(topic = "my-topic",
// 			clazz = MyMsg.class,
// 			consumerName = "my-consumer",
// 			subscriptionName = "my-subscription")
// 		public void consumeString(Messages<MyMsg> msgs, Consumer<MyMsg> consumer) throws PulsarClientException {
// 			List<MessageId> ackList = new ArrayList<>();
// 			msgs.forEach((msg) -> {
// 				try {
// 					System.out.println(msg);
// 					ackList.add(msg.getMessageId());
// 				} catch (Exception ex) {
// 					System.err.println(ex.getMessage());
// 					consumer.negativeAcknowledge(msg);
// 				}
// 			});
// 			consumer.acknowledge(ackList);
// 		}
//
// 	}
//
// 	@Service
// 	public static class MyConsumer {
//
// 		@PulsarConsumer(topic = "my-topic", clazz = MyMsg.class)
// 		void consume(PulsarMessage<MyMsg> myMsg) {
// 			// producer.send("sdfsdf", myMsg.getValue());
// 		}
// 	}
//
// 	@Service
// 	public static class MyConsumerTest {
//
// 		@PulsarConsumer(
// 			topic = "${my.custom.topic.name}",
// 			clazz = MyMsg.class,
// 			consumerName = "${my.custom.consumer.name}",
// 			subscriptionName = "${my.custom.subscription.name}")
// 		public void consume(MyMsg myMsg) {
// 		}
// 	}
// }
