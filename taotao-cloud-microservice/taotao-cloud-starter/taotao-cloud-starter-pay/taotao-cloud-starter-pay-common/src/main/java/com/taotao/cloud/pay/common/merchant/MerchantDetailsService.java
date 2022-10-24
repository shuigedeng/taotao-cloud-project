package com.taotao.cloud.pay.common.merchant;

/**
 * 提供给客户端获取商户列表信息的服务
 */
public interface MerchantDetailsService<T extends MerchantDetails> {

	/**
	 * 通过支付商户id加载对应的商户信息列表
	 *
	 * @param merchantId 支付商户id
	 * @return 商户信息列表
	 */
	T loadMerchantByMerchantId(String merchantId);

}
