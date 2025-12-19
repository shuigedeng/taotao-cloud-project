/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestRedisController
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@RestController
@Tag(name = "管理端-TestRediscontroller", description = "管理端-测试redis")
public class TestRedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 什么是缓存穿透 缓存穿透指的是一个缓存系统无法缓存某个查询的数据，从而导致这个查询每一次都要访问数据库。
     *
     * <p>常见的Redis缓存穿透场景包括：
     *
     * <p>查询一个不存在的数据：攻击者可能会发送一些无效的查询来触发缓存穿透。
     * 查询一些非常热门的数据：如果一个数据被访问的非常频繁，那么可能会导致缓存系统无法处理这些请求，从而造成缓存穿透。
     * 查询一些异常数据：这种情况通常发生在数据服务出现故障或异常时，从而造成缓存系统无法访问相关数据，从而导致缓存穿透。
     */
    @GetMapping("/user/{id}")
    public User getUserById( @PathVariable Long id ) {
        // 先从布隆过滤器中判断此id是否存在
        // if (!BloomFilterUtil.mightContain(id.toString())) {
        //	return null;
        // }
        // 查询缓存数据
        String userKey = "user_" + id.toString();
        User user = (User) redisTemplate.opsForValue().get(userKey);
        if (user == null) {
            // 查询数据库
            // user = userRepository.findById(id).orElse(null);
            if (user != null) {
                // 将查询到的数据加入缓存
                redisTemplate.opsForValue().set(userKey, user, 300, TimeUnit.SECONDS);
            } else {
                // 查询结果为空，将请求记录下来，并在布隆过滤器中添加
                // BloomFilterUtil.add(id.toString());
            }
        }
        return user;
    }

    /**
     * 缓存击穿 什么是缓存击穿 缓存击穿指的是在一些高并发访问下，一个热点数据从缓存中不存在，每次请求都要直接查询数据库，从而导致数据库压力过大，并且系统性能下降的现象。
     *
     * <p>缓存击穿的原因通常有以下几种：
     *
     * <p>缓存中不存在所需的热点数据：当系统中某个热点数据需要被频繁访问时，如果这个热点数据最开始没有被缓存，那么就会导致系统每次请求都需要直接查询数据库，造成数据库负担。
     * 缓存的热点数据过期：当一个热点数据过期并需要重新缓存时，如果此时有大量请求，那么就会导致所有请求都要直接查询数据库。
     *
     * <p>在遇到缓存击穿问题时，我们可以在查询数据库之前，先判断一下缓存中是否已有数据，如果没有数据则使用Redis的单线程特性，先查询数据库然后将数据写入缓存中。
     */
    @GetMapping("/getUserById1/{id}")
    public User getUserById1( @PathVariable Long id ) {
        // 先从缓存中获取值
        String userKey = "user_" + id.toString();
        User user = (User) redisTemplate.opsForValue().get(userKey);
        if (user == null) {
            // 查询数据库之前加锁
            String lockKey = "lock_user_" + id.toString();
            String lockValue = UUID.randomUUID().toString();
            try {
                Boolean lockResult = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 60, TimeUnit.SECONDS);
                if (lockResult != null && lockResult) {
                    // 查询数据库
                    // user = userRepository.findById(id).orElse(null);
                    if (user != null) {
                        // 将查询到的数据加入缓存
                        redisTemplate.opsForValue().set(userKey, user, 300, TimeUnit.SECONDS);
                    }
                }
            } finally {
                // 释放锁
                if (lockValue.equals(redisTemplate.opsForValue().get(lockKey))) {
                    redisTemplate.delete(lockKey);
                }
            }
        }
        return user;
    }

    /**
     * 什么是缓存雪崩 指缓存中大量数据的失效时间集中在某一个时间段，导致在这个时间段内缓存失效并额外请求数据库查询数据的请求大量增加，从而对数据库造成极大的压力和负荷。
     *
     * <p>常见的Redis缓存雪崩场景包括：
     *
     * <p>缓存服务器宕机：当缓存服务器宕机或重启时，大量的访问请求将直接命中数据库，并在同一时间段内导致大量的数据库查询请求，从而将数据库压力大幅提高。
     * 缓存数据同时失效：在某个特定时间点，缓存中大量数据的失效时间集中在一起，这些数据会在同一时间段失效，并且这些数据被高频访问，将导致大量的访问请求去查询数据库。
     * 缓存中数据过期时间设计不合理：当缓存中的数据有效时间过短，且数据集中在同一时期失效时，就容易导致大量的请求直接查询数据库，加剧数据库压力。
     * 波动式的访问过程：当数据的访问存在波动式特征时，例如输出某些活动物品或促销商品时，将会带来高频的查询请求访问，导致缓存大量失效并产生缓存雪崩
     *
     * <p>在遇到缓存雪崩时，我们可以使用两种方法：一种是将缓存过期时间分散开，即为不同的数据设置不同的过期时间；另一种是使用Redis的多级缓存架构，通过增加一层代理层来解决
     */
    //@Autowired
    // private CacheManager ehCacheManager;
    @GetMapping("/getUserById2/{id}")
    @Cacheable(value = "userCache", key = "#id")
    public User getUserById2( @PathVariable Long id ) {
        // 先从Ehcache缓存中获取
        String userKey = "user_" + id.toString();
        // User user = (User) ehCacheManager.getCache("userCache").get(userKey).get();
        // if (user == null) {
        //	// 再从Redis缓存中获取
        //	user = (User) redisTemplate.opsForValue().get(userKey);
        //	if (user != null) {
        //		ehCacheManager.getCache("userCache").put(userKey, user);
        //	}
        // }
        // return user;
        return null;
    }

    public static class BloomFilterUtil {

        //// 布隆过滤器的预计容量
        // private static final int expectedInsertions = 1000000;
        //// 布隆过滤器误判率
        // private static final double fpp = 0.001;
        // private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(
        //	Charset.defaultCharset()), expectedInsertions, fpp);
        //
        /// **
        // * 向Bloom Filter中添加元素
        // */
        // public static void add(String key) {
        //	bloomFilter.put(key);
        // }
        //
        /// **
        // * 判断元素是否存在于Bloom Filter中
        // */
        // public static boolean mightContain(String key) {
        //	return bloomFilter.mightContain(key);
        // }
    }

    public static class User {

    }
}
