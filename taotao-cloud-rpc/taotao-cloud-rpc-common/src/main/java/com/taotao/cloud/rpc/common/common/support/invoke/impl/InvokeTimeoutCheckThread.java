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

package com.taotao.cloud.rpc.common.common.support.invoke.impl;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 超时检测线程
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class InvokeTimeoutCheckThread implements Runnable {

    /**
     * 请求信息
     *
     * @since 2024.06
     */
    private final ConcurrentHashMap<String, Long> requestMap;

    /**
     * 请求信息
     *
     * @since 2024.06
     */
    private final ConcurrentHashMap<String, RpcResponse> responseMap;

    /**
     * 新建
     *
     * @param requestMap  请求 Map
     * @param responseMap 结果 map
     * @since 2024.06
     */
    public InvokeTimeoutCheckThread(
            ConcurrentHashMap<String, Long> requestMap,
            ConcurrentHashMap<String, RpcResponse> responseMap) {
        //        ArgUtil.notNull(requestMap, "requestMap");

        this.requestMap = requestMap;
        this.responseMap = responseMap;
    }

    @Override
    public void run() {
        for (Map.Entry<String, Long> entry : requestMap.entrySet()) {
            long expireTime = entry.getValue();
            //            long currentTime = Times.systemTime();
            //
            //            if (currentTime > expireTime) {
            //                final String key = entry.getKey();
            //                // 结果设置为超时，从请求 map 中移除, 同时设置 response 为超时
            //                responseMap.putIfAbsent(key, RpcResponseFactory.timeout());
            //                requestMap.remove(key);
            //            }
        }
    }
}
