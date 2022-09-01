package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import com.taotao.cloud.order.biz.service.order.ITradeService;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
		//如果订单需要支付金额为0，则将订单步入到下一个流程
		if (tradeDTO.getPriceDetailDTO().flowPrice().compareTo(BigDecimal.ZERO) <= 0) {
			tradeService.payTrade(tradeDTO.getSn(), PaymentMethodEnum.BANK_TRANSFER.name(), "-1");
		}
	}
}
