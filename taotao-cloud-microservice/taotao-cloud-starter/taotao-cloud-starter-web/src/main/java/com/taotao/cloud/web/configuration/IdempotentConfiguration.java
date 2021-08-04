package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.idempotent.IdempotentAspect;
import com.taotao.cloud.web.properties.EncryptProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 自动注入配置
 */
public class IdempotentConfiguration {

	@Bean
	@ConditionalOnClass({RedisRepository.class, DistributedLock.class})
	public IdempotentAspect idempotentAspect(DistributedLock distributedLock) {
		return new IdempotentAspect(distributedLock);
	}
}
