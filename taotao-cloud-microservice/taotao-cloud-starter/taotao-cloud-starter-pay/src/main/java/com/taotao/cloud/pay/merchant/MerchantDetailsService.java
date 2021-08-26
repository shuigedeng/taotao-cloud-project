package com.taotao.cloud.pay.merchant;

/**
 * 提供给客户端获取商户列表信息的服务
 *
 * @author egan
 * <pre>
 *         email egzosn@gmail.com
 *         date 2018-12-29 10:53:08
 *         </pre>
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
