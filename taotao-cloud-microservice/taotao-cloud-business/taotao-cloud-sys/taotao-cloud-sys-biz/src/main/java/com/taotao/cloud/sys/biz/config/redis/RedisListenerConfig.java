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

import com.google.protobuf.InvalidProtocolBufferException;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.support.thread.MDCThreadPoolExecutor;
import com.taotao.cloud.common.support.thread.ThreadPoolFactory;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.grpc.DeviceFix;
import com.taotao.cloud.sys.biz.config.redis.delegate.SensitiveWordsTopicMessageDelegate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisListenerConfig
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022/01/17 16:12
 */
@Configuration
public class RedisListenerConfig {

	/**
	 * 使用protobuf进行redis序列化
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean(name = "protoRedisTemplate")
	public RedisTemplate<String, byte[]> protoRedisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		RedisSerializer<byte[]> bytRedisSerializer = new RedisSerializer<byte[]>() {
			@Override
			public byte[] serialize(byte[] bytes) throws SerializationException {
				return bytes;
			}

			@Override
			public byte[] deserialize(byte[] bytes) throws SerializationException {
				return bytes;
			}
		};

		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);

		redisTemplate.setValueSerializer(bytRedisSerializer);
		redisTemplate.setHashValueSerializer(bytRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	public void testProto(RedisTemplate<String, byte[]> protoRedisTemplate) throws InvalidProtocolBufferException {
		DeviceFix deviceFix = DeviceFix.newBuilder().setType(1).setAddress("aaa").build();
		protoRedisTemplate.opsForValue().set("deviceFix_prot0"+ deviceFix.getType(), deviceFix.toByteArray());

		byte[] bytes = protoRedisTemplate.opsForValue().get("deviceFix_prot0" + deviceFix.getType());
		DeviceFix deviceFix1 = DeviceFix.parseFrom(bytes);

	}

    @Bean
    @Primary
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            // QuartzJobTopicMessageDelegate quartzJobTopicMessageDelegate,
            // ScheduledJobTopicMessageDelegate scheduledJobTopicMessageDelegate,
            SensitiveWordsTopicMessageDelegate sensitiveWordsTopicMessageDelegate
            // RequestLogTopicMessageDelegate requestLogTopicMessageDelegate
            ) {

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
        // MessageListenerAdapter addJob = new MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"addJob");
        // addJob.afterPropertiesSet();
        // listeners.put(addJob, List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_ADD_TOPIC)));
        //
        // MessageListenerAdapter deleteJob = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"deleteJob");
        // deleteJob.afterPropertiesSet();
        // listeners.put(deleteJob,
        // List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_DELETE_TOPIC)));
        //
        // MessageListenerAdapter resumeJob = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"resumeJob");
        // resumeJob.afterPropertiesSet();
        // listeners.put(resumeJob,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_RESUME_TOPIC)));
        //
        // MessageListenerAdapter pauseJob = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"pauseJob");
        // pauseJob.afterPropertiesSet();
        // listeners.put(pauseJob,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_PAUSE_TOPIC)));
        //
        // MessageListenerAdapter runJobNow = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"runJobNow");
        // runJobNow.afterPropertiesSet();
        // listeners.put(runJobNow,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_RUN_NOW_TOPIC)));
        //
        // MessageListenerAdapter updateJobCron = new MessageListenerAdapter(
        //	quartzJobTopicMessageDelegate, "updateJobCron");
        // updateJobCron.afterPropertiesSet();
        // listeners.put(updateJobCron,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_UPDATE_CRON_TOPIC)));
        //
        // MessageListenerAdapter updateJob = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"updateJob");
        // updateJob.afterPropertiesSet();
        // listeners.put(updateJob,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_UPDATE_TOPIC)));
        //
        // MessageListenerAdapter addJobLog = new
        // MessageListenerAdapter(quartzJobTopicMessageDelegate,
        //	"addJobLog");
        // addJobLog.afterPropertiesSet();
        // listeners.put(addJobLog,
        //	List.of(ChannelTopic.of(RedisConstant.QUARTZ_JOB_LOG_ADD_TOPIC)));
        //
        // MessageListenerAdapter updateCronScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "updateCronScheduled");
        // updateCronScheduled.afterPropertiesSet();
        // listeners.put(updateCronScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_UPDATE_CRON_TOPIC)));
        //
        // MessageListenerAdapter addCronScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "addCronScheduled");
        // addCronScheduled.afterPropertiesSet();
        // listeners.put(addCronScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_ADD_CRON_TOPIC)));
        //
        // MessageListenerAdapter updateFixedDelayScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "updateFixedDelayScheduled");
        // updateFixedDelayScheduled.afterPropertiesSet();
        // listeners.put(updateFixedDelayScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_UPDATE_FIXED_DELAY_TOPIC)));
        //
        // MessageListenerAdapter addFixedDelayScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "addFixedDelayScheduled");
        // addFixedDelayScheduled.afterPropertiesSet();
        // listeners.put(addFixedDelayScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_ADD_FIXED_DELAY_TOPIC)));
        //
        // MessageListenerAdapter updateFixedRateScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "updateFixedRateScheduled");
        // updateFixedRateScheduled.afterPropertiesSet();
        // listeners.put(updateFixedRateScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_UPDATE_FIXED_RATE_TOPIC)));
        //
        // MessageListenerAdapter addFixedRateScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "addFixedRateScheduled");
        // addFixedRateScheduled.afterPropertiesSet();
        // listeners.put(addFixedRateScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_ADD_FIXED_RATE_TOPIC)));
        //
        // MessageListenerAdapter cancelScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "cancelScheduled");
        // cancelScheduled.afterPropertiesSet();
        // listeners.put(cancelScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_CANCEL_TOPIC)));
        //
        // MessageListenerAdapter runOnceScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "runOnceScheduled");
        // runOnceScheduled.afterPropertiesSet();
        // listeners.put(runOnceScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_RUN_ONCE_TOPIC)));
        //
        // MessageListenerAdapter callOffScheduled = new MessageListenerAdapter(
        //	scheduledJobTopicMessageDelegate, "callOffScheduled");
        // callOffScheduled.afterPropertiesSet();
        // listeners.put(callOffScheduled,
        //	List.of(ChannelTopic.of(RedisConstant.SCHEDULED_CALL_OFF_TOPIC)));

        MessageListenerAdapter handleSensitiveWords =
                new MessageListenerAdapter(sensitiveWordsTopicMessageDelegate, "handleSensitiveWords");
        handleSensitiveWords.afterPropertiesSet();
        listeners.put(handleSensitiveWords, List.of(ChannelTopic.of(RedisConstant.SENSITIVE_WORDS_TOPIC)));

        // MessageListenerAdapter handleRequestLog = new MessageListenerAdapter(
        //	requestLogTopicMessageDelegate, "handleRequestLog");
        // handleRequestLog.afterPropertiesSet();
        // listeners.put(handleRequestLog,
        // List.of(ChannelTopic.of(RedisConstant.REQUEST_LOG_TOPIC)));

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
