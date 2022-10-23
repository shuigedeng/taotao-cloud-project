package com.taotao.cloud.pay.builders;


import com.taotao.cloud.pay.merchant.MerchantDetailsService;
import com.taotao.cloud.pay.merchant.PaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.AliMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.FuiouMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.PayoneerMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.PaypalMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.UnionMerchantDetails;
import com.taotao.cloud.pay.merchant.bean.WxMerchantDetails;
import com.taotao.cloud.pay.provider.InMemoryMerchantDetailsManager;
import java.util.ArrayList;
import java.util.List;


public class InMemoryMerchantDetailsServiceBuilder extends MerchantDetailsServiceBuilder {

	private List<PaymentPlatformMerchantDetails> merchantDetails = new ArrayList<PaymentPlatformMerchantDetails>();


	public void addMerchantDetails(PaymentPlatformMerchantDetails merchantDetail) {
		this.merchantDetails.add(merchantDetail);
	}

	public AliMerchantDetails ali() {
		AliMerchantDetails details = new AliMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}

	public FuiouMerchantDetails fuiou() {
		FuiouMerchantDetails details = new FuiouMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}

	public PayoneerMerchantDetails payoneer() {
		PayoneerMerchantDetails details = new PayoneerMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}

	public PaypalMerchantDetails payPal() {
		PaypalMerchantDetails details = new PaypalMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}

	public UnionMerchantDetails union() {
		UnionMerchantDetails details = new UnionMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}

	public WxMerchantDetails wx() {
		WxMerchantDetails details = new WxMerchantDetails(this);
		addMerchantDetails(details);
		return details;
	}


	/**
	 * 开始构建
	 *
	 * @return 商户列表服务
	 */
	@Override
	protected MerchantDetailsService performBuild() {
		InMemoryMerchantDetailsManager merchantDetailsManager = new InMemoryMerchantDetailsManager();
		merchantDetailsManager.setPayMessageConfigurer(configurer);
		merchantDetailsManager.createMerchant(merchantDetails);
		return merchantDetailsManager;
	}
}
