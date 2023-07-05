package com.taotao.cloud.gateway.authentication;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;

public class AuthorizationGrantedEventListener {

	@EventListener(AuthorizationGrantedEvent.class)
	public void authorizationDeniedEvent(AuthorizationGrantedEvent<?> event) {

	}
}
