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

package com.taotao.cloud.mq.client.producer.core;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.client.producer.constant.ProducerConst;
import com.taotao.cloud.mq.client.producer.constant.ProducerRespCode;
import com.taotao.cloud.mq.client.producer.dto.SendBatchResult;
import com.taotao.cloud.mq.client.producer.dto.SendResult;
import com.taotao.cloud.mq.client.producer.support.broker.ProducerBrokerService;
import com.taotao.cloud.mq.client.producer.support.broker.ProducerBrokerConfig;
import com.taotao.cloud.mq.client.producer.support.broker.DefaultProducerBrokerService;
import com.taotao.cloud.mq.common.balance.LoadBalance;
import com.taotao.cloud.mq.common.balance.impl.LoadBalances;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.rpc.RpcChannelFuture;
import com.taotao.cloud.mq.common.support.hook.DefaultShutdownHook;
import com.taotao.cloud.mq.common.support.hook.ShutdownHooks;
import com.taotao.cloud.mq.common.support.invoke.InvokeService;
import com.taotao.cloud.mq.common.support.invoke.impl.DefaultInvokeService;
import com.taotao.cloud.mq.common.support.status.StatusManager;
import com.taotao.cloud.mq.common.support.status.DefaultStatusManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认 mq 生产者
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class MqProducer extends Thread implements com.taotao.cloud.mq.client.producer.api.MqProducer {

    private static final Logger log = LoggerFactory.getLogger(MqProducer.class);

    /**
     * 分组名称
     */
    private String groupName = ProducerConst.DEFAULT_GROUP_NAME;

    /**
     * 中间人地址
     */
    private String brokerAddress = "127.0.0.1:9999";

    /**
     * 获取响应超时时间
     *
     * @since 2024.05
     */
    private long respTimeoutMills = 5000;

    /**
     * 检测 broker 可用性
     *
     * @since 2024.05
     */
    private volatile boolean check = true;

    /**
     * 调用管理服务
     *
     * @since 2024.05
     */
    private final InvokeService invokeService = new DefaultInvokeService();

    /**
     * 状态管理类
     *
     * @since 2024.05
     */
    private final StatusManager statusManager = new DefaultStatusManager();

    /**
     * 生产者-中间服务端服务类
     *
     * @since 2024.05
     */
    private final ProducerBrokerService producerBrokerService = new DefaultProducerBrokerService();

    /**
     * 为剩余的请求等待时间
     *
     * @since 2024.05
     */
    private long waitMillsForRemainRequest = 60 * 1000;

    /**
     * 负载均衡策略
     *
     * @since 2024.05
     */
    private LoadBalance<RpcChannelFuture> loadBalance = LoadBalances.weightRoundRobbin();

    /**
     * 消息发送最大尝试次数
     *
     * @since 2024.05
     */
    private int maxAttempt = 3;

    /**
     * 账户标识
     *
     * @since 2024.05
     */
    private String appKey;

    /**
     * 账户密码
     *
     * @since 2024.05
     */
    private String appSecret;

    public MqProducer appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public MqProducer appSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public MqProducer groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public MqProducer brokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
        return this;
    }

    public MqProducer respTimeoutMills(long respTimeoutMills) {
        this.respTimeoutMills = respTimeoutMills;
        return this;
    }

    public MqProducer check(boolean check) {
        this.check = check;
        return this;
    }

    public MqProducer waitMillsForRemainRequest(long waitMillsForRemainRequest) {
        this.waitMillsForRemainRequest = waitMillsForRemainRequest;
        return this;
    }

    public MqProducer loadBalance( LoadBalance<RpcChannelFuture> loadBalance) {
        this.loadBalance = loadBalance;
        return this;
    }

    public MqProducer maxAttempt(int maxAttempt) {
        this.maxAttempt = maxAttempt;
        return this;
    }

    /**
     * 参数校验
     */
    private void paramCheck() {
        ArgUtils.notEmpty(groupName, "groupName");
        ArgUtils.notEmpty(brokerAddress, "brokerAddress");
    }

    @Override
    public synchronized void run() {
        this.paramCheck();

        // 启动服务端
        log.info("MQ 生产者开始启动客户端 GROUP: {} brokerAddress: {}", groupName, brokerAddress);

        try {
            // 0. 配置信息
            ProducerBrokerConfig config =
                    ProducerBrokerConfig.newInstance()
                            .groupName(groupName)
                            .brokerAddress(brokerAddress)
                            .check(check)
                            .respTimeoutMills(respTimeoutMills)
                            .invokeService(invokeService)
                            .statusManager(statusManager)
                            .loadBalance(loadBalance)
                            .maxAttempt(maxAttempt)
                            .appKey(appKey)
                            .appSecret(appSecret);

            // 1. 初始化
            this.producerBrokerService.initChannelFutureList(config);

            // 2. 连接到服务端
            this.producerBrokerService.registerToBroker();

            // 3. 标识为可用
            statusManager.status(true);

            // 4. 添加钩子函数
            final DefaultShutdownHook rpcShutdownHook = new DefaultShutdownHook();
            rpcShutdownHook.setStatusManager(statusManager);
            rpcShutdownHook.setInvokeService(invokeService);
            rpcShutdownHook.setWaitMillsForRemainRequest(waitMillsForRemainRequest);
            rpcShutdownHook.setDestroyable(this.producerBrokerService);
            ShutdownHooks.rpcShutdownHook(rpcShutdownHook);

            log.info("MQ 生产者启动完成");
        } catch (Exception e) {
            log.error("MQ 生产者启动遇到异常", e);
            // 设置为初始化失败
            statusManager.initFailed(true);

            throw new MqException(ProducerRespCode.RPC_INIT_FAILED);
        }
    }

    @Override
    public SendResult send(MqMessage mqMessage) {
        return this.producerBrokerService.send(mqMessage);
    }

    @Override
    public SendResult sendOneWay(MqMessage mqMessage) {
        return this.producerBrokerService.sendOneWay(mqMessage);
    }

    @Override
    public SendBatchResult sendBatch(List<MqMessage> mqMessageList) {
        return producerBrokerService.sendBatch(mqMessageList);
    }

    @Override
    public SendBatchResult sendOneWayBatch(List<MqMessage> mqMessageList) {
        return producerBrokerService.sendOneWayBatch(mqMessageList);
    }
}
