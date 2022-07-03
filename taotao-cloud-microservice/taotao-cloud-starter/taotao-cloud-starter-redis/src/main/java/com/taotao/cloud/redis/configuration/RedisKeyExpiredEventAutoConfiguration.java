package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;

/**
 * 复述,关键过期事件配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:24:57
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "taotao.cloud.redis.key-expired-event.enable", value = "true", matchIfMissing = true)
public class RedisKeyExpiredEventAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RedisKeyExpiredEventAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Bean
	@ConditionalOnMissingBean
	public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
		RedisMessageListenerContainer listenerContainer) {
		return new KeyExpirationEventMessageListener(listenerContainer);
	}

	@Async
	@EventListener
	public void onRedisKeyExpiredEvent(RedisKeyExpiredEvent<Object> event) {
		LogUtil.info(event.toString());
	}
}
