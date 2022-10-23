package com.taotao.cloud.pay.configuration;

import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.wx.youdian.api.WxYouDianPayConfigStorage;
import com.egzosn.pay.wx.youdian.api.WxYouDianPayService;
import com.egzosn.pay.wx.youdian.bean.YoudianTransactionType;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * 友店支付平台
 *
 */
@Configuration(YoudianPaymentPlatform.platformName)
@ConditionalOnMissingBean(YoudianPaymentPlatform.class)
@ConditionalOnClass(name = {"com.egzosn.pay.wx.youdian.api.WxYouDianPayConfigStorage"})
public class YoudianPaymentPlatform implements PaymentPlatform {

	public static final String platformName = "youdianPay";


	/**
	 * 获取商户平台
	 *
	 * @return 商户平台
	 */
	@Override
	public String getPlatform() {
		return platformName;
	}

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @param payConfigStorage 支付配置
	 * @return 支付服务
	 */
	@Override
	public PayService getPayService(PayConfigStorage payConfigStorage) {
		if (payConfigStorage instanceof WxYouDianPayConfigStorage) {
			return new WxYouDianPayService((WxYouDianPayConfigStorage) payConfigStorage);
		}
		WxYouDianPayConfigStorage configStorage = new WxYouDianPayConfigStorage();
		configStorage.setKeyPrivate(payConfigStorage.getKeyPrivate());
		configStorage.setKeyPublic(payConfigStorage.getKeyPublic());
		configStorage.setSignType(payConfigStorage.getSignType());
		configStorage.setPayType(payConfigStorage.getPayType().toString());
		configStorage.setSeller(payConfigStorage.getSeller());
		configStorage.setInputCharset(payConfigStorage.getInputCharset());
		configStorage.setTest(payConfigStorage.isTest());
		return new WxYouDianPayService(configStorage);
	}

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @param payConfigStorage  支付配置
	 * @param httpConfigStorage 网络配置
	 * @return 支付服务
	 */
	@Override
	public PayService getPayService(PayConfigStorage payConfigStorage,
		HttpConfigStorage httpConfigStorage) {
		PayService payService = getPayService(payConfigStorage);
		payService.setRequestTemplateConfigStorage(httpConfigStorage);
		return payService;
	}

	@Override
	public TransactionType getTransactionType(String name) {
		return YoudianTransactionType.valueOf(name);
	}


}
