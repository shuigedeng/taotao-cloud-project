package com.taotao.cloud.payment.biz.kit.params.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderService;
import com.taotao.cloud.order.api.web.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.web.vo.order.OrderItemVO;
import com.taotao.cloud.order.api.web.vo.order.OrderVO;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.web.vo.setting.BaseSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单支付信息获取
 */
@Component
public class OrderCashier implements CashierExecute {
	/**
	 * 订单
	 */
	@Autowired
	private IFeignOrderService orderService;
	/**
	 * 设置
	 */
	@Autowired
	private IFeignSettingService settingService;

	@Override
	public CashierEnum cashierEnum() {
		return CashierEnum.ORDER;
	}

	@Override
	public CashierParam getPaymentParams(PayParam payParam) {
		if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
			//准备返回的数据
			CashierParam cashierParam = new CashierParam();
			//订单信息获取
			OrderDetailVO order = orderService.queryDetail(payParam.getSn()).data();

			//如果订单已支付，则不能发器支付
			if (order.order().payStatus().equals(PayStatusEnum.PAID.name())) {
				throw new BusinessException(ResultEnum.PAY_CASHIER_ERROR);
			}
			//如果订单状态不是待付款，则抛出异常
			if (!order.order().orderStatus().equals(OrderStatusEnum.UNPAID.name())) {
				throw new BusinessException(ResultEnum.PAY_BAN);
			}
			cashierParam.setPrice(order.order().flowPrice());

			try {
				BaseSetting baseSetting = settingService.getBaseSetting(SettingEnum.BASE_SETTING.name()).data();
				cashierParam.setTitle(baseSetting.getSiteName());
			} catch (Exception e) {
				cashierParam.setTitle("多用户商城，在线支付");
			}

			List<OrderItemVO> orderItemList = order.orderItems();
			StringBuilder subject = new StringBuilder();
			for (OrderItemVO orderItem : orderItemList) {
				subject.append(orderItem.goodsName()).append(";");
			}

			cashierParam.setDetail(subject.toString());

			cashierParam.setOrderSns(payParam.getSn());
			cashierParam.setCreateTime(order.order().getCreateTime());
			return cashierParam;
		}

		return null;
	}

	@Override
	public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {
		PayParam payParam = paymentSuccessParams.getPayParam();
		if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
			orderService.payOrder(payParam.getSn(),
				paymentSuccessParams.getPaymentMethod(),
				paymentSuccessParams.getReceivableNo());
			LogUtil.info("订单{}支付成功,金额{},方式{}", payParam.getSn(),
				paymentSuccessParams.getPaymentMethod(),
				paymentSuccessParams.getReceivableNo());
		}
	}

	@Override
	public Boolean paymentResult(PayParam payParam) {
		if (payParam.getOrderType().equals(CashierEnum.ORDER.name())) {
			OrderVO order = orderService.getBySn(payParam.getSn()).data();
			if (order != null) {
				return PayStatusEnum.PAID.name().equals(order.orderBase().payStatus());
			} else {
				throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
			}
		}
		return false;
	}
}
