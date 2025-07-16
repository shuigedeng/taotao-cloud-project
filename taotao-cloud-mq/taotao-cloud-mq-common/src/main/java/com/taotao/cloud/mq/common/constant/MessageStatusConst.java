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
public final class MessageStatusConst {

    private MessageStatusConst() {}

    /**
     * 待消费
     * ps: 生产者推送到 broker 的初始化状态
     */
    public static final String WAIT_CONSUMER = "W";

    /**
     * 推送给消费端处理中
     * ps: broker 准备推送时，首先将状态更新为 P，等待推送结果
     * @since 2024.05
     */
    public static final String TO_CONSUMER_PROCESS = "TCP";

    /**
     * 推送给消费端成功
     * @since 2024.05
     */
    public static final String TO_CONSUMER_SUCCESS = "TCS";

    /**
     * 推送给消费端失败
     * @since 2024.05
     */
    public static final String TO_CONSUMER_FAILED = "TCF";

    /**
     * 消费完成
     */
    public static final String CONSUMER_SUCCESS = "CS";

    /**
     * 消费失败
     */
    public static final String CONSUMER_FAILED = "CF";

    /**
     * 稍后消费
     * @since 2024.05
     */
    public static final String CONSUMER_LATER = "CL";
}
