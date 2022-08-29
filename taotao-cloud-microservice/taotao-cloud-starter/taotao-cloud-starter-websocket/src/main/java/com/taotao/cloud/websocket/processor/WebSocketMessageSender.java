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
package com.taotao.cloud.websocket.processor;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.websocket.domain.WebSocketChannel;
import com.taotao.cloud.websocket.domain.WebSocketMessage;
import com.taotao.cloud.websocket.exception.IllegalChannelException;
import com.taotao.cloud.websocket.exception.PrincipalNotFoundException;
import com.taotao.cloud.websocket.properties.CustomWebSocketProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;

/**
 * Web Socket 服务端消息发送
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 10:57:01
 */
public class WebSocketMessageSender {

	private SimpMessagingTemplate simpMessagingTemplate;
	private SimpUserRegistry simpUserRegistry;
	private CustomWebSocketProperties customWebSocketProperties;

	public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	public void setSimpUserRegistry(SimpUserRegistry simpUserRegistry) {
		this.simpUserRegistry = simpUserRegistry;
	}

	public void setWebSocketProperties(CustomWebSocketProperties customWebSocketProperties) {
		this.customWebSocketProperties = customWebSocketProperties;
	}

	/**
	 * 发送给指定用户信息。
	 *
	 * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
	 * @param <T>              指定 payload 类型
	 * @throws IllegalChannelException    Web Socket 通道设置错误
	 * @throws PrincipalNotFoundException 该服务中无法找到与 identity 对应的用户 Principal
	 */
	public <T> void toUser(WebSocketMessage<T> webSocketMessage)
		throws IllegalChannelException, PrincipalNotFoundException {
		WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannel(
			webSocketMessage.getChannel());
		if (ObjectUtils.isEmpty(webSocketChannel)) {
			throw new IllegalChannelException("Web socket channel is incorrect!");
		}

		SimpUser simpUser = simpUserRegistry.getUser(webSocketMessage.getTo());
		if (ObjectUtils.isEmpty(simpUser)) {
			throw new PrincipalNotFoundException("Web socket user principal is not found!");
		}

		LogUtils.debug("Web socket send message to user [{}].", webSocketMessage.getTo());
		simpMessagingTemplate.convertAndSendToUser(webSocketMessage.getTo(),
			webSocketChannel.getDestination(), webSocketMessage.getPayload());
	}

	/**
	 * 广播 WebSocket 信息
	 *
	 * @param payload 发送的内容
	 * @param <T>     payload 类型
	 */
	public <T> void toAll(T payload) {
		simpMessagingTemplate.convertAndSend(customWebSocketProperties.getBroadcast(), payload);
	}
}
