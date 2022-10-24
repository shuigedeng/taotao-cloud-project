package com.taotao.cloud.pay.common.provider;


import com.taotao.cloud.pay.common.configurers.PayMessageConfigurer;
import com.taotao.cloud.pay.common.merchant.MerchantDetails;
import com.taotao.cloud.pay.common.merchant.MerchantDetailsService;

import java.util.Collection;

/**
 * 商户列表管理器
 */
public interface MerchantDetailsManager<T extends MerchantDetails> extends
	MerchantDetailsService<T> {

	/**
	 * 创建商户
	 *
	 * @param merchant 商户信息
	 */
	void createMerchant(T merchant);

	/**
	 * 创建商户
	 *
	 * @param merchants 商户信息
	 */
	void createMerchant(Collection<T> merchants);

	/**
	 * 更新商户
	 *
	 * @param merchant 商户信息
	 */
	void updateMerchant(T merchant);

	/**
	 * 删除商户
	 *
	 * @param id 商户id
	 */
	void deleteMerchant(String id);

	/**
	 * 检查商户是否存在
	 *
	 * @param id 商户id
	 * @return 检查商户是否存在
	 */
	boolean merchantExists(String id);

	/**
	 * 设置支付消息配置中心
	 *
	 * @param configurer 配置
	 */
	void setPayMessageConfigurer(PayMessageConfigurer configurer);
}
