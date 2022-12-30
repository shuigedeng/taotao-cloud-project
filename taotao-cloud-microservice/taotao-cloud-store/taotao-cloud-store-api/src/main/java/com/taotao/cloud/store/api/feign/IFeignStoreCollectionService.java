package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import com.taotao.cloud.store.api.web.vo.StoreCollectionVO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "IFeignFreightTemplateService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignStoreCollectionService {

	PageResult<StoreCollectionVO> storeCollection(PageQuery page);

	Boolean addStoreCollection(Long id);

	Boolean deleteStoreCollection(Long id);

	Boolean isCollection(Long id);
}
