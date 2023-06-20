/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * 对第三方授权登录用户信息与 token 的持久化（jdbc）数据是否缓存到 redis 的配置
 * @author YongWu zheng
 * @version V1.0  Created by 2020/6/15 19:29
 */
@Getter
@ConfigurationProperties("ums.cache.redis")
public class RedisCacheProperties {

    private final Cache cache = new Cache();

    /**
     * Redis cache is open, 默认 false
     */
    @Setter
    private Boolean open = false;

    /**
     * 是否使用 spring IOC 容器中的 RedisConnectionFactory， 默认： false. <br>
     * 如果使用 spring IOC 容器中的 RedisConnectionFactory，则要注意 cache.database-index 要与 spring.redis.database 一样。
     */
    @Setter
    private Boolean useIocRedisConnectionFactory = false;



    @Getter
    @Setter
    public static class Cache {

        /**
         * redis cache 存放的 database index, 默认: 0
         */
        private Integer databaseIndex = 0;
        /**
         * 设置缓存管理器管理的缓存的默认过期时间, 默认: 200s
         */
        private Duration defaultExpireTime = Duration.ofSeconds(200);
        /**
         * cache ttl 。使用 0 声明一个永久的缓存。 默认: 180, 单位: 秒<br>
         * 取缓存时间的 20% 作为动态的随机变量上下浮动, 防止同时缓存失效而缓存击穿
         */
        private Duration entryTtl = Duration.ofSeconds(180);
        /**
         * Names of the default caches to consider for caching operations defined
         * in the annotated class.
         */
        private Set<String> cacheNames = new HashSet<>();

    }

}
