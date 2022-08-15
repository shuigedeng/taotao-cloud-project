package com.taotao.cloud.payment.biz.demo.test;

import com.yungouos.pay.entity.RePayBiz;
import com.yungouos.pay.finance.Finance;

/**
 * 
 * 转账业务demo
 * 
 * @author YunGouOS技术部-029
 *
 *
 */
public class RePayTest {

	public static void main(String[] args) {
		String merchant_id = "商户号";// YunGouOS商户ID 登录YunGouOS.com-》账户设置-》开发者身份-》账户商户号
		String key = "商户密钥";// 商户密钥 登录YunGouOS.com-》账户设置-》开发者身份-》账户商户号 商户密钥

		String out_trade_no = System.currentTimeMillis() + "";
		String account = "收款人openid";
		String account_name = "";
		String money = "0.01";
		String desc = "这是转账描述";
		String mch_id = null;
		String notify_url = null;

		// 微信转账
		RePayBiz rePayBiz = Finance.rePayWxPay(merchant_id, out_trade_no, account, account_name, money, desc, mch_id, notify_url, key);
		System.out.println(rePayBiz.toString());

		// 支付宝转账
		account = "收款支付宝账户";
		account_name = "支付宝姓名";
		RePayBiz payAliPay = Finance.rePayAliPay(merchant_id, out_trade_no, account, account_name, money, desc, mch_id, notify_url, key);
		System.out.println(payAliPay.toString());

		account = "银行卡号";
		account_name = "银行卡姓名";
		Integer bank_type = null;
		String bank_name = null;
		String bank_code = null;
		String app_id = null;
		// 银行卡转账
		RePayBiz rePayBank = Finance.rePayBank(merchant_id, out_trade_no, account, account_name, money, desc, bank_type, bank_name, bank_code, mch_id, app_id, notify_url, key);
		System.out.println(rePayBank.toString());

		// 查询转账结果
		rePayBiz = Finance.getRePayInfo(out_trade_no, merchant_id, key);
		System.out.println(rePayBiz.toString());
	}
}
