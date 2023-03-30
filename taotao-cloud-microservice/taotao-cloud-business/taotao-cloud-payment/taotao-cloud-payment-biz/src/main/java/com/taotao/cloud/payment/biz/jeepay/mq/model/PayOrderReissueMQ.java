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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.payment.biz.jeepay.mq.constant.MQSendTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定义MQ消息格式 业务场景： [ 支付订单补单（一般用于没有回调的接口，比如微信的条码支付） ]
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/7/22 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderReissueMQ extends AbstractMQ {

    /** 【！重要配置项！】 定义MQ名称 * */
    public static final String MQ_NAME = "QUEUE_PAY_ORDER_REISSUE";

    /** 内置msg 消息体定义 * */
    private MsgPayload payload;

    /** 【！重要配置项！】 定义Msg消息载体 * */
    @Data
    @AllArgsConstructor
    public static class MsgPayload {

        /** 支付订单号 * */
        private String payOrderId;

        /** 通知次数 * */
        private Integer count;
    }

    @Override
    public String getMQName() {
        return MQ_NAME;
    }

    /** 【！重要配置项！】 * */
    @Override
    public MQSendTypeEnum getMQType() {
        return MQSendTypeEnum.QUEUE; // QUEUE - 点对点 、 BROADCAST - 广播模式
    }

    @Override
    public String toMessage() {
        return JSONObject.toJSONString(payload);
    }

    /** 【！重要配置项！】 构造MQModel , 一般用于发送MQ时 * */
    public static PayOrderReissueMQ build(String payOrderId, Integer count) {
        return new PayOrderReissueMQ(new MsgPayload(payOrderId, count));
    }

    /** 解析MQ消息， 一般用于接收MQ消息时 * */
    public static MsgPayload parse(String msg) {
        return JSON.parseObject(msg, MsgPayload.class);
    }

    /** 定义 IMQReceiver 接口： 项目实现该接口则可接收到对应的业务消息 * */
    public interface IMQReceiver {
        void receive(MsgPayload payload);
    }
}
