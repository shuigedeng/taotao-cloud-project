package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import com.taotao.cloud.store.api.vo.StoreAfterSaleAddressVO;
import com.taotao.cloud.store.api.vo.StoreDetailVO;
import com.taotao.cloud.store.api.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "IFeignStoreDetailService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignStoreFlowService {

}
