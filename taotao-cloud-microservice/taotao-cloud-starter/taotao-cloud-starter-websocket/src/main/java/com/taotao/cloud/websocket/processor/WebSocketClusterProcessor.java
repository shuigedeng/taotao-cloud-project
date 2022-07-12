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

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.websocket.domain.WebSocketMessage;
import com.taotao.cloud.websocket.exception.IllegalChannelException;
import com.taotao.cloud.websocket.exception.PrincipalNotFoundException;
import com.taotao.cloud.websocket.properties.WebSocketProperties;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.InitializingBean;

/**
 * WebSocket集群处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 10:58:49
 */
public class WebSocketClusterProcessor implements InitializingBean {

	private RedissonClient redissonClient;
	private WebSocketProperties webSocketProperties;
	private WebSocketMessageSender webSocketMessageSender;

	public void setRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
		this.webSocketProperties = webSocketProperties;
	}

	public void setWebSocketMessageSender(WebSocketMessageSender webSocketMessageSender) {
		this.webSocketMessageSender = webSocketMessageSender;
	}

	/**
	 * 发送给集群中指定用户信息。
	 *
	 * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
	 */
	public void toClusterUser(WebSocketMessage<String> webSocketMessage) {
		try {
			webSocketMessageSender.toUser(webSocketMessage);
		} catch (PrincipalNotFoundException e) {
			RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic(),
				new JsonJacksonCodec());
			rTopic.publish(webSocketMessage);
			LogUtil.debug(
				"Current instance can not found user [{}], publish message.",
				webSocketMessage.getTo());
		} catch (IllegalChannelException e) {
			LogUtil.error("Web socket channel is incorrect.");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RTopic topic = redissonClient.getTopic(webSocketProperties.getTopic());
		topic.addListener(WebSocketMessage.class,
			(MessageListener<WebSocketMessage<String>>) (charSequence, webSocketMessage) -> {
				LogUtil.debug("Redisson received web socket sync message [{}]",
					webSocketMessage);
				webSocketMessageSender.toUser(webSocketMessage);
			});
	}
}
