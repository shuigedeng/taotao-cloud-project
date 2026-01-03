package com.taotao.cloud.message.biz.channels.websockt.spring.admin;

import com.taotao.boot.websocket.spring.admin.listener.SystemWebsocketEventListener;
import com.taotao.boot.websocket.spring.common.distribute.MessageDistributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SystemWebsocketEventListenerConfiguration
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@ConditionalOnClass(SystemWebsocketEventListener.class)
@Configuration(proxyBeanMethods = false)
public class SystemWebsocketEventListenerConfiguration {

    private final MessageDistributor messageDistributor;

    public SystemWebsocketEventListenerConfiguration( MessageDistributor messageDistributor ) {
        this.messageDistributor = messageDistributor;
    }

    @Bean
    public SystemWebsocketEventListener systemWebsocketEventListener() {
        return new SystemWebsocketEventListener(messageDistributor);
    }

}
