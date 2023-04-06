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

package com.taotao.cloud.payment.biz.jeepay.pay.mq;

import com.taotao.cloud.payment.biz.jeepay.mq.model.PayOrderDivisionMQ;
import com.taotao.cloud.payment.biz.jeepay.pay.service.PayOrderDivisionProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 接收MQ消息 业务： 支付订单分账处理逻辑
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/8/22 8:23
 */
@Slf4j
@Component
public class PayOrderDivisionMQReceiver implements PayOrderDivisionMQ.IMQReceiver {

    @Autowired
    private PayOrderDivisionProcessService payOrderDivisionProcessService;

    @Override
    public void receive(PayOrderDivisionMQ.MsgPayload payload) {

        try {
            log.info("接收订单分账通知MQ, msg={}", payload.toString());
            payOrderDivisionProcessService.processPayOrderDivision(
                    payload.getPayOrderId(),
                    payload.getUseSysAutoDivisionReceivers(),
                    payload.getReceiverList(),
                    payload.getIsResend());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
