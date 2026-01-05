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

package com.taotao.cloud.cache.support.proxy;

import static java.util.Objects.isNull;

import com.taotao.cloud.cache.api.Cache;
import com.taotao.cloud.cache.support.proxy.cglib.CglibProxy;
import com.taotao.cloud.cache.support.proxy.dynamic.DynamicProxy;
import com.taotao.cloud.cache.support.proxy.none.NoneProxy;
import java.lang.reflect.Proxy;

/**
 * <p> 代理信息 </p>
 *
 * @since 2024.06
 */
public final class DefaultCacheProxy {

    private DefaultCacheProxy() {}

    /**
     * 获取对象代理
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     * @param cache 对象代理
     * @return 代理信息
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static <K, V> Cache<K, V> getProxy(final Cache<K, V> cache) {
        if (isNull(cache)) {
            return (Cache<K, V>) new NoneProxy(cache).proxy();
        }

        final Class clazz = cache.getClass();

        // 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理。
        // 参考 spring 的 AOP 判断
        if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {
            return (Cache<K, V>) new DynamicProxy(cache).proxy();
        }

        return (Cache<K, V>) new CglibProxy(cache).proxy();
    }
}
