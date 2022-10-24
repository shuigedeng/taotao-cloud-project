package com.taotao.cloud.pay.common.merchant;

import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.BasePayType;
import com.egzosn.pay.common.http.HttpConfigStorage;

/**
 * 支付平台
 */
public interface PaymentPlatform<S extends PayService> extends BasePayType {

	/**
	 * 获取商户平台
	 *
	 * @return 商户平台
	 */
	String getPlatform();

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @param payConfigStorage 支付配置
	 * @return 支付服务
	 */
	S getPayService(PayConfigStorage payConfigStorage);

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @param payConfigStorage  支付配置
	 * @param httpConfigStorage 网络配置
	 * @return 支付服务
	 */
	S getPayService(PayConfigStorage payConfigStorage, HttpConfigStorage httpConfigStorage);

}
