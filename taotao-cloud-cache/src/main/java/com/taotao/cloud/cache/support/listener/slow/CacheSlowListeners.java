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

package com.taotao.cloud.cache.support.listener.slow;

import com.taotao.cloud.cache.api.ICacheSlowListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 慢日志监听工具类
 * @author shuigedeng
 * @since 2024.06
 */
public final class CacheSlowListeners {

    private CacheSlowListeners() {}

    /**
     * 无
     * @return 监听类列表
     * @since 2024.06
     */
    public static List<ICacheSlowListener> none() {
        return new ArrayList<>();
    }

    /**
     * 默认实现
     * @return 默认
     * @since 2024.06
     */
    public static ICacheSlowListener defaults() {
        return new CacheSlowListener();
    }
}
