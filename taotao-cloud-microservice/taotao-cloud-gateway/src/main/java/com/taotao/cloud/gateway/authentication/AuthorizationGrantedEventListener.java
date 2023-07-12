package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.stereotype.Component;

/**
 * 授权授予事件侦听器
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-12 11:06:55
 */
@Component
public class AuthorizationGrantedEventListener {

	@EventListener(AuthorizationGrantedEvent.class)
	public void authorizationDeniedEvent(AuthorizationGrantedEvent<?> event) {
		LogUtils.error("AuthorizationGrantedEvent", event.getAuthorizationDecision());
	}
}
