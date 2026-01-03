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

package com.taotao.cloud.tx.rm.transactional;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.tx.rm.netty.NettyClient;
import com.taotao.cloud.tx.rm.util.ApplicationContextProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// 事务参与者的核心实现类
/**
 * TtcTxParticipant
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class TtcTxParticipant {

    // 获取前面伴随服务启动产生的NettyClient客户端
    private static NettyClient nettyClient = ApplicationContextProvider.getBean(NettyClient.class);

    // 存储当前线程在执行的子事务对象
    private static ThreadLocal<TtcTx> current = new ThreadLocal<>();
    // 存储当前子事务所属的事务组ID值
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    // 存储当前子事务所属的事务组子事务总量
    private static ThreadLocal<Integer> transactionalCount = new ThreadLocal<>();
    // 事务ID和子事务对象的映射组
    private static Map<String, TtcTx> TTC_TRANSACTIONAL_MAP = new HashMap();

    /**
     * 向事务管理者中发送一个创建事务组的命令
     */
    public static String createTtcTransactionalManagerGroup() {
        // 随机产生一个UUID作为事务组ID
        String groupID = UUID.randomUUID().toString();
        // 把事务组ID存在currentGroupId当中
        currentGroupId.set(groupID);
        System.err.println(
                "创建事务组：" + Thread.currentThread().getId() + "，事务组ID：" + getCurrentGroupId());
        // 通过JSON做序列化
        JSONObject sendData = new JSONObject();
        // 传入前面产生的事务组ID，以及本次操作为create创建指令
        sendData.put("groupId", groupID);
        sendData.put("command", "create");
        // 调用客户端的send()方法向服务端发送数据
        nettyClient.send(sendData);
        System.out.println(">>>>>向管理者发送创建事务组命令成功<<<<<");
        // 对外返回事务组ID值
        return groupID;
    }

    /***
     *  创建一个子事务对象
     */
    public static TtcTx createTransactional( String groupId ) {
        // 随机产生一个UUID作为子事务ID
        String transactionalId = UUID.randomUUID().toString();
        // 示例化出一个子事务对象
        TtcTx ttcTx = new TtcTx(groupId, transactionalId);
        // 将创建出的子事务对象保存到相关的变量中
        TTC_TRANSACTIONAL_MAP.put(groupId, ttcTx);
        current.set(ttcTx);
        // 对事务组数量+1
        Integer integer = addTransactionCount();
        System.out.println("创建子事务,目前事务组长度为：" + integer);
        return ttcTx;
    }

    /**
     * 注册事务（向事务管理者的事务组中添加子事务）
     */
    public static TtcTx addTtcTransactional( TtcTx ztp, Boolean isEnd, TransactionalType type ) {
        // 通过JSON序列化一个对象
        JSONObject sendData = new JSONObject();
        // 传入当前子事务的组ID、事务ID、事务类型、操作类型....信息
        sendData.put("groupId", ztp.getGroupId());
        sendData.put("transactionalId", ztp.getTransactionalId());
        sendData.put("transactionalType", type);
        sendData.put("command", "add");
        sendData.put("isEnd", isEnd);
        sendData.put("transactionalCount", TtcTxParticipant.getTransactionalCount());
        // 将封装好的JSON发送给事务管理者
        nettyClient.send(sendData);
        System.out.println(">>>>>向管理者发送添加子事务命令成功<<<<<");
        return ztp;
    }

    // 增加事务组数量的方法
    public static Integer addTransactionCount() {
        System.out.println(transactionalCount.get());
        int i = ( transactionalCount.get() == null ? 0 : transactionalCount.get() ) + 1;
        transactionalCount.set(i);
        return i;
    }

    // 前面类成员的Get/Set方法
    public static Integer getTransactionalCount() {
        return transactionalCount.get();
    }

    public static TtcTx getCurrent() {
        return current.get();
    }

    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId( String groupId ) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionalCount.get();
    }

    public static void setTransactionCount( int i ) {
        transactionalCount.set(i);
    }

    public static TtcTx getTtcTransactional( String groupId ) {
        return TTC_TRANSACTIONAL_MAP.get(groupId);
    }
}
