package com.taotao.cloud.pay.configurers;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付回调配置中心
 */
public class DefalutPayMessageConfigurer implements PayMessageConfigurer {

	/**
	 * 消息处理器集
	 * <p>
	 * key 为商户平台 value 为对应得处理器
	 */
	private Map<PaymentPlatform, PayMessageHandler<PayMessage, PayService>> handlers = new HashMap<PaymentPlatform, PayMessageHandler<PayMessage, PayService>>();

	/**
	 * 消息拦截器集
	 * <p>
	 * key 为商户平台 value 为对应得拦截器
	 */
	private Map<PaymentPlatform, List<PayMessageInterceptor<PayMessage, PayService>>> interceptors = new HashMap<PaymentPlatform, List<PayMessageInterceptor<PayMessage, PayService>>>();

	/**
	 * 添加处理器
	 *
	 * @param platform 商户平台，渠道
	 * @param handler  处理器
	 */
	@Override
	public void addHandler(PaymentPlatform platform, PayMessageHandler handler) {
		if (handlers.containsKey(platform)) {
			throw new IllegalArgumentException(platform.getPlatform() + "已存在，请勿重复设置");
		}
		this.handlers.put(platform, handler);
	}

	/**
	 * 获取处理器
	 *
	 * @param platform 商户平台，渠道
	 * @return 处理器
	 */
	@Override
	public PayMessageHandler<PayMessage, PayService> getHandler(PaymentPlatform platform) {
		return handlers.get(platform);
	}

	/**
	 * 添加拦截器
	 *
	 * @param platform    商户平台，渠道
	 * @param interceptor 拦截器
	 */
	@Override
	public void addInterceptor(PaymentPlatform platform, PayMessageInterceptor interceptor) {
		List<PayMessageInterceptor<PayMessage, PayService>> interceptors = this.interceptors.get(
			platform);
		if (null == interceptors) {
			interceptors = new ArrayList<PayMessageInterceptor<PayMessage, PayService>>();
			this.interceptors.put(platform, interceptors);
		}
		interceptors.add(interceptor);
	}

	/**
	 * 获取拦截器
	 *
	 * @param platform 商户平台，渠道
	 * @return 拦截器
	 */
	@Override
	public List<PayMessageInterceptor<PayMessage, PayService>> getInterceptor(
		PaymentPlatform platform) {
		return interceptors.get(platform);
	}
}
