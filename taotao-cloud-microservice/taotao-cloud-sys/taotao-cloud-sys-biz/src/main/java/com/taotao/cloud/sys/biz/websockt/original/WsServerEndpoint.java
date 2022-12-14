package com.taotao.cloud.sys.biz.websockt.original;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/original/websocket")
public class WsServerEndpoint {

	/**
	 * 连接成功
	 *
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("连接成功");
	}

	/**
	 * 连接关闭
	 *
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("连接关闭");
	}

	/**
	 * 接收到消息
	 *
	 * @param text
	 */
	@OnMessage
	public String onMsg(String text) throws IOException {
		return "servet 发送：" + text;
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
	}
}
