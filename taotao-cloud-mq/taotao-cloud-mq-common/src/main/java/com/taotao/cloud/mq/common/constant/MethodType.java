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

package com.taotao.cloud.mq.common.constant;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MethodType {

    /**
     * 生产者发送消息
     */
    public static final String P_SEND_MSG = "P_SEND_MSG";

    /**
     * 生产者发送消息
     * @since 2024.05
     */
    public static final String P_SEND_MSG_ONE_WAY = "P_SEND_MSG_ONE_WAY";

    /**
     * 生产者注册
     * @since 2024.05
     */
    public static final String P_REGISTER = "P_REGISTER";

    /**
     * 生产者取消注册
     * @since 2024.05
     */
    public static final String P_UN_REGISTER = "P_UN_REGISTER";

    /**
     * 消费者注册
     * @since 2024.05
     */
    public static final String C_REGISTER = "C_REGISTER";

    /**
     * 消费者取消注册
     * @since 2024.05
     */
    public static final String C_UN_REGISTER = "C_UN_REGISTER";

    /**
     * 消费者订阅
     * @since 2024.05
     */
    public static final String C_SUBSCRIBE = "C_SUBSCRIBE";

    /**
     * 消费者取消订阅
     * @since 2024.05
     */
    public static final String C_UN_SUBSCRIBE = "C_UN_SUBSCRIBE";

    /**
     * 消费者消息主动拉取
     * @since 2024.05
     */
    public static final String C_MESSAGE_PULL = "C_MESSAGE_PULL";

    /**
     * 消费者心跳
     * @since 2024.05
     */
    public static final String C_HEARTBEAT = "C_HEARTBEAT";

    /**
     * 消费者消费状态
     * @since 2024.05
     */
    public static final String C_CONSUMER_STATUS = "C_CONSUMER_STATUS";

    /**
     * 中间人消息推送
     * @since 2024.05
     */
    public static final String B_MESSAGE_PUSH = "B_MESSAGE_PUSH";

    /**
     * 消费者消费状态-批量
     * @since 2024.05
     */
    public static final String C_CONSUMER_STATUS_BATCH = "C_CONSUMER_STATUS_BATCH";

    /**
     * 生产者发送消息-批量
     * @since 2024.05
     */
    public static final String P_SEND_MSG_BATCH = "P_SEND_MSG_BATCH";

    /**
     * 生产者发送消息-批量
     * @since 2024.05
     */
    public static final String P_SEND_MSG_ONE_WAY_BATCH = "P_SEND_MSG_ONE_WAY_BATCH";
}
