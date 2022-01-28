/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.biz.config;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolExecutor;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * RedisListenerConfig
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/01/17 16:12
 */
@Configuration
public class RedisListenerConfig {

	public static class QuartzJobTopicMessageDelegate {

		public void handleMessage(String message) {
			LogUtil.info(message);
		}
	}

	public static class SensitiveWordTopicMessageDelegate {

		public void handleMessage(String message) {
			LogUtil.info(message);
		}
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);

		MonitorThreadPoolExecutor executor = new MonitorThreadPoolExecutor(
			Runtime.getRuntime().availableProcessors(),
			Runtime.getRuntime().availableProcessors() * 2,
			60,
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new MonitorThreadPoolFactory("taotao-cloud-redis-listener-executor"));
		executor.setNamePrefix("taotao-cloud-redis-listener-executor");
		container.setTaskExecutor(executor);

		Map<MessageListenerAdapter, Collection<? extends Topic>> listeners = new HashMap<>();
		List<ChannelTopic> quartzJobTopic = new ArrayList<>();
		quartzJobTopic.add(new ChannelTopic("quartzJobTopic"));
		listeners.put(
			new MessageListenerAdapter(new QuartzJobTopicMessageDelegate(), "handleMessage"),
			quartzJobTopic);

		List<ChannelTopic> sensitiveWordTopics = new ArrayList<>();
		sensitiveWordTopics.add(new ChannelTopic("sensitiveWordTopic"));
		listeners.put(
			new MessageListenerAdapter(new SensitiveWordTopicMessageDelegate(), "handleMessage"),
			sensitiveWordTopics);

		container.setMessageListeners(listeners);
		return container;
	}


	@Component
	public static class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

		public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
			super(listenerContainer);
		}

		@Override
		public void onMessage(Message message, byte[] pattern) {
			LogUtil.info("接受到消息: {}, {}", message, new String(pattern));
		}
	}

}
