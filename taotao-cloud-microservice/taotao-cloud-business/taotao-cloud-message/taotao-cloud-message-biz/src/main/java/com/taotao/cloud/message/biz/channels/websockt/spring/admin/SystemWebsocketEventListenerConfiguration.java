package com.taotao.cloud.message.biz.channels.websockt.spring.admin;

import com.taotao.cloud.websocket.spring.admin.listener.SystemWebsocketEventListener;
import com.taotao.cloud.websocket.spring.common.distribute.MessageDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@ConditionalOnClass(SystemWebsocketEventListener.class)
@Configuration(proxyBeanMethods = false)
public class SystemWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	public SystemWebsocketEventListenerConfiguration(MessageDistributor messageDistributor) {
		this.messageDistributor = messageDistributor;
	}

	@Bean
	public SystemWebsocketEventListener systemWebsocketEventListener() {
		return new SystemWebsocketEventListener(messageDistributor);
	}

}
