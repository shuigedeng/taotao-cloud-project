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

package com.taotao.cloud.mq.client.consumer.support.broker;

import com.taotao.cloud.mq.client.consumer.support.listener.IMqListenerService;
import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.rpc.RpcChannelFuture;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.support.status.IStatusManager;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ConsumerBrokerConfig {

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 中间人地址
     */
    private String brokerAddress;

    /**
     * 调用管理服务
     *
     * @since 2024.05
     */
    private IInvokeService invokeService;

    /**
     * 获取响应超时时间
     *
     * @since 2024.05
     */
    private long respTimeoutMills;

    /**
     * 检测 broker 可用性
     *
     * @since 2024.05
     */
    private boolean check;

    /**
     * 状态管理
     *
     * @since 2024.05
     */
    private IStatusManager statusManager;

    /**
     * 监听服务类
     *
     * @since 2024.05
     */
    private IMqListenerService mqListenerService;

    /**
     * 负载均衡
     *
     * @since 2024.05
     */
    private ILoadBalance<RpcChannelFuture> loadBalance;

    /**
     * 订阅最大尝试次数
     *
     * @since 2024.05
     */
    private int subscribeMaxAttempt = 3;

    /**
     * 取消订阅最大尝试次数
     *
     * @since 2024.05
     */
    private int unSubscribeMaxAttempt = 3;

    /**
     * 消费状态更新最大尝试次数
     *
     * @since 2024.05
     */
    private int consumerStatusMaxAttempt = 3;

    /**
     * 账户标识
     *
     * @since 2024.05
     */
    protected String appKey;

    /**
     * 账户密码
     *
     * @since 2024.05
     */
    protected String appSecret;

    public String appKey() {
        return appKey;
    }

    public ConsumerBrokerConfig appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String appSecret() {
        return appSecret;
    }

    public ConsumerBrokerConfig appSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public int consumerStatusMaxAttempt() {
        return consumerStatusMaxAttempt;
    }

    public ConsumerBrokerConfig consumerStatusMaxAttempt(int consumerStatusMaxAttempt) {
        this.consumerStatusMaxAttempt = consumerStatusMaxAttempt;
        return this;
    }

    public static ConsumerBrokerConfig newInstance() {
        return new ConsumerBrokerConfig();
    }

    public int subscribeMaxAttempt() {
        return subscribeMaxAttempt;
    }

    public ConsumerBrokerConfig subscribeMaxAttempt(int subscribeMaxAttempt) {
        this.subscribeMaxAttempt = subscribeMaxAttempt;
        return this;
    }

    public int unSubscribeMaxAttempt() {
        return unSubscribeMaxAttempt;
    }

    public ConsumerBrokerConfig unSubscribeMaxAttempt(int unSubscribeMaxAttempt) {
        this.unSubscribeMaxAttempt = unSubscribeMaxAttempt;
        return this;
    }

    public String groupName() {
        return groupName;
    }

    public ConsumerBrokerConfig groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String brokerAddress() {
        return brokerAddress;
    }

    public ConsumerBrokerConfig brokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
        return this;
    }

    public IInvokeService invokeService() {
        return invokeService;
    }

    public ConsumerBrokerConfig invokeService(IInvokeService invokeService) {
        this.invokeService = invokeService;
        return this;
    }

    public long respTimeoutMills() {
        return respTimeoutMills;
    }

    public ConsumerBrokerConfig respTimeoutMills(long respTimeoutMills) {
        this.respTimeoutMills = respTimeoutMills;
        return this;
    }

    public boolean check() {
        return check;
    }

    public ConsumerBrokerConfig check(boolean check) {
        this.check = check;
        return this;
    }

    public IStatusManager statusManager() {
        return statusManager;
    }

    public ConsumerBrokerConfig statusManager(IStatusManager statusManager) {
        this.statusManager = statusManager;
        return this;
    }

    public IMqListenerService mqListenerService() {
        return mqListenerService;
    }

    public ConsumerBrokerConfig mqListenerService(IMqListenerService mqListenerService) {
        this.mqListenerService = mqListenerService;
        return this;
    }

    public ILoadBalance<RpcChannelFuture> loadBalance() {
        return loadBalance;
    }

    public ConsumerBrokerConfig loadBalance(ILoadBalance<RpcChannelFuture> loadBalance) {
        this.loadBalance = loadBalance;
        return this;
    }
}
