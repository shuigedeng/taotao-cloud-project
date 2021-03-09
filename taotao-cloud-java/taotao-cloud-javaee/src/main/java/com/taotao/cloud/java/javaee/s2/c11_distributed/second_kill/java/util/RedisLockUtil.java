package com.taotao.cloud.java.javaee.s2.c11_distributed.second_kill.java.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public boolean lock(String key,String value,int second){
        return redisTemplate.opsForValue().setIfAbsent(key,value,second, TimeUnit.SECONDS);
    }


    public void unlock(String key){
        redisTemplate.delete(key);
    }


}
