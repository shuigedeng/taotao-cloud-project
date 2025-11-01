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

package com.taotao.cloud.message.biz.channels.websockt.netty;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当ServerEndpointExporter类通过Spring配置进行声明并被使用，
 * 它将会去扫描带有@ServerEndpoint注解的类 被注解的类将被注册成为一个WebSocket端点
 * 所有的配置项都在这个注解的属性中 ( 如:@ServerEndpoint(“/ws”) )
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/09/06 11:51
 */
@ServerEndpoint(path = "/netty/websocket", port = "8989", host = "0.0.0.0", maxFramePayloadLength = "6553600", allIdleTimeSeconds = "300")
public class NettyWebSocket {

	/**
	 * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	 */
	private static ConcurrentHashMap<String, NettyWebSocket> userIdWebSocketMap = new ConcurrentHashMap<>();
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;
	/**
	 * 当前userId
	 */
	private String userId = "";

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 当有新的连接进入时
	 *
	 * @param session session
	 * @param headers headers
	 * @param req     通过 通过@RequestParam实现请求中query的获取参数
	 * @param reqMap  reqMap
	 * @param pathMap 支持RESTful风格中获取参数
	 *                <p>
	 * @BeforeHandshake 注解，可在握手之前对连接进行关闭 在@BeforeHandshake事件中可设置子协议
	 * 去掉配置端点类上的 @Component 更新Netty版本到 4.1.44.Final
	 * 当有新的连接进入时，对该方法进行回调 注入参数的类型:Session、HttpHeaders...
	 */
	@SuppressWarnings("rawtypes")
	@BeforeHandshake
	public void handshake(Session session, HttpHeaders headers, @RequestParam String req,
						  @RequestParam MultiValueMap reqMap, @PathVariable Map pathMap) {
		String userId = checkToken(headers, session);
		if (StrUtil.isNotBlank(userId)) {
			LogUtils.info("用户有新的连接进入 userId: {}", userId);
		}
	}

	/**
	 * 当有新的WebSocket连接完成时，对该方法进行回调 , ParameterMap
	 * parameterMap注入参数的类型:Session、HttpHeaders、ParameterMap
	 *
	 * @param session session
	 * @param headers headers
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	@OnOpen
	public void onOpen(
		Session session,
		HttpHeaders headers,
		@RequestParam String req,
		@RequestParam MultiValueMap reqMap,
		@PathVariable String arg,
		@PathVariable Map pathMap) throws IOException {
		//校验token 用户是否存在
		String userId = checkToken(headers, session);
		if (StrUtil.isBlank(userId)) {
			LogUtils.info("用户有新的连接进入, token校验失败");
			return;
		}

		// 加入set中
		userIdWebSocketMap.put(userId, this);
		// 在线数加1
		addOnlineCount();

		session.setAttribute("token", headers.get("token"));
		session.setAttribute("userId", userId);
		this.session = session;
		this.userId = userId;

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", userId);
		jsonObject.put("msg", "连接成功");
		session.sendText(jsonObject.toString());

		LogUtils.debug("UserId = {}, 通道ID={}, 当前连接人数={}", "xxx", getSessionId(session), getOnlineCount());
	}

	/**
	 * 当有WebSocket连接关闭时，对该方法进行回调 注入参数的类型:Session
	 *
	 * @param session session
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		//String userId = session.getAttribute("userId");
		if (userId != null) {
			this.session = null;
			this.userId = null;

			userIdWebSocketMap.remove(userId);
			// 在线数减1
			subOnlineCount();

			if (session.isOpen()) {
				session.sendText("连接关闭");
				session.close();
			}

			LogUtils.debug("==============>>>>>>>>>>>>>>>{},用户退出,当前在线人数为:{}", userId, getOnlineCount());
		}
	}

	/**
	 * 当有WebSocket抛出异常时，对该方法进行回调 注入参数的类型:Session、Throwable
	 *
	 * @param session session
	 * @param cause   throwable
	 */
	@OnError
	public void onError(Session session, Throwable cause) {
//		String userId = session.getAttribute("userId");
		if (userId != null) {
			if (Objects.nonNull(cause) && !(cause instanceof EOFException)) {
				LogUtils.error("UserId = {}, 通道ID={}, 出错信息={}", userId, session.id(), cause.toString());
			}
			if (session.isOpen()) {
				session.sendText("连接错误");
				session.close();
			}
		}
	}

	/**
	 * 接收到字符串消息时，对该方法进行回调 注入参数的类型:Session、String
	 *
	 * @param session
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(Session session, String message) {
		LogUtils.debug("用户消息:{},报文:{},session现有的主题：{},主题：{}", session.getAttribute("userId"), message, session.getAttribute("F39_PAN_KOU"), session.getAttribute("F39_PAN_KOU_GCC"));

		// 可以群发消息
		// 消息可异步保存到数据库、redis、MongoDB 等
		if (StringUtils.isNotBlank(message)) {
			try {
				// 解析发送的报文
				JSONObject jsonObject = JSON.parseObject(message);
				String type = jsonObject.getString("type");
//				if (MonitorTypeConstants.TO_SUB.equals(type)) {
//
//				} else if (MonitorTypeConstants.TO_UNSUB.equals(type)) {
//
//				} else if (MonitorTypeConstants.GET_ALL_L_LINE.equals(type)) {
//					socketServiceImpl.pushKLineData(session, jsonObject);
//				} else {
//					// webSocketMap.get(userId).sendMessage("你想干什么");
//				}
				// }else{
				// LogUtils.info("请求的userId:"+message+"不在该服务器上");
				// 否则不在这个服务器上，发送到mysql或者redis
				// }
			} catch (Exception e) {
				LogUtils.error(e);
			}
		}
	}

	/**
	 * 当接收到二进制消息时，对该方法进行回调 注入参数的类型:Session、byte[]
	 *
	 * @param session session
	 * @param bytes   bytes
	 */
	@OnBinary
	public void onBinary(Session session, byte[] bytes) {
		for (byte b : bytes) {
			LogUtils.info(b);
		}
		session.sendBinary(bytes);
	}

	/**
	 * 当接收到Netty的事件时，对该方法进行回调 注入参数的类型:Session、Object
	 *
	 * @param session session
	 * @param evt     evt
	 */
	@OnEvent
	public void onEvent(Session session, Object evt) {
		LogUtils.info("==netty心跳事件===evt=>>>>{},来自===userId:{}", JSONObject.toJSONString(evt), session.channel().id());
		if (evt instanceof IdleStateEvent idleStateEvent) {
			switch (idleStateEvent.state()) {
				case READER_IDLE -> LogUtils.debug("read idle");
				//		socketServiceImpl.sendHeart(session);
				case WRITER_IDLE -> LogUtils.debug("write idle");
				case ALL_IDLE -> LogUtils.debug("all idle");
				default -> {
				}
			}
		}
	}

	/**
	 * 获取通道ID
	 */
	private String getSessionId(Session session) {
		return session.id().asShortText();
	}

	/**
	 * 协议错误
	 */
//	public void errorMessage(String command) {
//		JSONObject retObj = new JSONObject();
//		retObj.set("code", ConstDef.ERROR_CODE_10001);
//		retObj.set("msg", ConstDef.ERROR_CODE_10001_DESP);
//		retObj.set("command", command);
//		try {
//			sendMessage(retObj.toString());
//		} catch (IOException e) {
//			LogUtils.error("UserId = {}, 通道ID={}, 解析执行出错信息={}", userInfo.toString(), getSessionId(session), e.getMessage());
//		}
//	}

	/**
	 * 实现服务器主动推送
	 */
	public ChannelFuture sendMessage(String message) throws IOException {
		return this.session.sendText(message);
	}

	/**
	 * 在线用户数
	 */
	public long getOnlineCount() {
		String onlineCountValue = (String) redisRepository.opsForValue().get("online_key");
		if (StrUtil.isBlank(onlineCountValue) || !NumberUtil.isNumber(onlineCountValue)) {
			return 0L;
		}
		return Long.parseLong(onlineCountValue);
	}

	/**
	 * 在线数+1
	 */
	private void addOnlineCount() {
		redisRepository.opsForValue().increment("online_key");
	}

	/**
	 * 在线数-1
	 */
	private void subOnlineCount() {
		redisRepository.opsForValue().decrement("online_key");
	}

	private String checkToken(HttpHeaders headers, Session session) {
		String token = headers.get("token");
		if (StringUtils.isEmpty(token)) {
			session.close();
			session.sendText("token不能为null");
			return null;
		}

		// todo 解密token获取userId
		String userId = token.split("\\|")[0];

		String redisToken = (String) redisRepository.get(StringUtils.join("netty-websocket-online-", userId));

		if (!(token).equals(redisToken)) {
			//用户不存在或者token失败
			session.close();
			session.sendText("用户校验失败");
			return null;
		} else {
			// 设置协议stomp
			// session.setSubprotocols("stomp");
		}
		return userId;
	}

	/**
	 * 自定义关闭
	 *
	 * @param userId
	 */
//	public static void close(String userId) {
//		if (webSocketMap.containsKey(userId)) {
//			webSocketMap.remove(userId);
//		}
//	}

	/**
	 * 自定义 指定的userId服务端向客户端发送消息
	 */
	public static void sendInfo(Chat chat, List<Chat> chats) {
		try {
			if (!StringUtils.isEmpty(chat.getTargetUserId().toString()) && userIdWebSocketMap.containsKey(chat.getTargetUserId().toString())) {
//				userIdWebSocketMap.get(chat.getUserId().toString()).sendMessage(JSONObject.toJSONString(chats));
				userIdWebSocketMap.get(chat.getTargetUserId().toString()).sendMessage(JSONObject.toJSONString(chats));
			} else {
				userIdWebSocketMap.get(chat.getUserId().toString()).sendMessage(JSONObject.toJSONString(chats));
			}
		} catch (IOException e) {
			LogUtils.error(e);
			LogUtils.error(e);
		}
	}

	public static void sendInfo(String userId, String message) {
		try {
			if (!StringUtils.isEmpty(userId) && userIdWebSocketMap.containsKey(userId)) {
//				userIdWebSocketMap.get(userId).sendMessage(JSONObject.toJSONString(chats));
				userIdWebSocketMap.get(userId).sendMessage(message);
			}
		} catch (IOException e) {
			LogUtils.error(e);
			LogUtils.error(e);
		}
	}

	/**
	 * 自定义关闭
	 *
	 * @param userId
	 */
	public static void close(String userId) {
		if (userIdWebSocketMap.containsKey(userId)) {
			userIdWebSocketMap.remove(userId);
		}
	}

	/**
	 * 获取在线用户信息
	 *
	 * @return
	 */
	public static Map getOnlineUser() {
		return userIdWebSocketMap;
	}
}
