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

package com.taotao.cloud.tx.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 分布式事务的核心处理器
/**
 * NettyServerHandler
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channelGroup =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 事务组中的事务状态列表
    private static Map<String, List<String>> transactionTypeMap = new ConcurrentHashMap<>();
    // 事务组是否已经接收到结束的标记
    private static Map<String, Boolean> isEndMap = new ConcurrentHashMap<>();
    // 事务组中应该有的事务个数
    private static Map<String, Integer> transactionCountMap = new ConcurrentHashMap<>();

    @Override
    public void handlerAdded( ChannelHandlerContext ctx ) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    /***
     *
     *  创建事务组，并且添加保存事务
     *  并且需要判断，如果所有事务都已经执行了（有结果了，要么提交，要么回滚）
     *      如果其中有一个事务需要回滚，那么通知所有客户进行回滚，否则则通知所有客户端进行提交
     */
    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
        System.out.println("接受数据：" + msg.toString());

        JSONObject jsonObject = JSON.parseObject((String) msg);

        // create：创建一个事务组，add：添加事务
        String command = jsonObject.getString("command");
        // 事务组ID
        String groupId = jsonObject.getString("groupId");
        // 子事务类型（commit：待提交、rollback：待回滚）
        String transactionType = jsonObject.getString("transactionalType");
        // 事务数量（当前这个全局事务的总参与者数量）
        Integer transactionCount = jsonObject.getInteger("transactionalCount");
        // 是否结束事务（是否为最后一个事务）
        Boolean isEnd = jsonObject.getBoolean("isEnd");

        // 如果参与者发来的是create指令，则创建一个事务组
        if ("create".equals(command)) {
            System.out.println("正在创建事务组.....");
            transactionTypeMap.put(groupId, new ArrayList<String>());
            System.out.println(transactionTypeMap);
        }
        // 如果参与者是add操作，则将对应子事务加入事务组
        else if ("add".equals(command)) {
            transactionTypeMap.get(groupId).add(transactionType);

            // 判断当前子事务是否为整组最后一个事务
            if (isEnd) {
                // 是则声明本组事务已结束
                isEndMap.put(groupId, true);
                transactionCountMap.put(groupId, transactionCount);
            } else {
                // 否则声明后续依旧会有事务到来
                isEndMap.put(groupId, false);
                transactionCountMap.put(groupId, transactionCount);
            }

            // 调试时的输出信息
            System.out.println("isEndMap长度：" + isEndMap.size());
            System.out.println("transactionCountMap长度：" + transactionCountMap.get(groupId));
            System.out.println("transactionTypeMap长度：" + transactionTypeMap.get(groupId).size());

            JSONObject result = new JSONObject();
            result.put("groupId", groupId);
            // 如果已经接收到结束事务的标记，则判断事务是否已经全部到达
            if (isEndMap.get(groupId)
                    && transactionCountMap
                    .get(groupId)
                    .equals(transactionTypeMap.get(groupId).size())) {

                // 如果已经全部到达则看是否需要回滚
                if (transactionTypeMap.get(groupId).contains("rollback")) {
                    System.out.println("事务最终回滚..........");
                    result.put("command", "rollback");
                    sendResult(result);
                    // 如果一组事务中没有任何事务需要回滚，则提交整组事务
                } else {
                    System.out.println("事务最终提交..........");
                    result.put("command", "commit");
                    sendResult(result);
                }
            }
        }
    }

    // 向客户端（事务参与者）发送最终处理结果的方法
    private void sendResult( JSONObject result ) {
        System.out.println("事务最终处理结果：" + result.toJSONString());
        for (Channel channel : channelGroup) {
            channel.writeAndFlush(result.toJSONString());
        }
    }
}
