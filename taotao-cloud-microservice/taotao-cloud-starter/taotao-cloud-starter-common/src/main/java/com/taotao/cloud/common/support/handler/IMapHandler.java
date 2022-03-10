/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.support.handler;

/**
 * 处理接口
 * @param <K> key
 * @param <V> value
 * @param <O> 原始信息
 */
public interface IMapHandler<K, V, O> {

    /**
     * 获取 key
     * @param o 单行信息
     * @return 结果
     * @since 0.1.83
     */
    K getKey(final O o);

    /**
     * 获取 value
     * @param o 单行信息
     * @return 结果
     * @since 0.1.83
     */
    V getValue(final O o);

}
