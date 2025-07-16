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

package com.taotao.cloud.rpc.client.handler;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 客户端处理类 </p>
 * @since 2024.06
 */
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcClientHandler.class);

    /**
     * 调用服务管理类
     *
     * @since 2024.06
     */
    private final InvokeManager invokeManager;

    public RpcClientHandler(InvokeManager invokeManager) {
        this.invokeManager = invokeManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        invokeManager.addResponse(rpcResponse.seqId(), rpcResponse);
        //        log.info("[Client] server response is :{}", rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[Rpc Client] meet ex ", cause);
        ctx.close();
    }
}
