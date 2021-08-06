package com.taotao.cloud.web.configuration;

import com.taotao.cloud.common.lock.DistributedLock;
import com.taotao.cloud.web.idempotent.IdempotentAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * 自动注入配置
 */
public class IdempotentConfiguration {

	@Bean
	@ConditionalOnBean({DistributedLock.class})
	public IdempotentAspect idempotentAspect(DistributedLock distributedLock) {
		return new IdempotentAspect(distributedLock);
	}
}
