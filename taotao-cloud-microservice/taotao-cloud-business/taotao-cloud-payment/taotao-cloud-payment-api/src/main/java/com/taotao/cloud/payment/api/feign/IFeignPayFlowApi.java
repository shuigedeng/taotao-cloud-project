package com.taotao.cloud.payment.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.payment.api.feign.fallback.FeignPayFlowApiFallback;
import com.taotao.cloud.payment.api.model.vo.PayFlowVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用快递公司模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_LOGISTICS_CENTER, fallbackFactory = FeignPayFlowApiFallback.class)
public interface IFeignPayFlowApi {

	/**
	 * 根据id查询支付信息
	 *
	 * @param id id
	 * @return 支付信息
	 * @since 2020/11/20 上午10:45
	 */
	@GetMapping("/pay/flow/info/id/{id:[0-9]*}")
	PayFlowVO findPayFlowById(@PathVariable(value = "id") Long id);
}

