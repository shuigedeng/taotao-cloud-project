package com.taotao.cloud.lock.config.strategy;

import com.taotao.cloud.lock.props.RedissonProperties;
import org.redisson.config.Config;

/**
 * Redisson配置构建接口
 *
 * @author pangu
 * @date 2020-10-22
 */
public interface RedissonConfigStrategy {

	/**
	 * 根据不同的Redis配置策略创建对应的Config
	 *
	 * @param redissonProperties redisson配置
	 * @return Config
	 */
	Config createRedissonConfig(RedissonProperties redissonProperties);
}
