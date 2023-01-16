package com.taotao.cloud.message.biz.websockt.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 基于Spring 封装开发
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 09:08:11
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private AuthTextWebSocketHandler authTextWebSocketHandler;
	@Autowired
	private WebsocketInterceptor websocketInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
			.addHandler(authTextWebSocketHandler, "/spring/websocket")
			.addInterceptors(websocketInterceptor)
			.setAllowedOrigins("*");
	}
}
