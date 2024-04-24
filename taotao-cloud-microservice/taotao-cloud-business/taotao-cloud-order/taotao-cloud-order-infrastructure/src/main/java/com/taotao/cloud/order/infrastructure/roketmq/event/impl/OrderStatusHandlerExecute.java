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

import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.infrastructure.roketmq.event.TradeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.ITradeService;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态处理类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:03:45
 */
@Service
public class OrderStatusHandlerExecute implements TradeEvent {

    @Autowired
    private ITradeService tradeService;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        // 如果订单需要支付金额为0，则将订单步入到下一个流程
        if (tradeDTO.getPriceDetailDTO().flowPrice().compareTo(BigDecimal.ZERO) <= 0) {
            tradeService.payTrade(tradeDTO.getSn(), PaymentMethodEnum.BANK_TRANSFER.name(), "-1");
        }
    }
}
