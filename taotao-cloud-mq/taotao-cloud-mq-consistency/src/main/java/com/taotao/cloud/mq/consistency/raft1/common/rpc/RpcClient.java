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

package com.taotao.cloud.mq.consistency.raft1.common.rpc;

import com.taotao.cloud.mq.consistency.raft1.common.core.LifeCycle;

/**
 * rpc 客户端
 * @author shuigedeng
 */
public interface RpcClient extends LifeCycle {

    /**
     * 发送请求, 并同步等待返回值.
     * @param request 参数
     * @param <R> 返回值泛型
     * @return 结果
     */
    <R> R send(RpcRequest request);

    /**
     * 发送请求
     * @param request 请求
     * @param timeoutMillis 超时
     * @return 结果
     * @param <R> 结果
     */
    <R> R send(RpcRequest request, int timeoutMillis);
}
