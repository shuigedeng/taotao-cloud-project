package com.taotao.cloud.message.biz.ballcat.admin.websocket;

import com.taotao.cloud.message.biz.ballcat.admin.websocket.listener.NotifyWebsocketEventListener;
import com.taotao.cloud.message.biz.ballcat.notify.service.UserAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass({ NotifyWebsocketEventListener.class, UserAnnouncementService.class })
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public NotifyWebsocketEventListener notifyWebsocketEventListener(
			NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler) {
		return new NotifyWebsocketEventListener(messageDistributor, notifyInfoDelegateHandler);
	}

}
