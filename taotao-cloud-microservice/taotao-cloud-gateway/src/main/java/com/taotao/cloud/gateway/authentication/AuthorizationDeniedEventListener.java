package com.taotao.cloud.gateway.authentication;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;

public class AuthorizationDeniedEventListener {

	@EventListener(AuthorizationDeniedEvent.class)
	public void authorizationDeniedEvent(AuthorizationDeniedEvent<?> event) {

	}
}
