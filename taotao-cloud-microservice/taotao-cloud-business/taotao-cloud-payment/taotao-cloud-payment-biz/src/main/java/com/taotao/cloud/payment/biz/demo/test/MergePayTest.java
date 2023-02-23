package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.common.PayException;
import com.yungouos.pay.merge.MergePay;

public class MergePayTest {

	public static void main(String[] args) {
		try {
			String mchId = "聚合支付商户号";
			String key = "聚合支付密钥";
			String url = MergePay.nativePay(System.currentTimeMillis() + "", "0.01", mchId, "一码付测试", "2", null, null, null, null, null, null, key);
			System.out.println(url);
		} catch (PayException e) {
			e.printStackTrace();
		}
	}
}
