package com.taotao.cloud.payment.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.payment.api.feign.fallback.FeignRefundLogServiceFallback;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import com.taotao.cloud.payment.api.vo.RefundLogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用快递公司模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_LOGISTICS_CENTER, fallbackFactory = FeignRefundLogServiceFallback.class)
public interface IFeignRefundLogService {

	@GetMapping("/pay/flow/info/id/{id:[0-9]*}")
	Result<PayFlowVO> findPayFlowById(@PathVariable(value = "id") Long id);

	RefundLogVO queryByAfterSaleSn(String sn);
}

