package com.taotao.cloud.payment.biz.service;


import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;

/**
 * 支付日志 业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 16:46:54
 */
public interface PaymentService {

	/**
	 * 支付成功通知
	 *
	 * @param paymentSuccessParams 支付成功回调参数
	 * @since 2022-05-30 16:46:54
	 */
	void success(PaymentSuccessParams paymentSuccessParams);


	/**
	 * 平台支付成功
	 *
	 * @param paymentSuccessParams 支付成功回调参数
	 * @since 2022-05-30 16:46:54
	 */
	void adminPaySuccess(PaymentSuccessParams paymentSuccessParams);

}
