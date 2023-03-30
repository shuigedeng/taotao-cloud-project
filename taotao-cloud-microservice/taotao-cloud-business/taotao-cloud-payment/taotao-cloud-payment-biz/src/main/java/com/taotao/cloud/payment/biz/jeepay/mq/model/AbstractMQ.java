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

package com.taotao.cloud.payment.biz.jeepay.mq.model;

import com.taotao.cloud.payment.biz.jeepay.mq.constant.MQSendTypeEnum;

/**
 * 定义MQ消息格式
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/7/22 15:33
 */
public abstract class AbstractMQ {

    /** MQ名称 * */
    public abstract String getMQName();

    /** MQ 类型 * */
    public abstract MQSendTypeEnum getMQType();

    /** 构造MQ消息体 String类型 * */
    public abstract String toMessage();
}
