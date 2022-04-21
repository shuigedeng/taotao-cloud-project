package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import com.taotao.cloud.order.biz.service.order.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态处理类
 *
 */
@Service
public class OrderStatusHandlerExecute implements TradeEvent {


    @Autowired
    private TradeService tradeService;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        //如果订单需要支付金额为0，则将订单步入到下一个流程
        if (tradeDTO.getPriceDetailDTO().getFlowPrice() <= 0) {
            tradeService.payTrade(tradeDTO.getSn(), PaymentMethodEnum.BANK_TRANSFER.name(), "-1");
        }

    }
}
