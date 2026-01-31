package com.taotao.cloud.idea.plugin.toolkit.service.impl;


import com.taotao.cloud.idea.plugin.toolkit.service.CacheService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * CacheServiceImpl
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class CacheServiceImpl implements CacheService {

    private ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void put( String key, Object vlaue ) {
        cache.put(key, vlaue);
    }

    @Override
    public Object get( String key ) {
        return cache.get(key);
    }
}
