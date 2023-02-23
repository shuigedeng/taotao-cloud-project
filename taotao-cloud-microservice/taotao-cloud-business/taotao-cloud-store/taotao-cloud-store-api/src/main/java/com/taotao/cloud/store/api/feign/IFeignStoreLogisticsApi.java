package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreApiFallback.class)
public interface IFeignStoreLogisticsApi {

	@GetMapping(value = "/getStoreSelectedLogisticsName")
	List<String> getStoreSelectedLogisticsName(Long storeId);
}
