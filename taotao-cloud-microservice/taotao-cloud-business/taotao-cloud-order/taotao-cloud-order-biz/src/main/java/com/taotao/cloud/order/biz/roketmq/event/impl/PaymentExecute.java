package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.egzosn.pay.paypal.bean.order.Payment;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.model.message.OrderMessage;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.service.business.order.IOrderService;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:03:55
 */
@Service
public class PaymentExecute implements OrderStatusChangeEvent {

	/**
	 * 订单
	 */
	@Autowired
	private IOrderService orderService;

	@Override
	public void orderChange(OrderMessage orderMessage) {
		if (orderMessage.getNewStatus() == OrderStatusEnum.CANCELLED) {
			Order order = orderService.getBySn(orderMessage.getOrderSn());

			//如果未付款，则不去要退回相关代码执行
			if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
				return;
			}
			PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(order.getPaymentMethod());

			//获取支付方式
			Payment payment =
				(Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());

			RefundLog refundLog = RefundLog.builder()
				.isRefund(false)
				.totalAmount(order.getFlowPrice())
				.payPrice(order.getFlowPrice())
				.memberId(order.getMemberId())
				.paymentName(order.getPaymentMethod())
				.afterSaleNo("订单取消")
				.orderSn(order.getSn())
				.paymentReceivableNo(order.getReceivableNo())
				.outOrderNo("AF" + SnowFlake.getIdStr())
				.outOrderNo("AF" + SnowFlake.getIdStr())
				.refundReason("订单取消")
				.build();
			payment.refund(refundLog);
		}
	}
}
