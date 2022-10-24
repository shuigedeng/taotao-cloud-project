package com.taotao.cloud.pay.common.model;


import com.taotao.cloud.pay.common.configurers.MerchantDetailsServiceConfigurer;
import com.taotao.cloud.pay.common.configurers.PayMessageConfigurer;

/**
 * 支付服务配置，用于支付服务相关的配置，暂时主要为商户相关的配置，后期在进行添加别的配置
 */
public interface PayServiceConfigurer {

	/**
	 * 商户配置
	 *
	 * @param configurer 商户配置
	 */
	void configure(MerchantDetailsServiceConfigurer configurer);

	/**
	 * 商户配置
	 *
	 * @param configurer 支付消息配置
	 */
	void configure(PayMessageConfigurer configurer);


}
