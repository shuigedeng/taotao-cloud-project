package com.taotao.cloud.sys.biz.pulsar;

import io.github.majusko.pulsar.error.exception.ClientInitException;
import io.github.majusko.pulsar.reactor.FluxConsumer;
import io.github.majusko.pulsar.reactor.FluxConsumerFactory;
import io.github.majusko.pulsar.reactor.PulsarFluxConsumer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Configuration
public class MyFluxConsumers {

	@Autowired
	private FluxConsumerFactory fluxConsumerFactory;

	@Bean
	public FluxConsumer<MyMsg> myFluxConsumer() throws ClientInitException, PulsarClientException {
		return fluxConsumerFactory.newConsumer(
			PulsarFluxConsumer.builder()
				.setTopic("flux-topic")
				.setConsumerName("flux-consumer")
				.setSubscriptionName("flux-subscription")
				.setMessageClass(MyMsg.class)
				.build());
	}

	@Service
	public static class MyFluxConsumerService {

		@Autowired
		private FluxConsumer<MyMsg> myFluxConsumer;

		@EventListener(ApplicationReadyEvent.class)
		public void subscribe() {
			myFluxConsumer
				.asSimpleFlux()
				.subscribe(msg -> System.out.println(msg.getData()));
		}
	}

	@Service
	public static class MyFluxConsumerTestService {

		@Autowired
		private FluxConsumer<MyMsg> myFluxConsumer;

		@EventListener(ApplicationReadyEvent.class)
		public void subscribe() {
			myFluxConsumer.asFlux()
				.subscribe(msg -> {
					try {
						final MyMsg myMsg = (MyMsg) msg.getMessage().getValue();

						System.out.println(myMsg.getData());

						// you need to acknowledge the message manually on finished job
						msg.getConsumer().acknowledge(msg.getMessage());
					} catch (PulsarClientException e) {
						// you need to negatively acknowledge the message manually on failures
						msg.getConsumer().negativeAcknowledge(msg.getMessage());
					}
				});
		}
	}
}
