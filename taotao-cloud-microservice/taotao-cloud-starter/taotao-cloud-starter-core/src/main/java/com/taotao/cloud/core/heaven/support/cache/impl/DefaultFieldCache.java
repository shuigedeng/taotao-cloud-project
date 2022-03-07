package com.taotao.cloud.core.heaven.support.cache.impl;


import com.taotao.cloud.core.heaven.annotation.NotThreadSafe;
import com.taotao.cloud.core.heaven.reflect.api.IField;
import com.taotao.cloud.core.heaven.support.cache.ICache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单个字段的缓存
 */
@NotThreadSafe
public class DefaultFieldCache implements ICache<Class, IField> {

    /**
     * 存放信息的 map
     */
    private static final Map<Class, IField> MAP = new ConcurrentHashMap<>();

    @Override
    public IField get(Class key) {
        return MAP.get(key);
    }

    @Override
    public void set(Class key, IField value) {
        MAP.put(key, value);
    }

}
