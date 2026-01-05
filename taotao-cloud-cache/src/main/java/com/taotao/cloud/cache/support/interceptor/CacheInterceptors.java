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

package com.taotao.cloud.cache.support.interceptor;

import com.taotao.cloud.cache.api.CacheInterceptor;
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
    public static List<CacheInterceptor> defaultCommonList() {
        List<CacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }

    /**
     * 默认刷新
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static List<CacheInterceptor> defaultRefreshList() {
        List<CacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }

    /**
     * AOF 模式
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static CacheInterceptor aof() {
        return new CacheInterceptorAof();
    }

    /**
     * 驱除策略拦截器
     * @return 结果
     * @since 2024.06
     */
    @SuppressWarnings("all")
    public static CacheInterceptor evict() {
        return new CacheInterceptorEvict();
    }
}
