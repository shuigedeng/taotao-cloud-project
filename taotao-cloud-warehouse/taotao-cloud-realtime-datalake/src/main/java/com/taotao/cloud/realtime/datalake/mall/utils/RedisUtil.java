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

package com.taotao.cloud.realtime.datalake.mall.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * Date: 2021/2/5
 * Desc: 通过JedisPool连接池获取Jedis连接
 */
public class RedisUtil {
    private static JedisPool jedisPool;

    public static Jedis getJedis() {
        if (jedisPool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100); // 最大可用连接数
            jedisPoolConfig.setBlockWhenExhausted(true); // 连接耗尽是否等待
            jedisPoolConfig.setMaxWaitMillis(2000); // 等待时间
            jedisPoolConfig.setMaxIdle(5); // 最大闲置连接数
            jedisPoolConfig.setMinIdle(5); // 最小闲置连接数
            jedisPoolConfig.setTestOnBorrow(true); // 取连接的时候进行一下测试 ping pong

            jedisPool = new JedisPool(jedisPoolConfig, "hadoop202", 6379, 10000);
        }
        return jedisPool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        System.out.println(jedis.ping());
    }
}
