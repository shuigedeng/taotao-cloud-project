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

package com.taotao.cloud.cache.bs;

import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.listener.MyRemoveListener;
import com.taotao.cloud.cache.listener.MySlowListener;
import com.taotao.cloud.cache.load.MyCacheLoad;
import com.taotao.cloud.cache.support.evict.CacheEvicts;
import com.taotao.cloud.cache.support.load.CacheLoads;
import com.taotao.cloud.cache.support.map.Maps;
import com.taotao.cloud.cache.support.persist.CachePersists;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 缓存引导类测试
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheBsTest {

    /**
     * 大小指定测试
     * @since 2024.06
     */
    @Test
    public void helloTest() {
        ICache<String, String> cache = CacheBs.<String, String>newInstance().size(2).build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assertions.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 配置指定测试
     * @since 2024.06
     */
    @Test
    public void configTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .map(Maps.<String, String>hashMap())
                        .evict(CacheEvicts.<String, String>fifo())
                        .size(2)
                        .build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assertions.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 过期测试
     * @since 2024.06
     */
    @Test
    public void expireTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String, String>newInstance().size(3).build();

        cache.put("1", "1");
        cache.put("2", "2");

        cache.expire("1", 40);
        Assertions.assertEquals(2, cache.size());

        TimeUnit.MILLISECONDS.sleep(50);
        Assertions.assertEquals(1, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 缓存删除监听器
     * @since 2024.06
     */
    @Test
    public void cacheRemoveListenerTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(1)
                        .addRemoveListener(new MyRemoveListener<String, String>())
                        .build();

        cache.put("1", "1");
        cache.put("2", "2");
    }

    /**
     * 加载接口测试
     * @since 2024.06
     */
    @Test
    public void loadTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance().load(new MyCacheLoad()).build();

        Assertions.assertEquals(2, cache.size());
    }

    /**
     * 持久化接口测试
     * @since 2024.06
     */
    @Test
    public void persistRdbTest() throws InterruptedException {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .load(new MyCacheLoad())
                        .persist(CachePersists.<String, String>dbJson("1.rdb"))
                        .build();

        Assertions.assertEquals(2, cache.size());
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 加载接口测试
     * @since 2024.06
     */
    @Test
    public void loadDbJsonTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .load(CacheLoads.<String, String>dbJson("1.rdb"))
                        .build();

        Assertions.assertEquals(2, cache.size());
    }

    /**
     * 慢日志接口测试
     * @since 2024.06
     */
    @Test
    public void slowLogTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance().addSlowListener(new MySlowListener()).build();

        cache.put("1", "2");
        cache.get("1");
    }

    /**
     * 持久化 AOF 接口测试
     * @since 2024.06
     */
    @Test
    public void persistAofTest() throws InterruptedException {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .persist(CachePersists.<String, String>aof("1.aof"))
                        .build();

        cache.put("1", "1");
        cache.expire("1", 10);
        cache.remove("2");

        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 加载 AOF 接口测试
     * @since 2024.06
     */
    @Test
    public void loadAofTest() throws InterruptedException {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .load(CacheLoads.<String, String>aof("default.aof"))
                        .build();

        Assertions.assertEquals(1, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * LRU 驱除策略测试
     * @since 2024.06
     */
    @Test
    public void lruEvictTest() throws InterruptedException {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lru())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    @Test
    public void lruDoubleListMapTest() throws InterruptedException {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lruDoubleListMap())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LinkedHashMap 实现
     * @since 2024.06
     */
    @Test
    public void lruLinkedHashMapTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lruLinkedHashMap())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LRU 2Q 实现
     * @since 2024.06
     */
    @Test
    public void lruQ2Test() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lru2Q())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LRU-2 实现
     * @since 2024.06
     */
    @Test
    public void lru2Test() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lru2())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LFU 实现
     * @since 2024.06
     */
    @Test
    public void lfuTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>lfu())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 clock 算法 实现
     * @since 2024.06
     */
    @Test
    public void clockTest() {
        ICache<String, String> cache =
                CacheBs.<String, String>newInstance()
                        .size(3)
                        .evict(CacheEvicts.<String, String>clock())
                        .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assertions.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }
}
