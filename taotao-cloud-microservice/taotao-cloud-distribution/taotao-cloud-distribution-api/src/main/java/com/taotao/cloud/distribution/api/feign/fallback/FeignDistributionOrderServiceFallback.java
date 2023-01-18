package com.taotao.cloud.distribution.api.feign.fallback;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.distribution.api.feign.IFeignDistributionOrderApi;
import org.springframework.cloud.openfeign.FallbackFactory;

public class FeignDistributionOrderServiceFallback implements FallbackFactory<IFeignDistributionOrderApi> {
	@Override
	public IFeignDistributionOrderApi create(Throwable throwable) {
		return new IFeignDistributionOrderApi() {

			@Override
			public void calculationDistribution(String orderSn) {

			}

			@Override
			public void cancelOrder(String orderSn) {

			}

			@Override
			public void rebate(String name, DateTime dateTime) {

			}

			@Override
			public void updateStatus() {

			}

			@Override
			public void refundOrder(String sn) {

			}
		};
	}
}
