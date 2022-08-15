package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.black.PayBlack;

/**
 * 
 * 黑名单API调用演示
 *
 * @author YunGouOS技术部-029
 */
public class PayBlackTest {

	public static void main(String[] args) {
		String mchId = "微信支付商户号";
		String key = "微信支付支付密钥";
		String account="一般是openid或支付宝buyer_id";
		PayBlack.create(mchId, account, "羊毛党", "2021-06-25 11:10:23", key);
		System.out.println("黑名单添加成功");
		
		boolean check = PayBlack.check(mchId, account, key);
		System.out.println("是否黑名单："+check);
	}
}
