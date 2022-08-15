package com.taotao.cloud.payment.biz.demo.service.alipay;

import java.util.Map;

public interface AliPayService {

	//支付宝扫码支付
	public Map<String, Object> nativePay(String body,String money);
	
	
	//支付宝H5支付
	public Map<String, Object> mobilePay(String body,String money);
}
