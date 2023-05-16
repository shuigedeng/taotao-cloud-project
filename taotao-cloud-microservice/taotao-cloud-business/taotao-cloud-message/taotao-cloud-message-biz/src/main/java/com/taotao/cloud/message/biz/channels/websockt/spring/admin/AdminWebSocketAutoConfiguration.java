package com.taotao.cloud.message.biz.channels.websockt.spring.admin;

import com.taotao.cloud.message.biz.ballcat.admin.websocket.component.UserAttributeHandshakeInterceptor;
import com.taotao.cloud.message.biz.ballcat.admin.websocket.component.UserSessionKeyGenerator;
import com.taotao.cloud.message.biz.ballcat.common.websocket.session.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Import({ SystemWebsocketEventListenerConfiguration.class, NotifyWebsocketEventListenerConfiguration.class })
@Configuration
@RequiredArgsConstructor
public class AdminWebSocketAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(UserAttributeHandshakeInterceptor.class)
	public HandshakeInterceptor authenticationHandshakeInterceptor() {
		return new UserAttributeHandshakeInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(SessionKeyGenerator.class)
	public SessionKeyGenerator userSessionKeyGenerator() {
		return new UserSessionKeyGenerator();
	}

}
