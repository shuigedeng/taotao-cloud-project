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

package com.taotao.cloud.rpc.core.net.netty.client;

import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 未处理的请求
 */
public class UnprocessedRequests {

    /**
     * k - request id v - 可将来获取 的 response
     */
    private static ConcurrentMap<String, CompletableFuture<RpcResponse>>
            unprocessedResponseFutures = new ConcurrentHashMap<>();

    private static ConcurrentMap<String, Integer> unprocessedResponseReentrantCounts =
            new ConcurrentHashMap<>();

    /**
     * 订阅
     *
     * @param requestId 请求体的 requestId 字段
     * @param future    经过 CompletableFuture 包装过的 响应体
     */
    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        Integer reentrantCount = unprocessedResponseReentrantCounts.get(requestId);
        if (unprocessedResponseFutures.containsKey(requestId)) {
            unprocessedResponseReentrantCounts.put(requestId, reentrantCount + 1);
            return;
        }
        unprocessedResponseFutures.put(requestId, future);
        unprocessedResponseReentrantCounts.put(requestId, 0);
    }

    /**
     * 移除 CompletableFuture<RpcResponse> 处理失败请求 id
     *
     * @param requestId 请求体的 requestId 字段
     */
    public void remove(String requestId) {
        Integer reentrantCount = unprocessedResponseReentrantCounts.get(requestId);
        if (unprocessedResponseFutures.containsKey(requestId) && reentrantCount > 0) {
            unprocessedResponseReentrantCounts.put(requestId, reentrantCount - 1);
            return;
        }
        unprocessedResponseFutures.remove(requestId);
    }

    /**
     * 通知
     *
     * @param rpcResponse 响应体
     */
    public void complete(RpcResponse rpcResponse) {
        String requestId = rpcResponse.getRequestId();
        Integer reentrantCount = unprocessedResponseReentrantCounts.get(requestId);
        if (unprocessedResponseFutures.containsKey(requestId) && reentrantCount > 0) {
            unprocessedResponseReentrantCounts.put(requestId, reentrantCount - 1);
            CompletableFuture<RpcResponse> completableFuture =
                    unprocessedResponseFutures.get(requestId);
            completableFuture.complete(rpcResponse);
            return;
        }
        CompletableFuture<RpcResponse> completableFuture =
                unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        completableFuture.complete(rpcResponse);
    }
}
