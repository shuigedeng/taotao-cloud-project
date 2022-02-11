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
package com.taotao.cloud.sys.biz.config.redis;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolExecutor;
import com.taotao.cloud.core.configuration.MonitorAutoConfiguration.MonitorThreadPoolFactory;
import com.taotao.cloud.sys.biz.config.redis.delegate.QuartzJobTopicMessageDelegate;
import com.taotao.cloud.sys.biz.config.redis.delegate.ScheduledJobTopicMessageDelegate;
import com.taotao.cloud.sys.biz.config.redis.delegate.SensitiveWordsTopicMessageDelegate;
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

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		QuartzJobTopicMessageDelegate quartzJobTopicMessageDelegate,
		ScheduledJobTopicMessageDelegate scheduledJobTopicMessageDelegate,
		SensitiveWordsTopicMessageDelegate sensitiveWordsTopicMessageDelegate) {

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
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "addJob"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_ADD_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "deleteJob"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_DELETE_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "resumeJob"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_RESUME_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "pauseJob"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_PAUSE_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "runJobNow"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_RUN_NOW_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "updateJobCron"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_UPDATE_CRON_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "updateJob"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_UPDATE_TOPIC)));
		listeners.put(new MessageListenerAdapter(quartzJobTopicMessageDelegate, "addJobLog"),
			List.of(new ChannelTopic(RedisConstant.QUARTZ_JOB_LOG_ADD_TOPIC)));

		listeners.put(
			new MessageListenerAdapter(sensitiveWordsTopicMessageDelegate, "sensitiveWords"),
			List.of(new ChannelTopic(RedisConstant.SENSITIVE_WORDS_TOPIC)));

		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "updateCronScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_UPDATE_CRON_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "addCronScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_ADD_CRON_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate,
				"updateFixedDelayScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_UPDATE_FIXED_DELAY_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "addFixedDelayScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_ADD_FIXED_DELAY_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate,
				"updateFixedRateScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_UPDATE_FIXED_RATE_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "addFixedRateScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_ADD_FIXED_RATE_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "cancelScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_CANCEL_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "runOnceScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_RUN_ONCE_TOPIC)));
		listeners.put(
			new MessageListenerAdapter(scheduledJobTopicMessageDelegate, "callOffScheduled"),
			List.of(new ChannelTopic(RedisConstant.SCHEDULED_CALL_OFF_TOPIC)));

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
