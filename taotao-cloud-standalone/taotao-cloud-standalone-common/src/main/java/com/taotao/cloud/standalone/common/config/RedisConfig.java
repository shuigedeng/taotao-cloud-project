package com.taotao.cloud.standalone.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Classname RedisConfig
 * @Description redis配置
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-06-19 10:40
 * @Version 1.0
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //使用fastjson序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // key的序列化采用StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value值的序列化采用fastJsonRedisSerializer
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    //缓存管理器 Cache
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory);
        return builder.build();
    }

}

