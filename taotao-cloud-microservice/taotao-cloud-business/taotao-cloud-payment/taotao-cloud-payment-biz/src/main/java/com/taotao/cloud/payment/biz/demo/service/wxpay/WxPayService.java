package com.taotao.cloud.payment.biz.demo.service.wxpay;

import java.util.Map;

public interface WxPayService {

	//微信扫码支付
	public Map<String, Object> nativePay(String body,String money);
}
