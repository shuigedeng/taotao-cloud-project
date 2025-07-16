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

package com.taotao.cloud.ccsr.core.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuigedeng
 */
public abstract class AbstractStorage<T> implements Storage<T> {

    protected final int maxSize = 1024 * 64;

    // FIXME 这里应该需要按照namespace做租户隔离
    private final Map<String, T> storage = new HashMap<>();

    public abstract String key(T data);

    public abstract boolean check(T data) throws IllegalArgumentException;

    @Override
    public String put(T data) {

        String key = key(data);
        if (key == null) {
            throw new IllegalArgumentException("data key is null");
        }

        if (check(data)) {
            storage.put(key, data);
            return key;
        }

        return null;
    }

    @Override
    public T get(String key) {
        return storage.get(key);
    }

    @Override
    public T delete(String key) {
        return storage.remove(key);
    }

    @Override
    public List<T> list() {
        return storage.values().stream().toList();
    }

    public Map<String, T> getStorage() {
        return storage;
    }

    public boolean limit(String content) {
        if (content == null) {
            return false;
        }
        return content.getBytes().length > maxSize;
    }
}
