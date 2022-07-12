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

package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.CharPool;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.net.INetUtil;
import com.taotao.cloud.redis.properties.CacheProperties;
import com.taotao.cloud.redis.stream.DefaultRStreamTemplate;
import com.taotao.cloud.redis.stream.RStreamListenerDetector;
import com.taotao.cloud.redis.stream.RStreamTemplate;
import java.time.Duration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.util.ErrorHandler;

/**
 * redis Stream 配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:33:26
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = CacheProperties.Stream.PREFIX, name = "enable", havingValue = "true")
public class RedisStreamAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RedisStreamAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public StreamMessageListenerContainerOptions<String, MapRecord<String, String, byte[]>> streamMessageListenerContainerOptions(
		CacheProperties properties,
		ObjectProvider<ErrorHandler> errorHandlerObjectProvider) {
		StreamMessageListenerContainer.StreamMessageListenerContainerOptionsBuilder<String, MapRecord<String, String, byte[]>> builder = StreamMessageListenerContainerOptions
			.builder()
			.keySerializer(RedisSerializer.string())
			.hashKeySerializer(RedisSerializer.string())
			.hashValueSerializer(RedisSerializer.byteArray());
		CacheProperties.Stream streamProperties = properties.getStream();
		// 批量大小
		Integer pollBatchSize = streamProperties.getPollBatchSize();
		if (pollBatchSize != null && pollBatchSize > 0) {
			builder.batchSize(pollBatchSize);
		}
		// poll 超时时间
		Duration pollTimeout = streamProperties.getPollTimeout();
		if (pollTimeout != null && !pollTimeout.isNegative()) {
			builder.pollTimeout(pollTimeout);
		}
		// errorHandler
		errorHandlerObjectProvider.ifAvailable((builder::errorHandler));

		// TODO  executor
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		StreamMessageListenerContainerOptions<String, MapRecord<String, String, byte[]>> streamMessageListenerContainerOptions) {
		// 根据配置对象创建监听容器
		return StreamMessageListenerContainer.create(redisConnectionFactory,
			streamMessageListenerContainerOptions);
	}

	@Bean
	@ConditionalOnMissingBean
	public RStreamListenerDetector streamListenerDetector(
		StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer,
		RedisTemplate<String, Object> redisTemplate,
		ObjectProvider<ServerProperties> serverPropertiesObjectProvider,
		CacheProperties properties,
		Environment environment) {
		CacheProperties.Stream streamProperties = properties.getStream();

		// 消费组名称
		String consumerGroup = streamProperties.getConsumerGroup();
		if (StringUtil.isBlank(consumerGroup)) {
			String appName = environment.getRequiredProperty("spring.application.name");
			String profile = environment.getProperty("spring.profiles.active");
			consumerGroup =
				StringUtil.isBlank(profile) ? appName : appName + CharPool.COLON + profile;
		}

		// 消费者名称
		String consumerName = streamProperties.getConsumerName();
		if (StringUtil.isBlank(consumerName)) {
			final StringBuilder consumerNameBuilder = new StringBuilder(INetUtil.getHostIp());
			serverPropertiesObjectProvider.ifAvailable(serverProperties -> {
				consumerNameBuilder.append(CharPool.COLON).append(serverProperties.getPort());
			});
			consumerName = consumerNameBuilder.toString();
		}
		return new RStreamListenerDetector(streamMessageListenerContainer, redisTemplate,
			consumerGroup, consumerName);
	}

	@Bean
	public RStreamTemplate streamTemplate(RedisTemplate<String, Object> redisTemplate) {
		return new DefaultRStreamTemplate(redisTemplate);
	}

}
