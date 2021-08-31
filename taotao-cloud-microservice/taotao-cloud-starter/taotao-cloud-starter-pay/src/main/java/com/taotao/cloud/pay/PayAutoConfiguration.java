package com.taotao.cloud.pay;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.pay.configurers.DefalutPayMessageConfigurer;
import com.taotao.cloud.pay.configurers.MerchantDetailsServiceConfigurer;
import com.taotao.cloud.pay.configurers.PayMessageConfigurer;
import com.taotao.cloud.pay.merchant.MerchantDetailsService;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.AliPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.FuiouPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.PaymentPlatforms;
import com.taotao.cloud.pay.provider.merchant.platform.PayoneerPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.PaypalPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.UnionPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.WxPaymentPlatform;
import com.taotao.cloud.pay.provider.merchant.platform.YoudianPaymentPlatform;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 支付转载配置
 *
 * @author egan
 * <pre>
 *                         email egzosn@gmail.com
 *                         date  2018/11/21.
 *                         </pre>
 */
@Configuration
@ImportAutoConfiguration({
	AliPaymentPlatform.class,
	FuiouPaymentPlatform.class,
	PayoneerPaymentPlatform.class,
	PaypalPaymentPlatform.class,
	UnionPaymentPlatform.class,
	WxPaymentPlatform.class,
	YoudianPaymentPlatform.class
})
public class PayAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(PayAutoConfiguration.class, StarterName.PAY_STARTER);
	}


	@Autowired
	@Order
	public void loadPaymentPlatforms(List<PaymentPlatform> platforms) {
		LogUtil.started(PaymentPlatform.class, StarterName.PAY_STARTER);

		for (PaymentPlatform platform : platforms) {
			PaymentPlatforms.loadPaymentPlatform(platform);
		}
	}


	@Bean
	@ConditionalOnMissingBean(MerchantDetailsServiceConfigurer.class)
	@ConditionalOnBean(PayServiceConfigurer.class)
	public MerchantDetailsServiceConfigurer detailsServiceConfigurer() {
		LogUtil.started(MerchantDetailsServiceConfigurer.class, StarterName.PAY_STARTER);

		return new MerchantDetailsServiceConfigurer();
	}

	@Bean
	@ConditionalOnMissingBean(MerchantDetailsService.class)
	@ConditionalOnBean(PayServiceConfigurer.class)
	protected MerchantDetailsService configure(PayServiceConfigurer configurer,
		MerchantDetailsServiceConfigurer merchantDetails,
		PayMessageConfigurer payMessageConfigurer) {
		LogUtil.started(MerchantDetailsService.class, StarterName.PAY_STARTER);

		configurer.configure(merchantDetails);
		configurer.configure(payMessageConfigurer);
		MerchantDetailsService detailsService = merchantDetails.getBuilder().build();
		return detailsService;
	}

	@Bean
	@Order
	@ConditionalOnBean(MerchantDetailsService.class)
	@ConditionalOnMissingBean(PayServiceManager.class)
	public PayServiceManager payServiceManager() {
		LogUtil.started(PayServiceManager.class, StarterName.PAY_STARTER);

		return new MerchantPayServiceManager();
	}


	@Bean
	@ConditionalOnMissingBean(PayMessageConfigurer.class)
	public PayMessageConfigurer messageHandlerConfigurer() {
		LogUtil.started(PayMessageConfigurer.class, StarterName.PAY_STARTER);

		return new DefalutPayMessageConfigurer();
	}


}
