//package com.taotao.cloud.sys.biz.pulsar;
//
//import io.github.majusko.pulsar.consumer.ConsumerAggregator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PulsarErrorHandler {
//
//	@Autowired
//	private ConsumerAggregator aggregator;
//
//	@EventListener(ApplicationReadyEvent.class)
//	public void pulsarErrorHandler() {
//		aggregator.onError(failedMessage ->
//			failedMessage.getException()
//				.printStackTrace());
//	}
//}
