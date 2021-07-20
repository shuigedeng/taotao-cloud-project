package com.taotao.cloud.lock.strategy;

import com.taotao.cloud.lock.props.RedissonProperties;
import lombok.AllArgsConstructor;
import org.redisson.config.Config;

/**
 * Redisson配置上下文，产出真正的Redisson的Config
 *
 * @author shuigedeng
 * @date 2020-10-22
 */
@AllArgsConstructor
public class RedissonConfigContext {

	private final RedissonConfigStrategy redissonConfigStrategy;

	/**
	 * 上下文根据构造中传入的具体策略产出真实的Redisson的Config
	 *
	 * @param redissonProperties redisson配置
	 * @return Config
	 */
	public Config createRedissonConfig(RedissonProperties redissonProperties) {
		return this.redissonConfigStrategy.createRedissonConfig(redissonProperties);
	}
}
