package com.taotao.cloud.ccsr.core.storage;

import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SpringCat
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
