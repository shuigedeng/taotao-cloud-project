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

package com.taotao.cloud.cache.support.proxy.bs;

import com.taotao.cloud.cache.annotation.CacheInterceptor;
import com.taotao.cloud.cache.api.ICache;
import java.lang.reflect.Method;

/**
 * @author shuigedeng
 * @since 2024.06
 */
public interface ICacheProxyBsContext {

    /**
     * 拦截器信息
     * @return 拦截器
     * @since 2024.06
     */
    CacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     * @return 代理
     * @since 2024.06
     */
    ICache target();

    /**
     * 目标对象
     * @param target 对象
     * @return 结果
     * @since 2024.06
     */
    ICacheProxyBsContext target(final ICache target);

    /**
     * 参数信息
     * @return 参数信息
     * @since 2024.06
     */
    Object[] params();

    /**
     * 方法信息
     * @return 方法信息
     * @since 2024.06
     */
    Method method();

    /**
     * 方法执行
     * @return 执行
     * @since 2024.06
     * @throws Throwable 异常信息
     */
    Object process() throws Throwable;
}
