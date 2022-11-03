package com.taotao.cloud.sys.biz.pulsar.example.error;

import io.github.majusko.pulsar.consumer.ConsumerAggregator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PulsarErrorHandler {

	private final ConsumerAggregator aggregator;

	public PulsarErrorHandler(ConsumerAggregator aggregator) {
		this.aggregator = aggregator;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void pulsarErrorHandler() {
		aggregator.onError(failedMessage -> failedMessage.getException().printStackTrace());
	}
}
