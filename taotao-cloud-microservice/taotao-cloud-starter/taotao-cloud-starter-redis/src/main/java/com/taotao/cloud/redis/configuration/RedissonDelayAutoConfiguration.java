package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.delay.config.RedissonAnnotationBeanPostProcessor;
import com.taotao.cloud.redis.delay.config.RedissonConfigUtils;
import com.taotao.cloud.redis.delay.config.RedissonListenerRegistry;
import com.taotao.cloud.redis.delay.config.RedissonQueueBeanPostProcessor;
import com.taotao.cloud.redis.delay.config.RedissonQueueRegistry;
import com.taotao.cloud.redis.delay.config.RedissonTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;

/**
 * RedissonAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
@AutoConfiguration
@ConditionalOnBean({RedissonClient.class})
public class RedissonDelayAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(RedissonDelayAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Scope(BeanDefinition.SCOPE_SINGLETON)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean(name = RedissonConfigUtils.REDISSON_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	public RedissonAnnotationBeanPostProcessor redissonAnnotationBeanPostProcessor() {
		return new RedissonAnnotationBeanPostProcessor();
	}

	@Scope(BeanDefinition.SCOPE_SINGLETON)
	@Bean(name = RedissonConfigUtils.REDISSON_LISTENER_REGISTRY_BEAN_NAME)
	public RedissonListenerRegistry redissonListenerRegistry() {
		return new RedissonListenerRegistry();
	}

	@Scope(BeanDefinition.SCOPE_SINGLETON)
	@Bean(name = RedissonConfigUtils.REDISSON_QUEUE_BEAN_PROCESSOR_BEAN_NAME)
	public RedissonQueueBeanPostProcessor redissonQueueBeanPostProcessor() {
		return new RedissonQueueBeanPostProcessor();
	}

	@Scope(BeanDefinition.SCOPE_SINGLETON)
	@Bean(name = RedissonConfigUtils.REDISSON_QUEUE_REGISTRY_BEAN_NAME)
	public RedissonQueueRegistry redissonQueueRegistry() {
		return new RedissonQueueRegistry();
	}

	@Scope(BeanDefinition.SCOPE_SINGLETON)
	@Bean
	@ConditionalOnMissingBean
	public RedissonTemplate redissonTemplate() {
		return new RedissonTemplate();
	}
}
