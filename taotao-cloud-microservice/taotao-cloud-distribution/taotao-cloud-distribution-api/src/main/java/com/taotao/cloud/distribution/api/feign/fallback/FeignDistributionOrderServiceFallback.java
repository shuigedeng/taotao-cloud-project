package com.taotao.cloud.distribution.api.feign.fallback;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.distribution.api.feign.IFeignDistributionOrderService;
import org.springframework.cloud.openfeign.FallbackFactory;

public class FeignDistributionOrderServiceFallback implements FallbackFactory<IFeignDistributionOrderService> {
	@Override
	public IFeignDistributionOrderService create(Throwable throwable) {
		return new IFeignDistributionOrderService() {

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
