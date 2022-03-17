package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.egzosn.pay.paypal.bean.order.Payment;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.biz.entity.order.Order;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付
 */
@Service
public class PaymentExecute implements OrderStatusChangeEvent {

	/**
	 * 订单
	 */
	@Autowired
	private OrderService orderService;

	@Override
	public void orderChange(OrderMessage orderMessage) {

		switch (orderMessage.getNewStatus()) {
			case CANCELLED:
				Order order = orderService.getBySn(orderMessage.getOrderSn());

				//如果未付款，则不去要退回相关代码执行
				if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
					return;
				}
				PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(
					order.getPaymentMethod());
				//进行退款操作
				switch (paymentMethodEnum) {
					case WALLET:
					case ALIPAY:
					case WECHAT:
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
						payment.cancel(refundLog);
						break;
					case BANK_TRANSFER:
						break;
					default:
						log.error("订单支付执行异常,订单编号：{}", orderMessage.getOrderSn());
						break;
				}
				break;
			default:
				break;
		}


	}


}
