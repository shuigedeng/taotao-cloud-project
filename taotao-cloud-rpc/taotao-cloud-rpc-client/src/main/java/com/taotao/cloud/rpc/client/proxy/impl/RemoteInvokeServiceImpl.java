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

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.proxy.RemoteInvokeService;
import com.taotao.cloud.rpc.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.client.support.calltype.CallTypeStrategy;
import com.taotao.cloud.rpc.client.support.calltype.impl.CallTypeStrategyFactory;
import com.taotao.cloud.rpc.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.client.support.fail.impl.FailStrategyFactory;
import com.taotao.cloud.rpc.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.client.support.register.ClientRegisterManager;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.tmp.ILoadBalance;
import io.netty.channel.Channel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程调用实现
 * @author shuigedeng
 * @since 0.1.1
 */
public class RemoteInvokeServiceImpl implements RemoteInvokeService {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteInvokeServiceImpl.class);

    @Override
    @SuppressWarnings("all")
    public Object remoteInvoke(RemoteInvokeContext context) {
        final RpcRequest rpcRequest = context.request();
        final ServiceContext proxyContext = context.serviceProxyContext();
        final RpcFilter rpcFilter = proxyContext.rpcFilter();

        // 设置唯一标识
        //        final String seqId = Ids.uuid32();
        //        rpcRequest.seqId(seqId);

        // 构建 filter 相关信息,结合 pipeline 进行整合
        rpcFilter.filter(context);

        // 负载均衡
        // 这里使用 load-balance 进行选择 channel 写入。
        final Channel channel = getLoadBalanceChannel(proxyContext);
        //        LOG.info("[Client] start call channel id: {}", channel.id().asLongText());

        // 对于信息的写入，实际上有着严格的要求。
        // writeAndFlush 实际是一个异步的操作，直接使用 sync() 可以看到异常信息。
        // 支持的必须是 ByteBuf
        channel.writeAndFlush(rpcRequest).syncUninterruptibly();
        //        LOG.info("[Client] start call remote with request: {}", rpcRequest);
        final InvokeManager invokeManager = proxyContext.invokeManager();
        invokeManager.addRequest("seqId", proxyContext.timeout());

        // 获取结果
        CallTypeStrategy callTypeStrategy =
                CallTypeStrategyFactory.callTypeStrategy(proxyContext.callType());
        RpcResponse rpcResponse = callTypeStrategy.result(proxyContext, rpcRequest);
        //        invokeManager.removeReqAndResp(seqId);

        // 获取调用结果
        context.rpcResponse(rpcResponse);
        FailStrategy failStrategy = FailStrategyFactory.failStrategy(proxyContext.failType());
        return failStrategy.fail(context);
    }

    /**
     * 获取负载均衡的 channel
     *
     * ps: java 的类型擦除真的麻烦。
     *
     * @param serviceContext 服务上下文
     * @return 结果
     */
    private Channel getLoadBalanceChannel(final ServiceContext serviceContext) {
        final ClientRegisterManager clientRegisterManager = serviceContext.clientRegisterManager();
        final String serviceId = serviceContext.serviceId();
        List<RpcChannelFuture> channelFutures =
                clientRegisterManager.queryServerChannelFutures(serviceId);

        final ILoadBalance loadBalance = serviceContext.loadBalance();
        //        List<IServer> servers = CollectionUtil.toList(channelFutures, new
        // IHandler<RpcChannelFuture, IServer>() {
        //            @Override
        //            public IServer handle(RpcChannelFuture rpcChannelFuture) {
        //                return rpcChannelFuture;
        //            }
        //        });
        //        LoadBalanceContext context = LoadBalanceContext.newInstance()
        //                .servers(servers);

        //        IServer server = loadBalance.select(context);
        //        LOG.info("负载均衡获取地址信息：{}", server.url());
        //        RpcChannelFuture future = (RpcChannelFuture) server;
        //        return future.channelFuture().channel();
        return null;
    }
}
