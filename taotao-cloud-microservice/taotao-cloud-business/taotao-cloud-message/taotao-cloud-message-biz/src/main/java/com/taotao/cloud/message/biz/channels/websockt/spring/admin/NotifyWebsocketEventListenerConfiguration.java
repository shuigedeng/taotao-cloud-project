package com.taotao.cloud.message.biz.channels.websockt.spring.admin;

import com.taotao.boot.websocket.spring.admin.listener.NotifyWebsocketEventListener;
import com.taotao.boot.websocket.spring.common.distribute.MessageDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NotifyWebsocketEventListenerConfiguration
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@ConditionalOnClass({NotifyWebsocketEventListener.class, UserAnnouncementService.class})
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

    private final MessageDistributor messageDistributor;

    @Bean
    public NotifyWebsocketEventListener notifyWebsocketEventListener(
            NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler ) {
        return new NotifyWebsocketEventListener(messageDistributor, notifyInfoDelegateHandler);
    }

}
