package com.taotao.cloud.cache.support.interceptor;

import com.taotao.cloud.cache.api.ICacheInterceptor;
import com.taotao.cloud.cache.support.interceptor.aof.CacheInterceptorAof;
import com.taotao.cloud.cache.support.interceptor.common.CacheInterceptorCost;
import com.taotao.cloud.cache.support.interceptor.evict.CacheInterceptorEvict;
import com.taotao.cloud.cache.support.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存拦截器工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CacheInterceptors {

    /**
     * 默认通用
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultCommonList() {
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }

    /**
     * 默认刷新
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultRefreshList() {
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }

    /**
     * AOF 模式
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor aof() {
        return new CacheInterceptorAof();
    }

    /**
     * 驱除策略拦截器
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor evict() {
        return new CacheInterceptorEvict();
    }

}
