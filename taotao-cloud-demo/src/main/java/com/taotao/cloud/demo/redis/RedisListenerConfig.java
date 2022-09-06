// /*
//  * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *      https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.demo.redis;
//
// import com.taotao.cloud.common.utils.log.LogUtils;
// import java.util.Collection;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.jetbrains.annotations.NotNull;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.Message;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.listener.ChannelTopic;
// import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
// import org.springframework.data.redis.listener.RedisMessageListenerContainer;
// import org.springframework.data.redis.listener.Topic;
// import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
// /**
//  * RedisListenerConfig
//  *
//  * @author shuigedeng
//  * @version 2022.03
//  * @since 2022/01/17 16:12
//  */
// @Configuration
// public class RedisListenerConfig {
//
// 	@Bean
// 	public RedisMessageListenerContainer customRedisMessageListenerContainer(
// 		RedisConnectionFactory redisConnectionFactory,
// 		SensitiveWordsTopicMessageDelegate sensitiveWordsTopicMessageDelegate) {
//
// 		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
// 		container.setConnectionFactory(redisConnectionFactory);
//
// 		Map<MessageListenerAdapter, Collection<? extends Topic>> listeners = new HashMap<>();
// 		MessageListenerAdapter handleSensitiveWords = new MessageListenerAdapter(
// 			sensitiveWordsTopicMessageDelegate, "handleSensitiveWords");
// 		handleSensitiveWords.afterPropertiesSet();
// 		listeners.put(handleSensitiveWords,
// 			List.of(new ChannelTopic("sdflasldf")));
//
// 		container.setMessageListeners(listeners);
// 		return container;
// 	}
//
// 	@Configuration
// 	public static class RedisKeyExpireListener extends KeyExpirationEventMessageListener {
//
// 		public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
// 			super(listenerContainer);
// 		}
//
// 		@Override
// 		public void onMessage(@NotNull Message message, byte[] pattern) {
// 			LogUtils.info("接受到消息: {}, {}", message, new String(pattern));
// 		}
// 	}
//
// }
