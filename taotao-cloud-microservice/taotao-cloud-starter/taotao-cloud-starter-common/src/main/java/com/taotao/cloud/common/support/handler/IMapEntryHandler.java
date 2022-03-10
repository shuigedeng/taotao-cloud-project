/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.support.handler;

import java.util.Map;

/**
 * 处理接口
 * @param <K> key
 * @param <V> value
 * @param <T> 目标信息
 */
public interface IMapEntryHandler<K, V, T> {

    /**
     * 获取 key
     * @param entry 明细信息
     * @since 0.1.85
     * @return 处理结果
     */
    T handler(final Map.Entry<K,V> entry);

}
