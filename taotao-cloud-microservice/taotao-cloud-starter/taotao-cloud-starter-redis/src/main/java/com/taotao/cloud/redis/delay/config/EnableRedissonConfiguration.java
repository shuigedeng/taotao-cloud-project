package com.taotao.cloud.redis.delay.config;

import com.taotao.cloud.redis.delay.annotation.EnableRedisson;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;


@Configuration
@ConditionalOnClass({EnableRedisson.class})
public class EnableRedissonConfiguration {

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

}
