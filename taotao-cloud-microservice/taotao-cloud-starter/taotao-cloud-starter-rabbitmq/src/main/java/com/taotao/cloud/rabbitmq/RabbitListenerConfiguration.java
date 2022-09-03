package com.taotao.cloud.rabbitmq;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
* Rabbit 侦听器配置器
*/
@Configuration
public class RabbitListenerConfiguration implements org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer {
    private final DefaultMessageHandlerMethodFactory jsonHandlerMethodFactory;

	public RabbitListenerConfiguration(
		DefaultMessageHandlerMethodFactory jsonHandlerMethodFactory) {
		this.jsonHandlerMethodFactory = jsonHandlerMethodFactory;
	}

	@Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(jsonHandlerMethodFactory);
    }
}
