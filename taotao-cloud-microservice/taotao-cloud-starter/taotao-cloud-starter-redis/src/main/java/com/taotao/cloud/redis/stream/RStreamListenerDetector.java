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
package com.taotao.cloud.redis.stream;

import cn.hutool.core.util.ReflectUtil;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * Redisson 监听器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:35:02
 */
public class RStreamListenerDetector implements BeanPostProcessor, InitializingBean {
	private final StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer;
	private final RedisTemplate<String, Object> redisTemplate;
	private final String consumerGroup;
	private final String consumerName;

	public RStreamListenerDetector(StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer,
								   RedisTemplate<String, Object> redisTemplate, String consumerGroup, String consumerName) {
		this.streamMessageListenerContainer = streamMessageListenerContainer;
		this.redisTemplate = redisTemplate;
		this.consumerGroup = consumerGroup;
		this.consumerName = consumerName;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> userClass = ClassUtils.getUserClass(bean);
		ReflectionUtils.doWithMethods(userClass, method -> {
			RStreamListener listener = AnnotationUtils.findAnnotation(method, RStreamListener.class);
			if (listener != null) {
				String streamKey = listener.name();
				Assert.hasText(streamKey, "@RStreamListener name must not be empty.");
				LogUtils.info("Found @RStreamListener on bean:{} method:{}", beanName, method);

				// 校验 method，method 入参数大于等于1
				int paramCount = method.getParameterCount();
				if (paramCount > 1) {
					throw new IllegalArgumentException("@RStreamListener on method " + method + " parameter count must less or equal to 1.");
				}

				// streamOffset
				ReadOffset readOffset = listener.offsetModel().getReadOffset();
				StreamOffset<String> streamOffset = StreamOffset.create(streamKey, readOffset);

				// 消费模式
				MessageModel messageModel = listener.messageModel();
				if (MessageModel.BROADCASTING == messageModel) {
					broadCast(streamOffset, bean, method, listener.readRawBytes());
				} else {
					String groupId = StringUtils.isNotBlank(listener.group()) ? listener.group() : consumerGroup;
					Consumer consumer = Consumer.from(groupId, consumerName);
					// 如果需要，创建 group
					createGroupIfNeed(redisTemplate, streamKey, readOffset, groupId);
					cluster(consumer, streamOffset, listener, bean, method);
				}
			}
		}, ReflectionUtils.USER_DECLARED_METHODS);
		return bean;
	}

	private void broadCast(StreamOffset<String> streamOffset, Object bean, Method method, boolean isReadRawBytes) {
		streamMessageListenerContainer.receive(streamOffset, (message) -> {
			// MapBackedRecord
			invokeMethod(bean, method, message, isReadRawBytes);
		});
	}

	private void cluster(Consumer consumer, StreamOffset<String> streamOffset, RStreamListener listener, Object bean, Method method) {
		boolean autoAcknowledge = listener.autoAcknowledge();
		StreamMessageListenerContainer.ConsumerStreamReadRequest<String> readRequest = StreamMessageListenerContainer.StreamReadRequest.builder(streamOffset).consumer(consumer).autoAcknowledge(autoAcknowledge).build();
		StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
		streamMessageListenerContainer.register(readRequest, (message) -> {
			// MapBackedRecord
			invokeMethod(bean, method, message, listener.readRawBytes());

			// ack
			if (!autoAcknowledge) {
				opsForStream.acknowledge(consumer.getGroup(), message);
			}
		});
	}

	private static void createGroupIfNeed(RedisTemplate<String, Object> redisTemplate, String streamKey, ReadOffset readOffset, String group) {
		StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
		try {
			StreamInfo.XInfoGroups groups = opsForStream.groups(streamKey);
			if (groups.stream().noneMatch((x) -> group.equals(x.groupName()))) {
				opsForStream.createGroup(streamKey, readOffset, group);
			}
		} catch (RedisSystemException e) {
			// RedisCommandExecutionException: ERR no such key
			opsForStream.createGroup(streamKey, group);
		}
	}

	private void invokeMethod(Object bean, Method method, MapRecord<String, String, byte[]> mapRecord, boolean isReadRawBytes) {
		// 支持没有参数的方法
		if (method.getParameterCount() == 0) {
			ReflectUtil.invoke( bean,method);
			return;
		}
		if (isReadRawBytes) {
			ReflectUtil.invoke(bean,method, mapRecord);
		} else {
			ReflectUtil.invoke(bean,method, getRecordValue(mapRecord));
		}
	}

	private Object getRecordValue(MapRecord<String, String, byte[]> mapRecord) {
		Map<String, byte[]> messageValue = mapRecord.getValue();
		if (messageValue.containsKey(RStreamTemplate.OBJECT_PAYLOAD_KEY)) {
			byte[] payloads = messageValue.get(RStreamTemplate.OBJECT_PAYLOAD_KEY);
			Object deserialize = redisTemplate.getValueSerializer().deserialize(payloads);
			return ObjectRecord.create(mapRecord.getStream(), deserialize);
		} else {
			return mapRecord.mapEntries(entry -> {
				String key = entry.getKey();
				Object value = redisTemplate.getValueSerializer().deserialize(entry.getValue());
				return Collections.singletonMap(key, value).entrySet().iterator().next();
			});
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		streamMessageListenerContainer.start();
	}

}
