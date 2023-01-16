package com.taotao.cloud.payment.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.payment.api.feign.fallback.FeignRefundLogServiceFallback;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import com.taotao.cloud.payment.api.vo.RefundLogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_LOGISTICS_CENTER, fallbackFactory = FeignRefundLogServiceFallback.class)
public interface IFeignRefundSupportService {

	@PostMapping("/refund")
	void refund(String sn);
}
