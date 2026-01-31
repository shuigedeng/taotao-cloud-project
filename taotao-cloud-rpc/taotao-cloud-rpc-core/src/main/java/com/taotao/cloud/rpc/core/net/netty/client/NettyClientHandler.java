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

import com.taotao.cloud.rpc.common.factory.SingleFactory;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.common.protocol.RpcResponse;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * NettyClientHandler
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        unprocessedRequests = SingleFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, RpcResponse msg ) throws Exception {
        try {
            log.info(
                    String.format(
                            "customer has received response package {requestId: %s, message: %s, statusCode: %s ]}",
                            msg.getRequestId(), msg.getMessage(), msg.getStatusCode()));
            /**
             * 1. 取出 缓存在 AttributeKey 中 常量池ConstantPool 的 ConcurrentMap<String, RpcResponse>
             *  key 为 "rpcResponse"的 AttributeKey<RpcResponse>
             * 2. 底层原理是 putIfAbsent(name, tempConstant), 只第一次有效，下次不会 put, 只返回 第一次的值
             * 3. 一个 AttributeKey 类 分配 一个 常量池，多个AttributeKey 共享
             * 4. 多线程环境下 常量池中的 ConcurrentHashMap<String,T > 是共享的，并且是 线程安全的
             */
            // AttributeKey<RpcResponse> key = AttributeKey.valueOf(msg.getRequestId());
            // ctx.channel().attr(key).set(msg);
            unprocessedRequests.complete(msg);
            // ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelWritabilityChanged( ChannelHandlerContext ctx ) throws Exception {
        // super.channelWritabilityChanged(ctx);
        log.warn(
                "trigger hi-lo channel buffer，now channel status:[active {}, writable: {}]",
                ctx.channel().isActive(),
                ctx.channel().isWritable());
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        log.error("error occurred while invoking, error information:", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered( ChannelHandlerContext ctx, Object evt ) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ( (IdleStateEvent) evt ).state();
            // 客户端 没有发送 数据了，设置 写超时总会被 触发，从而 发送心跳包 给 服务端
            if (state == IdleState.WRITER_IDLE) {
                log.debug("Send heartbeat packets to server[{}]", ctx.channel().remoteAddress());
                ChannelProvider.get(
                        (InetSocketAddress) ctx.channel().remoteAddress(),
                        CommonSerializer.getByCode(CommonSerializer.HESSIAN_SERIALIZER));
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setHeartBeat(true);
                ctx.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
