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

package com.taotao.cloud.sys.biz.config.redis;

import com.taotao.boot.common.constant.RedisConstants;
import com.taotao.boot.common.support.thread.MDCThreadPoolExecutor;
import com.taotao.boot.common.support.thread.ThreadPoolFactory;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.sys.biz.config.redis.delegate.DataVersionLogTopicMessageDelegate;
import com.taotao.cloud.sys.biz.config.redis.delegate.RequestLogTopicMessageDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * RedisListenerConfig
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/01/17 16:12
 */
@Configuration
public class RedisListenerConfig {

	@Bean
	@Primary
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		RequestLogTopicMessageDelegate requestLogTopicMessageDelegate,
		DataVersionLogTopicMessageDelegate dataVersionLogTopicMessageDelegate) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);

		// Runtime.getRuntime().availableProcessors() * 2
		MDCThreadPoolExecutor executor = new MDCThreadPoolExecutor(
			100,
			1500,
			2000,
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new ThreadPoolFactory("taotao-cloud-redis-listener-executor"));
		container.setTaskExecutor(executor);

		Map<MessageListenerAdapter, Collection<? extends Topic>> listeners = new HashMap<>();

		MessageListenerAdapter requestLogMessageListenerAdapter =
			new MessageListenerAdapter(requestLogTopicMessageDelegate, "handleRequestLog");
		requestLogMessageListenerAdapter.afterPropertiesSet();
		listeners.put(requestLogMessageListenerAdapter, List.of(ChannelTopic.of(RedisConstants.REQUEST_LOG_TOPIC)));

		MessageListenerAdapter dataVersionLogListenerAdapter =
			new MessageListenerAdapter(dataVersionLogTopicMessageDelegate, "handleRequestLog");
		dataVersionLogListenerAdapter.afterPropertiesSet();
		listeners.put(dataVersionLogListenerAdapter, List.of(ChannelTopic.of(RedisConstants.DATA_VERSION_LOG_TOPIC)));

		container.setMessageListeners(listeners);
		return container;
	}

	@Bean
	@Primary
	public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
		RedisMessageListenerContainer listenerContainer) {
		return new RedisKeyExpirationEventMessageListener(listenerContainer);
	}

	public static class RedisKeyExpirationEventMessageListener extends KeyExpirationEventMessageListener {

		public RedisKeyExpirationEventMessageListener(RedisMessageListenerContainer listenerContainer) {
			super(listenerContainer);
		}

		@Override
		public void onMessage(Message message, byte[] pattern) {
			LogUtils.info("接受到消息: {}, {}", message, new String(pattern));
		}
	}
}
