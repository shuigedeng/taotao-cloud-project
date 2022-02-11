package com.taotao.cloud.order.biz.stream.pulsar;

import com.taotao.cloud.common.utils.LogUtil;
import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {
    
    @PulsarConsumer(topic=Product.PRODUCT_TOPIC, clazz= Product.class)
    void consume(Product product) {
    	// TODO process your message
    	LogUtil.info(product.getData());
    }


	@PulsarConsumer(topic=Product.PRODUCT_TOPIC, clazz=Product.class)
	void consume(PulsarMessage<Product> message) {
		LogUtil.info(message.getValue().toString());
		//producer.send(TOPIC, msg.getValue());
	}
}
