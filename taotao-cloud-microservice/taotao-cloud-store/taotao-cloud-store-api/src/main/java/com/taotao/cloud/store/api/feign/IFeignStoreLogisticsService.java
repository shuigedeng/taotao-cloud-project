package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignStoreLogisticsService {

	@GetMapping(value = "/getStoreSelectedLogisticsName")
	List<String> getStoreSelectedLogisticsName(Long storeId);
}
