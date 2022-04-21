/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ws.netty;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * NettyWebSocketServer
 *
 * @author shuigedeng
 * @since 2020/12/30 下午4:39
 * @version 2022.03
 */
@ServerEndpoint(value = "/ws/taotao-cloud")
@Component
public class NettyWebSocketServer {

	@PostConstruct
	public void init() {
		System.out.println("websocket 加载");
	}

	private static Logger log = LoggerFactory.getLogger(NettyWebSocketServer.class);
	private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
	// concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
	private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();


	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		SessionSet.add(session);
		// 在线数加1
		int cnt = ONLINE_COUNT.incrementAndGet();
		log.info(String.valueOf(session.getRequestURI()));
		log.info("有连接加入，当前连接数为：{},sessionId={}", cnt, session.getId());
		sendMessage(session, "连接成功");
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		SessionSet.remove(session);
		int cnt = ONLINE_COUNT.decrementAndGet();
		log.info("有连接关闭，当前连接数为：{}", cnt);
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到客户端信息 =====" + message);
		sendMessage(session, message);
	}

	/**
	 * 出现错误
	 *
	 * @param session session
	 * @param error   error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
		error.printStackTrace();
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
	 *
	 * @param session session
	 * @param message message
	 */
	public static void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			log.error("发送消息出错：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 群发消息
	 *
	 * @param message message
	 * @throw IOException
	 */
	public static void broadCastInfo(String message) throws IOException {
		for (Session session : SessionSet) {
			if (session.isOpen()) {
				sendMessage(session, message);
			}
		}
	}

	/**
	 * 指定Session发送消息
	 *
	 * @param sessionId sessionId
	 * @param message   message
	 */
	public static void sendMessage(String message, String sessionId) throws IOException {
		Session session = null;
		for (Session s : SessionSet) {
			if (s.getId().equals(sessionId)) {
				session = s;
				break;
			}
		}
		if (session != null) {
			sendMessage(session, message);
		} else {
			log.warn("没有找到你指定ID的会话：{}", sessionId);
		}
	}
}
