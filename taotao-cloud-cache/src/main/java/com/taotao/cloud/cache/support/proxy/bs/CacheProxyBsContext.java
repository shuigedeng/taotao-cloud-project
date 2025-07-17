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
 * 代理引导类上下文
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheProxyBsContext implements ICacheProxyBsContext {

    /**
     * 目标
     * @since 2024.06
     */
    private ICache target;

    /**
     * 入参
     * @since 2024.06
     */
    private Object[] params;

    /**
     * 方法
     * @since 2024.06
     */
    private Method method;

    /**
     * 拦截器
     * @since 2024.06
     */
    private CacheInterceptor interceptor;

    /**
     * 新建对象
     * @return 对象
     * @since 2024.06
     */
    public static CacheProxyBsContext newInstance() {
        return new CacheProxyBsContext();
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public CacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target, params);
    }

    public CacheProxyBsContext method(Method method) {
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }
}
