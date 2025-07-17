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

import static com.taotao.boot.common.utils.io.FileUtils.createFile;
import static com.taotao.boot.common.utils.io.FileUtils.truncate;
import static com.taotao.boot.common.utils.io.FileUtils.write;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.model.PersistRdbEntry;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-db-基于 JSON
 * @author shuigedeng
 * @since 2024.06
 */
public class CachePersistDbJson<K, V> extends CachePersistAdaptor<K, V> {

    /**
     * 数据库路径
     * @since 2024.06
     */
    private final String dbPath;

    public CachePersistDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K, V>> entrySet = cache.entrySet();

        // 创建文件
        createFile(dbPath);
        // 清空文件
        truncate(dbPath);

        for (Map.Entry<K, V> entry : entrySet) {
            K key = entry.getKey();
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K, V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(entry.getValue());
            persistRdbEntry.setExpire(expireTime);

            String line = JSON.toJSONString(persistRdbEntry);
            write(dbPath, line, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
