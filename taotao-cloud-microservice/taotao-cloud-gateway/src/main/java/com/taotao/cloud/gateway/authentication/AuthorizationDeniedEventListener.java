package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

/**
 * 授权拒绝事件侦听器
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-12 11:06:59
 */
@Component
public class AuthorizationDeniedEventListener {

	@EventListener(AuthorizationDeniedEvent.class)
	public void authorizationDeniedEvent(AuthorizationDeniedEvent<?> event) {
		LogUtils.error("AuthorizationDeniedEvent", event.getAuthorizationDecision());
	}
}
