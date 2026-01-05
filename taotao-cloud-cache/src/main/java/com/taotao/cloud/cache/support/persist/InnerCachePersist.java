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

package com.taotao.cloud.cache.support.persist;

import com.taotao.cloud.cache.api.Cache;
import com.taotao.cloud.cache.api.CachePersist;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 内部缓存持久化类
 * @author shuigedeng
 * @param <K> key
 * @param <V> value
 * @since 2024.06
 */
public class InnerCachePersist<K, V> {

    private static final Logger log = LoggerFactory.getLogger(InnerCachePersist.class);

    /**
     * 缓存信息
     * @since 2024.06
     */
    private final Cache<K, V> cache;

    /**
     * 缓存持久化策略
     * @since 2024.06
     */
    private final CachePersist<K, V> persist;

    /**
     * 线程执行类
     * @since 2024.06
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE =
            Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist( Cache<K, V> cache, CachePersist<K, V> persist) {
        this.cache = cache;
        this.persist = persist;

        // 初始化
        this.init();
    }

    /**
     * 初始化
     * @since 2024.06
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("开始持久化缓存信息");
                            persist.persist(cache);
                            log.info("完成持久化缓存信息");
                        } catch (Exception exception) {
                            log.error("文件持久化异常", exception);
                        }
                    }
                },
                persist.delay(),
                persist.period(),
                persist.timeUnit());
    }
}
