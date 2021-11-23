package com.taotao.cloud.pay.merchant.bean;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.fuiou.api.FuiouPayConfigStorage;
import com.taotao.cloud.pay.model.PayConfigurerAdapter;
import com.taotao.cloud.pay.builders.InMemoryMerchantDetailsServiceBuilder;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import com.taotao.cloud.pay.merchant.PaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.merchant.PaymentPlatformServiceAdapter;
import com.taotao.cloud.pay.configuration.FuiouPaymentPlatform;
import com.taotao.cloud.pay.provider.PaymentPlatforms;

/**
 * 富友商户信息列表
 *
 *
 */
public class FuiouMerchantDetails extends FuiouPayConfigStorage implements
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

	public FuiouMerchantDetails(InMemoryMerchantDetailsServiceBuilder builder) {
		this();
		this.builder = builder;
	}

	public FuiouMerchantDetails() {
		String platformName = FuiouPaymentPlatform.platformName;
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

	public FuiouMerchantDetails httpConfigStorage(HttpConfigStorage httpConfigStorage) {
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


	public FuiouMerchantDetails detailsId(String detailsId) {
		this.detailsId = detailsId;
		return this;
	}

	public FuiouMerchantDetails notifyUrl(String notifyUrl) {
		setNotifyUrl(notifyUrl);
		return this;
	}

	public FuiouMerchantDetails returnUrl(String returnUrl) {
		setReturnUrl(returnUrl);
		return this;
	}

	public FuiouMerchantDetails signType(String signType) {
		setSignType(signType);
		return this;
	}

	public FuiouMerchantDetails inputCharset(String inputCharset) {
		setInputCharset(inputCharset);
		return this;
	}

	public FuiouMerchantDetails test(boolean test) {
		setTest(test);
		return this;
	}

	public FuiouMerchantDetails mchntCd(String mchntCd) {
		setMchntCd(mchntCd);
		return this;
	}

	public FuiouMerchantDetails keyPrivate(String keyPrivate) {
		setKeyPrivate(keyPrivate);
		return this;
	}

	public FuiouMerchantDetails keyPublic(String keyPublic) {
		setKeyPublic(keyPublic);
		return this;
	}


}
