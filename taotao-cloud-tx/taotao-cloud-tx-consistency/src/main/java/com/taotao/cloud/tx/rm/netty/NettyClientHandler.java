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

package com.taotao.cloud.tx.rm.netty;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.tx.rm.transactional.TransactionalType;
import com.taotao.cloud.tx.rm.transactional.TtcTx;
import com.taotao.cloud.tx.rm.transactional.TtcTxParticipant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * NettyClientHandler
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext channelHandlerContext;

    @Override
    public void handlerAdded( ChannelHandlerContext ctx ) throws Exception {
        channelHandlerContext = ctx;
    }

    @Override
    public synchronized void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
        System.out.println("接收到事务管理者的最终决断：" + msg.toString());

        // 反序列化解析JSON数据
        JSONObject data = JSON.parseObject((String) msg);
        String groupId = data.getString("groupId");
        String command = data.getString("command");
        System.out.println("接收command：" + command);

        // 对事务进行操作
        TtcTx ttcTx = TtcTxParticipant.getTtcTransactional(groupId);

        // 如果事务管理者最终决定提交事务
        if ("commit".equals(command)) {
            // 根据groupID找到子事务并设置commit状态
            ttcTx.setTransactionalType(TransactionalType.commit);
        }
        // 如果事务管理者最终决定回滚事务
        else {
            // 根据groupID找到子事务并设置rollback回滚状态
            ttcTx.setTransactionalType(TransactionalType.rollback);
        }

        // 唤醒在之前阻塞的、负责提交/回滚事务的线程
        ttcTx.getTask().signalTask();
    }

    public void sendData( JSONObject result ) {
        System.out.println("向事务管理者发送数据：" + result.toJSONString());
        channelHandlerContext.writeAndFlush(result.toJSONString());
    }
}
