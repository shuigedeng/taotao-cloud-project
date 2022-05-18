package com.taotao.cloud.distribution.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.distribution.api.feign.fallback.FeignDistributionOrderServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignDistributionOrderService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignDistributionOrderServiceFallback.class)
public interface IFeignDistributionOrderService {

	//记录分销订单
	void calculationDistribution(String orderSn);

	//修改分销订单状态
	void cancelOrder(String orderSn);
}

