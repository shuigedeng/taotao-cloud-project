package com.taotao.cloud.sys.biz.websockt.stomp;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 基于STOMP开发
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 09:08:11
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 配置客户端尝试连接地址
		registry.addEndpoint("/stomp/websocket")
			.setAllowedOrigins("*")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 设置广播节点
		registry.enableSimpleBroker("/topic", "/user");
		// 客户端向服务端发送消息需有/app 前缀
		registry.setApplicationDestinationPrefixes("/app");
		// 指定用户发送（一对一）的前缀 /user/
		registry.setUserDestinationPrefix("/user/");
	}
}
