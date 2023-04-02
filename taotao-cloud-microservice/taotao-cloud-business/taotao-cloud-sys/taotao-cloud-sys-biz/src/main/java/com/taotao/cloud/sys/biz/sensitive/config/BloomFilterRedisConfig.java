package com.taotao.cloud.sys.biz.sensitive.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.daffodil.common.config.RedisTemplateConfig;
import com.daffodil.util.hash.BloomFilterRedis;
import com.daffodil.util.hash.BloomFilterRedis.Strategy;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * 
 * @author yweijian
 * @date 2022年9月14日
 * @version 2.0.0
 * @description
 */
@Configuration
@AutoConfigureAfter(RedisTemplateConfig.class)
public class BloomFilterRedisConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Bean
    public BloomFilterRedis<String> bloomFilter(){

        return BloomFilterRedis.create(new Funnel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void funnel(String from, PrimitiveSink into) {
                into.putString(from, StandardCharsets.UTF_8);
            }
        }, new Strategy() {
            private static final long serialVersionUID = 1L;

            @Override
            public <T> boolean put(String cacheKey, T object, long[] offsets) {
                boolean bitsChanged = false;
                for (long offset : offsets) {
                    if(!redisTemplate.opsForValue().getBit(cacheKey, offset)) {
                        bitsChanged = redisTemplate.opsForValue().setBit(cacheKey, offset, true);
                    }
                }
                return bitsChanged;
            }

            @Override
            public <T> boolean mightContain(String cacheKey, T object, long[] offsets) {
                for (long offset : offsets) {
                    if (!redisTemplate.opsForValue().getBit(cacheKey, offset)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean clean(String cacheKey) {
                return redisTemplate.delete(cacheKey);
            }
        }, 100000L, 0.03);
    }
}
