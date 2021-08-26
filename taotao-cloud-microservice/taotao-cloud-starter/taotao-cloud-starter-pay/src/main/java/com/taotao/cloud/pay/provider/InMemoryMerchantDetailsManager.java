package com.taotao.cloud.pay.provider;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.taotao.cloud.pay.configurers.PayMessageConfigurer;
import com.taotao.cloud.pay.merchant.MerchantNotFoundException;
import com.taotao.cloud.pay.merchant.PaymentPlatformMerchantDetails;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.util.Assert;


/**
 * 内存型支付账户(商户)存储器
 *
 * @author egan
 * <pre>
 *                 email egzosn@gmail.com
 *                 date 2018-11-22 17:18:03
 *           </pre>
 */
public class InMemoryMerchantDetailsManager implements
	MerchantDetailsManager<PaymentPlatformMerchantDetails> {

	private Map<String, PaymentPlatformMerchantDetails> merchantDetails = new HashMap<String, PaymentPlatformMerchantDetails>();
	private PayMessageConfigurer configurer;

	public InMemoryMerchantDetailsManager() {
	}

	public InMemoryMerchantDetailsManager(
		Collection<PaymentPlatformMerchantDetails> merchantDetails) {
		createMerchant(merchantDetails);
	}

	public InMemoryMerchantDetailsManager(
		Map<String, PaymentPlatformMerchantDetails> merchantDetails) {
		this.merchantDetails = merchantDetails;
	}

	/**
	 * 这个暂时还未定义，后期版本进行
	 *
	 * @param merchants 属性配置
	 */
	public InMemoryMerchantDetailsManager(Properties merchants) {

	}


	/**
	 * 创建商户
	 *
	 * @param merchants 商户信息
	 */
	@Override
	public void createMerchant(Collection<PaymentPlatformMerchantDetails> merchants) {
		for (PaymentPlatformMerchantDetails merchant : merchants) {
			this.createMerchant(merchant);
		}
	}

	/**
	 * 创建商户
	 *
	 * @param merchant 商户信息
	 */
	@Override
	public void createMerchant(PaymentPlatformMerchantDetails merchant) {

		Assert.isTrue(!merchantExists(merchant.getDetailsId()), "商户信息已存在");
		InMemoryMerchantDetailsManager.setPayMessageConfigurer(merchant.getPayService(), merchant,
			configurer);
		merchantDetails.put(merchant.getDetailsId(), merchant);

	}

	protected static void setPayMessageConfigurer(PayService payService,
		PaymentPlatformMerchantDetails details, PayMessageConfigurer configurer) {
		PayMessageHandler<PayMessage, PayService> handler = configurer.getHandler(
			details.getPaymentPlatform());
		if (null != handler) {
			payService.setPayMessageHandler(handler);
		}

		List<PayMessageInterceptor<PayMessage, PayService>> interceptors = configurer.getInterceptor(
			details.getPaymentPlatform());
		if (null == interceptors || interceptors.isEmpty()) {
			return;
		}
		for (PayMessageInterceptor<PayMessage, PayService> interceptor : interceptors) {
			payService.addPayMessageInterceptor(interceptor);
		}
	}

	/**
	 * 更新商户
	 *
	 * @param merchant 商户信息
	 */
	@Override
	public void updateMerchant(PaymentPlatformMerchantDetails merchant) {
		Assert.isTrue(merchantExists(merchant.getDetailsId()), "商户信息不存在");
		merchantDetails.put(merchant.getDetailsId(), merchant);
	}

	/**
	 * 删除商户
	 *
	 * @param id 商户id
	 */
	@Override
	public void deleteMerchant(String id) {
		merchantDetails.remove(id);
	}

	/**
	 * 检查商户是否存在
	 *
	 * @param id 商户id
	 * @return 检查商户是否存在
	 */
	@Override
	public boolean merchantExists(String id) {
		return merchantDetails.containsKey(id);
	}

	/**
	 * 设置支付消息配置中心
	 *
	 * @param configurer 配置
	 */
	@Override
	public void setPayMessageConfigurer(PayMessageConfigurer configurer) {
		this.configurer = configurer;
	}

	/**
	 * 通过支付商户id加载对应的商户信息列表
	 *
	 * @param merchantId 支付商户id
	 * @return 商户信息列表
	 */
	@Override
	public PaymentPlatformMerchantDetails loadMerchantByMerchantId(String merchantId) {

		PaymentPlatformMerchantDetails details = merchantDetails.get(merchantId);
		if (null == details) {
			throw new MerchantNotFoundException(merchantId);
		}
		return details;
	}
}
