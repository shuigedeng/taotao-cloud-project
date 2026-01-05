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

package com.taotao.cloud.rpc.client.proxy.impl;

import com.taotao.cloud.rpc.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.client.support.fail.enums.FailTypeEnum;
import com.taotao.cloud.rpc.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.client.support.register.ClientRegisterManager;
import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import com.taotao.cloud.rpc.common.tmp.LoadBalance;

/**
 * 反射调用上下文
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultServiceContext<T> implements ServiceContext<T> {

    /**
     * 服务唯一标识
     * @since 2024.06
     */
    private String serviceId;

    /**
     * 服务接口
     * @since 2024.06
     */
    private Class<T> serviceInterface;

    /**
     * channel handler 信息
     *
     * @since 2024.06
     */
    private InvokeManager invokeManager;

    /**
     * 超时时间
     * @since 2024.06
     */
    private long timeout;

    /**
     * 调用方式
     * @since 0.1.0
     */
    private CallTypeEnum callType;

    /**
     * 失败策略
     * @since 0.1.1
     */
    private FailTypeEnum failType;

    /**
     * 是否进行泛化调用
     * @since 0.1.2
     */
    private boolean generic;

    /**
     * 状态管理类
     * @since 0.1.3
     */
    private StatusManager statusManager;

    /**
     * 拦截器
     */
    private RpcInterceptor rpcInterceptor;

    /**
     * 客户端注册中心管理类
     * @since 0.1.8
     */
    private ClientRegisterManager clientRegisterManager;

    /**
     * rpc 过滤器
     *
     * @since 0.2.0
     */
    private RpcFilter rpcFilter;

    /**
     * 负载均衡
     * @since 0.2.0
     */
    private LoadBalance loadBalance;

    @Override
    public String serviceId() {
        return serviceId;
    }

    public DefaultServiceContext<T> serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public Class<T> serviceInterface() {
        return serviceInterface;
    }

    public DefaultServiceContext<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    @Override
    public CallTypeEnum callType() {
        return callType;
    }

    @Override
    public FailTypeEnum failType() {
        return failType;
    }

    public DefaultServiceContext<T> failType(FailTypeEnum failType) {
        this.failType = failType;
        return this;
    }

    public DefaultServiceContext<T> callType(CallTypeEnum callType) {
        this.callType = callType;
        return this;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    public DefaultServiceContext<T> timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public boolean generic() {
        return generic;
    }

    public DefaultServiceContext<T> generic(boolean generic) {
        this.generic = generic;
        return this;
    }

    @Override
    public StatusManager statusManager() {
        return statusManager;
    }

    public DefaultServiceContext<T> statusManager(StatusManager statusManager) {
        this.statusManager = statusManager;
        return this;
    }

    @Override
    public InvokeManager invokeManager() {
        return invokeManager;
    }

    public DefaultServiceContext<T> invokeManager(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
        return this;
    }

    @Override
    public RpcInterceptor interceptor() {
        return rpcInterceptor;
    }

    public DefaultServiceContext<T> interceptor(RpcInterceptor rpcInterceptor) {
        this.rpcInterceptor = rpcInterceptor;
        return this;
    }

    @Override
    public ClientRegisterManager clientRegisterManager() {
        return clientRegisterManager;
    }

    public DefaultServiceContext<T> clientRegisterManager(
            ClientRegisterManager clientRegisterManager) {
        this.clientRegisterManager = clientRegisterManager;
        return this;
    }

    @Override
    public RpcFilter rpcFilter() {
        return rpcFilter;
    }

    public DefaultServiceContext<T> rpcFilter(RpcFilter rpcFilter) {
        this.rpcFilter = rpcFilter;
        return this;
    }

    @Override
    public LoadBalance loadBalance() {
        return loadBalance;
    }

    public DefaultServiceContext<T> loadBalance( LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        return this;
    }
}
