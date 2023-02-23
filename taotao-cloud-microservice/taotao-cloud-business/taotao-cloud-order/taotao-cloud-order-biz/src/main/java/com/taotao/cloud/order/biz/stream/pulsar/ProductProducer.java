
package com.taotao.cloud.order.biz.stream.pulsar;

import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {

	@Autowired
	private PulsarTemplate<Product> producer;

	public void send() throws PulsarClientException {
		producer.send(Product.PRODUCT_TOPIC, new Product("Hello world!"));
	}
}
