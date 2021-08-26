package com.taotao.cloud.pay.merchant.bean;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.wx.youdian.api.WxYouDianPayConfigStorage;
import com.taotao.cloud.pay.PayConfigurerAdapter;
import com.taotao.cloud.pay.builders.InMemoryMerchantDetailsServiceBuilder;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import com.taotao.cloud.pay.merchant.PaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.merchant.PaymentPlatformServiceAdapter;
import com.taotao.cloud.pay.provider.merchant.platform.PaymentPlatforms;
import com.taotao.cloud.pay.provider.merchant.platform.PayoneerPaymentPlatform;

/**
 * P卡(派安盈)商户信息列表
 *
 * @author egan
 * <pre>
 *                         email egzosn@gmail.com
 *                         date   2019/4/6 14:30.
 *                         </pre>
 */
public class WxYouDianMerchantDetails extends WxYouDianPayConfigStorage implements
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

	public WxYouDianMerchantDetails(InMemoryMerchantDetailsServiceBuilder builder) {
		this();
		this.builder = builder;
	}

	public WxYouDianMerchantDetails() {
		String platformName = PayoneerPaymentPlatform.platformName;
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

	public WxYouDianMerchantDetails httpConfigStorage(HttpConfigStorage httpConfigStorage) {
		this.httpConfigStorage = httpConfigStorage;
		return this;
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

	public WxYouDianMerchantDetails detailsId(String detailsId) {
		this.detailsId = detailsId;
		return this;
	}


	public WxYouDianMerchantDetails keyPrivate(String keyPrivate) {
		setKeyPrivate(keyPrivate);
		return this;
	}

	public WxYouDianMerchantDetails keyPublic(String keyPublic) {
		setKeyPublic(keyPublic);
		return this;
	}

	public WxYouDianMerchantDetails seller(String seller) {
		setSeller(seller);
		return this;
	}


	public WxYouDianMerchantDetails signType(String signType) {
		setSignType(signType);
		return this;
	}

	public WxYouDianMerchantDetails inputCharset(String inputCharset) {
		setInputCharset(inputCharset);
		return this;
	}


}
