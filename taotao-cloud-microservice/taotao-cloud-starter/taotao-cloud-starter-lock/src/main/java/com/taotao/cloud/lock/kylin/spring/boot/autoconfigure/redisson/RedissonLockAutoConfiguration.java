package com.taotao.cloud.lock.kylin.spring.boot.autoconfigure.redisson;

import com.taotao.cloud.lock.kylin.executor.redisson.RedissonLockExecutor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Redisson锁自动配置器
 *
 * @author wangjinkui
 */
@Configuration
@ConditionalOnClass(Redisson.class)
@ConditionalOnProperty(name = "kylin.lock.redisson", havingValue = "true")
public class RedissonLockAutoConfiguration {

	@Bean
	@Order(100)//从小到大 排序 依次执行
	public RedissonLockExecutor redissonLockExecutor(RedissonClient redissonClient) {
		return new RedissonLockExecutor(redissonClient);
	}
}
