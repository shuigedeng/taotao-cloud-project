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

package com.taotao.cloud.rpc.client.proxy;

import com.taotao.cloud.rpc.client.proxy.impl.DefaultRemoteInvokeContext;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 远程调用上下文
 * <p>
 * 核心目的： （1）用于定义 filter 相关信息 （2）用于 load-balance 相关信息处理
 *
 * @param <T> 泛型信息
 * @author shuigedeng
 * @since 0.1.1
 */
public interface RemoteInvokeContext<T> {

    /**
     * 请求信息
     *
     * @return 请求信息
     * @since 0.1.1
     */
    RpcRequest request();

    /**
     * 服务代理上下文信息
     *
     * @return 服务代理信息
     * @since 0.1.1
     */
    ServiceContext<T> serviceProxyContext();

    /**
     * 设置 channel future （1）可以通过 load balance （2）其他各种方式
     *
     * @param channelFuture 消息
     * @return this
     * @since 2024.06
     */
    RemoteInvokeContext<T> channelFuture(final RpcChannelFuture channelFuture);

    /**
     * 请求响应结果
     *
     * @return 请求响应结果
     * @since 0.1.1
     */
    RpcResponse rpcResponse();

    /**
     * 请求响应结果
     *
     * @param rpcResponse 响应结果
     * @return 请求响应结果
     * @since 0.1.1
     */
    DefaultRemoteInvokeContext<T> rpcResponse(final RpcResponse rpcResponse);

    /**
     * 获取重试次数
     *
     * @return 重试次数
     */
    int retryTimes();

    /**
     * 设置重试次数
     *
     * @param retryTimes 设置重试次数
     * @return this
     */
    DefaultRemoteInvokeContext<T> retryTimes(final int retryTimes);

    /**
     * 在整个调用生命周期中唯一的标识号 （1）重试也不会改变 （2）只在第一次调用的时候进行设置。
     *
     * @return 订单号
     * @since 0.1.1
     */
    String traceId();

    /**
     * 远程调用服务信息
     *
     * @return 远程调用服务信息
     * @since 0.1.1
     */
    RemoteInvokeService remoteInvokeService();
}
