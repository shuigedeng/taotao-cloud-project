package com.taotao.cloud.message.biz.channels.websockt.spring.admin;

import com.taotao.cloud.websocket.spring.admin.listener.NotifyWebsocketEventListener;
import com.taotao.cloud.websocket.spring.common.distribute.MessageDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass({NotifyWebsocketEventListener.class, UserAnnouncementService.class})
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public NotifyWebsocketEventListener notifyWebsocketEventListener(
		NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler) {
		return new NotifyWebsocketEventListener(messageDistributor, notifyInfoDelegateHandler);
	}

}
