package com.taotao.cloud.rpc.client.client.proxy.impl;

import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.client.proxy.RemoteInvokeService;
import com.taotao.cloud.rpc.client.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import io.netty.channel.Channel;
/**
 * 默认远程调用上下文实现
 *
 * @author shuigedeng
 * @since 0.1.1
 */
public class DefaultRemoteInvokeContext<T> implements RemoteInvokeContext<T> {

    /**
     * 请求信息
     * @since 0.1.1
     */
    private RpcRequest request;

    /**
     * 服务代理上下文信息
     * @since 0.1.1
     */
    private ServiceContext<T> serviceContext;

    /**
     * channel 信息
     * @since 0.1.1
     */
    private RpcChannelFuture channelFuture;

    /**
     * 请求结果
     * @since 0.1.1
     */
    private RpcResponse rpcResponse;

    /**
     * 重试次数
     * @since 0.1.1
     */
    private int retryTimes;

    /**
     * 全局唯一标识
     * @since 0.1.1
     */
    private String traceId;

    /**
     * 远程调用服务
     * @since 0.1.1
     */
    private RemoteInvokeService remoteInvokeService;

    @Override
    public RpcRequest request() {
        return request;
    }

    public DefaultRemoteInvokeContext<T> request(RpcRequest request) {
        this.request = request;
        return this;
    }

    @Override
    public ServiceContext<T> serviceProxyContext() {
        return serviceContext;
    }

    public DefaultRemoteInvokeContext<T> serviceProxyContext(ServiceContext<T> serviceContext) {
        this.serviceContext = serviceContext;
        return this;
    }

    public RpcChannelFuture channelFuture() {
        return channelFuture;
    }

    @Override
    public DefaultRemoteInvokeContext<T> channelFuture(RpcChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
        return this;
    }

    @Override
    public RpcResponse rpcResponse() {
        return rpcResponse;
    }

    @Override
    public DefaultRemoteInvokeContext<T> rpcResponse(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        return this;
    }

    @Override
    public int retryTimes() {
        return retryTimes;
    }

    @Override
    public DefaultRemoteInvokeContext<T> retryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    @Override
    public String traceId() {
        return traceId;
    }

    public DefaultRemoteInvokeContext<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public RemoteInvokeService remoteInvokeService() {
        return remoteInvokeService;
    }

    public DefaultRemoteInvokeContext<T> remoteInvokeService(RemoteInvokeService remoteInvokeService) {
        this.remoteInvokeService = remoteInvokeService;
        return this;
    }
}
