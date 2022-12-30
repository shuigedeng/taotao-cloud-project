package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import com.taotao.cloud.store.api.web.vo.StoreCollectionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(contextId = "IFeignFreightTemplateService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignStoreCollectionService {
	@GetMapping(value = "/storeCollection")
	PageResult<StoreCollectionVO> storeCollection(PageQuery page);
	@GetMapping(value = "/addStoreCollection")
	Boolean addStoreCollection(Long id);
	@GetMapping(value = "/deleteStoreCollection")
	Boolean deleteStoreCollection(Long id);
	@GetMapping(value = "/isCollection")
	Boolean isCollection(Long id);
}
