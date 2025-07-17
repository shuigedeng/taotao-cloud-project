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

package com.taotao.cloud.cache.api;

import java.lang.reflect.Method;

/**
 * 拦截器上下文接口
 *
 * （1）get
 * （2）put
 * （3）remove
 * （4）expire
 * （5）evict
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheInterceptorContext<K, V> {

    /**
     * 缓存信息
     * @return 缓存信息
     * @since 2024.06
     */
    ICache<K, V> cache();

    /**
     * 执行的方法信息
     * @return 方法
     * @since 2024.06
     */
    Method method();

    /**
     * 执行的参数
     * @return 参数
     * @since 2024.06
     */
    Object[] params();

    /**
     * 方法执行的结果
     * @return 结果
     * @since 2024.06
     */
    Object result();

    /**
     * 开始时间
     * @return 时间
     * @since 2024.06
     */
    long startMills();

    /**
     * 结束时间
     * @return 时间
     * @since 2024.06
     */
    long endMills();
}
