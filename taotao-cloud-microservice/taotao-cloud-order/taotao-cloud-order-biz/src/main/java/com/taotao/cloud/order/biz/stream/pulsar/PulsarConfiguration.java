package com.taotao.cloud.order.biz.stream.pulsar;

import io.github.majusko.pulsar.consumer.ConsumerAggregator;
import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class PulsarConfiguration {

    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
            .addProducer(Product.PRODUCT_TOPIC, Product.class)
            .addProducer("other-topic", String.class);
    }

	@Configuration
	public static class PulsarErrorHandler {

		@Autowired
		private ConsumerAggregator aggregator;

		@EventListener(ApplicationReadyEvent.class)
		public void pulsarErrorHandler() {
			aggregator.onError(failedMessage ->
				failedMessage.getException().printStackTrace());
		}
	}
}
