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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.enums.SerializerType;
import com.taotao.cloud.redis.properties.CacheProperties;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * TaoTaoCloudRedisAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:02
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = com.taotao.cloud.redis.properties.RedisProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({RedisProperties.class, CacheProperties.class, com.taotao.cloud.redis.properties.RedisProperties.class})
public class RedisAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RedisAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	//@Bean
	//public RedisSerializer<String> redisKeySerializer() {
	//	return RedisSerializer.string();
	//}

	@Bean
	@ConditionalOnMissingBean(RedisSerializer.class)
	public RedisSerializer<Object> redisSerializer(CacheProperties properties,
												   ObjectProvider<ObjectMapper> objectProvider) {
		SerializerType serializerType = properties.getSerializerType();

		if (SerializerType.JDK == serializerType) {
			ClassLoader classLoader = this.getClass().getClassLoader();
			return new JdkSerializationRedisSerializer(classLoader);
		}

		//// jackson findAndRegisterModules，use copy
		//ObjectMapper objectMapper = objectProvider.getIfAvailable(ObjectMapper::new).copy();
		//objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		//// findAndRegisterModules
		//objectMapper.findAndRegisterModules();
		//// class type info to json
		//GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
		//objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
		//	DefaultTyping.NON_FINAL, As.PROPERTY);

		return new GenericJackson2JsonRedisSerializer(JsonUtils.MAPPER);
	}

	@Bean
	public RedisConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
		return new RedissonConnectionFactory(redissonClient);
	}

	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
													   RedisSerializer<Object> redisSerializer) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		//Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
		//	Object.class);
		//jackson2JsonRedisSerializer.setObjectMapper(JsonUtil.MAPPER);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);

		// value序列化方式采用jackson
		template.setValueSerializer(redisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(redisSerializer);
		template.afterPropertiesSet();

		return template;
	}

	@Bean
	public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate) {
		return new RedisRepository(redisTemplate, false);
	}

	@Bean
	@ConditionalOnMissingBean(ValueOperations.class)
	public ValueOperations<String, Object> valueOperations(
		RedisTemplate<String, Object> micaRedisTemplate) {
		return micaRedisTemplate.opsForValue();
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(factory);
		return template;
	}

}
