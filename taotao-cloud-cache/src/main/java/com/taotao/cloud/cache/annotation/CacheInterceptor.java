package com.taotao.cloud.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存拦截器
 * @author shuigedeng
 * @since 2024.06
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {

    /**
     * 通用拦截器
     *
     * 1. 耗时统计
     * 2. 慢日志统计
     *
     * etc.
     * @return 默认开启
     * @since 2024.06
     */
    boolean common() default true;

    /**
     * 是否启用刷新
     * @return false
     * @since 2024.06
     */
    boolean refresh() default false;

    /**
     * 操作是否需要 append to file，默认为 false
     * 主要针对 cache 内容有变更的操作，不包括查询操作。
     * 包括删除，添加，过期等操作。
     * @return 是否
     * @since 2024.06
     */
    boolean aof() default false;

    /**
     * 是否执行驱除更新
     *
     * 主要用于 LRU/LFU 等驱除策略
     * @return 是否
     * @since 2024.06
     */
    boolean evict() default false;

}
