package com.taotao.cloud.sys.biz.websockt.spring;

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
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private HttpAuthHandler httpAuthHandler;
	@Autowired
	private MyInterceptor myInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
			.addHandler(httpAuthHandler, "/spring/websocket")
			.addInterceptors(myInterceptor)
			.setAllowedOrigins("*");
	}
}
