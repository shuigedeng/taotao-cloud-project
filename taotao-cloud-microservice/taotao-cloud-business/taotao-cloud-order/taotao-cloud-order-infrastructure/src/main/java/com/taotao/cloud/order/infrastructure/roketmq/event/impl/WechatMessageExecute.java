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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.sys.model.vo.order.OrderVO;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.roketmq.event.TradeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信消息执行器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:12:33
 */
@Service
public class WechatMessageExecute implements OrderStatusChangeEvent, TradeEvent {

    @Autowired
    private WechatMessageUtil wechatMessageUtil;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        for (OrderVO orderVO : tradeDTO.getOrderVO()) {
            try {
                wechatMessageUtil.sendWechatMessage(orderVO.getSn());
            } catch (Exception e) {
                LogUtils.error("微信消息发送失败：" + orderVO.getSn(), e);
            }
        }
    }

    @Override
    public void orderChange(OrderMessage orderMessage) {
        switch (orderMessage.newStatus()) {
            case PAID:
            case UNDELIVERED:
            case DELIVERED:
            case COMPLETED:
                try {
                    wechatMessageUtil.sendWechatMessage(orderMessage.getOrderSn());
                } catch (Exception e) {
                    LogUtils.error("微信消息发送失败", e);
                }
                break;
            default:
                break;
        }
    }
}
