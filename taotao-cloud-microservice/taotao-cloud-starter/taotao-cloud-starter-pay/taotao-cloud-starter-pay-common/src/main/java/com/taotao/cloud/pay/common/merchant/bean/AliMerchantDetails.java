package com.taotao.cloud.pay.common.merchant.bean;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.taotao.cloud.pay.common.builders.InMemoryMerchantDetailsServiceBuilder;
import com.taotao.cloud.pay.common.configuration.AliPaymentPlatform;
import com.taotao.cloud.pay.common.merchant.PaymentPlatform;
import com.taotao.cloud.pay.common.merchant.PaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.common.merchant.PaymentPlatformServiceAdapter;
import com.taotao.cloud.pay.common.model.PayConfigurerAdapter;
import com.taotao.cloud.pay.common.provider.PaymentPlatforms;

/**
 * 支付宝商户信息列表
 */
public class AliMerchantDetails extends AliPayConfigStorage implements
	PaymentPlatformMerchantDetails, PaymentPlatformServiceAdapter,
	PayConfigurerAdapter<InMemoryMerchantDetailsServiceBuilder> {


	private String detailsId;

	/**
	 * 商户对应的支付服务
	 */
	private volatile PayService payService;

	/**
	 * 商户平台
	 */
	private PaymentPlatform platform;

	private InMemoryMerchantDetailsServiceBuilder builder;
	/**
	 * HTTP请求配置
	 */
	private HttpConfigStorage httpConfigStorage;


	/**
	 * 外部调用者使用，链式的做法
	 *
	 * @return 返回对应外部调用者
	 */
	@Override
	public InMemoryMerchantDetailsServiceBuilder and() {
		initService();
		return getBuilder();
	}

	/**
	 * 获取构建器
	 *
	 * @return 构建器
	 */
	@Override
	public InMemoryMerchantDetailsServiceBuilder getBuilder() {
		return builder;
	}

	public AliMerchantDetails(InMemoryMerchantDetailsServiceBuilder builder) {
		this();
		this.builder = builder;
	}

	public AliMerchantDetails() {
		String platformName = AliPaymentPlatform.platformName;
		setPayType(platformName);
		platform = PaymentPlatforms.getPaymentPlatform(platformName);
	}

	/**
	 * 获取支付平台
	 *
	 * @return 支付平台
	 */
	@Override
	public PaymentPlatform getPaymentPlatform() {
		return platform;
	}

	/**
	 * 初始化服务
	 *
	 * @return 支付商户服务适配器
	 */
	@Override
	public PaymentPlatformServiceAdapter initService() {
		if (null == payService) {
			payService = platform.getPayService(this, getHttpConfigStorage());
		}

		return this;
	}

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @return 支付服务
	 */
	@Override
	public PayService getPayService() {
		return payService;
	}

	/**
	 * 获取HTTP请求配置
	 *
	 * @return HTTP请求配置
	 */
	@Override
	public HttpConfigStorage getHttpConfigStorage() {
		return httpConfigStorage;
	}

	public AliMerchantDetails httpConfigStorage(HttpConfigStorage httpConfigStorage) {
		this.httpConfigStorage = httpConfigStorage;
		return this;
	}

	public void setDetailsId(String detailsId) {
		this.detailsId = detailsId;
	}

	/**
	 * 获取支付商户id
	 *
	 * @return 支付商户id
	 */
	@Override
	public String getDetailsId() {
		return detailsId;
	}

	public AliMerchantDetails detailsId(String detailsId) {
		this.detailsId = detailsId;
		return this;
	}

	public AliMerchantDetails notifyUrl(String notifyUrl) {
		setNotifyUrl(notifyUrl);
		return this;
	}

	public AliMerchantDetails returnUrl(String returnUrl) {
		setReturnUrl(returnUrl);
		return this;
	}

	public AliMerchantDetails signType(String signType) {
		setSignType(signType);
		return this;
	}

	public AliMerchantDetails inputCharset(String inputCharset) {
		setInputCharset(inputCharset);
		return this;
	}

	public AliMerchantDetails test(boolean test) {
		setTest(test);
		return this;
	}

	public AliMerchantDetails pid(String pid) {
		setPid(pid);
		return this;
	}

	public AliMerchantDetails appid(String appid) {
		setAppid(appid);
		return this;
	}

	public AliMerchantDetails keyPrivate(String keyPrivate) {
		setKeyPrivate(keyPrivate);
		return this;
	}

	public AliMerchantDetails keyPublic(String keyPublic) {
		setKeyPublic(keyPublic);
		return this;
	}

	public AliMerchantDetails seller(String seller) {
		setSeller(seller);
		return this;
	}


}
