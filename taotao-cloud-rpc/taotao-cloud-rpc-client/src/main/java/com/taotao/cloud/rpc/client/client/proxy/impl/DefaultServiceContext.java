package com.github.houbb.rpc.client.proxy.impl;

import com.github.houbb.load.balance.api.ILoadBalance;
import com.github.houbb.rpc.client.proxy.ServiceContext;
import com.github.houbb.rpc.client.support.fail.enums.FailTypeEnum;
import com.github.houbb.rpc.client.support.filter.RpcFilter;
import com.github.houbb.rpc.client.support.register.ClientRegisterManager;
import com.github.houbb.rpc.common.constant.enums.CallTypeEnum;
import com.github.houbb.rpc.common.support.inteceptor.RpcInterceptor;
import com.github.houbb.rpc.common.support.invoke.InvokeManager;
import com.github.houbb.rpc.common.support.status.service.StatusManager;

/**
 * 反射调用上下文
 * @author shuigedeng
 * @since 0.0.6
 */
public class DefaultServiceContext<T> implements ServiceContext<T> {

    /**
     * 服务唯一标识
     * @since 0.0.6
     */
    private String serviceId;

    /**
     * 服务接口
     * @since 0.0.6
     */
    private Class<T> serviceInterface;

    /**
     * channel handler 信息
     *
     * @since 0.0.6
     */
    private InvokeManager invokeManager;

    /**
     * 超时时间
     * @since 0.0.7
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
    private ILoadBalance loadBalance;

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

    public DefaultServiceContext<T> clientRegisterManager(ClientRegisterManager clientRegisterManager) {
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
    public ILoadBalance loadBalance() {
        return loadBalance;
    }

    public DefaultServiceContext<T> loadBalance(ILoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        return this;
    }
}

