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

import com.taotao.cloud.rpc.client.support.register.ClientRegisterManager;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.domain.message.body.RegisterCenterAddNotifyBody;
import com.taotao.cloud.rpc.registry.domain.message.body.RegisterCenterRemoveNotifyBody;
import com.taotao.cloud.rpc.registry.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.simple.constant.MessageTypeConst;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 客户端注册中心处理类 </p>
 *
 * @since 2024.06
 */
public class RpcClientRegisterHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcClientRegisterHandler.class);

    /**
     * 注册服务
     *
     * @since 2024.06
     */
    private final InvokeManager invokeManager;

    /**
     * 客户端注册中心管理类
     *
     * @since 0.1.8
     */
    private final ClientRegisterManager clientRegisterManager;

    public RpcClientRegisterHandler(
            InvokeManager invokeManager, ClientRegisterManager clientRegisterManager) {
        this.invokeManager = invokeManager;
        this.clientRegisterManager = clientRegisterManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        NotifyMessage notifyMessage = (NotifyMessage) msg;
        Object body = notifyMessage.body();
        String type = NotifyMessages.type(notifyMessage);
        String seqId = notifyMessage.seqId();
        //        log.info("[Register Client] received message type: {}, seqId: {} ", type,
        //                seqId);

        // 回写
        final Channel channel = ctx.channel();
        switch (type) {
            case MessageTypeConst.CLIENT_LOOK_UP_SERVER_RESP:
                RpcResponse rpcResponse = (RpcResponse) body;
                //                log.info("[Register Client] Register response is :{}",
                // rpcResponse);
                invokeManager.addResponse(rpcResponse.seqId(), rpcResponse);
                break;

            case MessageTypeConst.SERVER_REGISTER_NOTIFY_CLIENT_REQ:
                ServiceEntry serviceEntry = (ServiceEntry) body;
                clientRegisterManager.serverRegisterNotify(serviceEntry);
                break;

            case MessageTypeConst.SERVER_UNREGISTER_NOTIFY_CLIENT_REQ:
                ServiceEntry serviceEntry2 = (ServiceEntry) body;
                clientRegisterManager.serverUnRegisterNotify(serviceEntry2);
                break;

            case MessageTypeConst.REGISTER_CENTER_ADD_NOTIFY:
                RegisterCenterAddNotifyBody addNotifyBody = (RegisterCenterAddNotifyBody) body;
                clientRegisterManager.addRegisterChannel(addNotifyBody, channel);
                break;

            case MessageTypeConst.REGISTER_CENTER_REMOVE_NOTIFY:
                RegisterCenterRemoveNotifyBody removeNotifyBody =
                        (RegisterCenterRemoveNotifyBody) body;
                clientRegisterManager.removeRegisterChannel(removeNotifyBody);
                break;

            default:
                //                log.warn("[Register Client] not support type: {} and seqId: {}",
                //                        type, seqId);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 每次用完要关闭，不然拿不到response，我也不知道为啥（目测得了解netty才行）
        // 个人理解：如果不关闭，则永远会被阻塞。
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[Rpc Client] meet ex ", cause);
        ctx.close();
    }
}
