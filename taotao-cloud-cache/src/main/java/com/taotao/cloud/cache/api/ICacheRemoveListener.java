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

/**
 * 删除监听器接口
 *
 * @author shuigedeng
 * @since 22024.06
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListener<K, V> {

    /**
     * 监听
     * @param context 上下文
     * @since 22024.06
     */
    void listen(final ICacheRemoveListenerContext<K, V> context);
}
