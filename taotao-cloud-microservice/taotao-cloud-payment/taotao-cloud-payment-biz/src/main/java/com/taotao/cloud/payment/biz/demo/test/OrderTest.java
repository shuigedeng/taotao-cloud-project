package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.entity.PayOrder;
import com.yungouos.pay.order.SystemOrder;

/**
 * 
 * 订单接口调用演示
 * 
 * @author YunGouOS技术部-029
 *
 */
public class OrderTest {

	public static void main(String[] args) {

		// 商户号可以是支付宝也可以是微信
		String mch_id = "商户号";
		// 商户密钥
		String key = "支付密钥";

		/**
		 * 查询订单
		 */
		try {
			PayOrder payOrder = SystemOrder.getOrderInfoByOutTradeNo("Y194506551713811", mch_id, key);
			System.out.println("查询系统订单返回结果：" + payOrder);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
