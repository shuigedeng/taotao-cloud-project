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

package com.taotao.cloud.rpc.server.handler;

import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.DefaultRpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponseFactory;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import com.taotao.cloud.rpc.server.service.impl.DefaultServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.06
 */
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServerHandler.class);

    /**
     * 调用管理类
     *
     * @since 0.1.3
     */
    private final InvokeManager invokeManager;

    /**
     * 状态管理类
     *
     * @since 0.1.3
     */
    private final StatusManager statusManager;

    public RpcServerHandler(InvokeManager invokeManager, StatusManager statusManager) {
        this.invokeManager = invokeManager;
        this.statusManager = statusManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String id = ctx.channel().id().asLongText();
        LOG.info("[Server] channel {} connected " + id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        final String id = ctx.channel().id().asLongText();
        //		log.info("[Server] channel read start: {}", id);

        // 1. 接受客户端请求
        RpcRequest rpcRequest = (RpcRequest) msg;
        final String seqId = rpcRequest.seqId();
        try {
            //			log.info("[Server] receive seqId: {} request: {}", seqId, rpcRequest);
            // 2. 设置请求信息和超时时间
            invokeManager.addRequest(rpcRequest.seqId(), rpcRequest.timeout());

            // 3. 回写到 client 端
            // 3.1 获取结果
            RpcResponse rpcResponse = this.handleRpcRequest(rpcRequest);
            // 3.2 回写结果
            final CallTypeEnum callType = rpcRequest.callType();
            if (CallTypeEnum.SYNC.equals(callType)) {
                ctx.writeAndFlush(rpcResponse);
            } else {
                //				log.info("[Server] seqId: {} callType: {} ignore write back.", seqId,
                // callType);
            }
            //			log.info("[Server] seqId: {} response {}", seqId, rpcResponse);
        } finally {
            // 3.3 移除对应的信息，便于优雅关闭
            invokeManager.removeReqAndResp(seqId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[Server] meet ex: ", cause);
        ctx.close();
    }

    /**
     * 处理请求信息 （1）这里为了简单考虑，暂时不采用异步执行的方式，直接同步执行 （2）缺点就是超时检测的数据会被忽略。
     *
     * @param rpcRequest 请求信息
     * @return 结果信息
     * @since 2024.06
     */
    private RpcResponse handleRpcRequest(final RpcRequest rpcRequest) {
        // 1. 判断是否为 shutdown 状态
        // 状态判断
        final int statusCode = statusManager.status();
        if (StatusEnum.ENABLE.code() != statusCode) {
            //			log.error("[Server] current status is: {} , not enable to handle request",
            // statusCode);
            return RpcResponseFactory.shutdown();
        }

        // 3. 异步执行调用，这样才能进行检测
        DefaultRpcResponse rpcResponse = new DefaultRpcResponse();
        rpcResponse.seqId(rpcRequest.seqId());

        try {
            // 获取对应的 service 实现类
            // rpcRequest=>invocationRequest
            // 执行 invoke
            Object result =
                    DefaultServiceFactory.getInstance()
                            .invoke(
                                    rpcRequest.serviceId(),
                                    rpcRequest.methodName(),
                                    rpcRequest.paramTypeNames(),
                                    rpcRequest.paramValues());
            rpcResponse.result(result);
        } catch (Exception e) {
            rpcResponse.error(e);
            //			log.error("[Server] execute meet ex for request: {}", rpcRequest, e);
        }

        // 构建结果值
        return rpcResponse;
    }
}
