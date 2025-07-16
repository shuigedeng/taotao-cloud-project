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

package com.taotao.cloud.mq.client.producer.handler;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.util.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqProducerHandler extends SimpleChannelInboundHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MqProducerHandler.class);

    /**
     * 调用管理类
     */
    private IInvokeService invokeService;

    public void setInvokeService(IInvokeService invokeService) {
        this.invokeService = invokeService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String text = new String(bytes);
        LOG.info("[Client] channelId {} 接收到消息 {}", ChannelUtil.getChannelId(ctx), text);

        RpcMessageDto rpcMessageDto = null;
        try {
            rpcMessageDto = JSON.parseObject(bytes, RpcMessageDto.class);
        } catch (Exception exception) {
            LOG.error("RpcMessageDto json 格式转换异常 {}", JSON.parse(bytes));
            return;
        }

        if (rpcMessageDto.isRequest()) {
            // 请求类
            final String methodType = rpcMessageDto.getMethodType();
            final String json = rpcMessageDto.getJson();
        } else {
            // 丢弃掉 traceId 为空的信息
            if (StringUtils.isBlank(rpcMessageDto.getTraceId())) {
                LOG.info("[Client] response traceId 为空，直接丢弃", JSON.toJSON(rpcMessageDto));
                return;
            }

            invokeService.addResponse(rpcMessageDto.getTraceId(), rpcMessageDto);
            LOG.info("[Client] response is :{}", JSON.toJSON(rpcMessageDto));
        }
    }
}
