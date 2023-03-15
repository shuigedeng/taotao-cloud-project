package com.taotao.cloud.payment.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.payment.api.feign.fallback.FeignRefundLogApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_PAYMENT, fallbackFactory = FeignRefundLogApiFallback.class)
public interface IFeignRefundSupportApi {

	@PostMapping("/refund")
	void refund(String sn);
}
