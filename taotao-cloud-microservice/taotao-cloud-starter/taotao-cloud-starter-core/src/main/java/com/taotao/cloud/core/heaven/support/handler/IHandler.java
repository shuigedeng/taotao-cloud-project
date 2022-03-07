/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.support.handler;

/**
 * 处理接口
 * @param <T> 泛型入参
 * @param <R> 泛型结果
 */
public interface IHandler<T, R> {

    /**
     * 处理
     * @param t 模板
     * @return 结果
     */
    R handle(T t);

}
