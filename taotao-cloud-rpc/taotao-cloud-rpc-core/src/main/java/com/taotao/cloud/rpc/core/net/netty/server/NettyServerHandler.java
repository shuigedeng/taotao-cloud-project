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

package com.taotao.cloud.rpc.core.net.netty.server;

import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    /**
     * 服务器的监听通道读取方法是 多线程的，这样能应对多个 客户端的并发访问
     *
     * @param ctx 通道处理上下文
     * @param msg 请求包
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        /**
         * 心跳包 只 作为 检测包，不做处理
         */
        if (msg.getHeartBeat()) {
            log.debug("receive hearBeatPackage from customer...");
            return;
        }
        NettyChannelDispatcher.dispatch(ctx, msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // super.channelWritabilityChanged(ctx);
        log.warn(
                "trigger hi-lo channel buffer，now channel status:[active {}, writable: {}]",
                ctx.channel().isActive(),
                ctx.channel().isWritable());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error occurred while invoking! info: ", cause);
        ctx.close();
    }

    /**
     * 监听 所有 客户端 发送的 心跳包 IdleState.READER_IDLE 时间内 服务端 没有 读操作（即客户端没有写操作，心跳包发送失败，失去连接） 触发方法执行，关闭 服务端
     * 与 客户端的 通道 channel
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("Heartbeat packets have not been received for a long time");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
