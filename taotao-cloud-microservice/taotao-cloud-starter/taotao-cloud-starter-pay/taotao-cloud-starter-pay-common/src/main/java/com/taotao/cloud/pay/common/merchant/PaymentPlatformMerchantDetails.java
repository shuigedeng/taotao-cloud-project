package com.taotao.cloud.pay.common.merchant;

import com.egzosn.pay.common.api.PayService;

/**
 * 支付平台商户列表
 */
public interface PaymentPlatformMerchantDetails<S extends PayService> extends MerchantDetails {

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @return 支付服务
	 */
	S getPayService();

	/**
	 * 获取支付平台
	 *
	 * @return 支付平台
	 */
	PaymentPlatform getPaymentPlatform();
}
