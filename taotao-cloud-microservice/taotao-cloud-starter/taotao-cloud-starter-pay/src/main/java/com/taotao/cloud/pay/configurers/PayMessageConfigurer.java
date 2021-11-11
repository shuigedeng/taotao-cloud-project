package com.taotao.cloud.pay.configurers;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import java.util.List;

/**
 */
public interface PayMessageConfigurer {

	/**
	 * 添加处理器
	 *
	 * @param platform 商户平台，渠道
	 * @param handler  处理器
	 */
	void addHandler(PaymentPlatform platform, PayMessageHandler handler);

	/**
	 * 获取处理器
	 *
	 * @param platform 商户平台，渠道
	 * @return 处理器
	 */
	PayMessageHandler<PayMessage, PayService> getHandler(PaymentPlatform platform);

	/**
	 * 添加拦截器
	 *
	 * @param platform    商户平台，渠道
	 * @param interceptor 拦截器
	 */
	void addInterceptor(PaymentPlatform platform, PayMessageInterceptor interceptor);

	/**
	 * 获取拦截器
	 *
	 * @param platform 商户平台，渠道
	 * @return 拦截器
	 */
	List<PayMessageInterceptor<PayMessage, PayService>> getInterceptor(PaymentPlatform platform);

}
