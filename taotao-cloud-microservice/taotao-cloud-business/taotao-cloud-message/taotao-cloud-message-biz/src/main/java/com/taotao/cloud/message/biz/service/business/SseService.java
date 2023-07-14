package com.taotao.cloud.message.biz.service.business;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface SseService {
	SseEmitter getConn(String clientId);

	void send( String clientId) throws IOException;
	public void closeDialogueConn( String clientId);
}
